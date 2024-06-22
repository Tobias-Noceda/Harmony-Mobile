package ar.edu.itba.harmony_mobile.ui.devices

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ar.edu.itba.harmony_mobile.DataSourceException
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

class RefrigeratorViewModel(
    private val repository: DeviceRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(RefrigeratorUiState())
    val uiState = _uiState.asStateFlow()

    fun turnOn(refrigerator: Refrigerator) = runOnViewModelScope(
        {
            repository.executeDeviceAction(
                refrigerator.id!!,
                Refrigerator.TURN_ON_ACTION
            )
        },
        { state, _ -> state }
    )

    fun turnOff(refrigerator: Refrigerator) = runOnViewModelScope(
        {
            repository.executeDeviceAction(
                refrigerator.id!!,
                Refrigerator.TURN_OFF_ACTION
            )
        },
        { state, _ -> state }
    )

    fun setTemperature(refrigerator: Refrigerator, temperature: Int) = runOnViewModelScope(
        {
            repository.executeDeviceAction(
                refrigerator.id!!,
                Refrigerator.SET_TEMPERATURE_ACTION,
                arrayOf(temperature)
            )
        },
        { state, _ -> state }
    )

    fun setFreezerTemperature(refrigerator: Refrigerator, temperature: Int) = runOnViewModelScope(
        {
            repository.executeDeviceAction(
                refrigerator.id!!,
                Refrigerator.SET_FREEZER_TEMPERATURE_ACTION,
                arrayOf(temperature)
            )
        },
        { state, _ -> state }
    )

    fun setMode(refrigerator: Refrigerator, mode: String) = runOnViewModelScope(
        {
            repository.executeDeviceAction(
                refrigerator.id!!,
                Refrigerator.SET_MODE_ACTION,
                arrayOf(mode)
            )
        },
        { state, _ -> state }
    )

    private fun <T> collectOnViewModelScope(
        flow: Flow<T>,
        updateState: (RefrigeratorUiState, T) -> RefrigeratorUiState
    ) = viewModelScope.launch {
        flow
            .distinctUntilChanged()
            .catch { e -> _uiState.update { it.copy(error = handleError(e)) } }
            .collect { response -> _uiState.update { updateState(it, response) } }
    }

    private fun <R> runOnViewModelScope(
        block: suspend () -> R,
        updateState: (RefrigeratorUiState, R) -> RefrigeratorUiState
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