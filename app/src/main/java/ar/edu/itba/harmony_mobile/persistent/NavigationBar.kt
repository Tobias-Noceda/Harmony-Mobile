package ar.edu.itba.harmony_mobile.persistent
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.NavigationRailItemDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteDefaults
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffoldDefaults
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteType
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.window.core.layout.WindowWidthSizeClass
import ar.edu.itba.harmony_mobile.AppDestinations
import ar.edu.itba.harmony_mobile.screens.RoomsScreen
import ar.edu.itba.harmony_mobile.screens.RoutinesScreen
import ar.edu.itba.harmony_mobile.screens.devices_screen.DevicesScreen
import ar.edu.itba.harmony_mobile.ui.theme.*


@Composable
fun HarmonyNavigationBar(modifier: Modifier = Modifier, currentDestination: MutableState<AppDestinations>) {

    val myNavigationSuiteItemColors = NavigationSuiteDefaults.itemColors(
        navigationBarItemColors = NavigationBarItemDefaults.colors(
            primary, primary, tertiary.copy(0.5f), primary, primary, disabled,
            disabled
        ),
        navigationRailItemColors = NavigationRailItemDefaults.colors(
            primary, primary, tertiary.copy(0.5f), primary, primary, disabled,
            disabled
        ),
    )

    val adaptiveInfo = currentWindowAdaptiveInfo()


    NavigationSuiteScaffold(
        layoutType = with(adaptiveInfo){
            if (windowSizeClass.windowWidthSizeClass == WindowWidthSizeClass.COMPACT) {
                NavigationSuiteType.NavigationBar
            } else {
                NavigationSuiteScaffoldDefaults.calculateFromAdaptiveInfo(adaptiveInfo)
            }
        },
        modifier = Modifier.shadow(16.dp),
        navigationSuiteColors = NavigationSuiteDefaults.colors(
            navigationBarContainerColor = secondary,
            navigationRailContainerColor = secondary,
        ),
        navigationSuiteItems = {
            AppDestinations.entries.forEach {
                item(
                    icon = {
                        Icon(
                            it.icon,
                            contentDescription = stringResource(it.contentDescription)
                        )
                    },
                    label = { Text(stringResource(it.label)) },
                    selected = it == currentDestination.value,
                    colors = myNavigationSuiteItemColors,
                    onClick = { currentDestination.value = it }
                )
            }
        }
    ) {
        when (currentDestination.value) {
            AppDestinations.ROOMS -> RoomsScreen()
            AppDestinations.DEVICES -> DevicesScreen()
            AppDestinations.ROUTINES -> RoutinesScreen()
        }
    }
}