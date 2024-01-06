package com.example.aplikacjapogoda;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.widget.RemoteViews;
import android.widget.TextView;

public class WeatherWidget extends AppWidgetProvider{
    private TextView weatherIconWidget;
    private TextView typeWeatherTextViewWidget;
    private TextView locationTextViewWidget;
    private TextView dateTextViewWidget;
    private TextView timeTextViewWidget;
    private TextView temperatureTextViewWidget;
    private TextView humidityTextViewWidget;
    private TextView precipitationTextViewWidget;
    private TextView windSpeedTextViewWidget;
    private TextView windDirectionViewWidget;
    private TextView pressureTextViewWidget;
    private TextView indexUvTextViewWidget;
    private TextView cloudyTextViewWidget;

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId){
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.weather_widget);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds){
        // There may be multiple widgets active, so update all of them
        for(int appWidgetId : appWidgetIds){
            updateAppWidget(context, appWidgetManager, appWidgetId);
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