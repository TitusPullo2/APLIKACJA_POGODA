package com.example.aplikacjapogoda;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;

/**
 * Główna aktywność aplikacji zawierająca pola do wprowadzania danych, przyciski oraz pola tekstowe z danymi pogodowymi.
 * Obsługuje pobieranie współrzędnych GPS i wyświetlanie danych pogodowych.
 */
public class MainActivity extends AppCompatActivity{
    /**
     * Elementy interfejsu użytkownika.
     */
    private EditText timeEditText;
    private EditText dateEditText;
    private EditText locationEditText;
    private Button gpsButton;
    private Button searchButton;
    private ImageView weatherIcon;
    private TextView typeWeatherTextView;
    private TextView locationTextView;
    private TextView dateTextView;
    private TextView timeTextView;
    private TextView temperatureTextView;
    private TextView humidityTextView;
    private TextView precipitationTextView;
    private TextView windSpeedTextView;
    private TextView windDirectionView;
    private TextView pressureTextView;
    private TextView indexUvTextView;
    private TextView cloudyTextView;

    /**
     * Metoda wywoływana przy tworzeniu aktywności.
     * Inicjalizuje elementy interfejsu użytkownika.
     * Ustawia obsługę przycisków.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inicjalizacja elementów interfejsu użytkownika.
        weatherIcon = findViewById(R.id.weatherIcon);
        typeWeatherTextView = findViewById(R.id.typeWeatherTextView);
        locationTextView = findViewById(R.id.locationTextView);
        dateTextView = findViewById(R.id.dateTextView);
        timeTextView = findViewById(R.id.timeTextView);
        temperatureTextView = findViewById(R.id.temperatureTextView);
        humidityTextView = findViewById(R.id.humidityTextView);
        precipitationTextView = findViewById(R.id.precipitationTextView);
        windSpeedTextView = findViewById(R.id.windSpeedTextView);
        windDirectionView = findViewById(R.id.windDirectionView);
        pressureTextView = findViewById(R.id.pressureTextView);
        indexUvTextView = findViewById(R.id.indexUvTextView);
        cloudyTextView = findViewById(R.id.cloudyTextView);
        timeEditText = findViewById(R.id.timeEditText);
        dateEditText = findViewById(R.id.dateEditText);
        locationEditText = findViewById(R.id.locationEditText);
        gpsButton = findViewById(R.id.gpsButton);
        searchButton = findViewById(R.id.searchButton);

        /*
          Tablice przechowujące współrzędne GPS.
          Używane w celu przekazania koordynatów z gpsButton do searchButton.
         */
        String[] locationLatitude = {""};
        String[] locationLongitude = {""};

