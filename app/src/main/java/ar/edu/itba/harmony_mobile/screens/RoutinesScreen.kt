package ar.edu.itba.harmony_mobile.screens

import android.widget.Toast
import androidx.activity.compose.BackHandler
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
import androidx.window.core.layout.WindowHeightSizeClass
import androidx.window.core.layout.WindowWidthSizeClass
import ar.edu.itba.harmony_mobile.R
import ar.edu.itba.harmony_mobile.ui.theme.primary
import ar.edu.itba.harmony_mobile.ui.theme.secondary
import ar.edu.itba.harmony_mobile.ui.theme.tertiary

val routines = listOf(
    Pair("Wake Up", "mdi-white-balance-sunny"),
    Pair("Sleep", "mdi-bed-king-outline"),
    Pair("Go To Work", "mdi-briefcase-variant-outline")
)

@Composable
fun RoutinesScreen(modifier: Modifier = Modifier, currentHouse: String) {

    var inList by rememberSaveable { mutableStateOf(false) }

    Box(modifier = modifier) {
        if (inList) {
            BackHandler(onBack = { inList = false })
        } else {
            RoutinesList(currentHouse) { inList = true }
        }
    }
}

@Composable
fun RoutinesList(currentHouse: String, onNav: () -> Unit) {

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
                .padding(horizontal = 6.dp,)
                .verticalScroll(scState)
        ) {
            val actions = listOf(
                "Start VacuumCleaner",
                "Turn on FootLamp",
                "Open Sprinkler",
                "Turn on CeilingLamp",
                "Turn on BarLight"
            )
            for (routine in routines) {
                if(isExpandedWidth) {
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
                                painterResource(id = getIcon(routine.second)),
                                contentDescription = null,
                                modifier = Modifier
                                    .padding(start = 16.dp)
                                    .height(80.dp)
                            )
                            Text(
                                text = routine.first,
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
                                if(actions.size > 3) {
                                    for(i in 0 until 2) {
                                        text = "• ${actions[i]}"
                                        Text(
                                            text = text,
                                            color = Color.Black
                                        )
                                    }
                                    Text(
                                        text = "Expand",
                                        style = MaterialTheme.typography.labelLarge,
                                        color = Color.Black.copy(alpha = .4f),
                                        modifier = Modifier.clickable { onNav() }
                                    )
                                } else {
                                    for(action in actions) {
                                        text = "• $action"
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
                                    Toast.makeText(
                                        context,
                                        "$text ${routine.first}",
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
                        onClick = { onNav() },
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
                                painterResource(id = getIcon(routine.second)),
                                contentDescription = null,
                                modifier = Modifier.height(80.dp)
                            )
                            Text(
                                text = routine.first,
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
                                    Toast.makeText(
                                        context,
                                        "$text ${routine.first}",
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

fun getIcon(webIcon: String): Int {
    val icon = when(webIcon) {
        "mdi-white-balance-sunny" -> R.drawable.sun_icon
        "mdi-briefcase-variant-outline" -> R.drawable.work_icon
        "mdi-bed-king-outline" -> R.drawable.night_icon
        "mdi-party-popper" -> R.drawable.party_icon
        else -> R.drawable.generic_rout_icon
    }

    return icon
}