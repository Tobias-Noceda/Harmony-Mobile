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
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.KeyboardArrowDown
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
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteType
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.window.core.layout.WindowWidthSizeClass
import ar.edu.itba.harmony_mobile.model.Blinds
import ar.edu.itba.harmony_mobile.model.Device
import ar.edu.itba.harmony_mobile.model.Door
import ar.edu.itba.harmony_mobile.model.Home
import ar.edu.itba.harmony_mobile.model.Lamp
import ar.edu.itba.harmony_mobile.model.Refrigerator
import ar.edu.itba.harmony_mobile.model.Sprinkler
import ar.edu.itba.harmony_mobile.model.Vacuum
import ar.edu.itba.harmony_mobile.persistent.HarmonyTopAppBar
import ar.edu.itba.harmony_mobile.remote.api.RetrofitClient
import ar.edu.itba.harmony_mobile.screens.DevicesScreen
import ar.edu.itba.harmony_mobile.screens.RoomsScreen
import ar.edu.itba.harmony_mobile.screens.RoutinesScreen
import ar.edu.itba.harmony_mobile.screens.devices.BlindsScreen
import ar.edu.itba.harmony_mobile.screens.devices.DoorScreen
import ar.edu.itba.harmony_mobile.screens.devices.FridgeScreen
import ar.edu.itba.harmony_mobile.screens.devices.LightScreen
import ar.edu.itba.harmony_mobile.screens.devices.SprinklerScreen
import ar.edu.itba.harmony_mobile.screens.devices.VacuumScreen
import ar.edu.itba.harmony_mobile.ui.devices.DevicesUiState
import ar.edu.itba.harmony_mobile.ui.devices.DevicesViewModel
import ar.edu.itba.harmony_mobile.ui.getViewModelFactory
import ar.edu.itba.harmony_mobile.ui.homes.HomesViewModel
import ar.edu.itba.harmony_mobile.ui.rooms.RoomsUiState
import ar.edu.itba.harmony_mobile.ui.rooms.RoomsViewModel
import ar.edu.itba.harmony_mobile.ui.theme.primary
import ar.edu.itba.harmony_mobile.ui.theme.secondary
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking

enum class AppDestinations(
    @StringRes val label: Int,
    val icon: ImageVector?,
    @DrawableRes val drawable: Int?,
    val route: String
) {
    ROOMS(R.string.rooms, Icons.Default.LocationOn, null, "rooms"),
    DEVICES(R.string.devices, null, R.drawable.devices, "devices"),
    ROUTINES(R.string.routines, null, R.drawable.calendar, "routines")
}

