/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import kunigami.composeapp.generated.resources.Res
import kunigami.composeapp.generated.resources.pathwayextreme_14pt_black
import kunigami.composeapp.generated.resources.pathwayextreme_14pt_bold
import kunigami.composeapp.generated.resources.pathwayextreme_14pt_extrabold
import kunigami.composeapp.generated.resources.pathwayextreme_14pt_extralight
import kunigami.composeapp.generated.resources.pathwayextreme_14pt_light
import kunigami.composeapp.generated.resources.pathwayextreme_14pt_medium
import kunigami.composeapp.generated.resources.pathwayextreme_14pt_regular
import kunigami.composeapp.generated.resources.pathwayextreme_14pt_semibold
import kunigami.composeapp.generated.resources.pathwayextreme_14pt_thin
import kunigami.composeapp.generated.resources.pathwayextreme_italic_variablefont
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.Font

@OptIn(ExperimentalResourceApi::class)
@Composable
expect fun provideBodyFontFamily(
    fontFamily: FontFamily = FontFamily(
        Font(resource = Res.font.pathwayextreme_14pt_black, weight = FontWeight.Black, style = FontStyle.Normal),
        Font(resource = Res.font.pathwayextreme_14pt_bold, weight = FontWeight.Bold, style = FontStyle.Normal),
        Font(resource = Res.font.pathwayextreme_14pt_extrabold, weight = FontWeight.ExtraBold, style = FontStyle.Normal),
        Font(resource = Res.font.pathwayextreme_14pt_extralight, weight = FontWeight.ExtraLight, style = FontStyle.Normal),
        Font(resource = Res.font.pathwayextreme_14pt_light, weight = FontWeight.Light, style = FontStyle.Normal),
        Font(resource = Res.font.pathwayextreme_14pt_medium, weight = FontWeight.Medium, style = FontStyle.Normal),
        Font(resource = Res.font.pathwayextreme_14pt_regular, weight = FontWeight.Normal, style = FontStyle.Normal),
        Font(resource = Res.font.pathwayextreme_14pt_semibold, weight = FontWeight.SemiBold, style = FontStyle.Normal),
        Font(resource = Res.font.pathwayextreme_14pt_thin, weight = FontWeight.Thin, style = FontStyle.Normal),
        Font(resource = Res.font.pathwayextreme_italic_variablefont, weight = FontWeight.W100, style = FontStyle.Italic),
        Font(resource = Res.font.pathwayextreme_italic_variablefont, weight = FontWeight.W200, style = FontStyle.Italic),
        Font(resource = Res.font.pathwayextreme_italic_variablefont, weight = FontWeight.W300, style = FontStyle.Italic),
        Font(resource = Res.font.pathwayextreme_italic_variablefont, weight = FontWeight.W400, style = FontStyle.Italic),
        Font(resource = Res.font.pathwayextreme_italic_variablefont, weight = FontWeight.W500, style = FontStyle.Italic),
        Font(resource = Res.font.pathwayextreme_italic_variablefont, weight = FontWeight.W600, style = FontStyle.Italic),
        Font(resource = Res.font.pathwayextreme_italic_variablefont, weight = FontWeight.W700, style = FontStyle.Italic),
        Font(resource = Res.font.pathwayextreme_italic_variablefont, weight = FontWeight.W800, style = FontStyle.Italic),
        Font(resource = Res.font.pathwayextreme_italic_variablefont, weight = FontWeight.W900, style = FontStyle.Italic),
    ),
): FontFamily
