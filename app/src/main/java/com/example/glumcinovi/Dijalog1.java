package com.example.glumcinovi;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class Dijalog1 extends AlertDialog.Builder {
    public Dijalog1(Context context) {
        super(context);
        setTitle("Dijalog");
        setMessage("Ovo je prozor za dialog");


        setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
    }
    public  AlertDialog pripremiDialog(){
        AlertDialog dialog=create();
        dialog.setCanceledOnTouchOutside(false);
        return dialog;


    }
}