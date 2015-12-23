package com.davidretler.sudokusolver;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Just makes simple alerts
 *
 * Created by david on 12/20/15.
 */
public class Alerts {

    // creates a simple info alert
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
                .setIcon(R.drawable.ic_info_outline_grey_800_18dp)
                .show();
    }

    // creates a simple error alert
    public static void error(String title, String text, Context context) {
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
                .setIcon(R.drawable.ic_warning_grey_800_18dp)
                .show();
    }

    // dialog to select the timestep
    public static void timeDialog(final Context context) {
        final Dialog myDialog = new Dialog(context);
        myDialog.setContentView(R.layout.time_dialog);
        myDialog.setTitle("Select the time step");

        final EditText timeEdit = (EditText) myDialog.findViewById(R.id.editTime);

        Button okButton = (Button) myDialog.findViewById(R.id.dialogButtonOK);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String entry = timeEdit.getText().toString();
                try {
                    double stepTime = Double.parseDouble(entry);
                    SudokuBoardActivity.stepTime = stepTime;
                    Log.d("timeDialog", "The step time is now " + stepTime);
                } catch (NumberFormatException ex) {
                    ex.printStackTrace();
                    Log.e("timeDialog", "Error parsing the number.");
                    Alerts.error("Error", "You enetered an invalid number.", context);
                }
                myDialog.dismiss();
            }
        });

        myDialog.show();
    }

}
