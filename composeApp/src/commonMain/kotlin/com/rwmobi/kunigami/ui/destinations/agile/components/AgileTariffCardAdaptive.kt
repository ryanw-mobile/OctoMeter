/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.ui.destinations.agile.components

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.VerticalDivider
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import com.rwmobi.kunigami.domain.extensions.getNextHalfHourCountdownMillis
import com.rwmobi.kunigami.domain.model.product.Tariff
import com.rwmobi.kunigami.domain.model.rate.PaymentMethod
import com.rwmobi.kunigami.domain.model.rate.Rate
import com.rwmobi.kunigami.ui.components.CommonPreviewSetup
import com.rwmobi.kunigami.ui.composehelper.palette.RatePalette
import com.rwmobi.kunigami.ui.composehelper.shouldUseDarkTheme
import com.rwmobi.kunigami.ui.model.rate.RateGroup
import com.rwmobi.kunigami.ui.model.rate.RateTrend
import com.rwmobi.kunigami.ui.model.rate.findActiveRate
import com.rwmobi.kunigami.ui.model.rate.getRateTrend
import com.rwmobi.kunigami.ui.previewsampledata.TariffSamples
import com.rwmobi.kunigami.ui.theme.cyanish
import com.rwmobi.kunigami.ui.theme.getDimension
import com.rwmobi.kunigami.ui.theme.purpleish
import kotlinx.coroutines.delay
import kotlinx.datetime.Clock
import kunigami.composeapp.generated.resources.Res
import kunigami.composeapp.generated.resources.agile_expire_time
import org.jetbrains.compose.resources.stringResource
import kotlin.time.Duration

private const val DELAY_ONE_SECOND = 1_000L
private const val MILLIS_IN_MINUTE = 60_000

/***
 * The caller should supply the final colorPalette (positive or negative)
 * This composable has no business logic to split positive / negative cases.
 */
@Composable
internal fun AgileTariffCardAdaptive(
    modifier: Modifier = Modifier,
    rateRange: ClosedFloatingPointRange<Double>,
    rateGroupedCells: List<RateGroup>,
    requestedAdaptiveLayout: WindowWidthSizeClass,
    latestFixedTariff: Tariff?,
    latestFlexibleTariff: Tariff?,
) {
    var activeRate by remember { mutableStateOf(rateGroupedCells.findActiveRate(referencePoint = Clock.System.now())) }
    var rateTrend by remember { mutableStateOf(rateGroupedCells.getRateTrend(activeRate = activeRate)) }
    var expireMillis by remember { mutableStateOf(Clock.System.now().getNextHalfHourCountdownMillis()) }
    var expireMinutes by remember { mutableStateOf(expireMillis / MILLIS_IN_MINUTE) }
    var expireSeconds by remember { mutableStateOf((expireMillis / DELAY_ONE_SECOND) % 60) }

    // We split the range into negative and positive as we have two spectrum for them
    val vatInclusivePrice = activeRate?.vatInclusivePrice ?: 0.0
    val effectiveVatInclusivePrice = vatInclusivePrice.coerceIn(rateRange.start, rateRange.endInclusive)
    val targetPercentage = if (vatInclusivePrice >= 0) {
        (effectiveVatInclusivePrice / rateRange.endInclusive).toFloat().coerceIn(0f, 1f)
    } else {
        // both value should be negative
        (effectiveVatInclusivePrice / rateRange.start).toFloat().coerceIn(0f, 1f) * -1
    }
    val rateTrendIconTint = RatePalette.lookupColorFromRange(
        value = effectiveVatInclusivePrice,
        range = rateRange,
        shouldUseDarkTheme = shouldUseDarkTheme(),
    )

    val countDownText = if (activeRate?.validity?.endInclusive != null) {
        stringResource(
            resource = Res.string.agile_expire_time,
            expireMinutes,
            expireSeconds.toString().padStart(length = 2, padChar = '0'),
        )
    } else {
        null
    }

    when (requestedAdaptiveLayout) {
        WindowWidthSizeClass.Compact,
        WindowWidthSizeClass.Medium,
        -> {
            AgileTariffCardCompact(
                modifier = modifier,
                targetPercentage = targetPercentage,
                vatInclusivePrice = activeRate?.vatInclusivePrice,
                rateTrend = rateTrend,
                rateTrendIconTint = rateTrendIconTint,
                countDownText = countDownText,
                latestFlexibleTariff = latestFlexibleTariff,
                latestFixedTariff = latestFixedTariff,
            )
        }

        else -> {
            AgileTariffCardExpanded(
                modifier = modifier,
                targetPercentage = targetPercentage,
                vatInclusivePrice = activeRate?.vatInclusivePrice,
                rateTrend = rateTrend,
                rateTrendIconTint = rateTrendIconTint,
                countDownText = countDownText,
                latestFlexibleTariff = latestFlexibleTariff,
                latestFixedTariff = latestFixedTariff,
            )
        }
    }

    LaunchedEffect(rateGroupedCells) {
        while (true) {
            val isActiveRateExpired =
                (activeRate == null) ||
                    (activeRate?.validity?.endInclusive?.compareTo(Clock.System.now()) ?: 1) <= 0

            if (isActiveRateExpired) {
                activeRate =
                    rateGroupedCells.findActiveRate(referencePoint = Clock.System.now())
                rateTrend = rateGroupedCells.getRateTrend(activeRate = activeRate)
            }

            expireMillis = activeRate?.validity?.endInclusive?.let {
                (it - Clock.System.now()).inWholeMilliseconds
            } ?: Clock.System.now().getNextHalfHourCountdownMillis()

            expireMinutes = expireMillis / MILLIS_IN_MINUTE
            expireSeconds = (expireMillis / DELAY_ONE_SECOND) % 60

            delay(DELAY_ONE_SECOND)
        }
    }
}

