package com.glamvibe.glamvibeadmin.domain.repository.promotions

import com.glamvibe.glamvibeadmin.domain.model.Promotion

interface PromotionsRepository {
    suspend fun getPromotions(): List<Promotion>
    suspend fun getPromotion(promotionId: Int): Promotion
}