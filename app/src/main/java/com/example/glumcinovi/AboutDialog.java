package com.example.glumcinovi;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class AboutDialog extends  AlertDialog.Builder{



        public AboutDialog(Context context) {
            super(context);
            setTitle(R.string.aboutDijalog);
            setMessage("App name: Glumci\nby: Jelena Radeka");
            setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

        }

        public AlertDialog prepareDialog(){
            AlertDialog dialog = create();
            dialog.setCanceledOnTouchOutside(false);
            return dialog;
        }
    }