@Composable
private fun AgileTariffCardCompact(
    modifier: Modifier = Modifier,
    targetPercentage: Float,
    vatInclusivePrice: Double?,
    countDownText: String?,
    rateTrend: RateTrend?,
    rateTrendIconTint: Color? = null,
    latestFixedTariff: Tariff?,
    latestFlexibleTariff: Tariff?,
) {
    val dimension = LocalDensity.current.getDimension()

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(space = dimension.grid_1),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(intrinsicSize = IntrinsicSize.Min),
            horizontalArrangement = Arrangement.spacedBy(space = dimension.grid_1),
        ) {
            CurrentRateCard(
                modifier = Modifier
                    .weight(weight = 1f)
                    .fillMaxHeight(),
                vatInclusivePrice = vatInclusivePrice,
                rateTrend = rateTrend,
                rateTrendIconTint = rateTrendIconTint,
                textStyle = CurrentRateCardTextStyle(
                    standingChargeStyle = MaterialTheme.typography.bodyMedium,
                    agilePriceStyle = MaterialTheme.typography.headlineSmall,
                    agilePriceUnitStyle = MaterialTheme.typography.bodySmall,
                ),
            )

            RateGaugeCountdownCard(
                modifier = Modifier
                    .weight(weight = 1f)
                    .fillMaxHeight(),
                countDownText = countDownText,
                targetPercentage = targetPercentage,
                colorPalette = RatePalette.getPositiveSpectrum(),
            )
        }

        if (latestFixedTariff != null || latestFlexibleTariff != null) {
            HorizontalDivider(
                modifier = Modifier.height(height = dimension.grid_1),
                thickness = Dp.Hairline,
            )

            LatestTariffsCard(
                modifier = Modifier.fillMaxWidth(),
                latestFlexibleTariff = latestFlexibleTariff,
                latestFixedTariff = latestFixedTariff,
                latestFlexibleTariffColor = cyanish,
                latestFixedTariffColor = purpleish,
            )
        }
    }
}

