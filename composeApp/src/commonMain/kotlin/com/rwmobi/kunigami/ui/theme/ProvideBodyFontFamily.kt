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
import kunigami.composeapp.generated.resources.pathwayextreme_14pt_blackitalic
import kunigami.composeapp.generated.resources.pathwayextreme_14pt_bold
import kunigami.composeapp.generated.resources.pathwayextreme_14pt_bolditalic
import kunigami.composeapp.generated.resources.pathwayextreme_14pt_extrabold
import kunigami.composeapp.generated.resources.pathwayextreme_14pt_extrabolditalic
import kunigami.composeapp.generated.resources.pathwayextreme_14pt_extralight
import kunigami.composeapp.generated.resources.pathwayextreme_14pt_extralightitalic
import kunigami.composeapp.generated.resources.pathwayextreme_14pt_italic
import kunigami.composeapp.generated.resources.pathwayextreme_14pt_light
import kunigami.composeapp.generated.resources.pathwayextreme_14pt_lightitalic
import kunigami.composeapp.generated.resources.pathwayextreme_14pt_medium
import kunigami.composeapp.generated.resources.pathwayextreme_14pt_mediumitalic
import kunigami.composeapp.generated.resources.pathwayextreme_14pt_regular
import kunigami.composeapp.generated.resources.pathwayextreme_14pt_semibold
import kunigami.composeapp.generated.resources.pathwayextreme_14pt_semibolditalic
import kunigami.composeapp.generated.resources.pathwayextreme_14pt_thin
import kunigami.composeapp.generated.resources.pathwayextreme_14pt_thinitalic
import org.jetbrains.compose.resources.Font

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

        Font(resource = Res.font.pathwayextreme_14pt_blackitalic, weight = FontWeight.Black, style = FontStyle.Italic),
        Font(resource = Res.font.pathwayextreme_14pt_bolditalic, weight = FontWeight.Bold, style = FontStyle.Italic),
        Font(resource = Res.font.pathwayextreme_14pt_extrabolditalic, weight = FontWeight.ExtraBold, style = FontStyle.Italic),
        Font(resource = Res.font.pathwayextreme_14pt_extralightitalic, weight = FontWeight.ExtraLight, style = FontStyle.Italic),
        Font(resource = Res.font.pathwayextreme_14pt_lightitalic, weight = FontWeight.Light, style = FontStyle.Italic),
        Font(resource = Res.font.pathwayextreme_14pt_mediumitalic, weight = FontWeight.Medium, style = FontStyle.Italic),
        Font(resource = Res.font.pathwayextreme_14pt_italic, weight = FontWeight.Normal, style = FontStyle.Italic),
        Font(resource = Res.font.pathwayextreme_14pt_semibolditalic, weight = FontWeight.SemiBold, style = FontStyle.Italic),
        Font(resource = Res.font.pathwayextreme_14pt_thinitalic, weight = FontWeight.Thin, style = FontStyle.Italic),
    ),
): FontFamily
