package com.hooptap.brandsampleaws.Actions;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
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

import com.hooptap.brandsampleaws.Generic.HooptapActivity;
import com.hooptap.brandsampleaws.HTApplication;
import com.hooptap.brandsampleaws.R;
import com.hooptap.brandsampleaws.Utils.Utils;
import com.hooptap.sdkbrandclub.Api.HooptapApi;
import com.hooptap.sdkbrandclub.Interfaces.HooptapCallback;
import com.hooptap.sdkbrandclub.Models.HooptapAction;
import com.hooptap.sdkbrandclub.Models.HooptapActionFields;
import com.hooptap.sdkbrandclub.Models.HooptapActionResult;
import com.hooptap.sdkbrandclub.Models.HooptapFilter;
import com.hooptap.sdkbrandclub.Models.HooptapListResponse;
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
import java.util.TimeZone;

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
    private HooptapAction action;

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

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            action = (HooptapAction) bundle.get("action");
            setUniqueActionName(action);
        } else {
            getActionNamesAvaiables();
        }

    }

    private void setUniqueActionName(HooptapAction action) {
        ArrayList<HooptapAction> array = new ArrayList<>();
        array.add(action);
        SpinnerAdapterActions adapter = new SpinnerAdapterActions(Actions.this, array);
        spinner.setAdapter(adapter);
    }

    private void getActionNamesAvaiables() {
        final ProgressDialog pd = Utils.showProgress("Loading Actions avaibles", this);
        HooptapFilter filter = new HooptapFilter.Builder().sort("name", HooptapFilter.Builder.Order.asc).build();
        HooptapApi.getActions(new HooptapOptions(), filter, new HooptapCallback<HooptapListResponse>() {

            @Override
            public void onSuccess(HooptapListResponse hooptapListResponse) {
                SpinnerAdapterActions adapter = new SpinnerAdapterActions(Actions.this, hooptapListResponse.getItemArray());
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

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
        HooptapAction action = (HooptapAction) adapterView.getItemAtPosition(position);
        ArrayList<HooptapActionFields> actionFields = action.getActionFields();
        matching_fields.removeAllViews();
        objectsResponses.clear();

        for (int i = 0; i < actionFields.size(); i++) {
            HooptapActionFields actionField = actionFields.get(i);
            View v = launchReflectionMethod(actionField.getKey(), actionField.getType());
            matching_fields.addView(v);
            checkSpecialInput(v);
        }
    }

    @OnClick(R.id.btn)
    public void llamarAccion() {
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void doAction(JSONObject jsonActionData) {
        final ProgressDialog pd = Utils.showProgress("Loading rewards", Actions.this);
        String nameAction = ((HooptapAction) spinner.getSelectedItem()).getName();
        HooptapApi.doAction(HTApplication.getTinydb().getString("user_id"), jsonActionData, nameAction, new HooptapCallback<HooptapActionResult>() {
            @Override
            public void onSuccess(HooptapActionResult action) {
                Utils.dismisProgres(pd);
                if (action.getRewards().size() > 0) {
                    createDialogRewards(action);
                } else {
                    if (action.getQuest() != null) {
                        Utils.createDialog(Actions.this, getResources().getString(R.string.no_rewards) + " pero " + getResources().getString(R.string.one_step_completed));
                    } else {
                        Utils.createDialog(Actions.this, getResources().getString(R.string.no_rewards));
                    }
                }
                if (action.getQuest() != null) {
                    Intent i = getIntent();
                    i.putExtra("num_steps_completeds", action.getQuest().getNumCompletedSteps());
                    i.putExtra("finished", action.getQuest().isFinished());
                    setResult(RESULT_OK, i);
                }
            }

            @Override
            public void onError(ResponseError responseError) {
                Utils.createDialogError(Actions.this, responseError.getReason());
                Utils.dismisProgres(pd);
            }

            private void createDialogRewards(final HooptapActionResult action) {
                AlertDialog.Builder alert = new AlertDialog.Builder(Actions.this);
                alert.setTitle("Congratulations!");

                int sizeRewards = action.getRewards().size();
                if (action.getLevel() != null)
                    sizeRewards++;
                if (action.getQuest() != null) {
                    alert.setMessage("You win " + sizeRewards + " rewards, además " + getResources().getString(R.string.one_step_completed));
                } else {
                    alert.setMessage("You win " + sizeRewards + " rewards");
                }
                alert.setPositiveButton("See", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        startActivity(new Intent(Actions.this, Rewards.class).putExtra("action", action));
                    }
                });
                alert.show();
            }
        });
    }

    private void checkSpecialInput(View v) {
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
                                    str_date = checkDigit(selectedDay) + "-" + checkDigit(selectedMonth + 1) + "-" + selectedYear;
                                    new TimePickerDialog(Actions.this, Actions.this, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show();
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

    private View launchReflectionMethod(String key, String type) {
        try {
            Class<?> c = Actions.class;
            Object o = c.newInstance();
            Class[] paramTypes = new Class[3];
            paramTypes[0] = String.class;
            paramTypes[1] = Activity.class;
            paramTypes[2] = HashMap.class;
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
        if (timePicker.isShown()) {
            str_date = str_date + " " + hourOfDay + ":" + minute;
            DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm");
            formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
            try {
                Date date = formatter.parse(str_date);
                edit.setText(date.getTime() + "");
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public String checkDigit(int number) {
        return number <= 9 ? "0" + number : String.valueOf(number);
    }
}
