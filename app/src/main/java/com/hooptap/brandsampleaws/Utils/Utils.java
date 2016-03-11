package com.hooptap.brandsampleaws.Utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hooptap.brandsampleaws.HTApplication;
import com.hooptap.brandsampleaws.R;

import java.util.HashMap;

/**
 * Created by root on 15/12/15.
 */
public class Utils {
    public static HashMap<Integer, String> listado = new HashMap<>();

    public static float convertDpToPixel(float dp, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * (metrics.densityDpi / 160f);
        return px;
    }

    public static String formatJSON(String text) {

        StringBuilder json = new StringBuilder();
        String indentString = "";

        if (text != null) {
            for (int i = 0; i < text.length(); i++) {
                char letter = text.charAt(i);
                switch (letter) {
                    case '{':
                    case '[':
                        json.append("\n" + indentString + letter + "\n");
                        indentString = indentString + "\t";
                        json.append(indentString);
                        break;
                    case '}':
                    case ']':
                        indentString = indentString.replaceFirst("\t", "");
                        json.append("\n" + indentString + letter);
                        break;
                    case ',':
                        json.append(letter + "\n" + indentString);
                        break;

                    default:
                        json.append(letter);
                        break;
                }
            }
        }

        return json.toString();
    }

    public static void log(String TAG, String sb) {
        if (sb.length() > 4000) {
            int chunkCount = sb.length() / 4000;     // integer division
            for (int i = 0; i <= chunkCount; i++) {
                int max = 4000 * (i + 1);
                if (max >= sb.length()) {
                    Log.e(TAG, "chunk " + i + " of " + chunkCount + ":" + sb.substring(4000 * i));
                } else {
                    Log.e(TAG, "chunk " + i + " of " + chunkCount + ":" + sb.substring(4000 * i, max));
                }
            }
        } else {
            Log.e(TAG, sb.toString());
        }
    }

    public static void createDialog(Activity activity, String json) {
        if (!activity.isFinishing()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(activity);

            builder.setCancelable(true);

            LayoutInflater inflater = activity.getLayoutInflater();

            View v = inflater.inflate(R.layout.show_json_text, null);

            builder.setView(v);

            TextView text = (TextView) v.findViewById(R.id.json_text);
            text.setText(Utils.formatJSON(json));

            builder.create().show();
        }
    }

    public static void createDialogError(final Activity activity, final String json) {
        if (!activity.isFinishing()) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    AlertDialog.Builder builder = new AlertDialog.Builder(activity);

                    builder.setCancelable(true);

                    LayoutInflater inflater = activity.getLayoutInflater();

                    View v = inflater.inflate(R.layout.show_json_text, null);

                    builder.setView(v);

                    TextView json_title = (TextView) v.findViewById(R.id.json_title);
                    json_title.setText("ERROR");

                    TextView text = (TextView) v.findViewById(R.id.json_text);
                    text.setText(Utils.formatJSON(json));

                    AlertDialog alert = builder.create();

                    alert.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialogInterface) {
                            activity.finish();
                        }
                    });

                    alert.show();
                }
            });
        }
    }

    public static void setUserId(String user_id){
        HTApplication.getTinydb().putString("user_id", user_id);
    }

    public static ProgressDialog showProgress(String text, Context context) {
        ProgressDialog pd = new ProgressDialog(context);
        pd.setTitle(context.getResources().getString(R.string.wait));
        pd.setMessage(text);

        pd.show();

        return pd;
    }

    public static void dismisProgres(ProgressDialog progres) {
        if (progres != null && progres.isShowing())
            progres.dismiss();
    }

    public static boolean thereAreEmptyFields(HashMap<String, Object>hasmap) {
        for (final HashMap.Entry<String, Object> entry : hasmap.entrySet()) {
            if (entry.getValue() instanceof EditText) {
                if (((EditText) entry.getValue()).getText().toString().trim().equals("")) {
                    return true;
                }
            }
        }
        return false;
    }
}

