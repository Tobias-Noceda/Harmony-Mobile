package ar.edu.itba.harmony_mobile.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ar.edu.itba.harmony_mobile.ui.theme.*

@Composable
fun RoomsScreen(modifier: Modifier = Modifier) {
    Box(modifier = modifier) {
        Text(text = "Rooms Screen", color = primary)
    }
}