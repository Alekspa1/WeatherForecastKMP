package com.drag0n.weatherforecastkmp.presentation

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.drag0n.weatherforecastkmp.presentation.others.App
import com.drag0n.weatherforecastkmp.presentation.others.MyViewModel
import org.koin.compose.viewmodel.koinViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(
                android.graphics.Color.TRANSPARENT // Пишем полный путь здесь
            ),
            navigationBarStyle = SystemBarStyle.dark(
                android.graphics.Color.TRANSPARENT
            )
        )
        setContent {
            val viewModel: MyViewModel = koinViewModel()
            val context = LocalContext.current
            var dialog by remember { mutableStateOf(false) }
            val lifecycleOwner = LocalLifecycleOwner.current

            // Подписываемся на состояния из ViewModel
            val statePermissionGps = viewModel.statePermissionGps
            val statePermissionLocation = viewModel.statePermissionLocation

            val permissionLauncher = rememberLauncherForActivityResult(
                ActivityResultContracts.RequestPermission()
            ) { isGranted ->
                // ОБЯЗАТЕЛЬНО обновляем стейты во ViewModel после закрытия системного окна
                viewModel.isPermissionFun(PERMISSION_LOCATION)
                viewModel.isGpsEnableFun()

                if (isGranted) {
                    if (!viewModel.statePermissionGps) dialog = true
                } else {
                    Toast.makeText(context, "Тогда вы не сможете получать погоду", Toast.LENGTH_SHORT).show()
                }
            }

            // 1. ПЕРВИЧНАЯ ПРОВЕРКА ПРИ ЗАПУСКЕ
            LaunchedEffect(Unit) {
                // Сначала синхронизируем стейты с реальной системой
                viewModel.isPermissionFun(PERMISSION_LOCATION)
                viewModel.isGpsEnableFun()

                if (!viewModel.statePermissionLocation) {
                    permissionLauncher.launch(PERMISSION_LOCATION)
                } else if (!viewModel.statePermissionGps) {
                    dialog = true
                }
            }

            // 2. СЛЕЖКА ЗА ИЗМЕНЕНИЯМИ (Например, возврат из настроек)
            DisposableEffect(lifecycleOwner) {
                val observer = LifecycleEventObserver { _, event ->
                    if (event == Lifecycle.Event.ON_RESUME) {
                        viewModel.isPermissionFun(PERMISSION_LOCATION)
                        viewModel.isGpsEnableFun()

                        if (viewModel.statePermissionLocation && !viewModel.statePermissionGps) {
                            dialog = true
                        } else if (viewModel.statePermissionGps) {
                            dialog = false
                        }
                    }
                }
                lifecycleOwner.lifecycle.addObserver(observer)
                onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
            }

            // 3. ДИАЛОГ GPS
            if (dialog) {
                AlertDialog(
                    onDismissRequest = { dialog = false },
                    confirmButton = {
                        Button(onClick = {
                            context.startActivity(Intent(OPEN_SETTINGS_GPS))
                        }) { Text("Настройки") }
                    },
                    title = { Text("GPS выключен") },
                    text = { Text("Включите GPS для получения погоды") }
                )
            }

            // 4. УСЛОВИЕ ЗАПУСКА
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = Color(0xFF121212) // Поставь сюда цвет фона своего App()
            ) {
                // 2. Используем Crossfade — он самый плавный для смены экранов
                Crossfade(
                    targetState = statePermissionGps && statePermissionLocation,
                    animationSpec = tween(600) // Поставь 1000 (1 секунда) для теста, чтобы УВИДЕТЬ плавность
                ) { isReady ->
                    if (isReady) {
                        App()
                    } else {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Приложению требуются разрешения для работы...",
                                color = Color.White // Чтобы текст не терялся на темном фоне
                            )
                        }
                    }
                }
            }
        }
    }
    companion object{
        const val PERMISSION_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION
        const val OPEN_SETTINGS_GPS = Settings.ACTION_LOCATION_SOURCE_SETTINGS
    }
}