package com.example.assignment2

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.assignment2.ui.theme.Assignment2Theme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Assignment2Theme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Assignment2(
                        onExplicitClick = {
                        startActivity(Intent(this, SecondActivity::class.java))
                    },
                        onImplicitClick = {
                            startActivity(Intent("com.example.assignment2.SECOND_ACTIVITY"))
                        }
                    )

                }


            }
        }
    }
}


@Composable
fun Assignment2(
    onExplicitClick: () -> Unit,
    onImplicitClick: () -> Unit,
    modifier: Modifier = Modifier
 ) {
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = "My name is: Jack Steffan! \n" +
                    "And my Student ID is 1363687"
        )

        Button(
            onClick = onExplicitClick
        ) {
            Text(text = "Second Activity EX")
        }
        Button(
            onImplicitClick
        ) {
            Text(text = "Second Activity IM")
        }


    }
}
@Composable
fun NameAndID(modifier: Modifier = Modifier) {

}

@Composable
fun SecondActivity(modifier: Modifier = Modifier){
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {

        Button(
            onClick = { },
        ) {
            Text(text = "Main Activity")
        }
    }
}


@Preview(showBackground = true)
@Composable
fun Assignment2Preview() {
    Assignment2Theme {
        NameAndID()
    }
}