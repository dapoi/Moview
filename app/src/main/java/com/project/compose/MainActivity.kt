package com.project.compose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle.Companion.dark
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.project.compose.core.common.ui.theme.StarterTheme
import com.project.compose.core.common.utils.LocalActivity
import com.project.compose.core.navigation.AppNavHost
import com.project.compose.core.navigation.base.BaseNavGraph
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var navGraphs: Set<@JvmSuppressWildcards BaseNavGraph>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(statusBarStyle = dark(scrim = Color.Transparent.toArgb()))
        setContent {
            StarterTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    CompositionLocalProvider(LocalActivity provides this) {
                        AppNavHost(navGraphs = navGraphs)
                    }
                }
            }
        }
    }
}