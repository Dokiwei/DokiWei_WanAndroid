package com.dokiwei.wanandroid.ui.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.dokiwei.wanandroid.ui.screens.mainnavhost.MainNavHost
import com.dokiwei.wanandroid.ui.screens.startscreen.StartScreen
import com.dokiwei.wanandroid.ui.theme.MainTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainTheme {
                MainNavHost()
            }
        }
    }
}



@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MainTheme {

    }
}