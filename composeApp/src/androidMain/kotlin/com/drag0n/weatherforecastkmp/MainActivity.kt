package com.drag0n.weatherforecastkmp

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.drag0n.weatherforecastkmp.presentation.App
import com.drag0n.weatherforecastkmp.presentation.MyViewModel
import org.koin.compose.viewmodel.koinViewModel


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val viewModel: MyViewModel = koinViewModel()
            val context = LocalContext.current

            // Только ваши две переменные
            val showGpsDialog = viewModel.showGpsDialog
            val requestPermission = viewModel.requestPermissionTrigger

            // Запускаем проверку при старте
            LaunchedEffect(Unit) {
                viewModel.isPermissionFun(Manifest.permission.ACCESS_FINE_LOCATION)
                viewModel.isGpsEnableFun()
            }

            // Лаунчер для разрешений
            val permissionLauncher = rememberLauncherForActivityResult(
                ActivityResultContracts.RequestPermission()
            ) { isGranted ->
                if (isGranted) {
                    viewModel.isGpsEnableFun()
                } else {
                    Toast.makeText(context, "Нужно разрешение на геолокацию", Toast.LENGTH_SHORT).show()
                }
            }

            // Следим за запросом разрешений
            LaunchedEffect(requestPermission) {
                if (requestPermission) {
                    permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                    viewModel.resetTriggers()
                }
            }

            // Диалог GPS
            if (showGpsDialog) {
                AlertDialog(
                    onDismissRequest = { viewModel.showGpsDialog = false },
                    title = { Text("GPS выключен") },
                    text = { Text("Включите GPS для получения погоды") },
                    confirmButton = {
                        Button(
                            onClick = {
                                // Не закрываем диалог до возврата
                                context.startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
                            }
                        ) {
                            Text("Настройки")
                        }
                    }
                )
            }

            // При возврате из настроек
            val lifecycleOwner = LocalLifecycleOwner.current
            DisposableEffect(lifecycleOwner) {
                val observer = LifecycleEventObserver { _, event ->
                    if (event == Lifecycle.Event.ON_RESUME) {
                        viewModel.isGpsEnableFun()
                    }
                }
                lifecycleOwner.lifecycle.addObserver(observer)
                onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
            }

            // App показываем только если нет ни диалога, ни запроса
            if (!showGpsDialog && !requestPermission) {
                App()
            } else {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Проверка доступа к геолокации...")
                }
            }
        }

    }
}











