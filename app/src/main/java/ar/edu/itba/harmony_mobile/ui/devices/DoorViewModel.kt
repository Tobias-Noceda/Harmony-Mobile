package ar.edu.itba.harmony_mobile.ui.devices

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ar.edu.itba.harmony_mobile.DataSourceException
import ar.edu.itba.harmony_mobile.model.Door
import ar.edu.itba.harmony_mobile.repository.DeviceRepository
import ar.edu.itba.harmony_mobile.model.Error
import ar.edu.itba.harmony_mobile.model.Refrigerator
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DoorViewModel(
    private val repository: DeviceRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(DoorUiState())
    val uiState = _uiState.asStateFlow()

    fun open(door: Door) = runOnViewModelScope(
        { repository.executeDeviceAction(door.id!!, Door.OPEN_ACTION) },
        { state, _ -> state }
    )

    fun close(door: Door) = runOnViewModelScope(
        { repository.executeDeviceAction(door.id!!, Door.CLOSE_ACTION) },
        { state, _ -> state }
    )

    fun lock(door: Door) = runOnViewModelScope(
        { repository.executeDeviceAction(door.id!!, Door.LOCK_ACTION) },
        { state, _ -> state }
    )

    fun unlock(door: Door) = runOnViewModelScope(
        { repository.executeDeviceAction(door.id!!, Door.UNLOCK_ACTION) },
        { state, _ -> state }
    )

    private fun <T> collectOnViewModelScope(
        flow: Flow<T>,
        updateState: (DoorUiState, T) -> DoorUiState
    ) = viewModelScope.launch {
        flow
            .distinctUntilChanged()
            .catch { e -> _uiState.update { it.copy(error = handleError(e)) } }
            .collect { response -> _uiState.update { updateState(it, response) } }
    }

    private fun <R> runOnViewModelScope(
        block: suspend () -> R,
        updateState: (DoorUiState, R) -> DoorUiState
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