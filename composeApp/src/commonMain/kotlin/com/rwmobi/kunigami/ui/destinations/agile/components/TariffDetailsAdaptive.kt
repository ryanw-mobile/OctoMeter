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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import com.rwmobi.kunigami.ui.theme.getDimension

@Composable
internal fun TariffDetailsAdaptive(
    modifier: Modifier = Modifier,
    agileTariffBlock: @Composable ((modifier: Modifier) -> Unit)?,
    countDownWidget: @Composable ((modifier: Modifier) -> Unit)?,
    currentTariffBlock: @Composable ((modifier: Modifier) -> Unit)?,
    windowWidthSizeClass: WindowWidthSizeClass,
) {
    when (windowWidthSizeClass) {
        WindowWidthSizeClass.Compact -> {
            TariffDetailsCompact(
                modifier = modifier,
                agileTariffBlock = agileTariffBlock,
                countDownWidget = countDownWidget,
                currentTariffBlock = currentTariffBlock,
            )
        }

        WindowWidthSizeClass.Medium -> {
            TariffDetailsMedium(
                modifier = modifier,
                agileTariffBlock = agileTariffBlock,
                countDownWidget = countDownWidget,
                currentTariffBlock = currentTariffBlock,
            )
        }

        else -> {
            TariffDetailsExpanded(
                modifier = modifier,
                agileTariffBlock = agileTariffBlock,
                countDownWidget = countDownWidget,
                currentTariffBlock = currentTariffBlock,
            )
        }
    }
}

@Composable
private fun TariffDetailsCompact(
    modifier: Modifier = Modifier,
    agileTariffBlock: @Composable ((modifier: Modifier) -> Unit)?,
    countDownWidget: @Composable ((modifier: Modifier) -> Unit)?,
    currentTariffBlock: @Composable ((modifier: Modifier) -> Unit)?,
) {
    val dimension = LocalDensity.current.getDimension()

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(space = dimension.grid_1),
    ) {
        countDownWidget?.let { it(Modifier.fillMaxWidth()) }
        agileTariffBlock?.let { it(Modifier.fillMaxWidth()) }
        currentTariffBlock?.let { it(Modifier.fillMaxWidth()) }
    }
}

@Composable
private fun TariffDetailsMedium(
    modifier: Modifier = Modifier,
    agileTariffBlock: @Composable ((modifier: Modifier) -> Unit)?,
    countDownWidget: @Composable ((modifier: Modifier) -> Unit)?,
    currentTariffBlock: @Composable ((modifier: Modifier) -> Unit)?,
) {
    val dimension = LocalDensity.current.getDimension()
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = dimension.grid_2),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(space = dimension.grid_2),
    ) {
        Column(modifier = Modifier.weight(0.6f)) {
            agileTariffBlock?.let { it(Modifier.fillMaxWidth()) }
            currentTariffBlock?.let { it(Modifier.fillMaxWidth()) }
        }
        countDownWidget?.let { it(Modifier.weight(0.4f)) }
    }
}

@Composable
private fun TariffDetailsExpanded(
    modifier: Modifier = Modifier,
    agileTariffBlock: @Composable ((modifier: Modifier) -> Unit)?,
    countDownWidget: @Composable ((modifier: Modifier) -> Unit)?,
    currentTariffBlock: @Composable ((modifier: Modifier) -> Unit)?,
) {
    val dimension = LocalDensity.current.getDimension()
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = dimension.grid_2),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(space = dimension.grid_2),
    ) {
        agileTariffBlock?.let { it(Modifier.weight(2f)) }
        countDownWidget?.let { it(Modifier.weight(1f)) }
        currentTariffBlock?.let { it(Modifier.weight(2f)) }
    }
}
