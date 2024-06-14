package ar.edu.itba.harmony_mobile

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ar.edu.itba.harmony_mobile.persistent.HarmonyNavigationBar
import ar.edu.itba.harmony_mobile.persistent.HarmonyTopAppBar
import ar.edu.itba.harmony_mobile.screens.DevicesScreen
import ar.edu.itba.harmony_mobile.screens.RoomsScreen
import ar.edu.itba.harmony_mobile.screens.RoutinesScreen
import ar.edu.itba.harmony_mobile.ui.theme.HarmonyTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HarmonyApp() {
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    var showBottomSheet by remember { mutableStateOf(false) }

    val currentScreen = remember { mutableStateOf("devices") }
    HarmonyTheme {


        Scaffold(
            topBar = { HarmonyTopAppBar(onButtonClick = {
                showBottomSheet = !showBottomSheet
            }) },
            bottomBar = { HarmonyNavigationBar(currentScreen = currentScreen) },
        ) { padding ->
            when(currentScreen.value){
                "rooms" -> RoomsScreen(Modifier.padding(padding))
                "devices" -> DevicesScreen(Modifier.padding(padding))
                "routines" -> RoutinesScreen(Modifier.padding(padding))
            }

            CustomTopSheet(
                visible = showBottomSheet,
                onDismiss = { showBottomSheet = false },
                padding = padding,
                houses = listOf("House1", "House2")
            )
        }
    }
}

@Composable
fun CustomTopSheet(
    visible: Boolean,
    onDismiss: () -> Unit,
    padding: PaddingValues,
    houses: List<String>,
    maxHeight: Dp = 700.dp
) {
    val animationOffset by animateDpAsState(
        targetValue = if (visible) 0.dp else (-maxHeight),
        animationSpec = tween(durationMillis = 300),
        label = ""
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(padding)
            .heightIn(max = maxHeight)
            .offset(y = animationOffset)
            .border(
                width = 1.dp,
                color = Color.Black.copy(alpha = 0.3f),
                shape = RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp)
            )
            .shadow(
                elevation = 16.dp,
                shape = RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp)
            )
            .background(
                MaterialTheme.colorScheme.secondary,
                shape = RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp)
            )
            .draggable(
                orientation = Orientation.Vertical,
                state = rememberDraggableState { deltaY ->
                    if (deltaY < -20) {
                        onDismiss()
                    }
                }
            )
    ) {
        if (visible) {
            Column(
                modifier = Modifier.padding(16.dp)
            ){
                Text(
                    text = "Houses",
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Row {
                    for (house in houses) {
                        Button(
                            onClick = { /* Acción al hacer clic en el botón */ },
                            modifier = Modifier.padding(horizontal = 8.dp)
                        ) {
                            Text(text = house)
                        }
                    }
                }
                Icon(
                   Icons.Default.KeyboardArrowUp,
                    contentDescription = null,
                    modifier = Modifier.align(Alignment.CenterHorizontally)

                )
            }
        }
    }
}