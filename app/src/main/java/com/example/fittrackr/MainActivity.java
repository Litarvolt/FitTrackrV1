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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    // Gestor de idioma de la aplicación
    private LocaleManager localeManager;
    // Gestor de la barra de herramientas (Toolbar)
    private ToolbarManager toolbarManager;

    // Variables para estadísticas de actividades
    private int totalCalories = 0; // Calorías totales acumuladas
    private int totalMinutes = 0;  // Minutos totales de actividad
    private boolean achievementShown = false; // Indica si ya se mostró el logro
    private int[] activityCounts; // Cantidad de veces realizada cada actividad
    private String[] activities;  // Nombres de las actividades
    // Calorías quemadas por minuto para cada actividad
    private int[] caloriesPerMinute = {4, 10, 8, 6, 3};
    // Imágenes asociadas a cada actividad
    private int[] images = {
            R.drawable.ic_walk,
            R.drawable.ic_run,
            R.drawable.ic_swim,
            R.drawable.ic_bike,
            R.drawable.ic_yoga
    };

    // Referencias a elementos de la interfaz de usuario
    private ArrayList<String> activityList; // Lista de actividades registradas
    private ArrayAdapter<String> listAdapter; // Adaptador para el ListView
    private TextView textViewCalories; // Muestra las estadísticas
    private ImageView imageView2; // Imagen de la actividad más realizada

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inicializa el gestor de idioma y aplica el idioma actual
        localeManager = new LocaleManager(this);
        localeManager.applyLocale();

        // Establece el layout principal de la actividad
        setContentView(R.layout.activity_main);

        // Inicializa el Spinner con las opciones de actividades
        Spinner spinner = findViewById(R.id.spinner);
        activities = getResources().getStringArray(R.array.activity_options);
        activityCounts = new int[activities.length]; // Inicializa el contador de actividades
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, activities);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        // Referencias a los elementos de entrada y botones
        EditText editText = findViewById(R.id.editTextText4);
        Button button = findViewById(R.id.button);

        // Inicializa la lista y el adaptador para mostrar las actividades agregadas
        ListView listView = findViewById(R.id.listView);
        activityList = new ArrayList<>();
        listAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, activityList);
        listView.setAdapter(listAdapter);

        // Referencias a las imágenes y al TextView de estadísticas
        ImageView imageView = findViewById(R.id.imageView);   // Imagen principal (cambia con el spinner)
        imageView2 = findViewById(R.id.imageView2);           // Imagen de la actividad más realizada
        textViewCalories = findViewById(R.id.textViewCalories); // Estadísticas

        // Listener para el botón de agregar actividad
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtiene el texto ingresado (minutos)
                String input = editText.getText().toString().trim();
                if (input.isEmpty()) {
                    // Si no hay minutos, muestra un mensaje de error
                    Snackbar.make(findViewById(R.id.main), getString(R.string.snackbar_enter_minutes), Snackbar.LENGTH_SHORT).show();
                } else {
                    int minutes = Integer.parseInt(input);
                    if (minutes <= 0) {
                        // Si los minutos no son válidos, muestra un mensaje de error
                        Snackbar.make(findViewById(R.id.main), getString(R.string.snackbar_enter_minutes), Snackbar.LENGTH_SHORT).show();
                        return;
                    }
                    // Obtiene la posición seleccionada en el spinner (actividad)
                    int position = spinner.getSelectedItemPosition();
                    // Calcula las calorías quemadas para la actividad seleccionada
                    int calories = minutes * caloriesPerMinute[position];
                    // Crea la cadena para mostrar en la lista
                    String entry = activities[position] + " - " + minutes + " min - " + calories + " cal";
                    // Agrega la actividad a la lista y actualiza el ListView
                    activityList.add(entry);
                    listAdapter.notifyDataSetChanged();
                    // Limpia el campo de texto
                    editText.setText("");

                    // Acumula las calorías y minutos totales
                    totalCalories += calories;
                    totalMinutes += minutes;
                    // Incrementa el contador de la actividad seleccionada
                    activityCounts[position]++;

                    // Determina cuál es la actividad más realizada
                    int maxIndex = 0;
                    for (int i = 1; i < activityCounts.length; i++) {
                        if (activityCounts[i] > activityCounts[maxIndex]) {
                            maxIndex = i;
                        }
                    }
                    String mostActivity = activities[maxIndex];

                    // Actualiza el TextView con las estadísticas actuales usando recursos de strings
                    String stats = getString(R.string.stats_calories, totalCalories)
                            + getString(R.string.stats_most, mostActivity)
                            + getString(R.string.stats_minutes, totalMinutes);
                    textViewCalories.setText(stats);

                    // Si se superan las 500 calorías y aún no se mostró el logro, muestra un Toast
                    if (totalCalories > 500 && !achievementShown) {
                        Toast.makeText(MainActivity.this, getString(R.string.achievement_unlocked), Toast.LENGTH_LONG).show();
                        achievementShown = true;
                    }

                    // Actualiza la imagen de la actividad más realizada
                    imageView2.setImageResource(images[maxIndex]);
                }
            }
        });

        // Configura la barra de herramientas (Toolbar)
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbarManager = new ToolbarManager(this, toolbar);
        toolbarManager.setupToolbar();

        // Listener para el spinner: cambia la imagen principal según la actividad seleccionada
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Cambia la imagen principal y aplica una animación de desvanecimiento
                imageView.setImageResource(images[position]);
                Animation fadeIn = new AlphaAnimation(0, 1);
                fadeIn.setDuration(1000);
                imageView.startAnimation(fadeIn);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    // Infla el menú de la barra de acciones (action bar)
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actionbar, menu);
        return true;
    }

    // Actualiza el icono de la bandera según el idioma actual
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        toolbarManager.updateFlagIcon(menu, localeManager.getCurrentLocaleIndex());
        return super.onPrepareOptionsMenu(menu);
    }

    // Maneja las acciones de los botones del menú de la barra de acciones
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Botón de basura: limpia la lista y reinicia las estadísticas
        if (item.getItemId() == R.id.action_clear) {
            // Limpia la lista de actividades
            activityList.clear();
            listAdapter.notifyDataSetChanged();
            // Reinicia las estadísticas
            totalCalories = 0;
            totalMinutes = 0;
            achievementShown = false;
            for (int i = 0; i < activityCounts.length; i++) activityCounts[i] = 0;
            // Actualiza el TextView y la imagen a los valores por defecto usando recursos de strings
            textViewCalories.setText(getString(R.string.stats_default));
            imageView2.setImageResource(R.drawable.ic_walk); // Imagen por defecto
            return true;
        }
        // Botón para cambiar el tema (oscuro/claro)
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
        // Maneja el cambio de idioma y la opción de salir
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