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
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.example.assignment2.ui.theme.Assignment2Theme

class MainActivity : ComponentActivity() {

    private val customAction = "com.example.MY_ACTION"
    private val MSE712_PERMISSION = "com.example.assignment2.MSE712"

    private val myReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            Toast.makeText(context, "Broadcast received!", Toast.LENGTH_SHORT).show()
        }
    }

    private lateinit var mse712PermissionLauncher: androidx.activity.result.ActivityResultLauncher<String>

    @SuppressLint("UnspecifiedRegisterReceiverFlag")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()


        mse712PermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { granted ->
            if (granted) {
                Toast.makeText(this, "MSE712 permission granted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "MSE712 permission denied", Toast.LENGTH_SHORT).show()
            }
        }


        if (ContextCompat.checkSelfPermission(this, MSE712_PERMISSION)
            != PackageManager.PERMISSION_GRANTED
        ) {
            mse712PermissionLauncher.launch(MSE712_PERMISSION)
        }

        val filter = IntentFilter(customAction)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            registerReceiver(myReceiver, filter, Context.RECEIVER_NOT_EXPORTED)
        } else {
            registerReceiver(myReceiver, filter)
        }

        setContent {
            Assignment2Theme {

                val context = LocalContext.current

                val launcher = rememberLauncherForActivityResult(
                    ActivityResultContracts.RequestPermission()
                ) { granted ->
                    if (!granted) {
                        Toast.makeText(context, "Permission denied", Toast.LENGTH_SHORT).show()
                    }
                }

                SideEffect {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        if (ContextCompat.checkSelfPermission(
                                context,
                                Manifest.permission.POST_NOTIFICATIONS
                            ) != PackageManager.PERMISSION_GRANTED
                        ) {
                            launcher.launch(Manifest.permission.POST_NOTIFICATIONS)
                        }
                    }
                }


                Surface(modifier = Modifier.fillMaxSize()) {
                    Assignment2(
                        onExplicitClick = {
                            startActivity(Intent(this, SecondActivity::class.java))
                        },
                        onImplicitClick = {
                            startActivity(Intent("com.example.assignment2.SECOND_ACTIVITY").apply {
                                addCategory(Intent.CATEGORY_DEFAULT)
                            })
                        },
                        onStartServiceClick = {
                            startService(Intent(this, MyForegroundService::class.java))
                        },
                        onSendBroadcastClick = {
                            sendBroadcast(Intent(customAction).setPackage(packageName))
                        },
                        onViewImageClick = {
                            startActivity(Intent(this, ImageActivity::class.java))
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
    onViewImageClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Spacer(modifier = Modifier.height(48.dp))

        Text("My name is: Jack Steffan\nAnd my Student ID is 1363687")

        Button(onClick = onExplicitClick) { Text("Second Activity EX") }
        Button(onClick = onImplicitClick) { Text("Second Activity IM") }
        Button(onClick = onStartServiceClick) { Text("Start Service") }
        Button(onClick = onSendBroadcastClick) { Text("Send Broadcast") }
        Button(onClick = onViewImageClick) { Text("View Image Activity") }
    }
}