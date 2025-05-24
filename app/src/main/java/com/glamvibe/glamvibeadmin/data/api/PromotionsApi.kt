package com.glamvibe.glamvibeadmin.data.api

import com.glamvibe.glamvibeadmin.domain.model.Promotion
import retrofit2.http.GET
import retrofit2.http.Path

interface PromotionsApi {
    @GET("promotions/current")
    suspend fun getCurrentPromotions(): List<Promotion>

    @GET("promotions/{id}")
    suspend fun getPromotion(@Path("id") promotionId: Int): Promotion
}