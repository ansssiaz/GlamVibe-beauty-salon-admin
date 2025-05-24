package com.glamvibe.glamvibeadmin.data.api

import com.glamvibe.glamvibeadmin.domain.repository.administrator.AdministratorLocalRepository
import com.glamvibe.glamvibeadmin.domain.repository.administrator.AdministratorNetworkRepository
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import org.json.JSONObject
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.io.IOException

class AuthInterceptor : Interceptor, KoinComponent {
    private val localRepository: AdministratorLocalRepository by inject()
    private val networkRepository: AdministratorNetworkRepository by inject()

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        if (shouldSkipAuth(originalRequest)) {
            return chain.proceed(originalRequest)
        }

        synchronized(this) {
            if (localRepository.isAccessTokenExpired()) {

                val refreshToken = localRepository.checkTokenPair().refreshToken
                if (refreshToken.isNotEmpty()) {
                    try {
                        val newTokens = runBlocking {
                            networkRepository.refreshTokenPair(refreshToken)
                        }
                        localRepository.saveTokenPair(newTokens.accessToken, newTokens.refreshToken)
                        localRepository.saveTokenExpirationTime(1800)
                    } catch (e: Exception) {
                        localRepository.deleteTokenPair()
                        throw IOException("Failed to refresh token", e)
                    }
                } else {
                    throw IOException("No refresh token available")
                }
            }
        }

            val requestBuilder = originalRequest.newBuilder()

            localRepository.checkTokenPair().accessToken.takeIf { it.isNotEmpty() }?.let {
                requestBuilder.header("Authorization", "Bearer $it")
            }

        val updatedRequest = if (originalRequest.body != null &&
            originalRequest.method in listOf("POST", "PUT", "PATCH")) {
            updateRequestBody(requestBuilder, originalRequest)
        } else {
            requestBuilder.build()
        }

        return chain.proceed(updatedRequest)
    }

    private fun shouldSkipAuth(request: Request): Boolean {
        val path = request.url.encodedPath
        return path.contains("login") || path.contains("registration") || path.contains("token-refresh")
    }

    private fun updateRequestBody(
        builder: Request.Builder,
        originalRequest: Request
    ): Request {
        val contentType = originalRequest.body?.contentType()
        val bodyString = originalRequest.body?.let { body ->
            val buffer = okio.Buffer()
            body.writeTo(buffer)
            buffer.readUtf8()
        } ?: return builder.build()

        return try {
            val jsonBody = JSONObject(bodyString)
            if (jsonBody.has("refreshToken")) {
                jsonBody.put("refreshToken", localRepository.checkTokenPair().refreshToken)
                builder.method(
                    originalRequest.method,
                    jsonBody.toString().toRequestBody(contentType)
                ).build()
            } else {
                builder.build()
            }
        } catch (e: Exception) {
            builder.build()
        }
    }
}