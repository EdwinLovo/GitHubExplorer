package edwinlovo.githubexplorer.presentation.ux.users.utils

import androidx.annotation.StringRes
import edwinlovo.githubexplorer.R
import edwinlovo.githubexplorer.domain.model.response.search.AccountType

@StringRes
fun AccountType.labelRes(): Int = when (this) {
    AccountType.USER -> R.string.account_type_user
    AccountType.ORGANIZATION -> R.string.account_type_organization
}
