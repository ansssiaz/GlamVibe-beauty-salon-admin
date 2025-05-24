package com.glamvibe.glamvibeadmin.data.repository.promotions

import com.glamvibe.glamvibeadmin.data.api.PromotionsApi
import com.glamvibe.glamvibeadmin.domain.model.Promotion
import com.glamvibe.glamvibeadmin.domain.repository.promotions.PromotionsRepository

class PromotionRepositoryImpl(private val api: PromotionsApi) : PromotionsRepository {
    override suspend fun getCurrentPromotions(): List<Promotion> = api.getCurrentPromotions()
    override suspend fun getPromotion(promotionId: Int): Promotion = api.getPromotion(promotionId)
}