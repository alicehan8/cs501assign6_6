package com.example.assign6_6

import android.os.Bundle
import android.widget.FrameLayout
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.assign6_6.ui.theme.Assign6_6Theme
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.PolygonOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Polygon
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Assign6_6Theme {
                HikingMapScreen()
            }
        }
    }

}


@Composable
fun HikingMapScreen() {
    // Trail coordinates (example: mountain hiking trail)
    val trailPoints = remember {
        listOf(
            LatLng(37.770352, -122.494247),
            LatLng(37.770658, -122.494848),
            LatLng(37.770689, -122.495419),
            LatLng(37.771569, -122.495467),
            LatLng(37.771553, -122.492495),
            LatLng(37.770903, -122.493366),
            LatLng(37.770352, -122.494247)
        )
    }

    // Park polygon coordinates
    val parkPolygon = remember {
        listOf(
            LatLng(37.764235, -122.510355),
            LatLng(37.766580, -122.453318),
            LatLng(37.774148, -122.455745),
            LatLng(37.771446, -122.510849)
        )
    }

    // Camera position
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(parkPolygon.first(), 14f)
    }

    // Customization states
    var polylineColor by remember { mutableStateOf(Color.Blue) }
    var polylineWidth by remember { mutableStateOf(10f) }
    var polygonFillColor by remember { mutableStateOf(Color.Green.copy(alpha = 0.3f)) }
    var polygonStrokeColor by remember { mutableStateOf(Color.Green) }
    var polygonStrokeWidth by remember { mutableStateOf(5f) }

    // Dialog states
    var showTrailInfo by remember { mutableStateOf(false) }
    var showParkInfo by remember { mutableStateOf(false) }
    var showCustomization by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            properties = MapProperties(isMyLocationEnabled = false),
            uiSettings = MapUiSettings(zoomControlsEnabled = true)
        ) {
            // Polygon for park area
            Polygon(
                points = parkPolygon,
                fillColor = polygonFillColor,
                strokeColor = polygonStrokeColor,
                strokeWidth = polygonStrokeWidth,
                clickable = true,
                onClick = {
                    showParkInfo = true
                }
            )

            // Polyline for hiking trail
            Polyline(
                points = trailPoints,
                color = polylineColor,
                width = polylineWidth,
                clickable = true,
                onClick = {
                    showTrailInfo = true
                },
                zIndex = 2f
            )
        }

        // Customization button
        FloatingActionButton(
            onClick = { showCustomization = true },
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(16.dp)
        ) {
            Text("Settings")
        }

        // Trail info dialog
        if (showTrailInfo) {
            InfoDialog(
                title = "Spreckels Lake Loop",
                description = """
                    Distance: 0.4 miles
                    Difficulty: Easy
                    Elevation Gain: 0 ft
                    Estimated Time: 0.5-1 hours
                    
                    This is a great spot for easy walks/strolls, meditate, read, and/or relax. Great for children and pets on leash.
                """.trimIndent(),
                onDismiss = { showTrailInfo = false }
            )
        }

        // Park info dialog
        if (showParkInfo) {
            InfoDialog(
                title = "Golden Gate Park",
                description = """
                    Area: 1,017 acres
                    Established: 1870
                    Features: Gardens, museums, lakes
                    Hours: Open 24/7
                    
                    One of the most visited urban parks in the United States.
                """.trimIndent(),
                onDismiss = { showParkInfo = false }
            )
        }

        // Customization dialog
        if (showCustomization) {
            CustomizationDialog(
                polylineColor = polylineColor,
                polylineWidth = polylineWidth,
                polygonFillColor = polygonFillColor,
                polygonStrokeColor = polygonStrokeColor,
                polygonStrokeWidth = polygonStrokeWidth,
                onPolylineColorChange = { polylineColor = it },
                onPolylineWidthChange = { polylineWidth = it },
                onPolygonFillColorChange = { polygonFillColor = it },
                onPolygonStrokeColorChange = { polygonStrokeColor = it },
                onPolygonStrokeWidthChange = { polygonStrokeWidth = it },
                onDismiss = { showCustomization = false }
            )
        }
    }
}

@Composable
fun InfoDialog(
    title: String,
    description: String,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.headlineSmall
                )
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodyMedium
                )
                Button(
                    onClick = onDismiss,
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text("Close")
                }
            }
        }
    }
}

@Composable
fun CustomizationDialog(
    polylineColor: Color,
    polylineWidth: Float,
    polygonFillColor: Color,
    polygonStrokeColor: Color,
    polygonStrokeWidth: Float,
    onPolylineColorChange: (Color) -> Unit,
    onPolylineWidthChange: (Float) -> Unit,
    onPolygonFillColorChange: (Color) -> Unit,
    onPolygonStrokeColorChange: (Color) -> Unit,
    onPolygonStrokeWidthChange: (Float) -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Customize Map Overlays",
                    style = MaterialTheme.typography.headlineSmall
                )

                // Polyline customization
                Text(
                    text = "Trail Line",
                    style = MaterialTheme.typography.titleMedium
                )

                ColorPicker(
                    label = "Line Color",
                    selectedColor = polylineColor,
                    onColorSelected = onPolylineColorChange
                )

                Text(text = "Line Width: ${polylineWidth.toInt()}px")
                Slider(
                    value = polylineWidth,
                    onValueChange = onPolylineWidthChange,
                    valueRange = 5f..30f,
                    steps = 24
                )

                HorizontalDivider()

                // Polygon customization
                Text(
                    text = "Park Area",
                    style = MaterialTheme.typography.titleMedium
                )

                ColorPicker(
                    label = "Fill Color",
                    selectedColor = polygonFillColor,
                    onColorSelected = onPolygonFillColorChange
                )

                ColorPicker(
                    label = "Border Color",
                    selectedColor = polygonStrokeColor,
                    onColorSelected = onPolygonStrokeColorChange
                )

                Text(text = "Border Width: ${polygonStrokeWidth.toInt()}px")
                Slider(
                    value = polygonStrokeWidth,
                    onValueChange = onPolygonStrokeWidthChange,
                    valueRange = 2f..20f,
                    steps = 17
                )

                Button(
                    onClick = onDismiss,
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text("Done")
                }
            }
        }
    }
}

@Composable
fun ColorPicker(
    label: String,
    selectedColor: Color,
    onColorSelected: (Color) -> Unit
) {
    val colors = listOf(
        Color.Red to "Red",
        Color.Blue to "Blue",
        Color.Green to "Green",
        Color.Yellow to "Yellow",
        Color.Magenta to "Purple",
        Color.Cyan to "Cyan",
        Color(0xFFFF9800) to "Orange"
    )

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(text = label, style = MaterialTheme.typography.bodyMedium)
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            colors.forEach { (color, name) ->
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(
                            color = color.copy(alpha = if (label.contains("Fill")) 0.3f else 1f),
                            shape = RoundedCornerShape(8.dp)
                        )
                        .then(
                            if (selectedColor.red == color.red &&
                                selectedColor.green == color.green &&
                                selectedColor.blue == color.blue
                            ) {
                                Modifier.border(
                                    width = 3.dp,
                                    color = Color.Black,
                                    shape = RoundedCornerShape(8.dp)
                                )
                            } else Modifier
                        )
                        .clickable {
                            onColorSelected(
                                if (label.contains("Fill")) color.copy(alpha = 0.3f) else color
                            )
                        }
                )
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    Assign6_6Theme {
        Greeting("Android")
    }
}