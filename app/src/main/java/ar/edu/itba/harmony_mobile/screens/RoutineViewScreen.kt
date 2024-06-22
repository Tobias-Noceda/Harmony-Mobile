package ar.edu.itba.harmony_mobile.screens

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ar.edu.itba.harmony_mobile.R
import ar.edu.itba.harmony_mobile.model.Routine
import ar.edu.itba.harmony_mobile.ui.rooms.RoutinesViewModel
import ar.edu.itba.harmony_mobile.ui.theme.primary
import ar.edu.itba.harmony_mobile.ui.theme.secondary
import ar.edu.itba.harmony_mobile.ui.theme.tertiary

@Composable
fun RoutineView(routine: Routine, rViewModel: RoutinesViewModel, onBack: () -> Unit) {
    val scState = rememberScrollState(0)

    BackHandler(onBack = onBack)
    Column(modifier = Modifier
        .fillMaxHeight()
        .padding(8.dp)
    ) {
        Text(
            text = routine.name,
            modifier = Modifier.padding(bottom = 12.dp),
            style = MaterialTheme.typography.titleLarge
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 6.dp,
                )
                .verticalScroll(scState),
            horizontalAlignment = Alignment.Start
        ) {
            var text: String
            for (action in routine.actions) {
                text = "• ${actionToString(action.actionName)} ${action.deviceName}"
                Text(
                    text = text,
                    color = Color.Black,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(2.dp)
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 8.dp)
            ) {
                Spacer(modifier = Modifier.weight(1f))
                val running = stringResource(id = R.string.running)
                val context = LocalContext.current
                Button(
                    modifier = Modifier
                        .padding(end = 8.dp, top = 20.dp)
                        .wrapContentWidth(),
                    shape = CircleShape,
                    onClick = {
                        if (routine.id != null)
                            rViewModel.executeRoutine(routine.id!!)
                        Toast.makeText(
                            context,
                            "$running ${routine.name}",
                            Toast.LENGTH_SHORT
                        ).show()
                    },
                    colors = ButtonColors(primary, secondary, tertiary, tertiary)
                ) {
                    Row {
                        Text(
                            text = stringResource(id = R.string.run)
                        )
                        Icon(
                            Icons.Outlined.PlayArrow,
                            contentDescription = null,
                            modifier = Modifier
                                .wrapContentWidth()
                                .padding(start = 8.dp)
                        )
                    }
                }
            }
        }
    }
}