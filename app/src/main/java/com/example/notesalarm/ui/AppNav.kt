package com.example.notesalarm.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun AppNav(modifier: Modifier = Modifier, navController: NavHostController = rememberNavController()) {
	NavHost(navController = navController, startDestination = "list", modifier = modifier) {
		composable("list") {
			val vm: NotesViewModel = viewModel()
			NotesListScreen(vm = vm, onCreate = { navController.navigate("edit") }, onEdit = { id ->
				navController.navigate("edit?id=$id")
			})
		}
		composable("edit") {
			val vm: NotesViewModel = viewModel()
			EditNoteScreen(vm = vm, onDone = { navController.popBackStack() })
		}
	}
}

