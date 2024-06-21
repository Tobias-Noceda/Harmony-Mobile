package ar.edu.itba.harmony_mobile.screens

import android.util.Log
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
import ar.edu.itba.harmony_mobile.R
import ar.edu.itba.harmony_mobile.model.Home
import ar.edu.itba.harmony_mobile.model.Room
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
    state: String = ""
) {
    var currentDestination by rememberSaveable { mutableStateOf(state) }

    Log.i("Tobi", currentHouse.toString())

    if (roomsState.getHomeRooms(currentHouse).size == 1) {
       currentDestination = roomsState.getHomeRooms(currentHouse)[0].id!!
    }

    Box(modifier = modifier) {
        if (currentDestination == "") {
            if (roomsState.getHomeRooms(currentHouse).isEmpty()) {
                EmptyScreen(description = "You have no devices in house: ${currentHouse.name}")
            } else {
                var show = false
                for(room in roomsState.getHomeRooms(currentHouse)) {
                    Log.i("Tobi", "Room: ${room.name}. Devices: ${devicesState.getRoomDevices(room)}")
                    if (devicesState.getRoomDevices(room).isNotEmpty()) {
                        show = true
                        break
                    }
                }
                // if(show) {
                    RoomsList(
                        rooms = roomsState.getHomeRooms(currentHouse),
                        onDeviceClick = { roomId ->
                            currentDestination = roomId
                        }
                    )
                // } else {
                //    EmptyScreen(description = "You have no devices in house: ${currentHouse.name}")
                // }
            }
        } else {
            RoomScreen(
                roomsState.getRoom(currentDestination),
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