package com.example.assignment2

import android.Manifest
import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.example.assignment2.ui.theme.Assignment2Theme
import kotlinx.coroutines.launch

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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Register broadcast receiver
        val filter = IntentFilter(customAction)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            registerReceiver(myReceiver, filter, Context.RECEIVER_NOT_EXPORTED)
        } else {
            registerReceiver(myReceiver, filter)
        }

        setContent {
            Assignment2Theme {
                // Handle Notification Permission for Android 13+ (API 33+)
                val context = LocalContext.current
                val launcher = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.RequestPermission(),
                    onResult = { isGranted ->
                        if (!isGranted) {
                            Toast.makeText(context, "Notification permission denied. Service might not show.", Toast.LENGTH_SHORT).show()
                        }
                    }
                )

                SideEffect {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        if (ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                            launcher.launch(Manifest.permission.POST_NOTIFICATIONS)
                        }
                    }
                }

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Assignment2(
                        onExplicitClick = {
                            startActivity(Intent(this, SecondActivity::class.java))
                        },
                        onImplicitClick = {
                            val intent = Intent("com.example.assignment2.SECOND_ACTIVITY")
                            intent.addCategory(Intent.CATEGORY_DEFAULT)
                            startActivity(intent)
                        },
                        onStartServiceClick = {
                            // Start service while app is in foreground
                            startForegroundServiceSafe()
                        },
                        onSendBroadcastClick = {
                            sendBroadcast(Intent(customAction).setPackage(packageName))
                        }
                    )
                }
            }
        }
    }

    private fun startForegroundServiceSafe() {
        val serviceIntent = Intent(this, MyForegroundService::class.java)
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(serviceIntent)
            } else {
                startService(serviceIntent)
            }
        } catch (e: Exception) {
            Toast.makeText(this, "Failed to start service: ${e.message}", Toast.LENGTH_LONG).show()
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
        Spacer(modifier = Modifier.height(48.dp))

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