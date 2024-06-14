package ar.edu.itba.harmony_mobile.persistent

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import ar.edu.itba.harmony_mobile.R
import ar.edu.itba.harmony_mobile.ui.theme.*


@Composable
fun HarmonyNavigationBar(modifier: Modifier = Modifier, currentScreen: MutableState<String>) {
    NavigationBar (
        containerColor = MaterialTheme.colorScheme.secondary,
        contentColor = MaterialTheme.colorScheme.primary,
        modifier = modifier
    ) {
        NavigationBarItem(
            label = {
                Text(stringResource(R.string.rooms))
            },
            icon = {
                Icon(
                    imageVector = Icons.Outlined.LocationOn,
                    contentDescription = null,
                )
            },
            colors = NavigationBarItemColors(primary, primary, tertiary.copy(0.5f), primary, primary, disabled,
                disabled),
            selected = currentScreen.value == "rooms",
            onClick = { currentScreen.value = "rooms"},
            
        )
        NavigationBarItem(
            icon = {
                Icon(
                    imageVector = Icons.AutoMirrored.Default.List,
                    contentDescription = null,
                )
            },
            label = {
                Text(stringResource(R.string.devices))
            },
            colors = NavigationBarItemColors(primary, primary, tertiary.copy(0.5f), primary, primary, disabled,
                disabled),
            selected = currentScreen.value == "devices",
            onClick = { currentScreen.value = "devices"}
        )
        NavigationBarItem(
            icon = {
                Icon(
                    imageVector = Icons.Default.DateRange,
                    contentDescription = null,
                )
            },
            label = {
                Text(stringResource(R.string.routines))
            },
            colors = NavigationBarItemColors(primary, primary, tertiary.copy(0.5f), primary, primary, disabled,
                disabled),
            selected = currentScreen.value == "routines",
            onClick = { currentScreen.value = "routines"}
        )
    }
}