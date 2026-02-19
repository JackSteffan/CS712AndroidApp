package com.example.assignment2

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.assignment2.ui.theme.Assignment2Theme

class MainActivity : ComponentActivity() {

    private val customAction = "com.example.MY_ACTION"

    private val myReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            Toast.makeText(
                context,
                "Broadcast received!",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    @SuppressLint("UnspecifiedRegisterReceiverFlag")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val filter = IntentFilter(customAction)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            registerReceiver(
                myReceiver,
                filter,
                Context.RECEIVER_NOT_EXPORTED
            )
        } else {
            registerReceiver(myReceiver, filter)
        }

        setContent {
            Assignment2Theme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Assignment2(
                        onExplicitClick = {
                            startActivity(
                                Intent(this, SecondActivity::class.java)
                            )
                        },
                        onImplicitClick = {
                            startActivity(
                                Intent("com.example.assignment2.SECOND_ACTIVITY")
                            )
                        },
                        onStartServiceClick = {
                            startForegroundService(
                                Intent(this, MyForegroundService::class.java)
                            )
                        },
                        onSendBroadcastClick = {
                            sendBroadcast(
                                Intent(customAction).setPackage(packageName)
                            )
                        }
                    )
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(myReceiver)
    }
}

@Composable
fun Assignment2(
    onExplicitClick: () -> Unit,
    onImplicitClick: () -> Unit,
    onStartServiceClick: () -> Unit,
    onSendBroadcastClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        Text(
            text = "My name is: Jack Steffan\nAnd my Student ID is 1363687",
            style = MaterialTheme.typography.bodyLarge
        )

        Button(onClick = onExplicitClick) {
            Text("Second Activity EX")
        }

        Button(onClick = onImplicitClick) {
            Text("Second Activity IM")
        }

        Button(onClick = onStartServiceClick) {
            Text("Start Service")
        }

        Button(onClick = onSendBroadcastClick) {
            Text("Send Broadcast")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun Assignment2Preview() {
    Assignment2Theme {
        Assignment2(
            onExplicitClick = {},
            onImplicitClick = {},
            onStartServiceClick = {},
            onSendBroadcastClick = {}
        )
    }
}
