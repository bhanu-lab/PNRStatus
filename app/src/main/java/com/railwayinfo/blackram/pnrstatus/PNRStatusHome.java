package com.railwayinfo.blackram.pnrstatus;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PNRStatusHome extends AppCompatActivity implements View.OnClickListener{

    ProgressDialog pd;
    TextView pnrStatusView, trainNumberResult, numberOfPass, doj, chartStatus, boardingPoint, reservationUpto, passengerInfo;
    Button pnrStatusCheck;
    AutoCompleteTextView autoCompletePNRNumber;
    ListView passengerListView;
    private static String pnrNumber;
    private static String railwayApiUrl;
    List<String> passengersList = new ArrayList<String>();
    String[] data = {};
    ArrayAdapter<String> autoCompleteAdapter;
    PNRStatusVO pnrValues = new PNRStatusVO();
    List<String> myAutoCompleteList = new ArrayList<String>();
    TableLayout tableLayout;
    boolean isDataAvailable;
    ScrollView scrollView;
    LayoutParams passengerParams;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pnrstatus_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        pnrStatusCheck = (Button) findViewById(R.id.pnrStatusCheck);
        pnrStatusCheck.setOnClickListener(this);
        autoCompletePNRNumber = (AutoCompleteTextView) findViewById(R.id.pnrNumberAutoComplete);
        trainNumberResult = (TextView) findViewById(R.id.resultTrainNumber);
        numberOfPass = (TextView) findViewById(R.id.noOfPassResult);
        doj = (TextView) findViewById(R.id.dojResult);
        chartStatus = (TextView) findViewById(R.id.chartStatusId);
        boardingPoint = (TextView) findViewById(R.id.boardingPointResult);
        reservationUpto = (TextView) findViewById(R.id.reservationUptoResult);
        passengerListView = (ListView) findViewById(R.id.passengersList);
        myAutoCompleteList.add("1234567890");
        tableLayout = (TableLayout) findViewById(R.id.pnrResultTable);
        scrollView = (ScrollView) findViewById(R.id.scrollView);
        passengerParams = (LayoutParams) passengerListView.getLayoutParams();

        autoCompletePNRNumber.setOnEditorActionListener(new TextView.OnEditorActionListener(){

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_DONE){
                    pnrStatusCheck.callOnClick();
                }
                return false;
            }
        });

        autoCompleteAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line, myAutoCompleteList);
        autoCompletePNRNumber.setAdapter(autoCompleteAdapter);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Copied Data to Clip Board", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, pnrValues.toString());
                sendIntent.setType("text/plain");
                startActivity(sendIntent);

            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_pnrstatus_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class PNRStatus extends AsyncTask<Void, Void, Void> {

        ArrayAdapter<String> adapter;

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            //show progress dialog
            pd = new ProgressDialog(PNRStatusHome.this);
            pd.setMessage("Please Wait ...");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            HttpHandler response = new HttpHandler();
            String reservationStatus = "";
            Log.d("before invoking", railwayApiUrl);
            String jsonResponse = response.makeServiceCall(railwayApiUrl);
            Log.e("RESPONSE", "Response from http "+jsonResponse);

            if(jsonResponse != null){
                try {
                    JSONObject jsonObject = new JSONObject(jsonResponse);

                    pnrValues.setResponseCode(jsonObject.getString("response_code"));
                    pnrValues.setTrainName(jsonObject.getString("train_name"));
                    pnrValues.setTrainNumber(jsonObject.getString("train_num"));
                    pnrValues.setTotalPassengersString(jsonObject.getString("total_passengers"));
                    pnrValues.setDojString(jsonObject.getString("doj"));
                    pnrValues.setChartStatusString(jsonObject.get("chart_prepared").equals("Y")? "Prepared": "Not Prepared");
                    JSONObject boardingJson  = jsonObject.getJSONObject("boarding_point");
                    pnrValues.setBoardingPointString(boardingJson.getString("name"));
                    //boolean hasErrorOccured = Boolean.parseBoolean(jsonObject.getString("error"));
                    JSONObject reservationUptoObject = jsonObject.getJSONObject("reservation_upto");
                    pnrValues.setReservationUptoString(reservationUptoObject.getString("name"));

                    JSONArray passengers = jsonObject.getJSONArray("passengers");
                    for(int count =0; count<passengers.length(); count++){
                        Passenger passenger = new Passenger();
                        passenger.setPassengerId(passengers.getJSONObject(count).getString("no"));
                        passenger.setBookingStatus(passengers.getJSONObject(count).getString("booking_status"));
                        passenger.setCurrentStatus(passengers.getJSONObject(count).getString("current_status"));
                        passengersList.add(passenger.toString());
                    }
                    passengerParams.height = passengersList.size();
                    pnrValues.setPassengers(passengersList);
                    adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.simple_text_view, android.R.id.text1, passengersList);
                    isDataAvailable = true;
                    Log.e("data availability", isDataAvailable+"");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result){
            if(pnrValues.getResponseCode().equals("200")) {
                trainNumberResult.setText(pnrValues.getTrainNumber());
                numberOfPass.setText(pnrValues.getTotalPassengersString());
                doj.setText(pnrValues.getDojString());
                chartStatus.setText(pnrValues.getChartStatusString());
                boardingPoint.setText(pnrValues.getBoardingPointString());
                reservationUpto.setText(pnrValues.getReservationUptoString());
                passengerListView.setAdapter(adapter);
                //passengerListView.setBackgroundColor(Color.WHITE);
                pnrValues.setPnrNumber(pnrNumber);
                if(isDataAvailable){
                    tableLayout.setVisibility(View.VISIBLE);
                    scrollView.setVisibility(View.VISIBLE);
                }
            }else{
                tableLayout.setVisibility(View.INVISIBLE);
                scrollView.setVisibility(View.INVISIBLE);
                Toast.makeText(getApplicationContext(), "Not able to reach server Try Again!!!", Toast.LENGTH_LONG).show();
            }

            pd.dismiss();
        }
    }

    public void onClick(View v){
        switch(v.getId()){
            case R.id.pnrStatusCheck:
                pnrValues = new PNRStatusVO();
                passengersList = new ArrayList<String>();
                pnrNumber = autoCompletePNRNumber.getText().toString();
                //using api key here is not a good idea as it can exposed by various means
                railwayApiUrl = "https://api.railwayapi.com/v2/pnr-status/pnr/"+pnrNumber+"/apikey/REPLACE_WITH_YOUR_API_KEY/";
                Log.d("Address", railwayApiUrl);
                if(!myAutoCompleteList.contains(pnrNumber)) {
                    myAutoCompleteList.add(pnrNumber);
                    autoCompleteAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, myAutoCompleteList);
                    autoCompletePNRNumber.setAdapter(autoCompleteAdapter);
                }
                new PNRStatus().execute();
                if(isDataAvailable){
                    tableLayout.setVisibility(View.VISIBLE);
                }

        }

    }
}
