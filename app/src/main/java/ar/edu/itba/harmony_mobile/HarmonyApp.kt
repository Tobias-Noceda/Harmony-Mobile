package ar.edu.itba.harmony_mobile

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun HarmonyApp() {
    Scaffold(
        topBar = { HarmonyTopAppBar() },
        bottomBar = { HarmonyNavigationBar() }
    ) { padding ->
        RoomsScreen(Modifier.padding(padding))
    }
}