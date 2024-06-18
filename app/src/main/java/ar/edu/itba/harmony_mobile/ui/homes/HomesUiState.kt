package ar.edu.itba.harmony_mobile.ui.homes

import ar.edu.itba.harmony_mobile.model.Home
import ar.edu.itba.harmony_mobile.model.Error

data class HomesUiState(
    val loading: Boolean = false,
    val error: Error? = null,
    val homes: List<Home> = emptyList(),
    val currentHome: Home? = null
)

val HomesUiState.canGetCurrent: Boolean get() = currentHome != null
val HomesUiState.canModify: Boolean get() = currentHome != null
val HomesUiState.canDelete: Boolean get() = canModify
