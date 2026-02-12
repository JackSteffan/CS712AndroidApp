package com.example.assignment2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

class SecondActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            SecondScreen(
                onBackClick = { finish() }
            )
        }
    }
}

@Composable
fun SecondScreen(onBackClick: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().padding(20.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = "Mobile Software Engineering Challenges:",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = """
                1. Device Fragmentation
                2. OS Version Compatibility
                3. Performance Optimization
                4. Battery Consumption
                5. Security and Data Privacy
            """.trimIndent()
        )

        Spacer(modifier = Modifier.height(30.dp))

        Button(onClick = onBackClick) {
            Text("Main Activity")
        }
    }
}
