package com.jmdev.app.flashlight

import android.content.Context
import android.content.res.Configuration
import android.hardware.camera2.CameraManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jmdev.app.flashlight.ui.theme.FlashlightTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FlashlightTheme {
                MainScreen()
            }
        }
    }
}
@Preview(device = "id:pixel_6_pro", showSystemUi = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL
)
@Composable
fun MainScreen() {
    val context = LocalContext.current
    val lightStatus = rememberSaveable { mutableStateOf(false) }
    val lightMsg = rememberSaveable { mutableStateOf("Apagada") }
    lateinit var cameraManager: CameraManager
    lateinit var cameraID: String

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.Black
    ) {
        Text(
            text = "Linterna " + lightMsg.value,
            Modifier.padding(32.dp, top = 60.dp),
            fontSize = 30.sp,
            fontWeight = FontWeight.Light,
            color = Color.White
        )
        Box(contentAlignment = Alignment.Center) {
            Switch(
                checked = lightStatus.value,
                onCheckedChange = {
                    lightStatus.value = it
                    cameraManager = context.getSystemService(Context.CAMERA_SERVICE) as CameraManager
                    try {
                        cameraID = cameraManager.cameraIdList[0]
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                    if (lightStatus.value) {
                        cameraManager.setTorchMode(cameraID, true)
                        lightMsg.value = "Encendida"
                    } else {
                        cameraManager.setTorchMode(cameraID, false)
                        lightMsg.value = "Apagada"
                    }
                }
            )
        }
    }
}