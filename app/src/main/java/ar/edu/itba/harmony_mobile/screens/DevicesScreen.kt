package ar.edu.itba.harmony_mobile.screens

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import ar.edu.itba.harmony_mobile.R

@Composable
fun DevicesScreen(modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Text(
            text = stringResource(id = R.string.devices),
            modifier = Modifier.padding(
                start = 12.dp,
                top = 12.dp
            ),
            style = MaterialTheme.typography.titleLarge
        )

        val scState = rememberScrollState(0)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 6.dp,
                    vertical = 12.dp
                )
                .verticalScroll(scState)
        ) {
            for (deviceType in DeviceTypes.entries) {
                Button(
                    modifier = Modifier.padding(4.dp),
                    shape = RoundedCornerShape(8.dp),
                    onClick = {}
                ) {
                    Row (
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            deviceType.icon,
                            contentDescription = null,
                            modifier = Modifier.height(60.dp)
                        )

                        Text(
                            text = stringResource(id = deviceType.type),
                            textAlign = TextAlign.Left,
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.padding(start = 16.dp)
                        )
                    }
                }
            }
        }
    }
}