@Composable
fun HarmonyApp(
    deviceId: String?,
    hViewModel: HomesViewModel = viewModel(factory = getViewModelFactory()),
    rViewModel: RoomsViewModel = viewModel(factory = getViewModelFactory()),
    dViewModel: DevicesViewModel = viewModel(factory = getViewModelFactory()),
    navController: NavHostController = rememberNavController()
) {
    var showBottomSheet by remember { mutableStateOf(false) }

    val houseState by hViewModel.uiState.collectAsState()
    val roomsState by rViewModel.uiState.collectAsState()
    val devicesState by dViewModel.uiState.collectAsState()

    val navStack by navController.currentBackStackEntryAsState()
    val currentRoute = navStack?.destination?.route

    var currentHouseId by rememberSaveable { mutableStateOf("0") }

    val myNavigationSuiteItemColors = NavigationSuiteDefaults.itemColors(
        navigationBarItemColors = NavigationBarItemDefaults.colors(
            primary, secondary, secondary, secondary, secondary, primary, primary
        ),
        navigationRailItemColors = NavigationRailItemDefaults.colors(
            primary, secondary, secondary, secondary, secondary, primary, primary
        ),
    )

    val adaptiveInfo = currentWindowAdaptiveInfo()
    val layoutType = NavigationSuiteScaffoldDefaults.calculateFromAdaptiveInfo(adaptiveInfo)

    HarmonyTopAppBar(houseState.getHome(currentHouseId)) { showBottomSheet = !showBottomSheet }
    NavigationSuiteScaffold(
        layoutType = layoutType,
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 82.dp)
            .shadow(16.dp),
        navigationSuiteColors = NavigationSuiteDefaults.colors(
            navigationBarContainerColor = primary,
            navigationRailContainerColor = primary,
        ),
        navigationSuiteItems = {
            if (layoutType == NavigationSuiteType.NavigationRail) {
                item(
                    icon = { Icon(Icons.Default.KeyboardArrowDown, null) },
                    label = { Text("") },
                    selected = false,
                    colors = myNavigationSuiteItemColors,
                    onClick = {},
                    enabled = false
                )
            }
            AppDestinations.entries.forEach {
                item(
                    icon = {
                        if (it.drawable == null) {
                            Icon(
                                it.icon!!,
                                contentDescription = stringResource(it.label)
                            )
                        } else {
                            Icon(
                                painterResource(id = it.drawable),
                                contentDescription = null
                            )
                        }
                    },
                    label = { Text(stringResource(it.label)) },
                    selected = it.route == currentRoute,
                    colors = myNavigationSuiteItemColors,
                    onClick = { navController.navigate(it.route) }
                )
            }
        }
    ) {
        var device: Device? = null
        if(deviceId != null) {
            val deviceService = RetrofitClient.deviceService
            runBlocking {
                val tasks = listOf(
                    async(Dispatchers.IO) {
                        device =
                            deviceService.getDevices().body()?.result?.find { it.id == deviceId }
                                ?.asModel()
                    },
                )
                tasks.awaitAll()
            }
            val room = device!!.room
            if (room != null) {
                currentHouseId = room.home!!.id!!
            }
        }
        Router(device, navController, houseState.getHome(currentHouseId), roomsState, devicesState)
        CustomTopSheet(
            visible = showBottomSheet,
            onDismiss = { showBottomSheet = false },
            onButtonClick = { house ->
                currentHouseId = house.id!!
                navController.navigate(currentRoute!!)
                showBottomSheet = false
            },
            houses = houseState.homes
        )
    }
}

@SuppressLint("UseOfNonLambdaOffsetOverload")
@Composable
private fun CustomTopSheet(
    visible: Boolean,
    onDismiss: () -> Unit,
    onButtonClick: (Home) -> Unit,
    houses: List<Home>,
    maxHeight: Dp = 700.dp
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
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
            val columns = with(adaptiveInfo) {
                if (windowSizeClass.windowWidthSizeClass == WindowWidthSizeClass.COMPACT) {
                    2
                } else {
                    3
                }
            }
            val chunkedHouse = houses.chunked(columns)
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
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(4.dp)
                    ) {
                        for (house in houseChunk) {
                            Button(
                                onClick = { onButtonClick(house) },
                                modifier = Modifier
                                    .padding(horizontal = 8.dp)
                                    .weight(1f),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Start
                                ) {
                                    if (house.id == "0") {
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
                                        text = if (house.id == "0") stringResource(id = R.string.personal_devices) else house.name,
                                        minLines = if (columns == 2) 2 else 1,
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

@Composable
@SuppressLint("StateFlowValueCalledInComposition")
private fun Router(
    device: Device?,
    navController: NavHostController,
    currentHome: Home,
    roomsState: RoomsUiState,
    devicesState: DevicesUiState
) {
    NavHost(
        navController = navController, startDestination = AppDestinations.ROOMS.route
    ) {
        val modifier = Modifier.fillMaxSize()
        composable(AppDestinations.ROOMS.route) {
            if (device == null)
                RoomsScreen(modifier, currentHome, roomsState, devicesState)
            else {
                when(device.type) {
                    DeviceTypes.LIGHTS -> LightScreen(device as Lamp)
                    DeviceTypes.DOORS -> DoorScreen(device as Door)
                    DeviceTypes.REFRIGERATORS -> FridgeScreen(device as Refrigerator)
                    DeviceTypes.SPRINKLERS -> SprinklerScreen(device as Sprinkler)
                    DeviceTypes.BLINDS -> BlindsScreen(device as Blinds)
                    else -> VacuumScreen(device as Vacuum, roomsState.getHomeRooms(currentHome))
                }
            }
        }

        composable(AppDestinations.DEVICES.route) {
            DevicesScreen(modifier, currentHome, roomsState, devicesState)
        }

        composable(AppDestinations.ROUTINES.route) {
            RoutinesScreen(modifier, currentHome)
        }
    }
}

// @Preview(device = Devices.TABLET)
// @Composable
// fun TabletPreview() {
//     HarmonyTheme {
//         HarmonyApp()
//     }
// }
