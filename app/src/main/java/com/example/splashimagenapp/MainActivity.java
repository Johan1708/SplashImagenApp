package com.example.splashimagenapp;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.splashscreen.SplashScreen;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

public class MainActivity extends AppCompatActivity {

    // Bandera para saber cuándo termina la carga
    private final AtomicBoolean cargaTerminada = new AtomicBoolean(false);

    // Hilo secundario para no congelar la pantalla principal
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // 1. Instalar el SplashScreen ANTES de super.onCreate()
        SplashScreen splashScreen = SplashScreen.installSplashScreen(this);

        super.onCreate(savedInstanceState);

        // 2. Mantener la pantalla de carga mientras la bandera sea false
        splashScreen.setKeepOnScreenCondition(() -> !cargaTerminada.get());

        setContentView(R.layout.activity_main);

        // 3. Iniciar la preparación/carga en segundo plano
        prepararAplicacion();
    }

    private void prepararAplicacion() {
        executor.execute(() -> {
            try {
                // Simulación de carga (1.2 segundos para ver el logo)
                Thread.sleep(1200);

                // Indicar que terminó la carga
                cargaTerminada.set(true);
            } catch (InterruptedException error) {
                Thread.currentThread().interrupt();
            }
        });
    }

    @Override
    protected void onDestroy() {
        executor.shutdownNow();
        super.onDestroy();
    }
}