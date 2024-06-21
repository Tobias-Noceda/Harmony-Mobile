package ar.edu.itba.harmony_mobile.ui.devices

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ar.edu.itba.harmony_mobile.DataSourceException
import ar.edu.itba.harmony_mobile.model.Vacuum
import ar.edu.itba.harmony_mobile.repository.DeviceRepository
import ar.edu.itba.harmony_mobile.model.Error
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class VacuumViewModel(
    private val repository: DeviceRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(VacuumUiState())
    val uiState = _uiState.asStateFlow()

    fun start(vacuum: Vacuum) = runOnViewModelScope(
        { repository.executeDeviceAction(vacuum.id!!, Vacuum.START_ACTION) },
        { state, _ -> state }
    )

    fun pause(vacuum: Vacuum) = runOnViewModelScope(
        { repository.executeDeviceAction(vacuum.id!!, Vacuum.PAUSE_ACTION) },
        { state, _ -> state }
    )

    fun dock(vacuum: Vacuum) = runOnViewModelScope(
        { repository.executeDeviceAction(vacuum.id!!, Vacuum.DOCK_ACTION) },
        { state, _ -> state }
    )

    fun setLocation(vacuum: Vacuum, roomId: String) = runOnViewModelScope(
        { repository.executeDeviceAction(vacuum.id!!, Vacuum.SET_LOCATION_ACTION, arrayOf(roomId)) },
        { state, _ -> state }
    )

    fun setMode(vacuum: Vacuum, mode: String) = runOnViewModelScope(
        { repository.executeDeviceAction(vacuum.id!!, Vacuum.SET_MODE_ACTION, arrayOf(mode)) },
        { state, _ -> state }
    )

    private fun <T> collectOnViewModelScope(
        flow: Flow<T>,
        updateState: (VacuumUiState, T) -> VacuumUiState
    ) = viewModelScope.launch {
        flow
            .distinctUntilChanged()
            .catch { e -> _uiState.update { it.copy(error = handleError(e)) } }
            .collect { response -> _uiState.update { updateState(it, response) } }
    }

    private fun <R> runOnViewModelScope(
        block: suspend () -> R,
        updateState: (VacuumUiState, R) -> VacuumUiState
    ): Job = viewModelScope.launch {
        _uiState.update { it.copy(loading = true, error = null) }
        runCatching {
            block()
        }.onSuccess { response ->
            _uiState.update { updateState(it, response).copy(loading = false) }
        }.onFailure { e ->
            _uiState.update { it.copy(loading = false, error = handleError(e)) }
        }
    }

    private fun handleError(e: Throwable): Error {
        return if (e is DataSourceException) {
            Error(e.code, e.message ?: "", e.details)
        } else {
            Error(null, e.message ?: "", null)
        }
    }
}