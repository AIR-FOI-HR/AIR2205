package hr.foi.air.qr.composables

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun CameraView(
    onSuccessfullScan: (String) -> Unit
) {
    val cameraPermissionState = rememberPermissionState(android.Manifest.permission.CAMERA)

    if (cameraPermissionState.status.isGranted) {
        QrScanner(onSuccessfullScan)
    } else if (cameraPermissionState.status.shouldShowRationale) {
        Text("Nije dopušteno korištenje kamere!")
    } else {
        SideEffect {
            cameraPermissionState.run { launchPermissionRequest() }
        }
        Text("Nema dopuštenja za korištenje kamere.")
    }
}