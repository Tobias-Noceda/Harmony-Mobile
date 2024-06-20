package ar.edu.itba.harmony_mobile.ui.homes

import ar.edu.itba.harmony_mobile.model.Home
import ar.edu.itba.harmony_mobile.model.Error
import ar.edu.itba.harmony_mobile.remote.GlobalDataHomes

data class HomesUiState(
    val loading: Boolean = false,
    val error: Error? = null,
    val homes: List<Home> = emptyList(),
    val currentHome: Home? = null
) {
    fun getHome(homeId: String): Home {
        val h: Home? = homes.find { it.id == homeId }
        if (h == null)
            return GlobalDataHomes.personalDevicesHome.asModel()
        return h;
    }
}

val HomesUiState.canGetCurrent: Boolean get() = currentHome != null
val HomesUiState.canModify: Boolean get() = currentHome != null
val HomesUiState.canDelete: Boolean get() = canModify