@Composable
private fun AgileTariffCardExpanded(
    modifier: Modifier = Modifier,
    targetPercentage: Float,
    vatInclusivePrice: Double?,
    countDownText: String?,
    rateTrend: RateTrend?,
    rateTrendIconTint: Color? = null,
    latestFixedTariff: Tariff?,
    latestFlexibleTariff: Tariff?,
) {
    val dimension = LocalDensity.current.getDimension()
    val shouldShowLatestTariff = latestFixedTariff != null || latestFlexibleTariff != null
    val cardCount = 2 + (if (shouldShowLatestTariff) 1 else 0)
    val maxRowWidth = dimension.windowWidthCompact * cardCount

    Box(
        modifier = modifier,
    ) {
        Row(
            modifier = Modifier
                .height(intrinsicSize = IntrinsicSize.Min)
                .widthIn(max = maxRowWidth)
                .align(alignment = Alignment.Center),
            horizontalArrangement = Arrangement.Center,
        ) {
            CurrentRateCard(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                vatInclusivePrice = vatInclusivePrice,
                rateTrend = rateTrend,
                rateTrendIconTint = rateTrendIconTint,
                textStyle = CurrentRateCardTextStyle(
                    standingChargeStyle = MaterialTheme.typography.labelLarge,
                    agilePriceStyle = MaterialTheme.typography.headlineLarge,
                    agilePriceUnitStyle = MaterialTheme.typography.bodyLarge,
                ),
            )

            VerticalDivider(
                modifier = Modifier.width(width = dimension.grid_1),
                thickness = Dp.Hairline,
            )

            RateGaugeCountdownCard(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                countDownText = countDownText,
                targetPercentage = targetPercentage,
                colorPalette = RatePalette.getPositiveSpectrum(),
            )

            if (shouldShowLatestTariff) {
                VerticalDivider(
                    modifier = Modifier.width(width = dimension.grid_1),
                    thickness = Dp.Hairline,
                )

                LatestTariffsCard(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                    latestFlexibleTariff = latestFlexibleTariff,
                    latestFixedTariff = latestFixedTariff,
                    latestFlexibleTariffColor = cyanish,
                    latestFixedTariffColor = purpleish,
                )
            }
        }
    }
}

@Preview
@Composable
private fun PreviewCompact() {
    CommonPreviewSetup { dimension ->
        AgileTariffCardAdaptive(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = dimension.grid_3,
                    end = dimension.grid_3,
                    top = dimension.grid_1,
                ),
            rateRange = 0.0..5.0,
            rateGroupedCells = listOf(
                RateGroup(
                    title = "Sample title",
                    rates = listOf(
                        Rate(
                            vatInclusivePrice = 25.076,
                            validity = Clock.System.now()..Clock.System.now() + Duration.parse("30m"),
                            paymentMethod = PaymentMethod.UNKNOWN,
                        ),
                    ),
                ),
            ),
            latestFlexibleTariff = TariffSamples.agileFlex221125,
            latestFixedTariff = TariffSamples.fix12M240411,
            requestedAdaptiveLayout = WindowWidthSizeClass.Compact,
        )
    }
}

@Preview
@Composable
private fun PreviewExpanded() {
    CommonPreviewSetup { dimension ->
        AgileTariffCardAdaptive(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = dimension.grid_3,
                    end = dimension.grid_3,
                    top = dimension.grid_1,
                ),
            rateRange = 0.0..5.0,
            rateGroupedCells = listOf(
                RateGroup(
                    title = "Sample title",
                    rates = listOf(
                        Rate(
                            vatInclusivePrice = 25.076,
                            validity = Clock.System.now()..Clock.System.now() + Duration.parse("30m"),
                            paymentMethod = PaymentMethod.UNKNOWN,
                        ),
                    ),
                ),
            ),
            requestedAdaptiveLayout = WindowWidthSizeClass.Expanded,
            latestFlexibleTariff = TariffSamples.agileFlex221125,
            latestFixedTariff = TariffSamples.fix12M240411,
        )
    }
}
