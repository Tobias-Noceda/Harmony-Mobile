package ar.edu.itba.harmony_mobile.ui.devices

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ar.edu.itba.harmony_mobile.DataSourceException
import ar.edu.itba.harmony_mobile.model.Sprinkler
import ar.edu.itba.harmony_mobile.repository.DeviceRepository
import ar.edu.itba.harmony_mobile.model.Error
import ar.edu.itba.harmony_mobile.model.Vacuum
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SprinklerViewModel(
    private val repository: DeviceRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SprinklerUiState())
    val uiState = _uiState.asStateFlow()

    fun start(sprinkler: Sprinkler) = runOnViewModelScope(
        { repository.executeDeviceAction(sprinkler.id!!, Sprinkler.OPEN_ACTION) },
        { state, _ -> state }
    )

    fun pause(sprinkler: Sprinkler) = runOnViewModelScope(
        { repository.executeDeviceAction(sprinkler.id!!, Sprinkler.CLOSE_ACTION) },
        { state, _ -> state }
    )

    fun dispense(sprinkler: Sprinkler, unit: String, quantity: Int) = runOnViewModelScope(
        {
            repository.executeDeviceAction(
                sprinkler.id!!,
                Sprinkler.DISPENSE_ACTION,
                arrayOf(unit, quantity)
            )
        },
        { state, _ -> state }
    )

    private fun <T> collectOnViewModelScope(
        flow: Flow<T>,
        updateState: (SprinklerUiState, T) -> SprinklerUiState
    ) = viewModelScope.launch {
        flow
            .distinctUntilChanged()
            .catch { e -> _uiState.update { it.copy(error = handleError(e)) } }
            .collect { response -> _uiState.update { updateState(it, response) } }
    }

    private fun <R> runOnViewModelScope(
        block: suspend () -> R,
        updateState: (SprinklerUiState, R) -> SprinklerUiState
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