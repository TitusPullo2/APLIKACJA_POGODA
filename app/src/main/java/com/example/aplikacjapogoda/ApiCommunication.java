package com.example.aplikacjapogoda;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.time.LocalDate;
import java.util.HashMap;

import javax.net.ssl.HttpsURLConnection;

/**
 * Klasa do komunikacji z WeatherAPI.
 * Zawiera metody do pobierania danych w formacie json o aktualnej pogodzie, prognozie pogody oraz historii pogody.
 */
public class ApiCommunication{
    /**
     * Adresy URL do pobierania danych o aktualnej pogodzie, prognozie pogody oraz historii pogody.
     */
    private final String URL_CURRENT = "https://api.weatherapi.com/v1/current.json?key=8b87ed58de2945a6876205648232812&q={location}&lang=pl&aqi=no";
    private final String URL_FORECAST = "https://api.weatherapi.com/v1/forecast.json?key=8b87ed58de2945a6876205648232812&q={location}&days={numberOfDay}&dt={dateOfDay}&hour={hourOfDay}&lang=pl&aqi=no&alerts=no";
    private final String URL_HISTORY = "https://api.weatherapi.com/v1/history.json?key=8b87ed58de2945a6876205648232812&q={location}&dt={date}&hour={hourOfDay}&lang=pl&aqi=no";
    /**
     * Dane do komunikacji z WeatherAPI.
     */
    private String locationName;
    private String locationLatitude;
    private String locationLongitude;
    private String date;
    private String time;

    /**
     * Konstruktor klasy ApiCommunication.
     * Dla pobierania danych o aktualnej pogodzie dla podanej lokalizacji.
     * @param locationName Nazwa lokalizacji.
     */
    public ApiCommunication(String locationName){
        this.locationName = locationName;
    }
    /**
     * Konstruktor klasy ApiCommunication.
     * Dla pobierania danych o aktualnej pogodzie dla podachnych współrzędnych geograficznych.
     * @param locationLatitude Szerokość geograficzna lokalizacji.
     * @param locationLongitude Długość geograficzna lokalizacji.
     */
    public ApiCommunication(String locationLatitude, String locationLongitude){
        this.locationLatitude = locationLatitude;
        this.locationLongitude = locationLongitude;
    }
    /**
     * Konstruktor klasy ApiCommunication.
     * Dla pobierania danych o prognozie pogody lub historii pogody dla podanej lokalizacji.
     * @param locationName Nazwa lokalizacji.
     * @param date Data.
     * @param time Godzina.
     */
    public ApiCommunication(String locationName, String date, String time){
        this.locationName = locationName;
        this.date = date;
        this.time = time;
    }
    /**
     * Konstruktor klasy ApiCommunication.
     * Dla pobierania danych o prognozie pogody lub historii pogody dla podanych współrzędnych geograficznych.
     * @param locationLatitude Szerokość geograficzna lokalizacji.
     * @param locationLongitude Długość geograficzna lokalizacji.
     * @param date Data.
     * @param time Godzina.
     */
    public ApiCommunication(String locationLatitude, String locationLongitude, String date, String time){
        this.locationLatitude = locationLatitude;
        this.locationLongitude = locationLongitude;
        this.date = date;
        this.time = time;
    }
    /**
     * Metoda do obliczania liczby dni między dniem aktualnym a dniem podanym w parametrze.
     * Wymagany przez WeatherAPI do pobierania danych o prognozie pogody.
     * @return Liczba dni.
     */
    private String numberOfDays(){
        LocalDate localDate = LocalDate.now();
        LocalDate targetDate = LocalDate.parse(this.date);
        int numberOfDays = targetDate.getDayOfYear() - localDate.getDayOfYear();
        return String.valueOf(numberOfDays);
    }
    /**
     * Metoda do tłumaczenia nazwy lokalizacji na język angielski.
     * Wymagany przez WeatherAPI do pobierania danych o aktualnej pogodzie, prognozie pogody oraz historii pogody.
     * @return Nazwa lokalizacji w języku angielskim.
     */
    private String locationNameTranslation(){
        String originalLocationName = this.locationName;
        String translatedLocationName;
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
    /**
     * Metoda do pobierania danych o aktualnej pogodzie.
     * @return Dane o aktualnej pogodzie w postaci String.
     * @throws IOException Wyjątek rzucany w przypadku błędu połączenia z serwerem.
     */
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
    /**
     * Metoda do pobierania danych o prognozie pogody.
     * @return Dane o prognozie pogody w postaci String.
     * @throws IOException Wyjątek rzucany w przypadku błędu połączenia z serwerem.
     */
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
    /**
     * Metoda do pobierania danych o historii pogody.
     * @return Dane o historii pogody w postaci String.
     * @throws IOException Wyjątek rzucany w przypadku błędu połączenia z serwerem.
     */
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