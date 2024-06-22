package ar.edu.itba.harmony_mobile.screens

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.window.core.layout.WindowHeightSizeClass
import androidx.window.core.layout.WindowWidthSizeClass
import ar.edu.itba.harmony_mobile.R
import ar.edu.itba.harmony_mobile.model.Home
import ar.edu.itba.harmony_mobile.model.Routine
import ar.edu.itba.harmony_mobile.ui.getViewModelFactory
import ar.edu.itba.harmony_mobile.ui.rooms.RoutinesViewModel
import ar.edu.itba.harmony_mobile.ui.theme.primary
import ar.edu.itba.harmony_mobile.ui.theme.secondary
import ar.edu.itba.harmony_mobile.ui.theme.tertiary

@Composable
fun RoutinesScreen(
    modifier: Modifier = Modifier,
    currentHouse: Home,
    rViewModel: RoutinesViewModel = viewModel(factory = getViewModelFactory())
) {
    val routinesState by rViewModel.uiState.collectAsState()

    var inList by rememberSaveable { mutableStateOf(false) }
    var showingRoutine by rememberSaveable { mutableStateOf("") }

    Box(modifier = modifier) {
        if (inList) {
            RoutineView(
                routine = routinesState.getRoutine(showingRoutine)!!,
                rViewModel = rViewModel
            ) { inList = false }
        } else {
            if (routinesState.getHomeRoutines(currentHouse).isEmpty()) {
                val text =
                    if (currentHouse.id == "0") stringResource(id = R.string.personal_devices) else currentHouse.name
                EmptyScreen(description = "${stringResource(id = R.string.no_routines)} $text")
            }
            RoutinesList(
                routines = routinesState.getHomeRoutines(currentHouse),
                rViewModel = rViewModel,
                onNav = { routineId ->
                    showingRoutine = routineId
                    inList = true
                })
        }
    }
}

