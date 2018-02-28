package com.example.arc.capstonedisplay;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.ColorRes;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AlertDialog;
import android.view.MenuItem;

/**
 * Created by arc on 26/01/18.
 */
//-----https://stackoverflow.com/questions/14996560/how-to-use-runonuithread-without-getting-cannot-make-a-static-reference-to-the

public class Utils {
    public static Handler UIHandler;

    static
    {
        UIHandler = new Handler(Looper.getMainLooper());
    }

    public static void runOnUI(Runnable runnable) {
        UIHandler.post(runnable);
    }

    public static void tintMenuIcon(final Context context, final MenuItem item, final @ColorRes int color) {
        //-----https://futurestud.io/tutorials/android-quick-tips-8-how-to-dynamically-tint-actionbar-menu-icons
        runOnUI(new Runnable() {
            @Override
            public void run() {
                Drawable normalDrawable=item.getIcon();
                Drawable wrapDrawable = DrawableCompat.wrap(normalDrawable);
                DrawableCompat.setTint(wrapDrawable, context.getResources().getColor(color));

                item.setIcon(wrapDrawable);
            }
        });
    }
    public static void displayConnectionError(final Context context, final CustomConnection con, final String error) {
        //-----https://futurestud.io/tutorials/android-quick-tips-8-how-to-dynamically-tint-actionbar-menu-icons
        runOnUI(new Runnable() {
            @Override
            public void run() {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
                builder1.setTitle("Connection Error");
                builder1.setMessage(error);
                builder1.setCancelable(true);

                builder1.setPositiveButton(
                        "Reconnect",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                con.connect(con.dstAddr,con.dstPort);
                            }
                        });

                builder1.setNegativeButton(
                        "Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Utils.tintMenuIcon(context,con.item,android.R.color.darker_gray);
                                dialog.cancel();
                            }
                        });

                AlertDialog alert11 = builder1.create();
                alert11.show();
            }
        });
    }

}
