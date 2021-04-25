package com.example.pray;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.pray.database.PrayContract.DayPrayEntry;
import com.example.pray.database.PrayContract.MonthPrayEntry;
import com.example.pray.database.PrayOpenHelper;
import com.example.pray.network.GetData;
import com.example.pray.network.PrayData;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<PrayData>>, AdapterView.OnItemSelectedListener {

    private String country = "Egypt";
    private String city = "Cairo";
    private String day = "05";
    private String month = "08";
    private String year = "2020";
    private String url = "https://api.pray.zone/v2/times/month.json?city=" + city +
            "&month=" + year + "-" + month;


    PrayOpenHelper prayOpenHelper;
    TextView textView, textView2, textView3;
    Spinner spinnerCity, spinnerYear, spinnerMonth, spinnerDay;
    String[] cityArray, yearArray, monthArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cityArray = getResources().getStringArray(R.array.city_array);
        yearArray = getResources().getStringArray(R.array.year_array);
        monthArray = getResources().getStringArray(R.array.month_array);

        textView = (TextView) findViewById(R.id.textviewid);
        textView2 = (TextView) findViewById(R.id.textViewPray);
        textView3 = (TextView) findViewById(R.id.textviewTime);

        spinnerCity = (Spinner) findViewById(R.id.spinnerCity);
        spinnerYear = (Spinner) findViewById(R.id.spinnerYear);
        spinnerMonth = (Spinner) findViewById(R.id.spinnerMonth);
        spinnerDay = (Spinner) findViewById(R.id.spinnerDay);

        setArrayAdapter(spinnerYear, R.array.year_array);
        setArrayAdapter(spinnerMonth, R.array.month_array);
        setArrayAdapter(spinnerCity, R.array.city_array);
        setArrayAdapter(spinnerDay, R.array.day_array);

        LoaderManager.getInstance(this).initLoader(1, null, this);

        prayOpenHelper = new PrayOpenHelper(this);
    }

    public void setArrayAdapter(Spinner spinner, int array) {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, array, android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
    }

    @NonNull
    @Override
    public Loader<ArrayList<PrayData>> onCreateLoader(int id, @Nullable Bundle args) {
        return new GetData(this, url);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<ArrayList<PrayData>> loader, ArrayList<PrayData> data) {
        setData(data);
        getData();
    }

    @Override
    public void onLoaderReset(@NonNull Loader<ArrayList<PrayData>> loader) {

    }

    public void setData(ArrayList<PrayData> data) {

        //make db writable
        SQLiteDatabase db = prayOpenHelper.getWritableDatabase();

        //delete tables
        db.delete(MonthPrayEntry.TABLE_NAME, null, null);
        db.delete(DayPrayEntry.TABLE_NAME, null, null);

        //make content value to put it in insert statement
        ContentValues values = new ContentValues();
        ContentValues values2 = new ContentValues();

        for (int i = 0; i < data.size(); i++) {

            //get date of the day
            values.put(MonthPrayEntry.DATE, data.get(i).getDayDate());

            //times for every day
            for (int j = 0; j < data.get(i).getPrayName().length; j++) {

                //number of selected day
                values2.put(DayPrayEntry.DAY_NUMBER, i + 1);

                //name of prayer in selected day
                values2.put(DayPrayEntry.Name_Prayer, data.get(i).getPrayName()[j]);

                //time of prayer in selected day
                values2.put(DayPrayEntry.TIME, data.get(i).getPrayTime()[j]);

                db.insert(DayPrayEntry.TABLE_NAME, null, values2);
            }
            db.insert(MonthPrayEntry.TABLE_NAME, null, values);
        }
    }

    public void getData() {

        //make db reader
        SQLiteDatabase db = prayOpenHelper.getReadableDatabase();

        //data of the Days Table
        //----------------------

        //define selected columns
        String[] columns = {MonthPrayEntry.DAY_NUMBER, MonthPrayEntry.DATE};

        //query that get selected columns
        Cursor cursor = db.query(MonthPrayEntry.TABLE_NAME, columns, null, null, null, null, null);

        //define the idNumber of every column
        int dayNumberColumnIndex = cursor.getColumnIndex(MonthPrayEntry.DAY_NUMBER);
        int dayDateColumnIndex = cursor.getColumnIndex(MonthPrayEntry.DATE);

        //get information of every row
        while (cursor.moveToNext()) {
            String dayNumber = cursor.getString(dayNumberColumnIndex);
            String dayDate = cursor.getString(dayDateColumnIndex);

            textView.append(dayNumber + "\n");
            textView3.append(dayDate + "\n");
        }
        cursor.close();

        textView.setText("");
        textView2.setText("");
        textView3.setText("");


        //data of the Times Table
        //----------------------

        //define selected columns
        String[] columns2 = {DayPrayEntry.DAY_NUMBER, DayPrayEntry.Name_Prayer, DayPrayEntry.TIME};

        //define selected Rows
        String selection = DayPrayEntry.DAY_NUMBER + "=? ";
        String[] selectionArg = {day};

        //query that get selected columns
        cursor = db.query(DayPrayEntry.TABLE_NAME, columns2, selection, selectionArg, null, null, null);

        //define the idNumber of every column
        dayNumberColumnIndex = cursor.getColumnIndex(DayPrayEntry.DAY_NUMBER);
        int prayTimeColumnIndex = cursor.getColumnIndex(DayPrayEntry.TIME);
        int prayNameColumnIndex = cursor.getColumnIndex(DayPrayEntry.Name_Prayer);

        //get information of every row
        while (cursor.moveToNext()) {
            String dayNumber = cursor.getString(dayNumberColumnIndex);
            String prayTime = cursor.getString(prayTimeColumnIndex);
            String prayName = cursor.getString(prayNameColumnIndex);

            textView.append(dayNumber + "\n");
            textView2.append(prayName + "\n");
            textView3.append(prayTime + "\n");
        }
        cursor.close();
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        switch (adapterView.getId()) {
            case R.id.spinnerCity:
                city = adapterView.getItemAtPosition(i).toString();
                break;
            case R.id.spinnerYear:
                year = adapterView.getItemAtPosition(i).toString();
                break;
            case R.id.spinnerMonth:
                month = adapterView.getItemAtPosition(i).toString();
                break;
            case R.id.spinnerDay:
                day = adapterView.getItemAtPosition(i).toString();
                break;
        }
//        String itemSelected = adapterView.getItemAtPosition(i).toString();
//        Log.v("city", url);
//
//            textView.setText(adapterView.getItemAtPosition(i).toString());
//            city = adapterView.getItemAtPosition(i).toString();
//            Log.v("city:", city);
//            url = "http://api.aladhan.com/v1/calendarByCity?city=" + city +
//                    "&country=" + country +
//                    "&month=" + month + "&year=" + year;
//            Log.v("url", url);
//            LoaderManager.getInstance(this).initLoader(3, null, this);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public void go(View view) {
        url = "https://api.pray.zone/v2/times/month.json?city=" + city +
                "&month=" + year + "-" + month;
        Log.v("url", url);
        LoaderManager.getInstance(this).restartLoader(0,null,this);
    }
}