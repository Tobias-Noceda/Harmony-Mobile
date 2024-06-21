package ar.edu.itba.harmony_mobile.screens

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.material3.Icon
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.window.core.layout.WindowWidthSizeClass
import ar.edu.itba.harmony_mobile.DeviceTypes
import ar.edu.itba.harmony_mobile.R
import ar.edu.itba.harmony_mobile.model.Blinds
import ar.edu.itba.harmony_mobile.model.Device
import ar.edu.itba.harmony_mobile.model.Door
import ar.edu.itba.harmony_mobile.model.Home
import ar.edu.itba.harmony_mobile.model.Lamp
import ar.edu.itba.harmony_mobile.model.Refrigerator
import ar.edu.itba.harmony_mobile.model.Sprinkler
import ar.edu.itba.harmony_mobile.model.Vacuum
import ar.edu.itba.harmony_mobile.screens.devices.BlindsScreen
import ar.edu.itba.harmony_mobile.screens.devices.DoorScreen
import ar.edu.itba.harmony_mobile.screens.devices.FridgeScreen
import ar.edu.itba.harmony_mobile.screens.devices.LightScreen
import ar.edu.itba.harmony_mobile.screens.devices.SprinklerScreen
import ar.edu.itba.harmony_mobile.screens.devices.VacuumScreen
import ar.edu.itba.harmony_mobile.screens.devices.deviceCards.BlindsCard
import ar.edu.itba.harmony_mobile.screens.devices.deviceCards.DoorCard
import ar.edu.itba.harmony_mobile.screens.devices.deviceCards.LightCard
import ar.edu.itba.harmony_mobile.screens.devices.deviceCards.RefrigeratorCard
import ar.edu.itba.harmony_mobile.screens.devices.deviceCards.SprinklerCard
import ar.edu.itba.harmony_mobile.screens.devices.deviceCards.VacuumCard
import ar.edu.itba.harmony_mobile.ui.devices.DevicesUiState
import ar.edu.itba.harmony_mobile.ui.rooms.RoomsUiState
import ar.edu.itba.harmony_mobile.ui.theme.primary
import ar.edu.itba.harmony_mobile.ui.theme.secondary
import ar.edu.itba.harmony_mobile.ui.theme.tertiary

@Composable
fun DevicesByType(
    type: DeviceTypes,
    currentHouse: Home,
    roomsState: RoomsUiState,
    devicesState: DevicesUiState,
    onBackCalled: () -> Unit
) {
    var currentId by rememberSaveable { mutableStateOf("") }

    BackHandler(onBack = onBackCalled)

    Log.i("Tobi", devicesState.getHomeDevices(currentHouse).toString())

    if (currentId == "") {
        val list = filterDevices(devicesState.getHomeDevices(currentHouse), type)
        if (list.isEmpty()) {
            val name = if(currentHouse.id == "0") stringResource(id = R.string.personal_devices) else currentHouse.name
            EmptyScreen(
                description = "${stringResource(id = R.string.no_devices1)} ${type.name} " +
                        "${stringResource(id = R.string.no_devices2)} $name"
            )
        } else {
            TypeList(
                type,
                list,
                onDeviceClick = { deviceId ->
                    currentId = deviceId
                }
            )
        }
    } else {
        when(type) {
            DeviceTypes.LIGHTS -> LightScreen(devicesState.getDevice(currentId) as Lamp) { currentId = "" }
            DeviceTypes.DOORS -> DoorScreen(devicesState.getDevice(currentId) as Door) { currentId = "" }
            DeviceTypes.REFRIS -> FridgeScreen(devicesState.getDevice(currentId) as Refrigerator) { currentId = "" }
            DeviceTypes.SPRINKLERS -> SprinklerScreen(devicesState.getDevice(currentId) as Sprinkler) { currentId = "" }
            DeviceTypes.VACUUMS -> VacuumScreen(devicesState.getDevice(currentId) as Vacuum) { currentId = "" }
            else -> BlindsScreen(devicesState.getDevice(currentId) as Blinds) { currentId = "" }
        }
        BackHandler(onBack = { currentId = "" })
    }
}

@Composable
fun TypeList(type: DeviceTypes, deviceList: List<Device>, onDeviceClick: (String) -> Unit) {
    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            text = stringResource(id = type.type),
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
        val isExpanded = widthClass == WindowWidthSizeClass.EXPANDED
        val modifier = Modifier
            .weight(1f)
            .padding(4.dp)

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 6.dp,
                )
                .verticalScroll(scState)
        ) {
            if(isCompact) {
                for (device in deviceList) {
                    Button(
                        modifier = Modifier.padding(4.dp),
                        shape = RoundedCornerShape(8.dp),
                        onClick = { onDeviceClick(device.id!!) },
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
                                .padding(vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                painterResource(id = type.icon),
                                contentDescription = null,
                                modifier = Modifier.height(60.dp)
                            )
                            Text(
                                text = device.name,
                                textAlign = TextAlign.Left,
                                style = MaterialTheme.typography.titleMedium,
                                modifier = Modifier.padding(start = 16.dp)
                            )
                        }
                    }
                }
            } else if (isExpanded) {
                val chunkedDevices = deviceList.chunked(2)

                for (chunk in chunkedDevices) {
                    Row(modifier = Modifier.fillMaxWidth()) {
                        for (device in chunk) {
                            when(type) {
                                DeviceTypes.LIGHTS -> LightCard(device, modifier = modifier) { onDeviceClick(device.id!!) }
                                DeviceTypes.DOORS -> DoorCard(device, modifier = modifier) { onDeviceClick(device.id!!) }
                                DeviceTypes.REFRIS -> RefrigeratorCard(device, modifier = modifier) { onDeviceClick(device.id!!) }
                                DeviceTypes.VACUUMS -> VacuumCard(device, modifier = modifier) { onDeviceClick(device.id!!) }
                                DeviceTypes.SPRINKLERS -> SprinklerCard(device, modifier = modifier) { onDeviceClick(device.id!!) }
                                else -> BlindsCard(device, modifier = modifier) { onDeviceClick(device.id!!) }
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
            } else {
                for (device in deviceList) {
                    when(type) {
                        DeviceTypes.LIGHTS -> LightCard(lamp = device) { onDeviceClick(device.id!!) }
                        DeviceTypes.DOORS -> DoorCard(door = device) { onDeviceClick(device.id!!) }
                        DeviceTypes.REFRIS -> RefrigeratorCard(refrigerator = device) { onDeviceClick(device.id!!) }
                        DeviceTypes.VACUUMS -> VacuumCard(vacuum = device) { onDeviceClick(device.id!!) }
                        DeviceTypes.SPRINKLERS -> SprinklerCard(sprinkler = device) { onDeviceClick(device.id!!) }
                        else -> BlindsCard(blinds = device) { onDeviceClick(device.id!!) }
                    }
                }
            }
        }
    }
}

@Composable
private fun filterDevices(devicesList: List<Device>, type: DeviceTypes): List<Device> {
    var toRet = emptyList<Device>()
    for(device in devicesList) {
        if(device.type == type) {
            toRet = toRet + device
        }
    }
    return toRet
}
