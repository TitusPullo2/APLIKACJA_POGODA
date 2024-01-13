package com.example.aplikacjapogoda;

/**
 * Interfejs do asynchronicznego odbierania współrzędnych GPS
 */
public interface LocationCallback{
    /**
     * Metoda wywoływana po otrzymaniu współrzędnych GPS
     * @param locationCoordinates tablica z współrzędnymi GPS
     */
    void onLocationReceived(String[] locationCoordinates);
}