        /*
          Obsługa przycisku GPS.
          Pobiera współrzędne GPS i zapisuje je w tablicach locationLatitude i locationLongitude.
          Wyświetla komunikat o udostępnieniu współrzędnych.
         */
        gpsButton.setOnClickListener(v -> getGpsLocation(locationCoordinates -> {
            locationLatitude[0] = locationCoordinates[0];
            locationLongitude[0] = locationCoordinates[1];
            Toast.makeText(MainActivity.this, "Współrzedne udostępnione", Toast.LENGTH_SHORT).show();
        }));
        /*
          Obsługa przycisku Szukaj.
          Pobiera dane wprowadzone przez użytkownika i wywołuje asynchroniczne pobieranie danych pogodowych z API.
         */
        searchButton.setOnClickListener(v -> {
            String time = timeEditText.getText().toString();
            String date = dateEditText.getText().toString();
            String locationName = locationEditText.getText().toString();
            new FetchWeatherDataTask(time, date, locationName, locationLatitude[0], locationLongitude[0]).execute();
        });
    }
    /**
     * Metoda pobierająca współrzędne GPS telefonu.
     * @param callback obiekt implementujący interfejs LocationCallback.
     */
    private void getGpsLocation(LocationCallback callback){
        String[] locationCoordinates = new String[2];
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        LocationListener locationListener = new LocationListener(){
            @Override
            public void onLocationChanged(Location location){
                locationCoordinates[0] = String.valueOf(location.getLatitude());
                locationCoordinates[1] = String.valueOf(location.getLongitude());
                callback.onLocationReceived(locationCoordinates);

                if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                    locationManager.removeUpdates(this);
                }
            }
            @Override
            public void onStatusChanged(String provider, int status, Bundle extras){
                // Auto-generated method stub
            }
            @Override
            public void onProviderEnabled(@NonNull String provider){
                // Auto-generated method stub
            }
            @Override
            public void onProviderDisabled(@NonNull String provider){
                // Auto-generated method stub
            }
        };
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
        else{
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        }
    }
    /**
     * Wewnętrzna klasa dziedzicząca po AsyncTask.
     * Obsługuje asynchroniczne pobieranie danych pogodowych z API i wyświetlanie ich w interfejsie użytkownika.
     * @see AsyncTask - klasa do wykonywania operacji w tle. Wymagana do obsługi połączenia z API.
     */
    private class FetchWeatherDataTask extends AsyncTask<Void, Void, ArrayList<String>>{
        /**
         * Dane wprowadzone przez użytkownika.
         */
        private String time;
        private String date;
        private String locationName;
        private String locationLatitude;
        private String locationLongitude;

        /**
         * Konstruktor klasy.
         * @param time czas.
         * @param date data.
         * @param locationName nazwa lokalizacji.
         * @param locationLatitude szerokość geograficzna.
         * @param locationLongitude długość geograficzna.
         */
        public FetchWeatherDataTask(String time, String date, String locationName, String locationLatitude, String locationLongitude){
            this.time = time;
            this.date = date;
            this.locationName = locationName;
            this.locationLatitude = locationLatitude;
            this.locationLongitude = locationLongitude;
        }
        /**
         * Metoda do pobierania danych pogodowych z API w tle.
         * @see UserInteraction - klasa do pobierania danych pogodowych z API.
         * @param voids parametr typu Void.
         * @return dane pogodowe.
         */
        @Override
        protected ArrayList<String> doInBackground(Void... voids){
            UserInteraction userInteraction = new UserInteraction(time, date, locationName, locationLatitude, locationLongitude);
            return userInteraction.getWeatherData();
        }
        /**
         * Metoda wywoływana po zakończeniu działania metody doInBackground.
         * Wyświetla dane pogodowe w interfejsie użytkownika.
         * @param weatherData dane pogodowe.
         */
        @Override
        protected void onPostExecute(ArrayList<String> weatherData){
            super.onPostExecute(weatherData);

            String temperature = "Temperatura: " + weatherData.get(3) + "°C";
            String humidity = "Wilgotność: " + weatherData.get(4) + "%";
            String precipitation = "Opady: " + weatherData.get(5) + " mm";
            String windSpeed = "Prędkość wiatru: " + weatherData.get(6) + " km/h";
            String windDir = "Kierunek wiatru: " + weatherData.get(7);
            String pressure = "Ciśnienie: " + weatherData.get(8) + " hPa";
            String uv = "Indeks UV: " + weatherData.get(9);
            String cloudy = "Zachmurzenie: " + weatherData.get(10) + "%";

            locationTextView.setText(weatherData.get(0));
            dateTextView.setText(weatherData.get(1));
            timeTextView.setText(weatherData.get(2));
            temperatureTextView.setText(temperature);
            humidityTextView.setText(humidity);
            precipitationTextView.setText(precipitation);
            windSpeedTextView.setText(windSpeed);
            windDirectionView.setText(windDir);
            pressureTextView.setText(pressure);
            indexUvTextView.setText(uv);
            cloudyTextView.setText(cloudy);
            typeWeatherTextView.setText(weatherData.get(11));
            weatherIcon.setImageResource(getResources().getIdentifier(weatherData.get(12), "drawable", getPackageName()));

            /*
              Zapis danych do SharedPreferences
              Przekazanie danych do widgetu za pomocą SharedPreferences
             */
            SharedPreferences sharedPreferences = getSharedPreferences("WeatherData", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            for (int i = 0; i < weatherData.size(); i++) {
                editor.putString(String.valueOf(i), weatherData.get(i));
            }
            editor.apply();

            /*
              Aktualizacja widgetu za pomocą broadcastu.
              Aktualizacja widgetu następuje po kliknięciu przycisku "Szukaj".
             */
            Intent intent = new Intent(MainActivity.this, WeatherWidget.class);
            intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
            int[] ids = AppWidgetManager.getInstance(getApplication()).getAppWidgetIds(getComponentName());
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
            sendBroadcast(intent);
        }
    }
}