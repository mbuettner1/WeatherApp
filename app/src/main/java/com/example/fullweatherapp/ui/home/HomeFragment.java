package com.example.fullweatherapp.ui.home;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.fullweatherapp.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class HomeFragment extends Fragment {

    private static final int REQ_CODE = 100;
    EditText editText;
    Button button;
    TextView country, city, temp, date, lat, lon, humidity, low, high, pressure, windSpeed;
    private GoogleMap mMap;
    Double latitude, longitude;
    String cityName;
    TextToSpeech t1;
    Button b1;

    private HomeViewModel homeViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        editText = root.findViewById(R.id.editTextTextPersonName);
        button = root.findViewById(R.id.button);
        country = root.findViewById(R.id.country);
        city = root.findViewById(R.id.city);
        temp = root.findViewById(R.id.temp);
        date = root.findViewById(R.id.date);
        lat = root.findViewById(R.id.lat);
        lon = root.findViewById(R.id.lon);
        humidity = root.findViewById(R.id.humidity);
        low = root.findViewById(R.id.min);
        high = root.findViewById(R.id.max);
        pressure = root.findViewById(R.id.pressure);
        windSpeed = root.findViewById(R.id.wind);
        b1 = root.findViewById(R.id.read);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findWeather();
            }
        });

        ImageView speak = root.findViewById(R.id.speak);
        speak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                        RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
                intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Need to speak");
                try {
                    startActivityForResult(intent, REQ_CODE);
                } catch (ActivityNotFoundException a) {
                    Toast.makeText(getActivity().getApplicationContext(),
                            "Sorry your device not supported",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        t1=new TextToSpeech(getActivity().getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    t1.setLanguage(Locale.UK);
                }
            }
        });

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String speakCity = city.getText().toString();
                Toast.makeText(getActivity().getApplicationContext(), speakCity,Toast.LENGTH_SHORT).show();
                t1.speak(speakCity, TextToSpeech.QUEUE_FLUSH, null);
                String speakCountry = country.getText().toString();
                Toast.makeText(getActivity().getApplicationContext(), speakCountry,Toast.LENGTH_SHORT).show();
                t1.speak(speakCountry, TextToSpeech.QUEUE_FLUSH, null);
                String speakDate = date.getText().toString();
                Toast.makeText(getActivity().getApplicationContext(), speakDate,Toast.LENGTH_SHORT).show();
                t1.speak(speakDate, TextToSpeech.QUEUE_FLUSH, null);
                String speakTemp = temp.getText().toString();
                Toast.makeText(getActivity().getApplicationContext(), speakTemp,Toast.LENGTH_SHORT).show();
                t1.speak(speakTemp, TextToSpeech.QUEUE_FLUSH, null);

            }
        });

        return root;
    }

    private void findWeather() {
        String search = editText.getText().toString();
        String url = "http://api.openweathermap.org/data/2.5/weather?q=" + search + "&appid=3012e8ef60b93ada1cd76432e09fffe4&units=imperial";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {

                    JSONObject jsonObject = new JSONObject(response);

                    JSONObject sys = jsonObject.getJSONObject("sys");
                    JSONObject main = jsonObject.getJSONObject("main");
                    JSONObject coord = jsonObject.getJSONObject("coord");
                    JSONObject wind = jsonObject.getJSONObject("wind");

                    String country_find = sys.getString("country");
                    country.setText(country_find);

                    String city_find = jsonObject.getString("name");
                    city.setText(city_find);
                    cityName = city_find;

                    String temp_find = main.getString("temp");
                    temp.setText(temp_find +"°F");

                    Calendar calendar = Calendar.getInstance();
                    SimpleDateFormat std = new SimpleDateFormat("dd/MM/yyyy");
                    //std.setTimeZone(TimeZone.getTimeZone("-14400"));
                    String date_find = std.format(calendar.getTime());
                    date.setText(date_find);

                    String lat_find = coord.getString("lat");
                    lat.setText(lat_find + "°N");
                    latitude = Double.parseDouble(coord.getString("lat"));

                    String lon_find = coord.getString("lon");
                    lon.setText(lon_find + "°E");
                    longitude = Double.parseDouble(coord.getString("lon"));

                    String humidity_find = main.getString("humidity");
                    humidity.setText(humidity_find + "%");

                    String low_find = main.getString("temp_min");
                    low.setText(low_find + "°F");

                    String high_find = main.getString("temp_max");
                    high.setText(high_find + "°F");

                    String pressure_find = main.getString("pressure");
                    pressure.setText(pressure_find + " hPa");

                    String wind_find =  wind.getString("speed");
                    windSpeed.setText(wind_find + " m/h");

                    newLocation();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), "" + error.getLocalizedMessage(),Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        requestQueue.add(stringRequest);
    }


    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        LatLng sydney = new LatLng(-33.852, 151.211);
        googleMap.addMarker(new MarkerOptions()
                .position(sydney)
                .title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    void newLocation(){
        if(mMap!= null){

            LatLng area = new LatLng(latitude, longitude);
            mMap.addMarker(new
                    MarkerOptions().position(area).title(cityName));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(area,11));
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQ_CODE: {
                if (resultCode == getActivity().RESULT_OK && null != data) {
                    ArrayList result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    editText.setText((CharSequence)result.get(0));
                }
                break;
            }
        }
    }

    public void onPause(){
        if(t1 !=null){
            t1.stop();
            t1.shutdown();
        }
        super.onPause();
    }

}