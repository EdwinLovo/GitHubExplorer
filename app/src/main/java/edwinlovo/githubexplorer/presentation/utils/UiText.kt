package edwinlovo.githubexplorer.presentation.utils

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import edwinlovo.githubexplorer.R

sealed interface UiText {
    data class StringResource(
        @param:StringRes val id: Int,
    ) : UiText

    data class DynamicString(val value: String) : UiText
}

@Composable
fun UiText.asString(): String =
    when (this) {
        is UiText.StringResource -> stringResource(id)
        is UiText.DynamicString -> value
    }

fun String?.toUiText(): UiText =
    this?.let { UiText.DynamicString(it) } ?: UiText.StringResource(R.string.generic_error)
