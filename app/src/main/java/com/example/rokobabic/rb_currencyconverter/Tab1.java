package com.example.rokobabic.rb_currencyconverter;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.location.GpsStatus;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class Tab1 extends Fragment {

    private TextView tw1;
    private TextView tw2;
    private EditText et1;
    private EditText et2;
    private EditText date_picker;
    private Button replace;
    private Button convert;
    private Button add_db;
    private Button check_db;
    private DBHelper mydb;
    private int flag;
    private float tmpv;

    private SimpleDateFormat dateFormatter;

    String conversionRate;

    private FetchConvRate fcr;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.tab_1, container, false);

        tw1 = (TextView) v.findViewById(R.id.textView);
        tw2 = (TextView) v.findViewById(R.id.textView2);
        et1 = (EditText) v.findViewById(R.id.editText);
        et2 = (EditText) v.findViewById(R.id.editText2);
        date_picker = (EditText) v.findViewById(R.id.date_text);
        date_picker.setInputType(InputType.TYPE_NULL);
        date_picker.requestFocus();

        mydb = new DBHelper(getContext());

        final Calendar newCalendar = Calendar.getInstance();
        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);

        et2.setFocusable(false);
        et2.setEnabled(false);
        et2.setCursorVisible(false);
        et2.setKeyListener(null);

        replace = (Button) v.findViewById(R.id.button2);
        convert = (Button) v.findViewById(R.id.button4);
        add_db = (Button) v.findViewById(R.id.button_db);
        check_db = (Button) v.findViewById(R.id.review_button);

        fcr = new FetchConvRate(tw1.getText().toString(), tw2.getText().toString());
        fcr.execute();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                conversionRate = fcr.res;
            }
        }, 2000);

        convert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Float first, result;

                if(!et1.getText().toString().isEmpty()){
                    first = Float.valueOf(et1.getText().toString());
                    tmpv = first;
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                        }
                    }, 1000);
                    if(conversionRate != "" && isNetworkConnected()){
                        result = first * Float.valueOf(conversionRate);
                        et2.setText(String.valueOf(result));
                        flag = 1;
                    }else{
                        Toast.makeText(getActivity().getBaseContext(), "No internet conenction!",
                                Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getActivity().getBaseContext(), "The first value is empty!",
                       Toast.LENGTH_LONG).show();
                }
            }
        });

        replace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String tmp;
                if(isNetworkConnected()) {
                    tmp = tw1.getText().toString();
                    tw1.setText(tw2.getText().toString());
                    tw2.setText(tmp);

                    // if(isNetworkConnected()){
                    fcr = new FetchConvRate(tw1.getText().toString(), tw2.getText().toString());
                    fcr.execute();
                    //conversionRate = fcr.getRate();

                    new Handler().postDelayed(new Runnable() {

                        @Override
                        public void run() {

                            //Toast.makeText(getActivity().getBaseContext(), "Rate: " + fcr.res,
                            //      Toast.LENGTH_LONG).show();
                            conversionRate = fcr.res;
                        }
                    }, 2000);

                    et1.setText("");
                    et2.setText("");

                }else{
                    Toast.makeText(getActivity().getBaseContext(), "No internet conenction!",
                            Toast.LENGTH_LONG).show();
            }}
        });

        add_db.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int y = newCalendar.get(Calendar.YEAR);
                int m = newCalendar.get(Calendar.MONTH);
                int d = newCalendar.get(Calendar.DAY_OF_MONTH);
                newCalendar.set(y, m, d);

                if((!et1.getText().toString().isEmpty() && Float.valueOf(et1.getText().toString()) == tmpv)){
                    if( flag == 1 ){
                    mydb.insertValue((tw1.getText().toString() + "-" + tw2.getText().toString() + " => " + et1.getText().toString() + " - " + et2.getText().toString()),
                            dateFormatter.format(newCalendar.getTime()));
                    Toast.makeText(getActivity().getBaseContext(), "Added to database. Date: " + dateFormatter.format(newCalendar.getTime()),
                            Toast.LENGTH_LONG).show();
                    flag = 0;
                    }
                } else {
                    Toast.makeText(getActivity().getBaseContext(), "Incorrect values!",
                        Toast.LENGTH_LONG).show();
                }
            }
        });

        check_db.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!date_picker.getText().toString().isEmpty()){
                    Cursor rs = mydb.getData(date_picker.getText().toString());
                    String nam ="";
                    if (rs != null && rs.moveToFirst()) {
                        // Loop through all Results
                        do {
                            nam += rs.getString(rs.getColumnIndex(DBHelper.VALUTE_COLUMN_NAME)) + "\n";

                            /*new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                }
                            }, 2000);*/
                        }while(rs.moveToNext());
                        Toast.makeText(getActivity().getBaseContext(), "Date: "+date_picker.getText().toString()+ "\n" + nam,
                                Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getActivity().getBaseContext(), "No values on that date.",
                                Toast.LENGTH_LONG).show();
                    }
                    if (!rs.isClosed())
                    {
                        rs.close();
                    }
                }
            }
        });

        date_picker.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                DatePickerDialog mDatePicker=new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                        // TODO Auto-generated method stub
                        Calendar newDate = Calendar.getInstance();
                        newDate.set(selectedyear, selectedmonth, selectedday);
                        date_picker.setText(dateFormatter.format(newDate.getTime()));
                    }
                },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
                mDatePicker.getDatePicker().setCalendarViewShown(false);
                mDatePicker.setTitle("Select date");
                mDatePicker.show();
            }
        });

        return v;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (Global.svar1 != null && !Global.svar1.isEmpty()) {
            tw1.setText(Global.svar1);
            tw2.setText(Global.svar2);

            //if(isNetworkConnected()){
            fcr = new FetchConvRate(tw1.getText().toString(), tw2.getText().toString());
            fcr.execute();
            //conversionRate = fcr.getRate();
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {

                    //Toast.makeText(getActivity().getBaseContext(), "Rate: " + fcr.res,
                           // Toast.LENGTH_LONG).show();
                    conversionRate = fcr.res;
                }
            }, 2000);
            et1.setText("");
            et2.setText("");
        }
        Global.svar1 = null;Global.svar2 = null;
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }
}

