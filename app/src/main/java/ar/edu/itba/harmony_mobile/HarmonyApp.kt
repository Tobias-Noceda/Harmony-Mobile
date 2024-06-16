package ar.edu.itba.harmony_mobile

import androidx.annotation.StringRes
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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ar.edu.itba.harmony_mobile.persistent.HarmonyNavigationBar
import ar.edu.itba.harmony_mobile.persistent.HarmonyTopAppBar

enum class AppDestinations(
    @StringRes val label: Int,
    val icon: ImageVector,
    @StringRes val contentDescription: Int
) {
    ROOMS(R.string.rooms, Icons.Default.LocationOn, R.string.rooms),
    DEVICES(R.string.devices, Icons.AutoMirrored.Filled.List, R.string.devices),
    ROUTINES(R.string.routines, Icons.Default.DateRange, R.string.routines)
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3AdaptiveApi::class)
@Composable
@Preview
fun HarmonyApp() {
    var showBottomSheet by remember { mutableStateOf(false) }

    val currentDestination = rememberSaveable { mutableStateOf(AppDestinations.ROOMS) }

    Scaffold(
        topBar = {
            HarmonyTopAppBar(onButtonClick = {
                showBottomSheet = !showBottomSheet
            })
        },
        bottomBar = { HarmonyNavigationBar(currentDestination = currentDestination) }
    ) { padding ->

        CustomTopSheet(
            visible = showBottomSheet,
            onDismiss = { showBottomSheet = false },
            padding = padding,
            houses = listOf("House1", "House2")
        )
    }
}

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
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
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    text = stringResource(R.string.houses),
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
                            Icon(
                                Icons.Default.Home,
                                contentDescription = null,
                                tint = Color.White
                            )
                            Text(text = house)
                        }
                    }
                }
                Icon(
                    Icons.Default.KeyboardArrowUp,
                    contentDescription = null,
                    tint = Color.Black.copy(alpha = 0.4f),
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .height(24.dp)
                        .padding(top = 6.dp)
                )
            }
        }
    }
}