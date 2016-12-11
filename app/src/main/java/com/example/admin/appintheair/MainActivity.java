package com.example.admin.appintheair;
//commit 3, change 1
//commit 3, change 2

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;


public class MainActivity extends AppCompatActivity {

    //change for commit test

    private static final String API_KEY = BuildConfig.API_KEY;
    final String LOG_CAT = "****";
    List<String> values = new ArrayList<String>();
    List<String> labels = new ArrayList<String>();
    private ArrayAdapter adapter ;
    Button hit_button;

    String departureLocation;
    String arrivalLocation;

    /*
    //gia to hmerologio
    private TextView departureDate;
    private TextView arrivalDate;
    private int pYear;
    private int pMonth;
    private int pDay;

    private int pYearArrival;
    private int pMonthArrival;
    private int pDayArrival;

    static final int DATE_DIALOG_ID = 0;
    //gia to hmerologio --//
    */

    private Calendar calendar;
    private int day, month, year;
    EditText departureDate;
    EditText arrivalDate;
    DatePicker datePicker;
    String departureDateString;
    String arrivalDateString;

    LinearLayout layout;

    AutoCompleteTextView departureLocationEditText = null;
    AutoCompleteTextView arrivalLocationEditText = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        departureDate = (EditText)findViewById(R.id.departure_date);
        arrivalDate = (EditText)findViewById(R.id.arrival_date);
        calendar = Calendar.getInstance();
        day = calendar.get(Calendar.DAY_OF_MONTH);
        month = calendar.get(Calendar.MONTH);
        year = calendar.get(Calendar.YEAR);
        showDate(departureDate, year, month+1, day);
        showDate(arrivalDate, year, month+1, day);





