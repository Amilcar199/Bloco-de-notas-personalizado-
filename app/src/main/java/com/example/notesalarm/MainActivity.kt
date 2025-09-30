package com.example.notesalarm

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.notesalarm.ui.theme.AppTheme
import com.example.notesalarm.ui.AppNav

class MainActivity : ComponentActivity() {
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContent {
			AppTheme {
				Surface(color = MaterialTheme.colorScheme.background) {
					AppRoot()
				}
			}
		}
	}
}

@Composable
fun AppRoot() {
	AppNav()
}

@Preview
@Composable
fun AppRootPreview() {
	MaterialTheme {
		AppRoot()
	}
}

