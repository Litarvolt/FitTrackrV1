// LocaleManager.java
package com.example.fittrackr;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import java.util.Locale;

public class LocaleManager {
    private final Context context;
    private final Locale[] locales = {Locale.ENGLISH, new Locale("es"), new Locale("de")};
    private int currentLocaleIndex;

    // Constructor que inicializa el contexto y carga el índice del locale actual desde SharedPreferences
    public LocaleManager(Context context) {
        this.context = context;
        SharedPreferences prefs = context.getSharedPreferences("settings", Context.MODE_PRIVATE);
        currentLocaleIndex = prefs.getInt("locale_index", 0);
    }

    // Esta función cambia el índice del locale actual al siguiente en la lista
    public void cycleLocale() {
        currentLocaleIndex = (currentLocaleIndex + 1) % locales.length;
        saveLocaleIndex();
    }

    // Esta función aplica el locale actual al contexto de la aplicación
    public void applyLocale() {
        // Coloca el locale actual como el predeterminado
        Locale locale = locales[currentLocaleIndex];
        Locale.setDefault(locale);
        Resources resources = context.getResources();
        Configuration config = new Configuration(resources.getConfiguration());
        config.setLocale(locale);
        resources.updateConfiguration(config, resources.getDisplayMetrics());
    }

    // Esta función devuelve el índice del locale actual
    public int getCurrentLocaleIndex() {
        return currentLocaleIndex;
    }

    // Esta función guarda el índice del locale actual en SharedPreferences
    private void saveLocaleIndex() {
        SharedPreferences prefs = context.getSharedPreferences("settings", Context.MODE_PRIVATE);
        prefs.edit().putInt("locale_index", currentLocaleIndex).apply();
    }
}