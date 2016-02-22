package com.hooptap.brandsampleaws;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.hooptap.brandsampleaws.Adapters.SpinnerAdapterActions;
import com.hooptap.brandsampleaws.Generic.HooptapActivity;
import com.hooptap.brandsampleaws.Utils.Utils;
import com.hooptap.sdkbrandclub.Api.HooptapApi;
import com.hooptap.sdkbrandclub.Interfaces.HooptapCallback;
import com.hooptap.sdkbrandclub.Models.HooptapAction;
import com.hooptap.sdkbrandclub.Models.HooptapFilter;
import com.hooptap.sdkbrandclub.Models.HooptapOptions;
import com.hooptap.sdkbrandclub.Models.ResponseError;

import org.json.JSONObject;

import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Actions extends HooptapActivity implements AdapterView.OnItemSelectedListener, TimePickerDialog.OnTimeSetListener {

    @Bind(R.id.spinner)
    Spinner spinner;
    @Bind(R.id.matching_fields)
    LinearLayout matching_fields;

    private Calendar calendar;
    private String str_date;
    private EditText edit;

    private HashMap<String, Object> objectsResponses = new HashMap<>();

    public enum TYPES {
        Number, Boolean, Array, String, Timestamp
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actions);
        ButterKnife.bind(this);
        getSupportActionBar().setTitle("Actions");

        final Spinner spinner = (Spinner) findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(this);

        final ProgressDialog pd = Utils.showProgress("Loading Actions avaibles", this);
        HooptapApi.getActions(new HooptapOptions(), new HooptapFilter(), new HooptapCallback<ArrayList<String>>() {
            @Override
            public void onSuccess(ArrayList<String> strings) {
                SpinnerAdapterActions adapter = new SpinnerAdapterActions(Actions.this, strings);
                spinner.setAdapter(adapter);
                Utils.dismisProgres(pd);
            }

            @Override
            public void onError(ResponseError responseError) {
                Utils.createDialogError(Actions.this, responseError.getReason());
                Utils.dismisProgres(pd);
            }
        });
    }

    @OnClick(R.id.btn)
    public void llamarAccion() {
        if (!Utils.thereAreEmptyFields(objectsResponses)) {
            try {
                JSONObject jsonActionData = new JSONObject();
                for (final HashMap.Entry<String, Object> entry : objectsResponses.entrySet()) {
                    if (entry.getValue() instanceof EditText) {
                        jsonActionData.put(entry.getKey(), ((EditText) entry.getValue()).getText().toString());
                    } else {
                        jsonActionData.put(entry.getKey(), ((Switch) entry.getValue()).isChecked());
                    }
                }
                doAction(jsonActionData);
                Log.e("JSONDATA", jsonActionData + " / " + spinner.getSelectedItem().toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(getApplicationContext(), "There are empty fields", Toast.LENGTH_LONG).show();
        }
    }

    private void doAction(JSONObject json) {
        final ProgressDialog pd = Utils.showProgress("Loading rewards", Actions.this);
        Log.e("USER", HTApplication.getTinydb().getString("user_id"));
        HooptapApi.doAction(HTApplication.getTinydb().getString("user_id"), json.toString(), spinner.getSelectedItem().toString(), new HooptapCallback<HooptapAction>() {
            @Override
            public void onSuccess(HooptapAction action) {
                Utils.dismisProgres(pd);
                if (action.getRewards().size() > 0) {
                    createDialogRewards(action);
                } else {
                    Utils.createDialog(Actions.this, "Esta accion no tiene rewards");
                }

            }

            private void createDialogRewards(final HooptapAction action) {
                AlertDialog.Builder alert = new AlertDialog.Builder(Actions.this);
                alert.setTitle("Congratulations!");

                int sizeRewards = action.getRewards().size();
                if (action.getLevel() != null)
                    sizeRewards++;
                alert.setMessage("You win " + sizeRewards + " rewards");
                alert.setPositiveButton("See", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        startActivity(new Intent(Actions.this, Rewards.class).putExtra("action", action));
                    }
                });
                alert.show();
            }

            @Override
            public void onError(ResponseError responseError) {
                Utils.createDialogError(Actions.this, responseError.getReason());
                Utils.dismisProgres(pd);
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String action = adapterView.getItemAtPosition(i).toString();
        final ProgressDialog pd = Utils.showProgress("Loading matching fields required", Actions.this);
        HooptapApi.getMatchingFieldsForAction(action, new HooptapOptions(), new HooptapFilter(), new HooptapCallback<HashMap<String, String>>() {
            @Override
            public void onSuccess(HashMap<String, String> stringStringHashMap) {
                matching_fields.removeAllViews();
                objectsResponses.clear();
                for (final HashMap.Entry<String, String> entry : stringStringHashMap.entrySet()) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            View v = launchReflectionMethod(entry.getKey(), entry.getValue());
                            matching_fields.addView(v);
                            //Los metodos al llamarse por  reflection hace que no se puede acceder a nada
                            //de ellos ni vista ni variables, por eso debemos declarar los click especiales aqui
                            checkSpecialInput(v);
                        }
                    });
                }
                Utils.dismisProgres(pd);
            }

            @Override
            public void onError(ResponseError responseError) {
                Utils.createDialogError(Actions.this, responseError.getReason());
                Utils.dismisProgres(pd);
            }
        });
    }

    private void checkSpecialInput(View v) {
        Log.e("HEY", "checkSpecialInput");
        if (v.findViewById(R.id.picker) != null) {
            edit = (EditText) v.findViewById(R.id.picker);
            edit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean b) {
                    if (b) {
                        calendar = Calendar.getInstance();
                        DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
                            public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
                                if (view.isShown()) {
                                    str_date = selectedDay + "-" + selectedMonth + "-" + selectedYear;
                                    new TimePickerDialog(Actions.this, Actions.this, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false).show();
                                }
                            }
                        };

                        new DatePickerDialog(Actions.this, datePickerListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
                    }
                }
            });
        } else if (v.findViewById(R.id.array) != null) {

            final EditText edit = (EditText) v.findViewById(R.id.array);
            Button add = (Button) v.findViewById(R.id.add);
            add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(Actions.this);
                    builder.setTitle("ADD VALUE");

                    final EditText edittext = new EditText(Actions.this);
                    edittext.setPadding(20, 20, 20, 20);
                    builder.setView(edittext);

                    builder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            if (edittext.getText().toString().trim().equals("")) {
                                Toast.makeText(getApplicationContext(), "You need put something", Toast.LENGTH_LONG).show();
                            } else {
                                String currentTextInEdit = (edit.getText().toString().equals("")) ? edit.getText().toString() : edit.getText() + ",";
                                edit.setText(currentTextInEdit + edittext.getText().toString());
                            }
                        }
                    });
                    builder.create().show();
                }
            });
        }
    }

    //Con este metodo crearemos un editTeext con diferente input segun el typo que nos llegue
    private View launchReflectionMethod(String key, String type) {
        try {
            Class<?> c = Actions.class;
            Object o = c.newInstance();
            Class[] paramTypes = new Class[3];
            paramTypes[0] = String.class;
            paramTypes[1] = Activity.class;
            //El array se lo pasamos por referencia porque sino modifica la copia y no el original
            paramTypes[2] = HashMap.class;
            //Para cuando me lleguen types del estilo Game, etc.. los tratare como un input String
            if (!containsEnum(type)) {
                type = "String";
            }
            String methodName = "generateInput" + type;
            Method m = c.getDeclaredMethod(methodName, paramTypes);
            View v = (View) m.invoke(o, key, Actions.this, objectsResponses);
            return v;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean containsEnum(String s) {
        for (TYPES choice : TYPES.values())
            if (choice.name().equalsIgnoreCase(s))
                return true;
        return false;
    }

    private View generateInputString(final String key, Activity context, HashMap<String, Object> hasmap) {

        View action_edit = context.getLayoutInflater().inflate(R.layout.action_edit, null);
        TextView txt_boolean = (TextView) action_edit.findViewById(R.id.txt);
        txt_boolean.setText(key);
        final EditText edit = (EditText) action_edit.findViewById(R.id.edit);
        edit.setHint(key);
        edit.setInputType(InputType.TYPE_CLASS_TEXT);

        hasmap.put(key, edit);

        return action_edit;

    }

    private View generateInputNumber(final String key, Activity context, HashMap<String, Object> hasmap) {

        View action_edit = context.getLayoutInflater().inflate(R.layout.action_edit, null);
        TextView txt_boolean = (TextView) action_edit.findViewById(R.id.txt);
        txt_boolean.setText(key);
        final EditText edit = (EditText) action_edit.findViewById(R.id.edit);
        edit.setHint(key);
        edit.setInputType(InputType.TYPE_CLASS_NUMBER);

        hasmap.put(key, edit);

        return action_edit;
    }

    private View generateInputBoolean(final String key, Activity context, HashMap<String, Object> hasmap) {

        View action_boolean = context.getLayoutInflater().inflate(R.layout.action_boolean, null);
        TextView txt_boolean = (TextView) action_boolean.findViewById(R.id.txt);
        txt_boolean.setText(key);
        Switch switch_boolean = (Switch) action_boolean.findViewById(R.id.switch_boolean);

        hasmap.put(key, switch_boolean);

        return action_boolean;
    }

    private View generateInputTimestamp(final String key, final Activity context, HashMap<String, Object> hasmap) {

        View action_edit = context.getLayoutInflater().inflate(R.layout.action_picker, null);
        TextView txt_boolean = (TextView) action_edit.findViewById(R.id.txt);
        txt_boolean.setText(key);
        EditText edit = (EditText) action_edit.findViewById(R.id.picker);
        edit.setHint(key);

        hasmap.put(key, edit);

        return action_edit;
    }

    private View generateInputArray(final String key, final Activity context, HashMap<String, Object> hasmap) {

        View action_edit = context.getLayoutInflater().inflate(R.layout.action_array, null);
        TextView txt_boolean = (TextView) action_edit.findViewById(R.id.txt);
        txt_boolean.setText(key);
        EditText edit = (EditText) action_edit.findViewById(R.id.array);
        edit.setHint(key);

        hasmap.put(key, edit);

        return action_edit;
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
        str_date = str_date + " " + hourOfDay + ":" + minute;
        DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        try {
            Date date = formatter.parse(str_date);
            edit.setText(date.getTime() + "");
            //edit.clearFocus();
            //spinner.requestFocus();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
