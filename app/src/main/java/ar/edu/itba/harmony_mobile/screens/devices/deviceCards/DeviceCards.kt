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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import ar.edu.itba.harmony_mobile.DeviceTypes
import ar.edu.itba.harmony_mobile.ui.theme.primary
import ar.edu.itba.harmony_mobile.ui.theme.secondary
import ar.edu.itba.harmony_mobile.ui.theme.tertiary

@Composable
fun MyCard(
    name: String,
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
                text = name,
                textAlign = TextAlign.Left,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(start = 16.dp)
            )
            Spacer(modifier = Modifier.weight(1f))
            content()
        }
    }
}

@SuppressLint("ModifierParameter")
@Composable
fun LightCard(name: String, modifier: Modifier = Modifier.fillMaxWidth().padding(4.dp), onClick: () -> Unit) {
    val status = "On"
    val brightness = 75

    MyCard(
        name = name,
        type = DeviceTypes.LIGHTS,
        modifier = modifier,
        content = {
            Column {
                Text(
                    text = "Status: $status",
                    style = MaterialTheme.typography.bodyLarge
                )
                if(status == "On") {
                    Text(
                        text = "Brightness: $brightness%",
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
fun DoorCard(name: String, modifier: Modifier = Modifier.fillMaxWidth().padding(4.dp), onClick: () -> Unit) {
    val status = "Closed"
    val lock = "Locked"
    MyCard(
        name = name,
        type = DeviceTypes.DOORS,
        modifier = modifier,
        content = {
            Column {
                Text(
                    text = "Status: $status",
                    style = MaterialTheme.typography.bodyLarge
                )
                if(status == "Closed")
                Text(
                    text = "Lock: $lock",
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
fun RefrigeratorCard(name: String, modifier: Modifier = Modifier.fillMaxWidth().padding(4.dp), onClick: () -> Unit) {
    val fridgeTemp = 2
    val freezerTemp = -8

    MyCard(
        name = name,
        type = DeviceTypes.REFRIS,
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
fun VacuumCard(name: String, modifier: Modifier = Modifier.fillMaxWidth().padding(4.dp), onClick: () -> Unit) {
    val status = "On" // Docked also possible
    val battery = 75
    MyCard(
        name = name,
        type = DeviceTypes.VACUUMS,
        modifier = modifier,
        content = {
            Column {
                Text(
                    text = "Status: $status",
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
fun SprinklerCard(name: String, modifier: Modifier = Modifier.fillMaxWidth().padding(4.dp), onClick: () -> Unit) {
    val status = "On"
    MyCard(
        name = name,
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
fun BlindsCard(name: String, modifier: Modifier = Modifier.fillMaxWidth().padding(4.dp), onClick: () -> Unit) {
    val status = "Closed"
    val maxLevel = 75
    MyCard(
        name = name,
        type = DeviceTypes.BLINDS,
        modifier = modifier,
        content = {
            Column {
                Text(
                    text = "Status: $status",
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = "Max Level: $maxLevel%",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
    ) {
        onClick()
    }
}