        hit_button = (Button) findViewById(R.id.search_button);
        departureLocationEditText = (AutoCompleteTextView)findViewById(R.id.departure_airport_editText);
        arrivalLocationEditText = (AutoCompleteTextView)findViewById(R.id.arrival_airport_editText);

/*
        Button hit_button = (Button) findViewById(R.id.search_button);
        hit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                departureLocation = departureLocationEditText.getText().toString();
                new FetchAutocompletedAirportTask().execute();
            }
        });
*/
        layout = (LinearLayout)findViewById(R.id.layout);

        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "klik", Toast.LENGTH_SHORT).show();
            }
        });

        hit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(labels.contains(departureLocation))
                    departureLocationEditText.setText(values.get(labels.indexOf(departureLocation)));
                if(labels.contains(arrivalLocation))
                    arrivalLocationEditText.setText(values.get(labels.indexOf(arrivalLocation)));
            }
        });
        //experimenting on addTextChangedListener
        departureLocationEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                departureLocation = departureLocationEditText.getText().toString();
                AsyncTask<String, Void, String> klhshAsyncTask = new FetchAutocompletedAirportTask().execute(departureLocation);
                adapter = new
                        ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, labels);
                try {
                    klhshAsyncTask.get(1000, TimeUnit.MILLISECONDS);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (TimeoutException e) {
                    e.printStackTrace();
                }
                if(labels.size()>=2)
                    Log.v("*********", "To deutero stoixeio: " + labels.get(1));

                departureLocationEditText.setAdapter(adapter);
                departureLocationEditText.setThreshold(1);
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });

        arrivalLocationEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                arrivalLocation = arrivalLocationEditText.getText().toString();
                AsyncTask<String, Void, String> klhshAsyncTask = new FetchAutocompletedAirportTask().execute(arrivalLocation);
                adapter = new
                        ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, labels);
                try {
                    klhshAsyncTask.get(1000, TimeUnit.MILLISECONDS);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (TimeoutException e) {
                    e.printStackTrace();
                }
                if(labels.size()>=2)
                    Log.v("*********", "To deutero stoixeio: " + labels.get(1));

                arrivalLocationEditText.setAdapter(adapter);
                arrivalLocationEditText.setThreshold(1);
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });

        /*
        //gia to hmerologio -- gia to departure
        // Capture our View elements
        departureDate = (TextView) findViewById(R.id.departure_date);

        // Listener for click event of the button
        departureDate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showDialog(DATE_DIALOG_ID);
                // Get the current date
                final Calendar cal = Calendar.getInstance();
                pYear = cal.get(Calendar.YEAR);
                pMonth = cal.get(Calendar.MONTH);
                pDay = cal.get(Calendar.DAY_OF_MONTH);

                // Display the current date in the TextView
                updateDisplay();
            }
        });



        //gia to hmerologio --//




        //gia to hmerologio -- gia to arrival
        // Capture our View elements
        arrivalDate = (TextView) findViewById(R.id.arrival_date);

        // Listener for click event of the button
        arrivalDate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showDialog(DATE_DIALOG_ID);
                // Get the current date
                final Calendar calArrival = Calendar.getInstance();
                pYearArrival = calArrival.get(Calendar.YEAR);
                pMonthArrival = calArrival.get(Calendar.MONTH);
                pDayArrival = calArrival.get(Calendar.DAY_OF_MONTH);

                // Display the current date in the TextView
                updateDisplayArrival();
            }
        });



        //gia to hmerologio --//

        */

    }


    public class FetchAutocompletedAirportTask extends AsyncTask<String,Void,String> {

        private String getDataFromJson(String AutocompleteJsonStr)
                throws JSONException {

            // These are the names of the JSON objects that need to be extracted.
            final String value = "value";
            final String label = "label";

            JSONArray parentArray = new JSONArray(AutocompleteJsonStr);

            StringBuffer finalStringBuffer = new StringBuffer();

            //tis adeiazw ka8e fora prin na tis gemisw gia na diagrafoun oi times apo thn prohgoumenh klhsh sto idio session
            
            for(int i=0; i<parentArray.length(); i++) {

                JSONObject finalObject = parentArray.getJSONObject(i);

                String objectValue = finalObject.getString(value);
                String objectLabel = finalObject.getString(label);
                finalStringBuffer.append(objectValue + " - " + objectLabel + "\n");
                Log.v("*********", objectValue + " - " + objectLabel + "\n");

                if(!labels.contains(objectLabel))
                    labels.add(i, objectLabel);
                if(!values.contains(objectValue))
                    values.add(i, objectValue);
            }
            Log.v("*********", "Megethos labels: " + labels.size());
            return finalStringBuffer.toString();

        }

        @Override
        protected String doInBackground(String... strings) {

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String autocompleteJsonStr = null;

            try {

                final String AIRPORT_AUTOCOMPLETE_BASE_URL = "https://api.sandbox.amadeus.com/v1.2/airports/autocomplete?apikey=" + API_KEY ;
                final String TERM = "term";

                Uri airportAutocompleteBuiltUri = Uri.parse(AIRPORT_AUTOCOMPLETE_BASE_URL)
                        .buildUpon()
                        .appendQueryParameter(TERM, strings[0])
                        .build();
                Log.v("****", "departure location string: " + strings[0]);

                URL autocompleteStringUrl = new URL(airportAutocompleteBuiltUri.toString());

                Log.v("*********", "Built URI: " + airportAutocompleteBuiltUri.toString());

                // Create the request to OpenWeatherMap, and open the connection
                urlConnection = (HttpURLConnection) autocompleteStringUrl.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }

                autocompleteJsonStr = buffer.toString();
                Log.v("****", "JSON String: " + autocompleteJsonStr);
            } catch (IOException e) {
                Log.e("****", "Error ", e);
                // If the code didn't successfully get the weather data, there's no point in attemping
                // to parse it.
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e("****", "Error closing stream", e);
                    }
                }
            }
            try {
                return getDataFromJson(autocompleteJsonStr);
            } catch (JSONException e) {
                Log.e("**********", e.getMessage(), e);
                e.printStackTrace();
            }

            // This will only happen if there was an error getting or parsing the forecast.
            return null;
        }

        @Override
        protected void onPostExecute(String strings) {
            super.onPostExecute(strings);
            //ShowAlertDialogWithListview();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
    }

