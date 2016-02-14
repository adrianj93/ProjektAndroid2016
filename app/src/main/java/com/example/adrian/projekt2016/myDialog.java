package com.example.adrian.projekt2016;

/**
 * Created by Adrian on 04.02.2016.
 */

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

public class myDialog {

    public static void dialog_confirm(Context ctx, String title, String message, final String mlat, final String mlong, final String time, final Activity context) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(ctx);
        builder1.setTitle(title);
        builder1.setMessage(message + "\n\n" +
                            "Długosć geograficzna: " + mlong + "\n" +
                            "Szerokość geograficzna: " + mlat);
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Toast.makeText(context.getApplicationContext(), "Dodano nowe ulubione miejsce", Toast.LENGTH_SHORT).show();
                        PreferenceManager.getDefaultSharedPreferences(context).edit().putString("latitude", mlat).commit();
                        PreferenceManager.getDefaultSharedPreferences(context).edit().putString("longitude", mlong).commit();
                        PreferenceManager.getDefaultSharedPreferences(context).edit().putString("lastUpdateTime", time).commit();

                        Intent intent = new Intent(context, MainActivity.class);
                        context.finish();
                        context.startActivity(intent);
                    }
                });

        builder1.setNegativeButton(
                "Anuluj",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }
}