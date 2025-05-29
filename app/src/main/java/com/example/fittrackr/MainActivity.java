// MainActivity.java
package com.example.fittrackr;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.AdapterView;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;

public class MainActivity extends AppCompatActivity {
    private LocaleManager localeManager;
    private ToolbarManager toolbarManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        localeManager = new LocaleManager(this);
        localeManager.applyLocale();
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbarManager = new ToolbarManager(this, toolbar);
        toolbarManager.setupToolbar();

        Spinner spinner = findViewById(R.id.spinner);
        String[] activities = getResources().getStringArray(R.array.activity_options);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, activities);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        ImageView imageView = findViewById(R.id.imageView);

        // Utilizamos el spinner para cambiar la imagen del ImageView
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int[] images = {
                        R.drawable.ic_walk,
                        R.drawable.ic_run,
                        R.drawable.ic_swim,
                        R.drawable.ic_bike,
                        R.drawable.ic_yoga
                };
                // Cambiamos la imagen del ImageView según la opción seleccionada en el spinner
                imageView.setImageResource(images[position]);
                Animation fadeIn = new AlphaAnimation(0, 1);
                fadeIn.setDuration(1000); // duration in ms
                imageView.startAnimation(fadeIn);
            }


            // Este metodo se llama cuando no hay ninguna opcion seleccionada
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // No hacemos nada si no hay ninguna opcion seleccionada
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actionbar, menu);
        return true;
    }

    // Este metodo hace que el icono de la bandera se actualice
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        toolbarManager.updateFlagIcon(menu, localeManager.getCurrentLocaleIndex());
        return super.onPrepareOptionsMenu(menu);
    }

    // Este metodo maneja los eventos de los elementos del menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_theme) {
            int currentNightMode = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
            if (currentNightMode == Configuration.UI_MODE_NIGHT_YES) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            }
            recreate();
            return true;
        }
        return toolbarManager.handleMenuItem(
                item,
                () -> {
                    localeManager.cycleLocale();
                    localeManager.applyLocale();
                    recreate();
                },
                this::finish
        ) || super.onOptionsItemSelected(item);
    }
}