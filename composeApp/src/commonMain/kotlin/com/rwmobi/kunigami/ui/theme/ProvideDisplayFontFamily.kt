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
import kunigami.composeapp.generated.resources.nunito_black
import kunigami.composeapp.generated.resources.nunito_blackitalic
import kunigami.composeapp.generated.resources.nunito_bold
import kunigami.composeapp.generated.resources.nunito_bolditalic
import kunigami.composeapp.generated.resources.nunito_extrabold
import kunigami.composeapp.generated.resources.nunito_extrabolditalic
import kunigami.composeapp.generated.resources.nunito_extralight
import kunigami.composeapp.generated.resources.nunito_extralightitalic
import kunigami.composeapp.generated.resources.nunito_italic
import kunigami.composeapp.generated.resources.nunito_light
import kunigami.composeapp.generated.resources.nunito_lightitalic
import kunigami.composeapp.generated.resources.nunito_medium
import kunigami.composeapp.generated.resources.nunito_mediumitalic
import kunigami.composeapp.generated.resources.nunito_regular
import kunigami.composeapp.generated.resources.nunito_semibold
import kunigami.composeapp.generated.resources.nunito_semibolditalic
import org.jetbrains.compose.resources.Font

@Composable
expect fun provideDisplayFontFamily(
    fontFamily: FontFamily = FontFamily(
        Font(resource = Res.font.nunito_black, weight = FontWeight.Black, style = FontStyle.Normal),
        Font(resource = Res.font.nunito_bold, weight = FontWeight.Bold, style = FontStyle.Normal),
        Font(resource = Res.font.nunito_extrabold, weight = FontWeight.ExtraBold, style = FontStyle.Normal),
        Font(resource = Res.font.nunito_extralight, weight = FontWeight.ExtraLight, style = FontStyle.Normal),
        Font(resource = Res.font.nunito_light, weight = FontWeight.Light, style = FontStyle.Normal),
        Font(resource = Res.font.nunito_medium, weight = FontWeight.Medium, style = FontStyle.Normal),
        Font(resource = Res.font.nunito_regular, weight = FontWeight.Normal, style = FontStyle.Normal),
        Font(resource = Res.font.nunito_semibold, weight = FontWeight.SemiBold, style = FontStyle.Normal),

        Font(resource = Res.font.nunito_blackitalic, weight = FontWeight.Black, style = FontStyle.Italic),
        Font(resource = Res.font.nunito_bolditalic, weight = FontWeight.Bold, style = FontStyle.Italic),
        Font(resource = Res.font.nunito_extrabolditalic, weight = FontWeight.ExtraBold, style = FontStyle.Italic),
        Font(resource = Res.font.nunito_extralightitalic, weight = FontWeight.ExtraLight, style = FontStyle.Italic),
        Font(resource = Res.font.nunito_lightitalic, weight = FontWeight.Light, style = FontStyle.Italic),
        Font(resource = Res.font.nunito_mediumitalic, weight = FontWeight.Medium, style = FontStyle.Italic),
        Font(resource = Res.font.nunito_italic, weight = FontWeight.Normal, style = FontStyle.Italic),
        Font(resource = Res.font.nunito_semibolditalic, weight = FontWeight.SemiBold, style = FontStyle.Italic),
    ),
): FontFamily