@Composable
fun RoutinesList(
    routines: List<Routine>,
    rViewModel: RoutinesViewModel,
    onNav: (String) -> Unit
) {

    val scState = rememberScrollState(0)
    val windowClass = currentWindowAdaptiveInfo().windowSizeClass
    val isExpandedWidth = windowClass.windowWidthSizeClass == WindowWidthSizeClass.EXPANDED
    val isCompactWidth = windowClass.windowWidthSizeClass == WindowWidthSizeClass.COMPACT
    val isExpandedHeight = windowClass.windowHeightSizeClass == WindowHeightSizeClass.EXPANDED

    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            text = stringResource(id = R.string.routines),
            modifier = Modifier.padding(
                start = 12.dp,
                top = 12.dp,
                bottom = 12.dp
            ),
            style = MaterialTheme.typography.titleLarge
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 6.dp)
                .verticalScroll(scState)
        ) {
            for (routine in routines) {
                if (isExpandedWidth) {
                    Surface(
                        modifier = Modifier
                            .padding(4.dp)
                            .fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp),
                        color = secondary,
                        shadowElevation = 8.dp,
                        border = BorderStroke(
                            width = 1.dp,
                            color = Color.Black.copy(alpha = 0.3f)
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 12.dp)
                                .height(90.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                painterResource(id = getIcon(routine.icon)),
                                contentDescription = null,
                                modifier = Modifier
                                    .padding(start = 16.dp)
                                    .height(80.dp)
                            )
                            Text(
                                text = routine.name,
                                textAlign = TextAlign.Left,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis,
                                style = MaterialTheme.typography.titleMedium,
                                modifier = Modifier
                                    .padding(start = 16.dp)
                                    .width(200.dp)
                            )
                            Column(
                                modifier = Modifier.padding(start = 4.dp),
                                horizontalAlignment = Alignment.Start
                            ) {
                                var text: String
                                if (routine.actions.size > 3) {
                                    for (i in 0 until 2) {
                                        text =
                                            "• ${actionToString(routine.actions[i].actionName)} ${routine.actions[i].deviceName}"
                                        Text(
                                            text = text,
                                            color = Color.Black
                                        )
                                    }
                                    Text(
                                        text = stringResource(id = R.string.expand),
                                        style = MaterialTheme.typography.headlineMedium,
                                        color = Color.Black.copy(alpha = .4f),
                                        modifier = Modifier.clickable { onNav(routine.id!!) }
                                    )
                                } else {
                                    for (action in routine.actions) {
                                        text =
                                            "• ${actionToString(action.actionName)} ${action.deviceName}"
                                        Text(
                                            text = text,
                                            color = Color.Black
                                        )
                                    }
                                }
                            }
                            Spacer(modifier = Modifier.weight(1f))
                            val text = stringResource(id = R.string.running)
                            val context = LocalContext.current
                            Button(
                                modifier = Modifier
                                    .padding(end = 8.dp)
                                    .wrapContentWidth(),
                                shape = CircleShape,
                                onClick = {
                                    if (routine.id != null)
                                        rViewModel.executeRoutine(routine.id!!)
                                    Toast.makeText(
                                        context,
                                        "$text ${routine.name}",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                },
                                colors = ButtonColors(primary, secondary, tertiary, tertiary)
                            ) {
                                Icon(
                                    Icons.Outlined.PlayArrow,
                                    contentDescription = null,
                                    modifier = Modifier.wrapContentWidth()
                                )
                            }
                        }
                    }
                } else if (isExpandedHeight) {
                    Surface(
                        modifier = Modifier
                            .padding(4.dp)
                            .fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp),
                        color = secondary,
                        shadowElevation = 8.dp,
                        border = BorderStroke(
                            width = 1.dp,
                            color = Color.Black.copy(alpha = 0.3f)
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 12.dp)
                                .height(140.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(
                                modifier = Modifier
                                    .padding(end = 16.dp)
                                    .width(200.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Icon(
                                    painterResource(id = getIcon(routine.icon)),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .padding(start = 16.dp)
                                        .height(80.dp)
                                )
                                Text(
                                    text = routine.name,
                                    textAlign = TextAlign.Center,
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis,
                                    style = MaterialTheme.typography.titleMedium,
                                    modifier = Modifier
                                        .padding(start = 16.dp)
                                        .width(200.dp)
                                )
                            }
                            Column(
                                modifier = Modifier.padding(start = 4.dp),
                                horizontalAlignment = Alignment.Start
                            ) {
                                var text: String
                                if (routine.actions.size > 6) {
                                    for (i in 0 until 5) {
                                        text =
                                            "• ${actionToString(routine.actions[i].actionName)} ${routine.actions[i].deviceName}"
                                        Text(
                                            text = text,
                                            color = Color.Black
                                        )
                                    }
                                    Text(
                                        text = stringResource(id = R.string.expand),
                                        style = MaterialTheme.typography.headlineMedium,
                                        color = Color.Black.copy(alpha = .4f),
                                        modifier = Modifier.clickable { onNav(routine.id!!) }
                                    )
                                } else {
                                    for (action in routine.actions) {
                                        text =
                                            "• ${actionToString(action.actionName)} ${action.deviceName}"
                                        Text(
                                            text = text,
                                            color = Color.Black
                                        )
                                    }
                                }
                            }
                            Spacer(modifier = Modifier.weight(1f))
                            val text = stringResource(id = R.string.running)
                            val context = LocalContext.current
                            Button(
                                modifier = Modifier
                                    .padding(end = 8.dp)
                                    .wrapContentWidth(),
                                shape = CircleShape,
                                onClick = {
                                    if (routine.id != null)
                                        rViewModel.executeRoutine(routine.id!!)
                                    Toast.makeText(
                                        context,
                                        "$text ${routine.name}",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                },
                                colors = ButtonColors(primary, secondary, tertiary, tertiary)
                            ) {
                                Icon(
                                    Icons.Outlined.PlayArrow,
                                    contentDescription = null,
                                    modifier = Modifier.wrapContentWidth()
                                )
                            }
                        }
                    }
                } else {
                    Button(
                        modifier = Modifier
                            .padding(4.dp)
                            .fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp),
                        onClick = { onNav(routine.id!!) },
                        elevation = ButtonDefaults.buttonElevation(8.dp),
                        colors = ButtonColors(
                            secondary,
                            primary,
                            tertiary.copy(alpha = .5f),
                            Color.White
                        ),
                        border = BorderStroke(
                            width = 1.dp,
                            color = Color.Black.copy(alpha = 0.3f)
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 12.dp)
                                .height(90.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                painterResource(id = getIcon(routine.icon)),
                                contentDescription = null,
                                modifier = Modifier.height(80.dp)
                            )
                            Text(
                                text = routine.name,
                                textAlign = TextAlign.Left,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis,
                                style = MaterialTheme.typography.titleMedium,
                                modifier = Modifier
                                    .padding(start = 16.dp)
                                    .widthIn(max = if (isCompactWidth) 120.dp else 360.dp)
                            )
                            Spacer(modifier = Modifier.weight(1f))
                            val text = stringResource(id = R.string.running)
                            val context = LocalContext.current
                            Button(
                                modifier = Modifier
                                    .padding(end = 8.dp)
                                    .wrapContentWidth(),
                                shape = CircleShape,
                                onClick = {
                                    if (routine.id != null)
                                        rViewModel.executeRoutine(routine.id!!)
                                    Toast.makeText(
                                        context,
                                        "$text ${routine.name}",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                },
                                colors = ButtonColors(primary, secondary, tertiary, tertiary)
                            ) {
                                Icon(
                                    Icons.Outlined.PlayArrow,
                                    contentDescription = null,
                                    modifier = Modifier.wrapContentWidth()
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

private fun getIcon(webIcon: String): Int {
    val icon = when (webIcon) {
        "mdi-white-balance-sunny" -> R.drawable.sun_icon
        "mdi-briefcase-variant-outline" -> R.drawable.work_icon
        "mdi-bed-king-outline" -> R.drawable.night_icon
        "mdi-party-popper" -> R.drawable.party_icon
        else -> R.drawable.generic_rout_icon
    }

    return icon
}

private val actionsMap = mapOf(
    Pair("turnOn", R.string.turn_on),
    Pair("open", R.string.open),
    Pair("close", R.string.close),
    Pair("turnOff", R.string.turn_off),
    Pair("dispense", R.string.dispense),
    Pair("setLevel", R.string.set_level),
    Pair("setColor", R.string.set_color),
    Pair("setBrightness", R.string.set_brightness),
    Pair("setTemperature", R.string.set_fridge_temperature),
    Pair("setFreezerTemperature", R.string.set_freezer_temperature),
    Pair("setMode", R.string.set_mode),
    Pair("start", R.string.start),
    Pair("pause", R.string.pause),
    Pair("dock", R.string.dock),
    Pair("setLocation", R.string.set_location)
)

@Composable
fun actionToString(action: String): String {
    return stringResource(id = actionsMap[action]!!)
}