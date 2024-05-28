/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.ui.destinations.agile.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
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
import androidx.compose.ui.unit.dp
import com.rwmobi.kunigami.domain.extensions.getNextHalfHourCountdownMillis
import com.rwmobi.kunigami.domain.model.Tariff
import com.rwmobi.kunigami.ui.components.TariffSummaryCardAdaptive
import com.rwmobi.kunigami.ui.composehelper.generateGYRHueColorPalette
import com.rwmobi.kunigami.ui.model.rate.RateGroupedCells
import com.rwmobi.kunigami.ui.model.rate.RateTrend
import com.rwmobi.kunigami.ui.model.rate.findActiveRate
import com.rwmobi.kunigami.ui.model.rate.getRateTrend
import com.rwmobi.kunigami.ui.previewsampledata.TariffSamples
import com.rwmobi.kunigami.ui.theme.AppTheme
import com.rwmobi.kunigami.ui.theme.getDimension
import io.github.koalaplot.core.util.toString
import kotlinx.coroutines.delay
import kotlinx.datetime.Clock
import kunigami.composeapp.generated.resources.Res
import kunigami.composeapp.generated.resources.agile_different_tariff
import kunigami.composeapp.generated.resources.agile_expires_in
import kunigami.composeapp.generated.resources.agile_tariff_standing_charge
import kunigami.composeapp.generated.resources.p_kwh
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
                vatInclusivePrice = activeRate?.vatInclusivePrice,
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
                vatInclusivePrice = activeRate?.vatInclusivePrice,
                rateTrend = rateTrend,
            )
        }

        else -> {
            AgileTariffCardExpanded(
                modifier = modifier,
                tariff = agileTariff,
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
    vatInclusivePrice: Double?,
    rateTrend: RateTrend?,
) {
    val dimension = LocalDensity.current.getDimension()

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(space = dimension.grid_1),
    ) {
        Card(
            modifier = Modifier,
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
                    val animatedVatInclusivePrice by animateFloatAsState(targetValue = vatInclusivePrice?.toFloat() ?: 0f)
                    if (vatInclusivePrice != null) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(space = dimension.grid_0_5),
                        ) {
                            rateTrend?.let {
                                Icon(
                                    modifier = Modifier.size(size = dimension.grid_4),
                                    tint = colorPalette[(targetPercentage * colorPalette.lastIndex).toInt()],
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
                                text = animatedVatInclusivePrice.toString(precision = 2),
                            )

                            Text(
                                modifier = Modifier.wrapContentSize(),
                                maxLines = 1,
                                overflow = TextOverflow.Clip,
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Bold,
                                text = stringResource(resource = Res.string.p_kwh),
                            )
                        }

                        if (tariff != null) {
                            Text(
                                modifier = Modifier.fillMaxWidth(),
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onSurface,
                                text = stringResource(resource = Res.string.agile_tariff_standing_charge, tariff.vatInclusiveStandingCharge),
                            )
                            Spacer(modifier = Modifier.size(size = dimension.grid_1))
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

        tariff?.let {
            TariffSummaryCardAdaptive(
                modifier = Modifier.fillMaxWidth(),
                heading = stringResource(resource = Res.string.agile_different_tariff).uppercase(),
                headingTextAlign = TextAlign.Start,
                tariff = it,
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
    tariff: Tariff?,
    vatInclusivePrice: Double?,
    rateTrend: RateTrend?,
) {
    val dimension = LocalDensity.current.getDimension()

    Row(
        modifier = modifier.height(intrinsicSize = IntrinsicSize.Min),
        horizontalArrangement = Arrangement.spacedBy(space = dimension.grid_1),
    ) {
        Card(
            modifier = Modifier
                .weight(weight = 2f)
                .fillMaxHeight(),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(all = dimension.grid_2),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column(
                    modifier = Modifier.weight(weight = 1f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(space = dimension.grid_0_5),
                ) {
                    val animatedVatInclusivePrice by animateFloatAsState(targetValue = vatInclusivePrice?.toFloat() ?: 0f)
                    if (vatInclusivePrice != null) {
                        Row(
                            modifier = Modifier.wrapContentSize(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(space = dimension.grid_0_5),
                        ) {
                            rateTrend?.let {
                                Icon(
                                    modifier = Modifier.size(size = dimension.grid_4),
                                    tint = colorPalette[(targetPercentage * colorPalette.lastIndex).toInt()],
                                    painter = painterResource(resource = it.drawableResource),
                                    contentDescription = it.name,
                                )
                            }

                            Text(
                                modifier = Modifier.wrapContentSize(),
                                maxLines = 1,
                                overflow = TextOverflow.Clip,
                                style = MaterialTheme.typography.headlineLarge,
                                fontWeight = FontWeight.Bold,
                                text = animatedVatInclusivePrice.toString(precision = 2),
                            )

                            Text(
                                modifier = Modifier.wrapContentSize(),
                                maxLines = 1,
                                overflow = TextOverflow.Clip,
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.Bold,
                                text = stringResource(resource = Res.string.p_kwh),
                            )
                        }
                    }

                    if (showCountdown) {
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            maxLines = 1,
                            overflow = TextOverflow.Clip,
                            style = MaterialTheme.typography.bodyLarge,
                            textAlign = TextAlign.Center,
                            text = stringResource(
                                resource = Res.string.agile_expires_in,
                                expireMinutes,
                                expireSeconds.toString().padStart(length = 2, padChar = '0'),
                            ),
                        )
                    }
                    Spacer(modifier = Modifier.size(size = dimension.grid_1))
                    if (tariff != null) {
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.onSurface,
                            text = stringResource(resource = Res.string.agile_tariff_standing_charge, tariff.vatInclusiveStandingCharge),
                        )
                    }
                }

                Row(
                    modifier = Modifier.weight(weight = 1f),
                ) {
                    Spacer(modifier = Modifier.weight(0.25f))
                    DashboardWidget(
                        modifier = Modifier
                            .weight(0.5f)
                            .heightIn(max = 120.dp),
                        colorPalette = colorPalette,
                        percentage = targetPercentage,
                    )
                    Spacer(modifier = Modifier.weight(0.25f))
                }
            }
        }

        tariff?.let {
            TariffSummaryCardAdaptive(
                modifier = Modifier
                    .weight(weight = 1f)
                    .fillMaxHeight(),
                heading = stringResource(resource = Res.string.agile_different_tariff).uppercase(),
                headingTextAlign = TextAlign.Start,
                tariff = it,
                layoutType = WindowWidthSizeClass.Compact,
            )
        }
    }
}

@Preview
@Composable
private fun Preview() {
    val dimension = LocalDensity.current.getDimension()
    AppTheme {
        Surface {
            AgileTariffCardAdaptive(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = dimension.grid_3,
                        end = dimension.grid_3,
                        top = dimension.grid_1,
                    ),
                agileTariff = TariffSamples.agileFlex221125,
                colorPalette = generateGYRHueColorPalette(),
                rateRange = 0.0..0.0,
                rateGroupedCells = emptyList(),
                requestedAdaptiveLayout = WindowWidthSizeClass.Expanded,
            )
        }
    }
}
