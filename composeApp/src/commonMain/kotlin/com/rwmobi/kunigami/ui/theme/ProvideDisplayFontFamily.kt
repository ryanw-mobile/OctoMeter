/*
 * Copyright (c) 2024. RW MobiMedia UK Limited
 *
 * Contributions made by other developers remain the property of their respective authors but are licensed
 * to RW MobiMedia UK Limited and others under the same licence terms as the main project, as outlined in
 * the LICENSE file.
 *
 * RW MobiMedia UK Limited reserves the exclusive right to distribute this application on app stores.
 * Reuse of this source code, with or without modifications, requires proper attribution to
 * RW MobiMedia UK Limited.  Commercial distribution of this code or its derivatives without prior written
 * permission from RW MobiMedia UK Limited is prohibited.
 *
 * Please refer to the LICENSE file for the full terms and conditions.
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
