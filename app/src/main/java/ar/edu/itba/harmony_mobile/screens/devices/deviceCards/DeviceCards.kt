package ar.edu.itba.harmony_mobile.screens.devices.deviceCards

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import ar.edu.itba.harmony_mobile.DeviceTypes
import ar.edu.itba.harmony_mobile.R
import ar.edu.itba.harmony_mobile.model.Blinds
import ar.edu.itba.harmony_mobile.model.Device
import ar.edu.itba.harmony_mobile.model.Door
import ar.edu.itba.harmony_mobile.model.Lamp
import ar.edu.itba.harmony_mobile.model.Refrigerator
import ar.edu.itba.harmony_mobile.model.Sprinkler
import ar.edu.itba.harmony_mobile.model.Status
import ar.edu.itba.harmony_mobile.model.Vacuum
import ar.edu.itba.harmony_mobile.ui.devices.DevicesViewModel
import ar.edu.itba.harmony_mobile.ui.getViewModelFactory
import ar.edu.itba.harmony_mobile.ui.theme.primary
import ar.edu.itba.harmony_mobile.ui.theme.secondary
import ar.edu.itba.harmony_mobile.ui.theme.tertiary

@Composable
fun MyCard(
    device: Device,
    type: DeviceTypes,
    modifier: Modifier,
    content: @Composable () -> Unit,
    onClick: () -> Unit
) {
    Button(
        modifier = modifier,
        shape = RoundedCornerShape(8.dp),
        onClick = { onClick() },
        colors = ButtonColors(secondary, primary, tertiary.copy(alpha = .5f), Color.White),
        elevation = ButtonDefaults.buttonElevation(8.dp),
        border = BorderStroke(
            width = 1.dp,
            color = Color.Black.copy(alpha = 0.3f)
        )
    ) {
        Row(
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
            Spacer(modifier = Modifier.weight(1f))
            content()
        }
    }
}

@Composable
private fun getLamp(lamp: Device): Lamp {
    val dViewModel: DevicesViewModel = viewModel(factory = getViewModelFactory())
    val devicesState by dViewModel.uiState.collectAsState()
    return if(devicesState.getDevice(lamp.id!!) == null) {
        lamp as Lamp
    } else {
        devicesState.getDevice(lamp.id) as Lamp
    }
}

@SuppressLint("ModifierParameter")
@Composable
fun LightCard(
    lamp: Device,
    modifier: Modifier = Modifier
        .fillMaxWidth()
        .padding(4.dp),
    onClick: () -> Unit
) {
    val actualLamp = getLamp(lamp = lamp)
    MyCard(
        device = lamp,
        type = DeviceTypes.LIGHTS,
        modifier = modifier,
        content = {
            Column {
                Text(
                    text = "${stringResource(R.string.status)} ${if (actualLamp.status == Status.ON) stringResource(R.string.on) else stringResource(R.string.off)}",
                    style = MaterialTheme.typography.bodyLarge
                )
                if(actualLamp.status == Status.ON) {
                    Text(
                        text = "${stringResource(id = R.string.brightness)} ${actualLamp.brightness}%",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }
        }
    ) {
        onClick()
    }
}

@SuppressLint("ModifierParameter")
@Composable
fun DoorCard(
    door: Device,
    modifier: Modifier = Modifier
        .fillMaxWidth()
        .padding(4.dp),
    onClick: () -> Unit
) {
    val actualDoor = door as Door
    val status = actualDoor.status
    var text = if(status == Status.OPEN) stringResource(R.string.open) else stringResource(R.string.closed)
    MyCard(
        device = door,
        type = DeviceTypes.DOORS,
        modifier = modifier,
        content = {
            Column {
                Text(
                    text = "${stringResource(id = R.string.status)} $text",
                    style = MaterialTheme.typography.bodyLarge
                )
                if(status == Status.CLOSED) {
                    val lock = if(actualDoor.lock) stringResource(R.string.locked) else stringResource(R.string.unlocked)
                    Text(
                        text = lock,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }
        }
    ) {
        onClick()
    }
}

@SuppressLint("ModifierParameter")
@Composable
fun RefrigeratorCard(
    refrigerator: Device,
    modifier: Modifier = Modifier
        .fillMaxWidth()
        .padding(4.dp),
    onClick: () -> Unit
) {
    val actualRefrigerator = refrigerator as Refrigerator
    val fridgeTemp = actualRefrigerator.temperature
    val freezerTemp = actualRefrigerator.freezerTemperature

    MyCard(
        device = refrigerator,
        type = DeviceTypes.REFRIGERATORS,
        modifier = modifier,
        content = {
            Column {
                Text(
                    text = "Fridge: $fridgeTemp°C",
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = "Freezer: $freezerTemp°C",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
    ) {
        onClick()
    }
}

@SuppressLint("ModifierParameter")
@Composable
fun VacuumCard(
    vacuum: Device,
    modifier: Modifier = Modifier
        .fillMaxWidth()
        .padding(4.dp),
    onClick: () -> Unit
) {
    val actualVacuum = vacuum as Vacuum
    val status = when (actualVacuum.status)
    {
        Status.ON -> { stringResource(id = R.string.on) }
        Status.DOCKED -> { stringResource(id = R.string.docked) }
        else -> { stringResource(id = R.string.off) }
    }
    val battery = actualVacuum.battery
    MyCard(
        device = vacuum,
        type = DeviceTypes.VACUUMS,
        modifier = modifier,
        content = {
            Column {
                Text(
                    text = "${stringResource(id = R.string.status)} $status",
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = "Battery: $battery%",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
    ) {
        onClick()
    }
}

@SuppressLint("ModifierParameter")
@Composable
fun SprinklerCard(
    sprinkler: Device,
    modifier: Modifier = Modifier
        .fillMaxWidth()
        .padding(4.dp),
    onClick: () -> Unit
) {
    val actualSprinkler = sprinkler as Sprinkler
    val status = if (actualSprinkler.status == Status.OPEN) {
        stringResource(id = R.string.opened)
    } else {
        stringResource(id = R.string.closed)
    }
    MyCard(
        device = sprinkler,
        type = DeviceTypes.SPRINKLERS,
        modifier = modifier,
        content = {
            Column {
                Text(
                    text = "Status: $status",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    ) {
        onClick()
    }
}

@SuppressLint("ModifierParameter")
@Composable
fun BlindsCard(
    blinds: Device,
    modifier: Modifier = Modifier
        .fillMaxWidth()
        .padding(4.dp),
    onClick: () -> Unit
) {
    val actualBlinds = blinds as Blinds
    val status = if (actualBlinds.status == Status.OPEN) {
        stringResource(id = R.string.opened)
    } else {
        stringResource(id = R.string.closed)
    }
    val maxLevel = actualBlinds.level
    MyCard(
        device = blinds,
        type = DeviceTypes.BLINDS,
        modifier = modifier,
        content = {
            Column {
                Text(
                    text = "${stringResource(id = R.string.status)} $status",
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = "${stringResource(id = R.string.limit)} $maxLevel%",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
    ) {
        onClick()
    }
}
