package ar.edu.itba.harmony_mobile.ui.devices

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ar.edu.itba.harmony_mobile.DataSourceException
import ar.edu.itba.harmony_mobile.model.Lamp
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

class LampViewModel(
    private val repository: DeviceRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(LampUiState())
    val uiState = _uiState.asStateFlow()

    fun turnOn(lamp: Lamp) = runOnViewModelScope(
        { repository.executeDeviceAction(lamp.id!!, Lamp.TURN_ON_ACTION) },
        { state, _ -> state }
    )

    fun turnOff(lamp: Lamp) = runOnViewModelScope(
        { repository.executeDeviceAction(lamp.id!!, Lamp.TURN_OFF_ACTION) },
        { state, _ -> state }
    )

    fun setColor(lamp: Lamp, color: String) = runOnViewModelScope(
        { repository.executeDeviceAction(lamp.id!!, Lamp.SET_COLOR_ACTION, arrayOf(color)) },
        { state, _ -> state }
    )

    fun setColor(lamp: Lamp, color: Color) = runOnViewModelScope(
        {
            delay(50)
            repository.executeDeviceAction(
                lamp.id!!,
                Lamp.SET_COLOR_ACTION,
                arrayOf(Lamp.colorToString(color))
            )
        },
        { state, _ -> state }
    )

    fun setBrightness(lamp: Lamp, brightness: Int) = runOnViewModelScope(
        {
            repository.executeDeviceAction(
                lamp.id!!,
                Lamp.SET_BRIGHTNESS_ACTION,
                arrayOf(brightness)
            )
        },
        { state, _ -> state }
    )

    private fun <T> collectOnViewModelScope(
        flow: Flow<T>,
        updateState: (LampUiState, T) -> LampUiState
    ) = viewModelScope.launch {
        flow
            .distinctUntilChanged()
            .catch { e -> _uiState.update { it.copy(error = handleError(e)) } }
            .collect { response -> _uiState.update { updateState(it, response) } }
    }

    private var debouncedJob: Job? = null;

    private fun <R> runOnViewModelScope(
        block: suspend () -> R,
        updateState: (LampUiState, R) -> LampUiState
    ): Job {
        debouncedJob?.cancel() // Method is modified to allow debouncing, if you call to runOnViewModelScope should be debounced, simply add a delay before the actual execution, this function will handle it on its own (note, all calls to this function are debounced jointly, I will not be changing this, as it is pointless)
        debouncedJob = viewModelScope.launch {
            _uiState.update { it.copy(loading = true, error = null) }
            runCatching {
                block()
            }.onSuccess { response ->
                _uiState.update { updateState(it, response).copy(loading = false) }
            }.onFailure { e ->
                _uiState.update { it.copy(loading = false, error = handleError(e)) }
            }
        }
        return debouncedJob as Job;
    }

    private fun handleError(e: Throwable): Error {
        return if (e is DataSourceException) {
            Error(e.code, e.message ?: "", e.details)
        } else {
            Error(null, e.message ?: "", null)
        }
    }
}