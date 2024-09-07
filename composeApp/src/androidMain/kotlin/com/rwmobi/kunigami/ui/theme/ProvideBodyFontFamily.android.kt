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
import kunigami.composeapp.generated.resources.pathwayextreme_italic_variablefont
import kunigami.composeapp.generated.resources.pathwayextreme_variablefont
import org.jetbrains.compose.resources.Font

@Composable
actual fun provideBodyFontFamily(fontFamily: FontFamily): FontFamily {
    return FontFamily(
        Font(resource = Res.font.pathwayextreme_variablefont, weight = FontWeight.W100, style = FontStyle.Normal),
        Font(resource = Res.font.pathwayextreme_variablefont, weight = FontWeight.W200, style = FontStyle.Normal),
        Font(resource = Res.font.pathwayextreme_variablefont, weight = FontWeight.W300, style = FontStyle.Normal),
        Font(resource = Res.font.pathwayextreme_variablefont, weight = FontWeight.W400, style = FontStyle.Normal),
        Font(resource = Res.font.pathwayextreme_variablefont, weight = FontWeight.W500, style = FontStyle.Normal),
        Font(resource = Res.font.pathwayextreme_variablefont, weight = FontWeight.W600, style = FontStyle.Normal),
        Font(resource = Res.font.pathwayextreme_variablefont, weight = FontWeight.W700, style = FontStyle.Normal),
        Font(resource = Res.font.pathwayextreme_variablefont, weight = FontWeight.W800, style = FontStyle.Normal),
        Font(resource = Res.font.pathwayextreme_variablefont, weight = FontWeight.W900, style = FontStyle.Normal),
        Font(resource = Res.font.pathwayextreme_italic_variablefont, weight = FontWeight.W100, style = FontStyle.Italic),
        Font(resource = Res.font.pathwayextreme_italic_variablefont, weight = FontWeight.W200, style = FontStyle.Italic),
        Font(resource = Res.font.pathwayextreme_italic_variablefont, weight = FontWeight.W300, style = FontStyle.Italic),
        Font(resource = Res.font.pathwayextreme_italic_variablefont, weight = FontWeight.W400, style = FontStyle.Italic),
        Font(resource = Res.font.pathwayextreme_italic_variablefont, weight = FontWeight.W500, style = FontStyle.Italic),
        Font(resource = Res.font.pathwayextreme_italic_variablefont, weight = FontWeight.W600, style = FontStyle.Italic),
        Font(resource = Res.font.pathwayextreme_italic_variablefont, weight = FontWeight.W700, style = FontStyle.Italic),
        Font(resource = Res.font.pathwayextreme_italic_variablefont, weight = FontWeight.W800, style = FontStyle.Italic),
        Font(resource = Res.font.pathwayextreme_italic_variablefont, weight = FontWeight.W900, style = FontStyle.Italic),
    )
}
