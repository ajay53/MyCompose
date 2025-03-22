package com.goazzi.mycompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.goazzi.mycompose.navigation.AppNavHost
import com.goazzi.mycompose.navigation.NavigationItem
import com.goazzi.mycompose.ui.theme.MyComposeTheme
import dagger.hilt.android.AndroidEntryPoint

private const val TAG = "MainActivity"

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyComposeTheme {
                val navController = rememberNavController()

                SmallTopAppBarExample(navController = navController)

                /*Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
//                    MyComposeTheme {

                        App(innerPadding = innerPadding, navController = navController)
//                    }
                }*/
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SmallTopAppBarExample(navController: NavHostController) {
    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
                    Text("Small Top App Bar")
                }
            )
        },
        bottomBar = {
            BottomAppBar(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.primary,
                modifier = Modifier.height(60.dp),
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.clickable(onClick = {
                            navController.navigate(NavigationItem.Yelp.route)
                        })
                    ) {
                        Icon(Icons.Default.Search, contentDescription = "Yelp")
                        Text("Yelp", fontSize = 12.sp)
                    }
                    /*IconButton(onClick = { *//* Navigate to Yelp *//* }) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(Icons.Default.Search, contentDescription = "Yelp")
                            Text("Yelp", fontSize = 12.sp)
                        }
                    }*/
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.clickable(onClick = {
                            navController.navigate(NavigationItem.Pixabay.route)
                        })
                    ) {
                        Icon(Icons.Default.Face, contentDescription = "Pixabay")
                        Text("Pixabay", fontSize = 12.sp)
                    }
                    /*IconButton(onClick = { *//* Navigate to Pixabay *//* }) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(Icons.Default.Face, contentDescription = "Pixabay")
                            Text("Pixabay", fontSize = 12.sp)
                        }
                    }*/
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.clickable(onClick = {
                            navController.navigate(NavigationItem.Account.route)
                        })
                    ) {
                        Icon(Icons.Default.AccountCircle, contentDescription = "Account")
                        Text("Account", fontSize = 12.sp)
                    }
                    /*IconButton(onClick = { *//* Navigate to Account *//* }) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(Icons.Default.AccountCircle, contentDescription = "Account")
                            Text("Account", fontSize = 12.sp)
                        }
                    }*/
                }
            }

        },
    ) { innerPadding ->
//        ScrollContent(innerPadding)
        App(innerPadding = innerPadding, navController = navController)

    }
}

@Composable
fun App(
    innerPadding: PaddingValues,
    navController: NavHostController
) {
    AppNavHost(
        navController = navController,
//        startDestination = getRouteName(NavigationItem.LockSite),
        startDestination = NavigationItem.Pixabay.route,
        modifier = Modifier.padding(innerPadding)
    )
}

/*@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SeparatorSpacerPreview(){
    SeparatorSpacer(modifier = Modifier.padding(top = 30.dp))
}*/