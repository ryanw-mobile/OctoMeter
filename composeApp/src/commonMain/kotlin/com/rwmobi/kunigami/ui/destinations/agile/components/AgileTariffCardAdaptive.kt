/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.ui.destinations.agile.components

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import com.rwmobi.kunigami.domain.extensions.getNextHalfHourCountdownMillis
import com.rwmobi.kunigami.domain.model.product.TariffSummary
import com.rwmobi.kunigami.domain.model.rate.PaymentMethod
import com.rwmobi.kunigami.domain.model.rate.Rate
import com.rwmobi.kunigami.ui.components.CommonPreviewSetup
import com.rwmobi.kunigami.ui.components.TariffSummaryCardAdaptive
import com.rwmobi.kunigami.ui.composehelper.generateGYRHueColorPalette
import com.rwmobi.kunigami.ui.model.rate.RateGroupedCells
import com.rwmobi.kunigami.ui.model.rate.RateTrend
import com.rwmobi.kunigami.ui.model.rate.findActiveRate
import com.rwmobi.kunigami.ui.model.rate.getRateTrend
import com.rwmobi.kunigami.ui.previewsampledata.TariffSamples
import com.rwmobi.kunigami.ui.theme.getDimension
import kotlinx.coroutines.delay
import kotlinx.datetime.Clock
import kunigami.composeapp.generated.resources.Res
import kunigami.composeapp.generated.resources.agile_different_tariff
import kunigami.composeapp.generated.resources.agile_expire_time
import org.jetbrains.compose.resources.stringResource

private const val DELAY_ONE_SECOND = 1_000L
private const val MILLIS_IN_MINUTE = 60_000

@Composable
internal fun AgileTariffCardAdaptive(
    modifier: Modifier = Modifier,
    colorPalette: List<Color>,
    rateRange: ClosedFloatingPointRange<Double>,
    rateGroupedCells: List<RateGroupedCells>,
    requestedAdaptiveLayout: WindowWidthSizeClass,
    agileTariffSummary: TariffSummary?,
    differentTariffSummary: TariffSummary?,
) {
    var activeRate by remember { mutableStateOf(rateGroupedCells.findActiveRate(pointOfReference = Clock.System.now())) }
    var rateTrend by remember { mutableStateOf(rateGroupedCells.getRateTrend(activeRate = activeRate)) }
    var expireMillis by remember { mutableStateOf(Clock.System.now().getNextHalfHourCountdownMillis()) }
    var expireMinutes by remember { mutableStateOf(expireMillis / MILLIS_IN_MINUTE) }
    var expireSeconds by remember { mutableStateOf((expireMillis / DELAY_ONE_SECOND) % 60) }
    val targetPercentage = ((activeRate?.vatInclusivePrice ?: 0.0) / rateRange.endInclusive).toFloat().coerceIn(0f, 1f)

    when (requestedAdaptiveLayout) {
        WindowWidthSizeClass.Compact,
        WindowWidthSizeClass.Medium,
        -> {
            AgileTariffCardCompact(
                modifier = modifier,
                agileTariffSummary = agileTariffSummary,
                differentTariffSummary = differentTariffSummary,
                colorPalette = colorPalette,
                targetPercentage = targetPercentage,
                expireMinutes = expireMinutes,
                expireSeconds = expireSeconds,
                showCountdown = activeRate?.validTo != null,
                vatInclusivePrice = activeRate?.vatInclusivePrice,
                rateTrend = rateTrend,
            )
        }

        else -> {
            AgileTariffCardExpanded(
                modifier = modifier,
                agileTariffSummary = agileTariffSummary,
                differentTariffSummary = differentTariffSummary,
                colorPalette = colorPalette,
                targetPercentage = targetPercentage,
                expireMinutes = expireMinutes,
                expireSeconds = expireSeconds,
                showCountdown = activeRate?.validTo != null,
                vatInclusivePrice = activeRate?.vatInclusivePrice,
                rateTrend = rateTrend,
            )
        }
    }

    LaunchedEffect(true) {
        while (true) {
            with(activeRate) {
                val isActiveRateExpired =
                    (this == null) ||
                        (validTo?.compareTo(Clock.System.now()) ?: 1) <= 0

                if (isActiveRateExpired) {
                    activeRate =
                        rateGroupedCells.findActiveRate(pointOfReference = Clock.System.now())
                    rateTrend = rateGroupedCells.getRateTrend(activeRate = activeRate)
                }

                expireMillis = activeRate?.validTo?.let {
                    (it - Clock.System.now()).inWholeMilliseconds
                } ?: Clock.System.now().getNextHalfHourCountdownMillis()

                expireMinutes = expireMillis / MILLIS_IN_MINUTE
                expireSeconds = (expireMillis / DELAY_ONE_SECOND) % 60
            }

            delay(DELAY_ONE_SECOND)
        }
    }
}

