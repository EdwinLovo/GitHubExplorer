package edwinlovo.githubexplorer.presentation.utils.ext

import edwinlovo.githubexplorer.presentation.utils.EMPTY_STRING
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.Locale

private val displayFormatter: DateTimeFormatter =
    DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)

fun String.toFormattedDate(): String = runCatching {
    Instant.parse(this)
        .atZone(ZoneId.systemDefault())
        .toLocalDate()
        .format(displayFormatter.withLocale(Locale.getDefault()))
}.getOrDefault(EMPTY_STRING)
