package com.example.smartrecipeexplorer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.smartrecipeexplorer.ui.navigation.NavGraph
import com.example.smartrecipeexplorer.ui.screens.HomeScreen
import com.example.smartrecipeexplorer.ui.theme.SmartRecipeExplorerTheme
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.HiltAndroidApp

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       // enableEdgeToEdge()
        setContent {
            SmartRecipeExplorerTheme {

                val systemUiController = rememberSystemUiController()

                SideEffect {
                    systemUiController.setStatusBarColor(
                        color = androidx.compose.ui.graphics.Color.Transparent,
                        darkIcons = true // 👈 THIS FIXES WHITE ICON ISSUE
                    )
                }

                NavGraph()
            }
        }
    }
}