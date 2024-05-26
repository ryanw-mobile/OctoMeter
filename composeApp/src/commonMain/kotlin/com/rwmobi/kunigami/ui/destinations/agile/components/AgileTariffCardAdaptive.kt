/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.ui.destinations.agile.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import com.rwmobi.kunigami.domain.extensions.getNextHalfHourCountdownMillis
import com.rwmobi.kunigami.domain.model.Tariff
import com.rwmobi.kunigami.ui.model.rate.RateGroupedCells
import com.rwmobi.kunigami.ui.model.rate.RateTrend
import com.rwmobi.kunigami.ui.model.rate.findActiveRate
import com.rwmobi.kunigami.ui.model.rate.getRateTrend
import com.rwmobi.kunigami.ui.theme.getDimension
import io.github.koalaplot.core.util.toString
import kotlinx.coroutines.delay
import kotlinx.datetime.Clock
import kunigami.composeapp.generated.resources.Res
import kunigami.composeapp.generated.resources.agile_expires_in
import kunigami.composeapp.generated.resources.agile_product_code_retail_region
import kunigami.composeapp.generated.resources.agile_tariff_standing_charge
import kunigami.composeapp.generated.resources.agile_unit_rate
import kunigami.composeapp.generated.resources.unknown
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun AgileTariffCardAdaptive(
    modifier: Modifier = Modifier,
    agileTariff: Tariff?,
    colorPalette: List<Color>,
    rateRange: ClosedFloatingPointRange<Double>,
    rateGroupedCells: List<RateGroupedCells>,
    requestedAdaptiveLayout: WindowWidthSizeClass,
) {
    var activeRate by remember { mutableStateOf(rateGroupedCells.findActiveRate(pointOfReference = Clock.System.now())) }
    var rateTrend by remember { mutableStateOf(rateGroupedCells.getRateTrend(activeRate = activeRate)) }
    var expireMillis by remember { mutableStateOf(Clock.System.now().getNextHalfHourCountdownMillis()) }
    var expireMinutes by remember { mutableStateOf(expireMillis / 60_000) }
    var expireSeconds by remember { mutableStateOf((expireMillis / 1_000) % 60) }
    val targetPercentage = ((activeRate?.vatInclusivePrice ?: 0.0) / rateRange.endInclusive).toFloat().coerceIn(0f, 1f)

    when (requestedAdaptiveLayout) {
        WindowWidthSizeClass.Compact -> {
            AgileTariffCardCompact(
                modifier = modifier,
                tariff = agileTariff,
                colorPalette = colorPalette,
                targetPercentage = targetPercentage,
                expireMinutes = expireMinutes,
                expireSeconds = expireSeconds,
                showCountdown = activeRate?.validTo != null,
                vatInclusivePrice = activeRate?.vatInclusivePrice?.toString(precision = 2),
                rateTrend = rateTrend,
            )
        }

        WindowWidthSizeClass.Medium -> {
            AgileTariffCardCompact(
                modifier = modifier,
                tariff = agileTariff,
                colorPalette = colorPalette,
                targetPercentage = targetPercentage,
                expireMinutes = expireMinutes,
                expireSeconds = expireSeconds,
                showCountdown = activeRate?.validTo != null,
                vatInclusivePrice = activeRate?.vatInclusivePrice?.toString(precision = 2),
                rateTrend = rateTrend,
            )
        }

        else -> {
            AgileTariffCardCompact(
                modifier = modifier,
                tariff = agileTariff,
                colorPalette = colorPalette,
                targetPercentage = targetPercentage,
                expireMinutes = expireMinutes,
                expireSeconds = expireSeconds,
                showCountdown = activeRate?.validTo != null,
                vatInclusivePrice = activeRate?.vatInclusivePrice?.toString(precision = 2),
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

                expireMinutes = expireMillis / 60_000
                expireSeconds = (expireMillis / 1_000) % 60
            }

            delay(1_000L)
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
    tariff: Tariff?,
    vatInclusivePrice: String?,
    rateTrend: RateTrend?,
) {
    val dimension = LocalDensity.current.getDimension()

    Card(
        modifier = modifier,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = dimension.grid_2),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(
                modifier = Modifier.weight(weight = 2f / 3),
            ) {
                if (tariff != null) {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.Bold,
                        text = tariff.displayName,
                    )

                    val regionCode = tariff.getRetailRegion() ?: stringResource(resource = Res.string.unknown)
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(
                            alpha = 0.68f,
                        ),
                        text = stringResource(resource = Res.string.agile_product_code_retail_region, tariff.productCode, regionCode),
                    )

                    Spacer(modifier = Modifier.size(size = dimension.grid_1))

                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                        text = stringResource(resource = Res.string.agile_tariff_standing_charge, tariff.vatInclusiveStandingCharge),
                    )
                    Spacer(modifier = Modifier.size(size = dimension.grid_1))
                }

                if (vatInclusivePrice != null) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(space = dimension.grid_0_5),
                    ) {
                        rateTrend?.let {
                            Icon(
                                modifier = Modifier.size(size = dimension.grid_4),
                                tint = colorPalette[targetPercentage.toInt()],
                                painter = painterResource(resource = it.drawableResource),
                                contentDescription = it.name,
                            )
                        }

                        Text(
                            modifier = Modifier.wrapContentSize(),
                            maxLines = 1,
                            overflow = TextOverflow.Clip,
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                            text = vatInclusivePrice,
                        )

                        Text(
                            modifier = Modifier.wrapContentSize(),
                            maxLines = 1,
                            overflow = TextOverflow.Clip,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            text = stringResource(resource = Res.string.agile_unit_rate),
                        )
                    }
                }
            }

            Column(
                modifier = Modifier.weight(weight = 1f / 3),
                verticalArrangement = Arrangement.spacedBy(space = dimension.grid_2),
            ) {
                DashboardWidget(
                    modifier = Modifier.fillMaxWidth(),
                    colorPalette = colorPalette,
                    percentage = targetPercentage,
                )

                if (showCountdown) {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        maxLines = 1,
                        overflow = TextOverflow.Clip,
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.labelMedium,
                        text = stringResource(
                            resource = Res.string.agile_expires_in,
                            expireMinutes,
                            expireSeconds.toString().padStart(length = 2, padChar = '0'),
                        ),
                    )
                }
            }
        }
    }
}

// @Preview
// @Composable
// private fun Preview() {
//    val dimension = LocalDensity.current.getDimension()
//    AppTheme {
//        Surface {
//            AgileTariffCardCompact(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(
//                        start = dimension.grid_3,
//                        end = dimension.grid_3,
//                        top = dimension.grid_1,
//                    ),
//                tariff = TariffSamples.agileFlex221125,
//                colorPalette = generateGYRHueColorPalette(),
//                rateRange = 0.0..0.0,
//                rateGroupedCells = emptyList(),
//            )
//        }
//    }
// }
