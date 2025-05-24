package com.glamvibe.glamvibeadmin.domain.repository.promotions

import com.glamvibe.glamvibeadmin.domain.model.Promotion

interface PromotionsRepository {
    suspend fun getCurrentPromotions(): List<Promotion>
    suspend fun getPromotion(promotionId: Int): Promotion
}