/*
    public void setDate(View v) {
        showDialog(1);
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        if(id == 1)
            return new DatePickerDialog(this, myDateListener, year, month, day);
        return null;
    }

    private DatePickerDialog.OnDateSetListener myDateListener =
            new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                    showDate(departureDate, i, i1+1, i2);
                }
            };



    private void showDate(TextView v, int year, int month, int day) {
        v.setText(new StringBuilder().append(day).append("/")
                .append(month).append("/")
                .append(year));

        if(v.equals(departureDate)) {
            departureDateString = v.getText().toString();
            Log.v(LOG_CAT, "lalalalalal: " + departureDateString);
        }

    }
*/














    public void ShowAlertDialogWithListview()
    {

        //Create sequence of items
        final CharSequence[] Values = values.toArray(new String[labels.size()]);
        final CharSequence[] Labels = labels.toArray(new String[labels.size()]);
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setTitle("Airports");
        dialogBuilder.setItems(Labels, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                String selectedValue = Values[item].toString();  //Selected item in listview
                String selectedLabel = Labels[item].toString();
                Toast.makeText(getApplicationContext(), "Selected airport: " + selectedLabel,Toast.LENGTH_SHORT).show();
                EditText departureLocationEditText = (EditText)findViewById(R.id.departure_airport_editText);
                departureLocationEditText.setText(selectedValue);
            }
        });
        //Create alert dialog object via builder
        AlertDialog alertDialogObject = dialogBuilder.create();
        //Show the dialog
        alertDialogObject.show();
    }


    /*
    private DatePickerDialog.OnDateSetListener pDateSetListenerArrival =
            new DatePickerDialog.OnDateSetListener() {

                public void onDateSet(DatePicker view, int year,
                                      int monthOfYear, int dayOfMonth) {
                    pYearArrival = year;
                    pMonthArrival = monthOfYear;
                    pDayArrival = dayOfMonth;
                    updateDisplayArrival();
                    displayToastArrival();
                }
            };

    private void updateDisplayArrival() {
        arrivalDate.setText(
                new StringBuilder()
                        // Month is 0 based so add 1
                        .append(pDayArrival).append("/")
                        .append(pMonthArrival + 1).append("/")
                        .append(pYearArrival).append(" "));
    }


    private void displayToastArrival() {
        Toast.makeText(this, new StringBuilder().append("Date choosen is ").append(arrivalDate.getText()),  Toast.LENGTH_SHORT).show();
    }



    private DatePickerDialog.OnDateSetListener pDateSetListener =
            new DatePickerDialog.OnDateSetListener() {

                public void onDateSet(DatePicker view, int year,
                                      int monthOfYear, int dayOfMonth) {
                    pYear = year;
                    pMonth = monthOfYear;
                    pDay = dayOfMonth;
                    updateDisplay();
                    displayToast();
                }
            };

    private void updateDisplay() {
        departureDate.setText(
                new StringBuilder()
                        // Month is 0 based so add 1
                        .append(pDay).append("/")
                        .append(pMonth + 1).append("/")
                        .append(pYear).append(" "));
    }


    private void displayToast() {
        Toast.makeText(this, new StringBuilder().append("Date choosen is ").append(departureDate.getText()),  Toast.LENGTH_SHORT).show();
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_DIALOG_ID:
                if(arrivalDate.isFocused()) {
                    Log.v("************", "nai mphka");
                    return new DatePickerDialog(this,
                            pDateSetListenerArrival,
                            pYearArrival, pMonthArrival, pDayArrival);
                }
                return new DatePickerDialog(this,
                        pDateSetListener,
                        pYear, pMonth, pDay);
        }
        return null;
    }
    */

    public void setDate(View v) {
        showDialog(1);
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        if(id == 1)
            return new DatePickerDialog(this, myDateListener, year, month, day);
        if(id == 2)
            return new DatePickerDialog(this, myDateListener2, year, month, day);
        return null;
    }

    private DatePickerDialog.OnDateSetListener myDateListener =
            new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                    showDate(departureDate, i, i1+1, i2);
                }
            };



    private void showDate(TextView v, int year, int month, int day) {
        v.setText(new StringBuilder().append(day).append("/")
                .append(month).append("/")
                .append(year));
        departureDateString = v.getText().toString();
    }


//-------------------------------------------------------------------------------------------
    public void setDate2(View v) {
        showDialog(2);
    }


    private DatePickerDialog.OnDateSetListener myDateListener2 =
            new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                    showDate2(arrivalDate, i, i1+1, i2);
                }
            };



    private void showDate2(TextView v, int year, int month, int day) {
        v.setText(new StringBuilder().append(day).append("/")
                .append(month).append("/")
                .append(year));
        arrivalDateString = v.getText().toString();
    }


















































}



































