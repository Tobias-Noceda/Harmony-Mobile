package ar.edu.itba.harmony_mobile

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import ar.edu.itba.harmony_mobile.ui.theme.HarmonyTheme
import kotlinx.coroutines.launch

@Composable
fun HarmonyApp() {
    val coroutineScope = rememberCoroutineScope()
    val sheetVisible by remember { mutableStateOf(false) }

    HarmonyTheme {
        Scaffold(
            topBar = { HarmonyTopAppBar(onButtonClick = {
                coroutineScope.launch {
                    sheetVisible.not()
                }
            }) },
            bottomBar = { HarmonyNavigationBar() }
        ) { padding ->
            RoomsScreen(Modifier.padding(padding))
        }
    }
}