@Composable
private fun AgileTariffCardCompact(
    modifier: Modifier = Modifier,
    targetPercentage: Float,
    showCountdown: Boolean,
    expireMinutes: Long,
    expireSeconds: Long,
    colorPalette: List<Color>,
    vatInclusivePrice: Double?,
    agileTariffSummary: TariffSummary?,
    differentTariffSummary: TariffSummary?,
    rateTrend: RateTrend?,
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
            AgilePriceCard(
                modifier = Modifier
                    .weight(weight = 1f)
                    .fillMaxHeight(),
                vatInclusivePrice = vatInclusivePrice,
                rateTrend = rateTrend,
                colorPalette = colorPalette,
                targetPercentage = targetPercentage,
                agileTariffSummary = agileTariffSummary,
                textStyle = AgilePriceCardTextStyle(
                    standingChargeStyle = MaterialTheme.typography.bodyMedium,
                    agilePriceStyle = MaterialTheme.typography.headlineSmall,
                    agilePriceUnitStyle = MaterialTheme.typography.bodySmall,
                ),
            )

            DashboardWidgetCard(
                modifier = Modifier
                    .weight(weight = 1f)
                    .fillMaxHeight(),
                countDownText = if (showCountdown) {
                    stringResource(
                        resource = Res.string.agile_expire_time,
                        expireMinutes,
                        expireSeconds.toString().padStart(length = 2, padChar = '0'),
                    )
                } else {
                    null
                },
                targetPercentage = targetPercentage,
                colorPalette = colorPalette,
            )
        }

        differentTariffSummary?.let {
            TariffSummaryCardAdaptive(
                modifier = Modifier.fillMaxWidth(),
                heading = stringResource(resource = Res.string.agile_different_tariff).uppercase(),
                headingTextAlign = TextAlign.Start,
                tariffSummary = it,
                layoutType = WindowWidthSizeClass.Compact,
            )
        }
    }
}

@Composable
private fun AgileTariffCardExpanded(
    modifier: Modifier = Modifier,
    targetPercentage: Float,
    showCountdown: Boolean,
    expireMinutes: Long,
    expireSeconds: Long,
    colorPalette: List<Color>,
    agileTariffSummary: TariffSummary?,
    differentTariffSummary: TariffSummary?,
    vatInclusivePrice: Double?,
    rateTrend: RateTrend?,
) {
    val dimension = LocalDensity.current.getDimension()

    Row(
        modifier = modifier.height(intrinsicSize = IntrinsicSize.Min),
        horizontalArrangement = Arrangement.spacedBy(space = dimension.grid_1),
    ) {
        AgilePriceCard(
            modifier = Modifier
                .weight(weight = 1f)
                .fillMaxHeight(),
            vatInclusivePrice = vatInclusivePrice,
            rateTrend = rateTrend,
            colorPalette = colorPalette,
            targetPercentage = targetPercentage,
            agileTariffSummary = agileTariffSummary,
            textStyle = AgilePriceCardTextStyle(
                standingChargeStyle = MaterialTheme.typography.labelLarge,
                agilePriceStyle = MaterialTheme.typography.headlineLarge,
                agilePriceUnitStyle = MaterialTheme.typography.bodyLarge,
            ),
        )

        DashboardWidgetCard(
            modifier = Modifier
                .weight(weight = 1f)
                .fillMaxHeight(),
            countDownText = if (showCountdown) {
                stringResource(
                    resource = Res.string.agile_expire_time,
                    expireMinutes,
                    expireSeconds.toString().padStart(length = 2, padChar = '0'),
                )
            } else {
                null
            },
            targetPercentage = targetPercentage,
            colorPalette = colorPalette,
        )

        differentTariffSummary?.let {
            TariffSummaryCardAdaptive(
                modifier = Modifier
                    .weight(weight = 1f)
                    .fillMaxHeight(),
                heading = stringResource(resource = Res.string.agile_different_tariff).uppercase(),
                headingTextAlign = TextAlign.Start,
                tariffSummary = it,
                layoutType = WindowWidthSizeClass.Compact,
            )
        }
    }
}

@Preview
@Composable
private fun Preview() {
    CommonPreviewSetup { dimension ->
        AgileTariffCardAdaptive(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = dimension.grid_3,
                    end = dimension.grid_3,
                    top = dimension.grid_1,
                ),
            agileTariffSummary = TariffSamples.agileFlex221125,
            differentTariffSummary = TariffSamples.agileFlex221125,
            colorPalette = generateGYRHueColorPalette(),
            rateRange = 0.0..5.0,
            rateGroupedCells = listOf(
                RateGroupedCells(
                    title = "Sample title",
                    rates = listOf(
                        Rate(
                            vatExclusivePrice = 45.075,
                            vatInclusivePrice = 25.076,
                            validFrom = Clock.System.now(),
                            validTo = null,
                            paymentMethod = PaymentMethod.UNKNOWN,
                        ),
                    ),
                ),
            ),
            requestedAdaptiveLayout = WindowWidthSizeClass.Compact,
        )
    }
}
