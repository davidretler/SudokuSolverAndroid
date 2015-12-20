package com.davidretler.sudokusolver;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

/**
 * Just makes simple alerts
 *
 * Created by david on 12/20/15.
 */
public class AlertFactory {
    public static void info(String title, String text, Context context) {
        new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(text)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //do nothing
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
}
