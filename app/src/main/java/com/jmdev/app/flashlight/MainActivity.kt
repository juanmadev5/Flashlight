package com.jmdev.app.flashlight

import android.content.Context
import android.content.res.Configuration
import android.hardware.camera2.CameraManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.Wallpapers
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jmdev.app.flashlight.ui.theme.FlashlightTheme

class MainActivity : ComponentActivity() {

    private lateinit var cameraManager: CameraManager
    private lateinit var cameraID: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FlashlightTheme {

                val context = LocalContext.current
                val lightStatus = rememberSaveable { mutableStateOf(false) }
                val lightState = rememberSaveable { mutableIntStateOf(R.drawable.tabler__bulb_off) }

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        Text(
                            text = stringResource(R.string.app_name),
                            Modifier.padding(dimensionResource(id = R.dimen.large_padding)),
                            fontSize = dimensionResource(id = R.dimen.text_size).value.sp,
                            fontWeight = FontWeight.Light,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Icon(
                            painter = painterResource(id = lightState.intValue),
                            contentDescription = stringResource(R.string.state),
                            modifier = Modifier
                                .padding(dimensionResource(id = R.dimen.large_padding))
                                .size(dimensionResource(id = R.dimen.icon_size))
                                .align(Alignment.Center)
                        )
                        Switch(
                            modifier = Modifier.align(Alignment.BottomCenter),
                            checked = lightStatus.value,
                            onCheckedChange = {
                                lightStatus.value = it
                                if (lightStatus.value) {
                                    lightState.intValue = R.drawable.tabler__bulb_filled
                                } else {
                                    lightState.intValue = R.drawable.tabler__bulb_off
                                }
                                cameraManager =
                                    context.getSystemService(Context.CAMERA_SERVICE) as CameraManager
                                try {
                                    cameraID = cameraManager.cameraIdList[0]
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }
                                if (lightStatus.value) {
                                    cameraManager.setTorchMode(cameraID, true)
                                } else {
                                    cameraManager.setTorchMode(cameraID, false)
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}