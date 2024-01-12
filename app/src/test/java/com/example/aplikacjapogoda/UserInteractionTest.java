package com.example.aplikacjapogoda;

import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.ArrayList;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE)
public class UserInteractionTest{
    @Test
    public void getWeatherDataReturnsCurrentWeatherWhenLocationNameProvided(){
        UserInteraction userInteraction = new UserInteraction("", "", "Warszawa", "", "");
        ArrayList<String> result = userInteraction.getWeatherData();
        assertNotNull(result);
    }
    @Test
    public void getWeatherDataReturnsCurrentWeatherWhenCoordinatesProvided(){
        UserInteraction userInteraction = new UserInteraction("", "", "", "52.2297", "21.0122");
        ArrayList<String> result = userInteraction.getWeatherData();
        assertNotNull(result);
    }
    @Test
    public void getWeatherDataReturnsCurrentWeatherWhenInvalidLocationNameProvided(){
        UserInteraction userInteraction = new UserInteraction("", "", "InvalidLocation", "", "");
        ArrayList<String> result = userInteraction.getWeatherData();
        assertNotNull(result);
    }
    @Test
    public void getWeatherDataReturnsCurrentWeatherWhenInvalidCoordinatesProvided(){
        UserInteraction userInteraction = new UserInteraction("", "", "", "InvalidLatitude", "InvalidLongitude");
        ArrayList<String> result = userInteraction.getWeatherData();
        assertNotNull(result);
    }
    @Test
    public void getWeatherDataReturnsHistoricalWeatherWhenLocationNameProvided(){
        UserInteraction userInteraction = new UserInteraction("12", "2024-01-11", "Warszawa", "", "");
        ArrayList<String> result = userInteraction.getWeatherData();
        assertNotNull(result);
    }
    @Test
    public void getWeatherDataReturnsHistoricalWeatherWhenCoordinatesProvided(){
        UserInteraction userInteraction = new UserInteraction("12", "2024-01-11", "", "52.2297", "21.0122");
        ArrayList<String> result = userInteraction.getWeatherData();
        assertNotNull(result);
    }
    @Test
    public void getWeatherDataReturnsHistoricalWeatherWhenInvalidLocationNameProvided(){
        UserInteraction userInteraction = new UserInteraction("12", "2024-01-11", "InvalidLocation", "", "");
        ArrayList<String> result = userInteraction.getWeatherData();
        assertNotNull(result);
    }
    @Test
    public void getWeatherDataReturnsHistoricalWeatherWhenInvalidCoordinatesProvided(){
        UserInteraction userInteraction = new UserInteraction("12", "2024-01-11", "", "InvalidLatitude", "InvalidLongitude");
        ArrayList<String> result = userInteraction.getWeatherData();
        assertNotNull(result);
    }
    @Test
    public void getWeatherDataReturnsForecastWeatherWhenLocationNameProvided(){
        UserInteraction userInteraction = new UserInteraction("12", "2024-01-17", "Warszawa", "", "");
        ArrayList<String> result = userInteraction.getWeatherData();
        assertNotNull(result);
    }
    @Test
    public void getWeatherDataReturnsForecastWeatherWhenCoordinatesProvided(){
        UserInteraction userInteraction = new UserInteraction("12", "2024-01-17", "", "52.2297", "21.0122");
        ArrayList<String> result = userInteraction.getWeatherData();
        assertNotNull(result);
    }
    @Test
    public void getWeatherDataReturnsForecastWeatherWhenInvalidLocationNameProvided(){
        UserInteraction userInteraction = new UserInteraction("12", "2024-01-17", "InvalidLocation", "", "");
        ArrayList<String> result = userInteraction.getWeatherData();
        assertNotNull(result);
    }
    @Test
    public void getWeatherDataReturnsForecastWeatherWhenInvalidCoordinatesProvided(){
        UserInteraction userInteraction = new UserInteraction("12", "2024-01-17", "", "InvalidLatitude", "InvalidLongitude");
        ArrayList<String> result = userInteraction.getWeatherData();
        assertNotNull(result);
    }
}