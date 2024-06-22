package ar.edu.itba.harmony_mobile.ui.devices

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ar.edu.itba.harmony_mobile.DataSourceException
import ar.edu.itba.harmony_mobile.model.Device
import ar.edu.itba.harmony_mobile.repository.DeviceRepository
import ar.edu.itba.harmony_mobile.model.Error
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DevicesViewModel(
    private val repository: DeviceRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(DevicesUiState())
    val uiState = _uiState.asStateFlow()

    init {
        collectOnViewModelScope(
            repository.devices
        ) { state, response -> state.copy(devices = response) }
    }

    private fun <T> collectOnViewModelScope(
        flow: Flow<T>,
        updateState: (DevicesUiState, T) -> DevicesUiState
    ) = viewModelScope.launch {
        flow
            .distinctUntilChanged()
            .catch { e -> _uiState.update { it.copy(error = handleError(e)) } }
            .collect { response -> _uiState.update { updateState(it, response) } }
    }

    fun getDevice(deviceId: String) {
        runOnViewModelScope(
            { repository.getDevice(deviceId) },
            { state, response -> state.copy(currentDevice = response) }
        )
    }

    private var updating: Job? = null;
    fun updateOnInterval(deviceId: String, delay: Long = 1000) {
        stopUpdating()
        updating = runOnViewModelScope(
            {
                while (true) {
                    delay(delay)
                    getDevice(deviceId)
                }
            },
            { state, _ -> state.copy() }
        )
    }

    fun stopUpdating() {
        updating?.cancel()
    }

    private fun <R> runOnViewModelScope(
        block: suspend () -> R,
        updateState: (DevicesUiState, R) -> DevicesUiState
    ): Job = viewModelScope.launch {
        _uiState.update { it.copy(isFetching = true, error = null) }
        runCatching {
            block()
        }.onSuccess { response ->
            _uiState.update { updateState(it, response).copy(isFetching = false) }
        }.onFailure { e ->
            _uiState.update { it.copy(isFetching = false, error = handleError(e)) }
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