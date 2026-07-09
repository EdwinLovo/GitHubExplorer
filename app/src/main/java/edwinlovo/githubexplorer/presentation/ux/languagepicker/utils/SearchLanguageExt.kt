package edwinlovo.githubexplorer.presentation.ux.languagepicker.utils

fun String.toSearchLanguage(): SearchLanguage =
    SearchLanguage.entries.find { it.queryValue == this } ?: SearchLanguage.Any
