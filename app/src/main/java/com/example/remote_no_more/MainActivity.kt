package com.example.remote_no_more

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.remote_no_more.ui.theme.Remote_No_MoreTheme
import com.example.remote_no_more.location.GeofenceManager

class MainActivity : ComponentActivity() {
    private lateinit var geofenceManager: GeofenceManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        geofenceManager = GeofenceManager(this)

        setContent {
            Remote_No_MoreTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    GeofenceScreen(geofenceManager, Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun GeofenceScreen(geofenceManager: GeofenceManager, modifier: Modifier = Modifier) {
    var latitude by remember { mutableStateOf("") }
    var longitude by remember { mutableStateOf("") }
    var radius by remember { mutableStateOf("") }

    Column(modifier = modifier.padding(16.dp)) {
        OutlinedTextField(value = latitude, onValueChange = { latitude = it }, label = { Text("Latitude") })
        OutlinedTextField(value = longitude, onValueChange = { longitude = it }, label = { Text("Longitude") })
        OutlinedTextField(value = radius, onValueChange = { radius = it }, label = { Text("Radius (m)") })
        Button(onClick = {
            val workLatitude = latitude.toDoubleOrNull()
            val workLongitude = longitude.toDoubleOrNull()
            val detectionRadius = radius.toFloatOrNull()
            if (workLatitude != null && workLongitude != null && detectionRadius != null) {
                geofenceManager.addGeofence(workLatitude, workLongitude, detectionRadius)
                Toast.makeText(LocalContext.current, "Geofence added!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(LocalContext.current, "Invalid input!", Toast.LENGTH_SHORT).show()
            }
        }) {
            Text("Set Geofence")
        }
    }
}