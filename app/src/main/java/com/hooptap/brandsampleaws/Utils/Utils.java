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

import com.hooptap.brandsampleaws.R;

import java.util.HashMap;

/**
 * Created by root on 15/12/15.
 */
public class Utils {
    public static HashMap<Integer, String> listado = new HashMap<>();

    public static LinearLayout createEditText(String name, Activity activity) {
        LinearLayout ll_custom = new LinearLayout(activity);
        //ll_custom.setBackgroundColor(getResources().getColor(R.color.White));
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0, 1, 0, 0);
        ll_custom.setOrientation(LinearLayout.HORIZONTAL);
        ll_custom.setLayoutParams(layoutParams);

        TextView txt = new TextView(activity);
        LinearLayout.LayoutParams layoutParamsTxt = new LinearLayout.LayoutParams((int) convertDpToPixel(120, activity), LinearLayout.LayoutParams.MATCH_PARENT);
        txt.setLayoutParams(layoutParamsTxt);
        txt.setAllCaps(true);
        txt.setGravity(Gravity.CENTER);
        //txt.setBackgroundColor(getResources().getColor(R.color.Text_Register));
        try {
            txt.setText(name);
        } catch (Exception e) {
            e.printStackTrace();
        }

        EditText edit = new EditText(activity);
        LinearLayout.LayoutParams layoutParamsEdit = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, (int) convertDpToPixel(36, activity));
        edit.setLayoutParams(layoutParamsEdit);
        //layoutParamsEdit.setMargins((int) UtilsFuctions.convertDpToPixel(2, this), (int) UtilsFuctions.convertDpToPixel(2, this), (int) UtilsFuctions.convertDpToPixel(2, this), (int) UtilsFuctions.convertDpToPixel(2, this));
        edit.setPadding(4, 0, 4, 0);
        edit.setSingleLine(true);
        //edit.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_laucher));
        edit.setCursorVisible(true);
        edit.setTextColor(activity.getResources().getColor(R.color.colorAccent));
        edit.setId(listado.size());
        listado.put(edit.getId(), name);
        ll_custom.addView(txt);
        ll_custom.addView(edit);
        return ll_custom;
    }

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

}

