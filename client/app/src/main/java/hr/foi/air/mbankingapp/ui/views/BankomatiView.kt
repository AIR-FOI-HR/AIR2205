package hr.foi.air.mbankingapp.ui.views

import android.Manifest
import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import hr.foi.air.mbankingapp.ui.composables.BankomatiMap

@SuppressLint("PermissionLaunchedDuringComposition")
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun BankomatiView(
    innerPadding: PaddingValues
) {
    val permissionState = rememberMultiplePermissionsState(
        listOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
        )
    )

    LaunchedEffect(permissionState) {
        val permissionsToRequest = permissionState.permissions.filter {
            !it.status.isGranted
        }

        if (permissionsToRequest.isNotEmpty()) permissionState.launchMultiplePermissionRequest()
    }

    Column(
        modifier = Modifier.padding(innerPadding)
    ) {
        if (permissionState.allPermissionsGranted) {
            BankomatiMap()
        } else {
            Text("Nema dopuštenja za korištenje lokacije.")
        }
    }
}

