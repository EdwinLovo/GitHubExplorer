package edwinlovo.githubexplorer.presentation.utils.ext

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

fun <T> MutableStateFlow<T>.reduce(block: T.() -> T) = update { it.block() }
