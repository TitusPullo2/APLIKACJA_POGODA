package com.example.aplikacjapogoda;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Klasa odpowiedzialna za przetwarzanie danych wprowadzonych przez użytkownika i odpowiedzi z API.
 */
public class UserInteraction{
    /**
     * Dane wprowadzone przez użytkownika.
     */
    private String time;
    private String date;
    private String locationName;
    private String locationLatitude;
    private String locationLongitude;

    /**
     * Konstruktor klasy UserInteraction.
     * @param time Czas.
     * @param date Data.
     * @param locationName Nazwa lokalizacji.
     * @param locationLatitude Szerokość geograficzna.
     * @param locationLongitude Długość geograficzna.
     */
    public UserInteraction(String time, String date, String locationName, String locationLatitude, String locationLongitude){
        this.time = time;
        this.date = date;
        this.locationName = locationName;
        this.locationLatitude = locationLatitude;
        this.locationLongitude = locationLongitude;
    }
    /**
     * Metoda odpowiedzialna za pobranie danych pogodowych.
     * Na podstawie danych wprowadzonych przez użytkownika, wywoływana jest odpowiednia metoda z klasy ApiCommunication.
     * @return Dane pogodowe w postaci listy.
     */
    public ArrayList<String> getWeatherData() {
        ArrayList<String> weatherExtractedData = new ArrayList<>();
        ApiCommunication apiCommunication;

        try{
            if(time.isEmpty() && date.isEmpty()){
                apiCommunication = locationName.isEmpty()? new ApiCommunication(locationLatitude, locationLongitude): new ApiCommunication(locationName);
                weatherExtractedData = extractCurrentWeatherData(apiCommunication.getCurrentWeather());
            }
            else{
                LocalDate localDate = LocalDate.now();
                LocalDate targetDate = LocalDate.parse(date);
                apiCommunication = locationName.isEmpty()? new ApiCommunication(locationLatitude, locationLongitude, date, time): new ApiCommunication(locationName, date, time);
                String weatherDirectData = localDate.isBefore(targetDate)? apiCommunication.getForecastWeather(): apiCommunication.getHistoryWeather();
                weatherExtractedData = extractOtherWeatherData(weatherDirectData);
            }
        }
        catch (IOException | JSONException e){
            e.printStackTrace();
        }
        return weatherExtractedData;
    }
    /**
     * Metoda odpowiedzialna za ektrakcję danych pogodowych aktualnych z odpowiedzi API.
     * @param weatherData Dane pogodowe w postaci Stringa.
     * @return Dane pogodowe w postaci listy.
     * @throws JSONException Wyjątek rzucany w przypadku błędu podczas parsowania danych JSON.
     */
    private ArrayList<String> extractCurrentWeatherData(String weatherData) throws JSONException{
        JSONObject jsonObject = new JSONObject(weatherData);

        JSONObject locationObject = jsonObject.getJSONObject("location");
        String name = locationObject.getString("name");
        String localTime = locationObject.getString("localtime");

        JSONObject currentObject = jsonObject.getJSONObject("current");
        String temperature = currentObject.getString("temp_c");
        String windKph = String.valueOf(currentObject.getDouble("wind_kph"));
        String windDir = currentObject.getString("wind_dir");
        String pressureMb = String.valueOf(currentObject.getDouble("pressure_mb"));
        String precipiceMm = String.valueOf(currentObject.getDouble("precip_mm"));
        String humidity = String.valueOf(currentObject.getInt("humidity"));
        String uv = String.valueOf(currentObject.getInt("cloud"));
        String cloud = String.valueOf(currentObject.getDouble("uv"));
        int isDay = currentObject.getInt("is_day");

        JSONObject conditionObject = currentObject.getJSONObject("condition");
        String text = conditionObject.getString("text");
        int code = conditionObject.getInt("code");

        String dateLocal = localTime.substring(0, 10);
        String timeLocal = localTime.substring(11, 16);

        String icon = setWeatherIcon(isDay, code);

        return new ArrayList<>(Arrays.asList(name, dateLocal, timeLocal, temperature, humidity, precipiceMm, windKph, windDir, pressureMb, cloud, uv, text, icon));
    }
    /**
     * Metoda odpowiedzialna za ektrakcję danych pogodowych prognozowanych lub historycznych z odpowiedzi API.
     * @param weatherData Dane pogodowe w postaci Stringa.
     * @return Dane pogodowe w postaci listy.
     * @throws JSONException Wyjątek rzucany w przypadku błędu podczas parsowania danych JSON.
     */
    private ArrayList<String> extractOtherWeatherData(String weatherData) throws JSONException{
        JSONObject jsonObject = new JSONObject(weatherData);

        JSONObject locationObject = jsonObject.getJSONObject("location");
        String name = locationObject.getString("name");

        JSONObject forecastObject = jsonObject.getJSONObject("forecast");
        JSONArray dateTimeArray = forecastObject.getJSONArray("forecastday").getJSONObject(0).getJSONArray("hour");

        JSONObject currentObject = dateTimeArray.getJSONObject(0);
        String localTime = currentObject.getString("time");
        String temperature = currentObject.getString("temp_c");
        String windKph = String.valueOf(currentObject.getDouble("wind_kph"));
        String windDir = currentObject.getString("wind_dir");
        String pressureMb = String.valueOf(currentObject.getDouble("pressure_mb"));
        String precipiceMm = String.valueOf(currentObject.getDouble("precip_mm"));
        String humidity = String.valueOf(currentObject.getInt("humidity"));
        String uv = String.valueOf(currentObject.getInt("cloud"));
        String cloud = String.valueOf(currentObject.getDouble("uv"));
        int isDay = currentObject.getInt("is_day");

        JSONObject conditionObject = currentObject.getJSONObject("condition");
        String text = conditionObject.getString("text");
        int code = conditionObject.getInt("code");

        String dateLocal = localTime.substring(0, 10);
        String timeLocal = localTime.substring(11, 16);

        String icon = setWeatherIcon(isDay, code);

        return new ArrayList<>(Arrays.asList(name, dateLocal, timeLocal, temperature, humidity, precipiceMm, windKph, windDir, pressureMb, cloud, uv, text, icon));
    }
    /**
     * Metoda odpowiedzialna za ustawienie ikony pogodowej.
     * @param isDay Czy dzień.
     * @param code Kod pogodowy.
     * @return Nazwa ikony pogodowej.
     */
    private String setWeatherIcon(int isDay, int code){
        String weatherIcon = "";

        if(isDay == 1){
            switch(code){
                case 1000:
                    weatherIcon = "clear_day";
                    break;
                case 1003:
                    weatherIcon = "cloudy_day";
                    break;
                case 1030:
                case 1135:
                    weatherIcon = "fog_day";
                    break;
                case 1063:
                case 1180:
                case 1183:
                case 1186:
                case 1189:
                case 1240:
                    weatherIcon = "rainy_day";
                    break;
                case 1066:
                case 1114:
                case 1210:
                case 1213:
                case 1216:
                case 1255:
                    weatherIcon = "snowy_day";
                    break;
                case 1147:
                    weatherIcon = "frost_day";
                    break;
                default:
            }
        }
        else{
            switch(code){
                case 1000:
                    weatherIcon = "clear_night";
                    break;
                case 1003:
                    weatherIcon = "cloudy_night";
                    break;
                case 1030:
                case 1135:
                    weatherIcon = "fog_night";
                    break;
                case 1063:
                case 1180:
                case 1183:
                case 1186:
                case 1189:
                case 1240:
                    weatherIcon = "rainy_night";
                    break;
                case 1066:
                case 1114:
                case 1210:
                case 1213:
                case 1216:
                case 1255:
                    weatherIcon = "snowy_night";
                    break;
                case 1147:
                    weatherIcon = "frost_night";
                    break;
                default:
            }
        }
        switch(code){
            case 1117:
                weatherIcon = "blizzard";
                break;
            case 1087:
                weatherIcon = "thunder";
                break;
            case 1273:
            case 1276:
            case 1279:
            case 1282:
                weatherIcon = "storm";
                break;
            case 1069:
            case 1204:
            case 1207:
            case 1249:
            case 1252:
                weatherIcon = "rain_snow";
                break;
            case 1006:
            case 1009:
                weatherIcon = "cloudy";
                break;
            case 1072:
            case 1150:
            case 1153:
            case 1168:
            case 1171:
                weatherIcon = "drizzle";
                break;
            case 1219:
            case 1222:
            case 1225:
            case 1258:
                weatherIcon = "snowy";
                break;
            case 1189:
            case 1192:
            case 1195:
            case 1201:
            case 1243:
            case 1246:
                weatherIcon = "rainy";
                break;
            case 1237:
            case 1261:
            case 1264:
                weatherIcon = "hail";
                break;
            default:
        }
        return weatherIcon;
    }
}