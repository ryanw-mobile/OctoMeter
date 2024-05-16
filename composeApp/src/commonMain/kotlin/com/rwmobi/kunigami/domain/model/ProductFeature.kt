/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

@file:OptIn(ExperimentalResourceApi::class)

package com.rwmobi.kunigami.domain.model

import androidx.compose.runtime.Immutable
import co.touchlab.kermit.Logger
import kunigami.composeapp.generated.resources.Res
import kunigami.composeapp.generated.resources.briefcase
import kunigami.composeapp.generated.resources.dashboard
import kunigami.composeapp.generated.resources.lock
import kunigami.composeapp.generated.resources.product_feature_business
import kunigami.composeapp.generated.resources.product_feature_green
import kunigami.composeapp.generated.resources.product_feature_prepay
import kunigami.composeapp.generated.resources.product_feature_restricted
import kunigami.composeapp.generated.resources.product_feature_tracker
import kunigami.composeapp.generated.resources.product_feature_variable
import kunigami.composeapp.generated.resources.shuffle
import kunigami.composeapp.generated.resources.trees
import kunigami.composeapp.generated.resources.wallet
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.StringResource

@Immutable
enum class ProductFeature(val stringResource: StringResource, val iconResource: DrawableResource) {
    VARIABLE(stringResource = Res.string.product_feature_variable, iconResource = Res.drawable.shuffle),
    GREEN(stringResource = Res.string.product_feature_green, iconResource = Res.drawable.trees),
    TRACKER(stringResource = Res.string.product_feature_tracker, iconResource = Res.drawable.dashboard),
    PREPAY(stringResource = Res.string.product_feature_prepay, iconResource = Res.drawable.wallet),
    BUSINESS(stringResource = Res.string.product_feature_business, iconResource = Res.drawable.briefcase),
    RESTRICTED(stringResource = Res.string.product_feature_restricted, iconResource = Res.drawable.lock),
    ;

    companion object {
        fun fromApiValue(value: String?): ProductFeature? = try {
            value?.let {
                ProductFeature.valueOf(it.uppercase())
            }
        } catch (e: IllegalArgumentException) {
            Logger.e(tag = "ProductFeature", messageString = "failed to convert $value")
            null
        }
    }
}
