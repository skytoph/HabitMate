package com.skytoph.taski.presentation.habit.icon

sealed interface RewardFailResult {
    data object General : RewardFailResult
    data object NoConnection : RewardFailResult
    data object ConsentError : RewardFailResult
    class NoAdsAvailable(val isOptionsRequired: Boolean) : RewardFailResult
}