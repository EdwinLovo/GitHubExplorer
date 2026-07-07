package edwinlovo.githubexplorer.presentation.ux.main

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import edwinlovo.githubexplorer.presentation.ui.navigation.ViewModelNav
import edwinlovo.githubexplorer.presentation.ui.navigation.ViewModelNavImpl
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor() : ViewModel(), ViewModelNav by ViewModelNavImpl()
