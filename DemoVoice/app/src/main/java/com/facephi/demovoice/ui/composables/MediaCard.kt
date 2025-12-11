package com.facephi.demovoice.ui.composables

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.facephi.demovoice.R
import com.facephi.demovoice.media.AppMediaPlayer

@Composable
fun MediaCard(
    title: String,
    desc: String,
    enabled: Boolean,
    audios: List<String>
) {

    var isPlaying by rememberSaveable {
        mutableStateOf(false)
    }

    var audioIndex by rememberSaveable {
        mutableIntStateOf(0)
    }

    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = colorResource(R.color.sdkBackgroundColor)
        ),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 6.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = desc,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            HorizontalDivider(
                thickness = 1.dp,
                color = MaterialTheme.colorScheme.outlineVariant
            )

            BaseButton(
                enabled = enabled,
                text = stringResource(id = R.string.onboarding_play),
                onClick = {
                    if (!audios.isEmpty()) {
                        audioIndex = 1
                        // AppMediaPlayer.stop()
                        AppMediaPlayer.init(
                            audios = audios.toList(),
                            indexOutput = {
                                audioIndex = it + 1
                            },
                            onStop = {
                                isPlaying = false
                            }
                        )
                        isPlaying = true
                        AppMediaPlayer.playAudios()
                    } else {
                        Log.d("APP", "Voice result is empty")
                    }
                }
            )


            if (isPlaying) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        modifier = Modifier
                            .padding(top = 16.dp, start = 16.dp, end = 16.dp)
                            .weight(1f),
                        text = "AUDIO $audioIndex",
                        color = colorResource(
                            id = R.color.sdkPrimaryColor
                        ),
                        textAlign = TextAlign.Center,
                    )

                    BaseButton(
                        modifier = Modifier
                            .padding(top = 16.dp, start = 16.dp, end = 16.dp)
                            .weight(1f),
                        text = "STOP",
                        onClick = {
                            AppMediaPlayer.stop()
                            isPlaying = false
                        }
                    )
                }
            }


        }
    }
}
