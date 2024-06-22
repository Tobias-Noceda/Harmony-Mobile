package ar.edu.itba.harmony_mobile.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.window.core.layout.WindowWidthSizeClass
import ar.edu.itba.harmony_mobile.DeviceTypes
import ar.edu.itba.harmony_mobile.R
import ar.edu.itba.harmony_mobile.model.Blinds
import ar.edu.itba.harmony_mobile.model.Door
import ar.edu.itba.harmony_mobile.model.Home
import ar.edu.itba.harmony_mobile.model.Lamp
import ar.edu.itba.harmony_mobile.model.Refrigerator
import ar.edu.itba.harmony_mobile.model.Room
import ar.edu.itba.harmony_mobile.model.Sprinkler
import ar.edu.itba.harmony_mobile.model.Vacuum
import ar.edu.itba.harmony_mobile.screens.devices.BlindsScreen
import ar.edu.itba.harmony_mobile.screens.devices.DoorScreen
import ar.edu.itba.harmony_mobile.screens.devices.FridgeScreen
import ar.edu.itba.harmony_mobile.screens.devices.LightScreen
import ar.edu.itba.harmony_mobile.screens.devices.SprinklerScreen
import ar.edu.itba.harmony_mobile.screens.devices.VacuumScreen
import ar.edu.itba.harmony_mobile.ui.devices.DevicesUiState
import ar.edu.itba.harmony_mobile.ui.rooms.RoomsUiState
import ar.edu.itba.harmony_mobile.ui.theme.primary
import ar.edu.itba.harmony_mobile.ui.theme.secondary
import ar.edu.itba.harmony_mobile.ui.theme.tertiary

@Composable
fun RoomsScreen(
    modifier: Modifier,
    currentHouse: Home,
    roomsState: RoomsUiState,
    devicesState: DevicesUiState,
    deviceId: String?,
    deviceBack: () -> Unit,
    state: String = ""
) {
    var currentDestination by rememberSaveable { mutableStateOf(state) }

    if(deviceId != null) {
        val device = devicesState.getDevice(deviceId)
        when(device!!.type) {
            DeviceTypes.LIGHTS -> LightScreen(device as Lamp) { deviceBack() }
            DeviceTypes.DOORS -> DoorScreen(device as Door) { deviceBack() }
            DeviceTypes.REFRIS -> FridgeScreen(device as Refrigerator) { deviceBack() }
            DeviceTypes.SPRINKLERS -> SprinklerScreen(device as Sprinkler) { deviceBack() }
            DeviceTypes.BLINDS -> BlindsScreen(device as Blinds) { deviceBack() }
            else -> VacuumScreen(device as Vacuum, roomsState.getHomeRooms(currentHouse)) { deviceBack() }
        }
    }

    if (roomsState.getHomeRooms(currentHouse).size == 1) {
       currentDestination = roomsState.getHomeRooms(currentHouse)[0].id!!
    }

    Box(modifier = modifier) {
        if (currentDestination == "" && currentHouse.id != "0") {
            if (roomsState.getHomeRooms(currentHouse).isEmpty()) {
                EmptyScreen(description = "${stringResource(id = R.string.empty_house)} ${currentHouse.name}")
            } else {
                val rooms = filterRooms(devicesState, roomsState.getHomeRooms(currentHouse))
                if(rooms.isNotEmpty()) {
                    RoomsList(
                        rooms = rooms,
                        onDeviceClick = { roomId ->
                            currentDestination = roomId
                        }
                    )
                } else {
                   EmptyScreen(description = "${stringResource(id = R.string.empty_house)} ${currentHouse.name}")
                }
            }
        } else {
            if (currentHouse.id == "0" && devicesState.getHomeDevices(currentHouse).isEmpty()) {
                EmptyScreen("${stringResource(id = R.string.empty_house)} ${stringResource(id = R.string.personal_devices)}")
            }
            RoomScreen(
                if (currentHouse.id == "0") {
                    Room("0", stringResource(id = R.string.personal_devices), currentHouse)
                } else {
                    roomsState.getRoom(currentDestination)
                },
                currentHouse,
                roomsState,
                devicesState,
            ) { currentDestination = "" }
        }
    }
}

@Composable
fun RoomsList(rooms: List<Room>, onDeviceClick: (String) -> Unit) {
    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            text = stringResource(id = R.string.rooms),
            modifier = Modifier.padding(
                start = 12.dp,
                top = 12.dp,
                bottom = 12.dp
            ),
            style = MaterialTheme.typography.titleLarge
        )

        val scState = rememberScrollState(0)
        val widthClass = currentWindowAdaptiveInfo().windowSizeClass.windowWidthSizeClass
        val isCompact = widthClass == WindowWidthSizeClass.COMPACT

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 6.dp,
                )
                .verticalScroll(scState)
        ) {
            if(isCompact) {
                for (room in rooms) {
                    Button(
                        modifier = Modifier.padding(4.dp),
                        shape = RoundedCornerShape(8.dp),
                        onClick = { onDeviceClick(room.id!!) },
                        colors = ButtonColors(secondary, primary, tertiary.copy(alpha = .5f), Color.White),
                        elevation = ButtonDefaults.buttonElevation(8.dp),
                        border = BorderStroke(
                            width = 1.dp,
                            color = Color.Black.copy(alpha = 0.3f)
                        )
                    ) {
                        Row (
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 12.dp)
                                .height(60.dp),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = room.name,
                                textAlign = TextAlign.Left,
                                style = MaterialTheme.typography.titleLarge
                            )
                        }
                    }
                }
            } else {
                val chunkedRooms = rooms.chunked(2)

                for (chunk in chunkedRooms) {
                    Row(modifier = Modifier.fillMaxWidth()) {
                        for (room in chunk) {
                            Button(
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(4.dp),
                                shape = RoundedCornerShape(8.dp),
                                onClick = { onDeviceClick(room.id!!) },
                                colors = ButtonColors(secondary, primary, tertiary.copy(alpha = .5f), Color.White),
                                elevation = ButtonDefaults.buttonElevation(8.dp),
                                border = BorderStroke(
                                    width = 1.dp,
                                    color = Color.Black.copy(alpha = 0.3f)
                                )
                            ) {
                                Row (
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 12.dp)
                                        .height(60.dp),
                                    horizontalArrangement = Arrangement.Center,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = room.name,
                                        textAlign = TextAlign.Left,
                                        style = MaterialTheme.typography.titleMedium,
                                        modifier = Modifier.padding(start = 16.dp)
                                    )
                                }
                            }
                        }
                        if(chunk.size != 2) {
                            Box(
                                modifier = Modifier
                                    .padding(4.dp)
                                    .weight(1f)
                            ) {}
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun filterRooms(devicesState: DevicesUiState, rooms: List<Room>): List<Room> {
    var toRet = emptyList<Room>()
    for(room in rooms) {
        if (devicesState.getRoomDevices(room).isNotEmpty()) {
           toRet = toRet + room 
        }
    }
    
    return toRet
}