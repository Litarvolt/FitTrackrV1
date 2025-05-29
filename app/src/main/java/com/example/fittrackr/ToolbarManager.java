// ToolbarManager.java
package com.example.fittrackr;

import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.Typeface;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.res.ResourcesCompat;

// Esta clase gestiona la barra de herramientas de la aplicación
// incluyendo la configuración del tipo de letra, el manejo de los elementos del menú
// y la actualización del icono de la bandera según el idioma seleccionado.
public class ToolbarManager {

    // Esta clase gestiona la barra de herramientas de la aplicación
    private final Activity activity;

    // Referencia a la barra de herramientas
    private final Toolbar toolbar;

    // Constructor que inicializa la actividad y la barra de herramientas
    public ToolbarManager(Activity activity, Toolbar toolbar) {
        this.activity = activity;
        this.toolbar = toolbar;
    }

    // Configura la barra de herramientas, aplicando un tipo de letra personalizado
    public void setupToolbar() {
        Typeface customFont = ResourcesCompat.getFont(activity, R.font.assistant);
        // El ciclo busca el TextView que contiene el título de la barra de herramientas
        for (int i = 0; i < toolbar.getChildCount(); i++) {
            // Verifica si el hijo es un TextView
            View view = toolbar.getChildAt(i);
            if (view instanceof TextView) {
                TextView tv = (TextView) view;
                if (tv.getText().equals(toolbar.getTitle())) {
                    // Aplica el tipo de letra personalizado al TextView y establece el estilo en negrita
                    tv.setTypeface(customFont, Typeface.BOLD);
                    // rompe el ciclo una vez que se encuentra el TextView correcto
                    break;
                }
            }
        }
    }

    // Revisa la bandera que se muestra en el menú de la barra de herramientas
    // Recibe un objeto Menu y un índice de idioma para actualizar el icono de la bandera
    public boolean handleMenuItem(MenuItem item, Runnable onFlag, Runnable onExit) {
        int id = item.getItemId();
        if (id == R.id.action_flag) {
            // Ejecuta la acción de cambio de idioma
            onFlag.run();
            return true;
        } else if (id == R.id.action_exit) {
            new AlertDialog.Builder(activity)
                    .setMessage(R.string.exit_message)
                    .setPositiveButton(android.R.string.yes, (dialog, which) -> activity.finish())
                    .setNegativeButton(android.R.string.no, null)
                    .show();
            return true;
        }
        return false;
    }

    // Actualiza el icono de la bandera en el menú de la barra de herramientas
    public void updateFlagIcon(Menu menu, int localeIndex) {
        MenuItem flagItem = menu.findItem(R.id.action_flag);
        if (flagItem != null) {
            int[] flagIcons = {R.drawable.flag, R.drawable.flag, R.drawable.flag};
            flagItem.setIcon(flagIcons[localeIndex]);
        }
    }
}
