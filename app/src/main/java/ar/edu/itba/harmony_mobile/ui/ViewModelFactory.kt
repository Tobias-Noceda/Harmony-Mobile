package ar.edu.itba.harmony_mobile.ui

import android.os.Bundle
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSavedStateRegistryOwner
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.savedstate.SavedStateRegistryOwner
import ar.edu.itba.harmony_mobile.ApiApplication
import ar.edu.itba.harmony_mobile.repository.DeviceRepository
import ar.edu.itba.harmony_mobile.repository.HomeRepository
import ar.edu.itba.harmony_mobile.repository.RoomRepository
import ar.edu.itba.harmony_mobile.repository.RoutineRepository
import ar.edu.itba.harmony_mobile.ui.devices.DevicesViewModel
import ar.edu.itba.harmony_mobile.ui.devices.LampViewModel
import ar.edu.itba.harmony_mobile.ui.homes.HomesViewModel
import ar.edu.itba.harmony_mobile.ui.rooms.RoomsViewModel
import ar.edu.itba.harmony_mobile.ui.rooms.RoutinesViewModel

@Composable
fun getViewModelFactory(defaultArgs: Bundle? = null): ViewModelFactory {
    val application = (LocalContext.current.applicationContext as ApiApplication)
    val roomRepository = application.roomRepository
    val deviceRepository = application.deviceRepository
    val routineRepository = application.routineRepository
    val homeRepository = application.homeRepository
    return ViewModelFactory(
        roomRepository,
        deviceRepository,
        homeRepository,
        routineRepository,
        LocalSavedStateRegistryOwner.current,
        defaultArgs
    )
}

class ViewModelFactory(
    private val roomRepository: RoomRepository,
    private val deviceRepository: DeviceRepository,
    private val homeRepository: HomeRepository,
    private val routineRepository: RoutineRepository,
    owner: SavedStateRegistryOwner,
    defaultArgs: Bundle? = null
) : AbstractSavedStateViewModelFactory(owner, defaultArgs) {

    override fun <T : ViewModel> create(
        key: String,
        modelClass: Class<T>,
        handle: SavedStateHandle
    ) = with(modelClass) {
        when {
            isAssignableFrom(RoomsViewModel::class.java) ->
                RoomsViewModel(roomRepository)

            isAssignableFrom(HomesViewModel::class.java) ->
                HomesViewModel(homeRepository)

            isAssignableFrom(RoutinesViewModel::class.java) ->
                RoutinesViewModel(routineRepository)

            isAssignableFrom(DevicesViewModel::class.java) ->
                DevicesViewModel(deviceRepository)

            isAssignableFrom(LampViewModel::class.java) ->
                LampViewModel(deviceRepository)

            else ->
                throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    } as T
}