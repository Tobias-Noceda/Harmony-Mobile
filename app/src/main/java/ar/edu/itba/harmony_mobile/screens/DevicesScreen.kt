package ar.edu.itba.harmony_mobile.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ar.edu.itba.harmony_mobile.screens.devices_screen.devices.LightScreen

@Composable
fun DevicesScreen(modifier: Modifier = Modifier) {
    Box(modifier = modifier) {
        LightScreen()
    }
}