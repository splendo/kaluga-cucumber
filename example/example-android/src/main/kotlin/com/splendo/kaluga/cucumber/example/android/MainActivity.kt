package com.splendo.kaluga.cucumber.example.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.splendo.kaluga.cucumber.example.Tags

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { CounterScreen() }
    }
}

@Composable
fun CounterScreen() {
    var count by remember { mutableIntStateOf(0) }
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = count.toString(),
            fontSize = 48.sp,
            modifier = Modifier.testTag(Tags.COUNTER_VALUE),
        )
        Button(
            onClick = { count++ },
            modifier = Modifier.testTag(Tags.INCREMENT_BUTTON),
        ) {
            Text("Increment")
        }
    }
}
