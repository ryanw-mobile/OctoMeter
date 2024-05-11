package com.rwmobi.kunigami.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import kunigami.composeapp.generated.resources.Res
import kunigami.composeapp.generated.resources.nunito_italic_variablefont
import kunigami.composeapp.generated.resources.nunito_variablefont
import kunigami.composeapp.generated.resources.pathwayextreme_italic_variablefont
import kunigami.composeapp.generated.resources.pathwayextreme_variablefont
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.Font

@OptIn(ExperimentalResourceApi::class)
@Composable
internal fun AppTypography(): Typography {
    val bodyFontFamily = FontFamily(
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
    val displayFontFamily = FontFamily(
        Font(resource = Res.font.nunito_variablefont, weight = FontWeight.W100, style = FontStyle.Normal),
        Font(resource = Res.font.nunito_variablefont, weight = FontWeight.W200, style = FontStyle.Normal),
        Font(resource = Res.font.nunito_variablefont, weight = FontWeight.W300, style = FontStyle.Normal),
        Font(resource = Res.font.nunito_variablefont, weight = FontWeight.W400, style = FontStyle.Normal),
        Font(resource = Res.font.nunito_variablefont, weight = FontWeight.W500, style = FontStyle.Normal),
        Font(resource = Res.font.nunito_variablefont, weight = FontWeight.W600, style = FontStyle.Normal),
        Font(resource = Res.font.nunito_variablefont, weight = FontWeight.W700, style = FontStyle.Normal),
        Font(resource = Res.font.nunito_variablefont, weight = FontWeight.W800, style = FontStyle.Normal),
        Font(resource = Res.font.nunito_variablefont, weight = FontWeight.W900, style = FontStyle.Normal),
        Font(resource = Res.font.nunito_italic_variablefont, weight = FontWeight.W100, style = FontStyle.Italic),
        Font(resource = Res.font.nunito_italic_variablefont, weight = FontWeight.W200, style = FontStyle.Italic),
        Font(resource = Res.font.nunito_italic_variablefont, weight = FontWeight.W300, style = FontStyle.Italic),
        Font(resource = Res.font.nunito_italic_variablefont, weight = FontWeight.W400, style = FontStyle.Italic),
        Font(resource = Res.font.nunito_italic_variablefont, weight = FontWeight.W500, style = FontStyle.Italic),
        Font(resource = Res.font.nunito_italic_variablefont, weight = FontWeight.W600, style = FontStyle.Italic),
        Font(resource = Res.font.nunito_italic_variablefont, weight = FontWeight.W700, style = FontStyle.Italic),
        Font(resource = Res.font.nunito_italic_variablefont, weight = FontWeight.W800, style = FontStyle.Italic),
        Font(resource = Res.font.nunito_italic_variablefont, weight = FontWeight.W900, style = FontStyle.Italic),
    )

    // Default Material 3 typography values
    val baseline = Typography()

    return Typography(
        displayLarge = baseline.displayLarge.copy(fontFamily = displayFontFamily),
        displayMedium = baseline.displayMedium.copy(fontFamily = displayFontFamily),
        displaySmall = baseline.displaySmall.copy(fontFamily = displayFontFamily),
        headlineLarge = baseline.headlineLarge.copy(fontFamily = displayFontFamily),
        headlineMedium = baseline.headlineMedium.copy(fontFamily = displayFontFamily),
        headlineSmall = baseline.headlineSmall.copy(fontFamily = displayFontFamily),
        titleLarge = baseline.titleLarge.copy(fontFamily = displayFontFamily),
        titleMedium = baseline.titleMedium.copy(fontFamily = displayFontFamily),
        titleSmall = baseline.titleSmall.copy(fontFamily = displayFontFamily),
        bodyLarge = baseline.bodyLarge.copy(fontFamily = bodyFontFamily),
        bodyMedium = baseline.bodyMedium.copy(fontFamily = bodyFontFamily),
        bodySmall = baseline.bodySmall.copy(fontFamily = bodyFontFamily),
        labelLarge = baseline.labelLarge.copy(fontFamily = bodyFontFamily),
        labelMedium = baseline.labelMedium.copy(fontFamily = bodyFontFamily),
        labelSmall = baseline.labelSmall.copy(fontFamily = bodyFontFamily),
    )
}
