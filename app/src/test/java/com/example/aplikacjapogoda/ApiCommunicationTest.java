package com.example.aplikacjapogoda;

import static org.junit.Assert.*;
import org.junit.Test;

import java.io.IOException;
import java.time.format.DateTimeParseException;

public class ApiCommunicationTest{
    @Test
    public void getCurrentWeatherReturnsWeatherDataWhenLocationNameProvided() throws IOException{
        ApiCommunication apiCommunication = new ApiCommunication("Warszawa");
        String result = apiCommunication.getCurrentWeather();
        assertNotNull(result);
    }
    @Test
    public void getCurrentWeatherReturnsWeatherDataWhenCoordinatesProvided() throws IOException{
        ApiCommunication apiCommunication = new ApiCommunication("52.2297", "21.0122");
        String result = apiCommunication.getCurrentWeather();
        assertNotNull(result);
    }
    @Test
    public void getCurrentWeatherThrowsIOExceptionWhenInvalidLocationNameProvided(){
        ApiCommunication apiCommunication = new ApiCommunication("InvalidLocation");
        assertThrows(IOException.class, apiCommunication::getCurrentWeather);
    }
    @Test
    public void getCurrentWeatherThrowsIOExceptionWhenInvalidCoordinatesProvided(){
        ApiCommunication apiCommunication = new ApiCommunication("InvalidLatitude", "InvalidLongitude");
        assertThrows(IOException.class, apiCommunication::getCurrentWeather);
    }
    @Test
    public void getForecastWeatherReturnsWeatherDataWhenLocationNameAndDateTimeProvided() throws IOException{
        ApiCommunication apiCommunication = new ApiCommunication("Warszawa", "2024-01-17", "12");
        String result = apiCommunication.getForecastWeather();
        assertNotNull(result);
    }
    @Test
    public void getForecastWeatherReturnsWeatherDataWhenCoordinatesAndDateTimeProvided() throws IOException{
        ApiCommunication apiCommunication = new ApiCommunication("52.2297", "21.0122", "2024-01-17", "12");
        String result = apiCommunication.getForecastWeather();
        assertNotNull(result);
    }
    @Test
    public void getForecastWeatherThrowsIOExceptionWhenInvalidLocationNameAndDateTimeProvided(){
        ApiCommunication apiCommunication = new ApiCommunication("InvalidLocation", "2024-01-17", "12");
        assertThrows(IOException.class, apiCommunication::getForecastWeather);
    }
    @Test
    public void getForecastWeatherThrowsIOExceptionWhenInvalidCoordinatesAndDateTimeProvided(){
        ApiCommunication apiCommunication = new ApiCommunication("InvalidLatitude", "InvalidLongitude", "2024-01-17", "12");
        assertThrows(IOException.class, apiCommunication::getForecastWeather);
    }
    @Test
    public void getForecastWeatherThrowsDateTimeParseExceptionWhenLocationNameAndInvalidDateTimeProvided(){
        ApiCommunication apiCommunication = new ApiCommunication("Warszawa", "InvalidDate", "InvalidTime");
        assertThrows(DateTimeParseException.class, apiCommunication::getForecastWeather);
    }
    @Test
    public void getForecastWeatherThrowsDateTimeParseExceptionWhenCoordinatesAndInvalidDateTimeProvided(){
        ApiCommunication apiCommunication = new ApiCommunication("52.2297", "21.0122", "InvalidDate", "InvalidTime");
        assertThrows(DateTimeParseException.class, apiCommunication::getForecastWeather);
    }
    @Test
    public void getHistoryWeatherReturnsWeatherDataWhenLocationNameAndDateTimeProvided() throws IOException{
        ApiCommunication apiCommunication = new ApiCommunication("Warszawa", "2024-01-11", "12");
        String result = apiCommunication.getHistoryWeather();
        assertNotNull(result);
    }
    @Test
    public void getHistoryWeatherReturnsWeatherDataWhenCoordinatesAndDateTimeProvided() throws IOException{
        ApiCommunication apiCommunication = new ApiCommunication("52.2297", "21.0122", "2024-01-11", "12");
        String result = apiCommunication.getHistoryWeather();
        assertNotNull(result);
    }
    @Test
    public void getHistoryWeatherThrowsIOExceptionWhenInvalidLocationNameAndDateTimeProvided(){
        ApiCommunication apiCommunication = new ApiCommunication("InvalidLocation", "2024-01-11", "12");
        assertThrows(IOException.class, apiCommunication::getHistoryWeather);
    }
    @Test
    public void getHistoryWeatherThrowsIOExceptionWhenInvalidCoordinatesAndDateTimeProvided(){
        ApiCommunication apiCommunication = new ApiCommunication("InvalidLatitude", "InvalidLongitude", "2024-01-11", "12");
        assertThrows(IOException.class, apiCommunication::getHistoryWeather);
    }
    @Test
    public void getHistoryWeatherThrowsIOExceptionWhenLocationNameAndInvalidDateTimeProvided(){
        ApiCommunication apiCommunication = new ApiCommunication("Warszawa", "InvalidDate", "InvalidTime");
        assertThrows(IOException.class, apiCommunication::getHistoryWeather);
    }
    @Test
    public void getHistoryWeatherThrowsIOExceptionWhenCoordinatesAndInvalidDateTimeProvided(){
        ApiCommunication apiCommunication = new ApiCommunication("52.2297", "21.0122", "InvalidDate", "InvalidTime");
        assertThrows(IOException.class, apiCommunication::getHistoryWeather);
    }
}