package com.avility.pruebameli.helpers;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.gson.GsonBuilder;

import java.text.NumberFormat;
import java.util.Locale;

public class Utils {

    /**
     * Se encarga de mostrar un toolbar en el activity
     * @param activity actividad de donde proviene la solicitud de mostrar un toolbar
     * @param toolbar el toolbar a mostrar
     * @param title el titulo a mostrar en el toolbar
     * @param upButton si es true, mostrará un boton hacia atras.
     */
    public static void showToolbar(AppCompatActivity activity, Toolbar toolbar, String title, Boolean upButton) {
        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setTitle(title);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(upButton);
    }

    /**
     * Se encarga de construir un GSON builder, se deja aparta porque es posible que en el tiempo
     * se configure de una manera diferente, por ejemplo agregando un formato de fecha
     * @return GsonBuilder
     */
    public static GsonBuilder getGsonBuilder() {
        return new GsonBuilder();
    }

    /**
     * Muestra un toast con el mensaje que llegue por parametro
     * @param context contexto de la aplicación
     * @param message mensaje que se desea mostrar
     */
    public static void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    /**
     * Tiene como objetivo tomar un numero y pasarlo al formato de moneda colombiana
     */
    public static String transformToCurrency(Long number) {
        NumberFormat formatCOP = NumberFormat.getCurrencyInstance(new Locale("es", "CO"));
        return formatCOP.format(number);
    }

}
