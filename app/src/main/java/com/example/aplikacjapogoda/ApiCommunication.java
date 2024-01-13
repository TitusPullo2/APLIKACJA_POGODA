package com.example.aplikacjapogoda;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.HashMap;

import javax.net.ssl.HttpsURLConnection;

public class ApiCommunication{
    private String URL_CURRENT = "https://api.weatherapi.com/v1/current.json?key=8b87ed58de2945a6876205648232812&q={location}&lang=pl&aqi=no";
    private String URL_FORECAST = "https://api.weatherapi.com/v1/forecast.json?key=8b87ed58de2945a6876205648232812&q={location}&days={numberOfDay}&dt={dateOfDay}&hour={hourOfDay}&lang=pl&aqi=no&alerts=no";
    private String URL_HISTORY = "https://api.weatherapi.com/v1/history.json?key=8b87ed58de2945a6876205648232812&q={location}&dt={date}&hour={hourOfDay}&lang=pl&aqi=no";
    private String locationName;
    private String locationLatitude;
    private String locationLongitude;
    private String date;
    private String time;

    public ApiCommunication(String locationName){
        this.locationName = locationName;
    }
    public ApiCommunication(String locationLatitude, String locationLongitude){
        this.locationLatitude = locationLatitude;
        this.locationLongitude = locationLongitude;
    }
    public ApiCommunication(String locationName, String date, String time){
        this.locationName = locationName;
        this.date = date;
        this.time = time;
    }
    public ApiCommunication(String locationLatitude, String locationLongitude, String date, String time){
        this.locationLatitude = locationLatitude;
        this.locationLongitude = locationLongitude;
        this.date = date;
        this.time = time;
    }
    private String numberOfDays(){
        LocalDate localDate = LocalDate.now();
        LocalDate targetDate = LocalDate.parse(this.date);
        int numberOfDays = targetDate.getDayOfYear() - localDate.getDayOfYear();
        return String.valueOf(numberOfDays);
    }
    private String locationNameTranslation(){
        String originalLocationName = this.locationName;
        String translatedLocationName = "";
        HashMap<String, String> nameTranslation = new HashMap<>();
        nameTranslation.put("Warszawa", "Warsaw");
        nameTranslation.put("Kraków", "Cracow");

        if(nameTranslation.containsKey(originalLocationName)){
            translatedLocationName = nameTranslation.get(originalLocationName);
        }
        else{
            translatedLocationName = originalLocationName
                    .replace("ą", "a")
                    .replace("ć", "c")
                    .replace("ę", "e")
                    .replace("ł", "l")
                    .replace("ń", "n")
                    .replace("ó", "o")
                    .replace("ś", "s")
                    .replace("ź", "z")
                    .replace("ż", "z");

        }
        return translatedLocationName;
    }
    public String getCurrentWeather() throws IOException{
        URL url;
        if(locationName != null){
            url = new URL(URL_CURRENT.replace("{location}", locationNameTranslation()));
        }
        else{
            url = new URL(URL_CURRENT.replace("{location}", locationLatitude + "," + locationLongitude));
        }

        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        try(BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()))){
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while((line = bufferedReader.readLine()) != null){
                stringBuilder.append(line);
            }
            return stringBuilder.toString();
        }
    }
    public String getForecastWeather() throws IOException{
        URL url;
        String replacedUrl;
        if(locationName != null){
            replacedUrl = URL_FORECAST
                    .replace("{location}", locationNameTranslation())
                    .replace("{numberOfDay}", numberOfDays())
                    .replace("{dateOfDay}", date)
                    .replace("{hourOfDay}", time);
        }
        else{
            replacedUrl = URL_FORECAST
                    .replace("{location}", locationLatitude + "," + locationLongitude)
                    .replace("{numberOfDay}", numberOfDays())
                    .replace("{dateOfDay}", date)
                    .replace("{hourOfDay}", time);
        }
        url = new URL(replacedUrl);

        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        try(BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()))){
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while((line = bufferedReader.readLine()) != null){
                stringBuilder.append(line);
            }
            return stringBuilder.toString();
        }
    }
    public String getHistoryWeather() throws IOException{
        URL url;
        String replacedUrl;
        if(locationName != null){
            replacedUrl = URL_HISTORY
                    .replace("{location}", locationNameTranslation())
                    .replace("{date}", date)
                    .replace("{hourOfDay}", time);
        }
        else{
            replacedUrl = URL_HISTORY
                    .replace("{location}", locationLatitude + "," + locationLongitude)
                    .replace("{date}", date)
                    .replace("{hourOfDay}", time);
        }
        url = new URL(replacedUrl);

        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        try(BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()))){
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while((line = bufferedReader.readLine()) != null){
                stringBuilder.append(line);
            }
            return stringBuilder.toString();
        }
    }
}