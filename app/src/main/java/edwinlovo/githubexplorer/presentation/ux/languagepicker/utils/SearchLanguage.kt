package edwinlovo.githubexplorer.presentation.ux.languagepicker.utils

import androidx.annotation.StringRes
import edwinlovo.githubexplorer.R
import edwinlovo.githubexplorer.presentation.utils.EMPTY_STRING

enum class SearchLanguage(
    @param:StringRes val labelRes: Int,
    val queryValue: String,
) {
    Any(R.string.language_any, EMPTY_STRING),
    C(R.string.language_c, "c"),
    Cpp(R.string.language_cpp, "c++"),
    Go(R.string.language_go, "go"),
    Java(R.string.language_java, "java"),
    JavaScript(R.string.language_javascript, "javascript"),
    Kotlin(R.string.language_kotlin, "kotlin"),
    Python(R.string.language_python, "python"),
    Rust(R.string.language_rust, "rust"),
    Swift(R.string.language_swift, "swift"),
    TypeScript(R.string.language_typescript, "typescript"),
}
