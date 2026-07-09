package edwinlovo.githubexplorer.presentation.utils

import edwinlovo.githubexplorer.R

/**
 * Every icon in the app is registered here — do not use Material Icons or import `Icons.*`.
 *
 * To add a new icon:
 * 1. Drop the vector drawable XML into `res/drawable/`, e.g. `ic_settings.xml`.
 * 2. Register it here: `val Settings = R.drawable.ic_settings`.
 * 3. Use it: `Icon(painterResource(GheIcons.Settings), contentDescription = ...)` for tinted vectors,
 *    or `Image(painterResource(GheIcons.SomeArt))` for full-color raster images.
 */
object GheIcons {
    val ArrowBack = R.drawable.ic_arrow_back
    val Close = R.drawable.ic_close
    val Delete = R.drawable.ic_delete
    val Eye = R.drawable.ic_eye
    val Favorite = R.drawable.ic_favorite
    val FavoriteOutline = R.drawable.ic_favorite_outline
    val Filter = R.drawable.ic_filter
    val Fork = R.drawable.ic_fork
    val Issue = R.drawable.ic_issue
    val OpenInNew = R.drawable.ic_open_in_new
    val Search = R.drawable.ic_search
    val Star = R.drawable.ic_star
    val Users = R.drawable.ic_users
}
