package ar.edu.itba.harmony_mobile

import android.annotation.SuppressLint
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.NavigationRailItemDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteDefaults
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffoldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.window.core.layout.WindowWidthSizeClass
import ar.edu.itba.harmony_mobile.persistent.HarmonyTopAppBar
import ar.edu.itba.harmony_mobile.screens.DevicesScreen
import ar.edu.itba.harmony_mobile.screens.RoomsScreen
import ar.edu.itba.harmony_mobile.screens.RoutinesScreen
import ar.edu.itba.harmony_mobile.ui.theme.HarmonyTheme
import ar.edu.itba.harmony_mobile.ui.theme.disabled
import ar.edu.itba.harmony_mobile.ui.theme.primary
import ar.edu.itba.harmony_mobile.ui.theme.secondary

enum class AppDestinations(
    @StringRes val label: Int,
    val icon: ImageVector?,
    @DrawableRes val drawable: Int?,
    @StringRes val contentDescription: Int
) {
    ROOMS(R.string.rooms, Icons.Default.LocationOn, null, R.string.rooms),
    DEVICES(R.string.devices, null, R.drawable.devices, R.string.devices),
    ROUTINES(R.string.routines, null, R.drawable.calendar, R.string.routines)
}

@Composable
fun HarmonyApp() {
    var showBottomSheet by remember { mutableStateOf(false) }

    var currentDestination by rememberSaveable { mutableStateOf(AppDestinations.ROOMS) }
    var currentHouseId by rememberSaveable { mutableStateOf("0") }

    val myNavigationSuiteItemColors = NavigationSuiteDefaults.itemColors(
        navigationBarItemColors = NavigationBarItemDefaults.colors(
            primary, secondary, secondary, secondary, secondary, disabled, disabled
        ),
        navigationRailItemColors = NavigationRailItemDefaults.colors(
            primary, secondary, secondary, secondary, secondary, disabled, disabled
        ),
    )

    val adaptiveInfo = currentWindowAdaptiveInfo()
    val layoutType = NavigationSuiteScaffoldDefaults.calculateFromAdaptiveInfo(adaptiveInfo)

    NavigationSuiteScaffold(
        layoutType = layoutType,
        modifier = Modifier.shadow(16.dp),
        navigationSuiteColors = NavigationSuiteDefaults.colors(
            navigationBarContainerColor = primary,
            navigationRailContainerColor = primary,
        ),
        navigationSuiteItems = {
            AppDestinations.entries.forEach {
                item(
                    icon = {
                        if (it.drawable == null) {
                            Icon(
                                it.icon!!,
                                contentDescription = stringResource(it.contentDescription)
                            )
                        } else {
                            Icon(
                                painterResource(id = it.drawable),
                                contentDescription = null
                            )
                        }
                    },
                    label = { Text(stringResource(it.label)) },
                    selected = it == currentDestination,
                    colors = myNavigationSuiteItemColors,
                    onClick = { currentDestination = it }
                )
            }
        }
    ) {
        HarmonyTopAppBar(onButtonClick = { showBottomSheet = !showBottomSheet })
        val screenModifier = Modifier
            .fillMaxSize()
            .padding(top = 80.dp)

        when (currentDestination) {
            AppDestinations.ROOMS -> RoomsScreen(screenModifier, currentHouseId)
            AppDestinations.DEVICES -> DevicesScreen(screenModifier, currentHouseId)
            AppDestinations.ROUTINES -> RoutinesScreen(screenModifier)
        }
        CustomTopSheet(
            visible = showBottomSheet,
            onDismiss = { showBottomSheet = false },
            onButtonClick = { houseId ->
                currentHouseId = houseId
                currentDestination = AppDestinations.ROOMS
                showBottomSheet = false
            },
            padding = PaddingValues(top = 80.dp),
            houses = listOf("House1", "House2")
        )
    }
}

@SuppressLint("UseOfNonLambdaOffsetOverload")
@Composable
fun CustomTopSheet(
    visible: Boolean,
    onDismiss: () -> Unit,
    onButtonClick: (String) -> Unit,
    padding: PaddingValues,
    houses: List<String>,
    maxHeight: Dp = 700.dp
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(padding)
            .heightIn(max = maxHeight)
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
            val adaptiveInfo = currentWindowAdaptiveInfo()
            val columns = with (adaptiveInfo) {
                if (windowSizeClass.windowWidthSizeClass == WindowWidthSizeClass.COMPACT) {
                    2
                } else {
                    3
                }
            }
            val allHouses = houses + stringResource(id = R.string.personal_devices)
            val chunkedHouse = allHouses.chunked(columns)
            Column(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    text = stringResource(R.string.houses),
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(bottom = 2.dp)
                )
                for (houseChunk in chunkedHouse) {
                    Row(modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp)
                    ) {
                        for (house in houseChunk) {
                            Button(
                                onClick = { /* Acción al hacer clic en el botón */ },
                                modifier = Modifier
                                    .padding(horizontal = 8.dp)
                                    .weight(1f),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Row (
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Start
                                ) {
                                    if (house == stringResource(id = R.string.personal_devices)) {
                                        Icon(
                                            painterResource(id = R.drawable.persona_devices),
                                            contentDescription = null,
                                            tint = Color.White,
                                            modifier = Modifier.padding(end = 8.dp)
                                        )
                                    } else {
                                        Icon(
                                            Icons.Default.Home,
                                            contentDescription = null,
                                            tint = Color.White,
                                            modifier = Modifier.padding(end = 8.dp)
                                        )
                                    }
                                    Text(
                                        text = house,
                                        minLines = if ( columns == 2 ) 2 else 1,
                                        overflow = TextOverflow.Ellipsis,
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                }
                            }
                        }
                        if (houseChunk.size != columns) {
                            for (i in 0 until (columns - houseChunk.size)) {
                                Box(
                                    modifier = Modifier
                                        .padding(horizontal = 8.dp)
                                        .weight(1f)
                                ) {}
                            }
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
                        .clickable { onDismiss() }
                )
            }
        }
    }
}

@Preview(device = Devices.TABLET)
@Composable
fun TabletPreview() {
    HarmonyTheme {
        HarmonyApp()
    }
}
