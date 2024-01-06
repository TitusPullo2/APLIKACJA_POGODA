package com.example.aplikacjapogoda;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.RemoteViews;

import java.util.ArrayList;

public class WeatherWidget extends AppWidgetProvider{
    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId){
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.weather_widget);

        SharedPreferences sharedPreferences = context.getSharedPreferences("WeatherData", Context.MODE_PRIVATE);
        ArrayList<String> weatherData = new ArrayList<>();

        for(int i = 0; i < 13; i++){
            weatherData.add(sharedPreferences.getString(String.valueOf(i), ""));
        }

        views.setTextViewText(R.id.locationTextViewWidget, weatherData.get(0));
        views.setTextViewText(R.id.dateTextViewWidget, weatherData.get(1));
        views.setTextViewText(R.id.timeTextViewWidget, weatherData.get(2));
        views.setTextViewText(R.id.temperatureTextViewWidget, weatherData.get(3) + "°C");
        views.setTextViewText(R.id.humidityTextViewWidget, weatherData.get(4) + "%");
        views.setTextViewText(R.id.precipitationTextViewWidget, weatherData.get(5) + " mm");
        views.setTextViewText(R.id.windSpeedTextViewWidget, weatherData.get(6) + " km/h");
        views.setTextViewText(R.id.windDirectionViewWidget, weatherData.get(7));
        views.setTextViewText(R.id.pressureTextViewWidget, weatherData.get(8) + " hPa");
        views.setTextViewText(R.id.indexUvTextViewWidget, "UV: " + weatherData.get(9));
        views.setTextViewText(R.id.cloudyTextViewWidget, weatherData.get(10) + "%");
        views.setTextViewText(R.id.typeWeatherTextViewWidget, weatherData.get(11));
        views.setImageViewResource(R.id.weatherIconWidget, context.getResources().getIdentifier(weatherData.get(12), "drawable", context.getPackageName()));

        appWidgetManager.updateAppWidget(appWidgetId, views);
    }
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds){
        for(int appWidgetId : appWidgetIds){
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }
    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        if (AppWidgetManager.ACTION_APPWIDGET_UPDATE.equals(intent.getAction())) {
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(context, WeatherWidget.class));
            for (int appWidgetId : appWidgetIds) {
                updateAppWidget(context, appWidgetManager, appWidgetId);
            }
        }
    }
    @Override
    public void onEnabled(Context context){
        // Enter relevant functionality for when the first widget is created
    }
    @Override
    public void onDisabled(Context context){
        // Enter relevant functionality for when the last widget is disabled
    }
}