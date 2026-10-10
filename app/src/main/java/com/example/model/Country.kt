package com.example.model

/**
 * Continent classification
 */
enum class Continent(val displayName: String, val arabicName: String) {
    AFRICA("Africa", "أفريقيا"),
    ASIA("Asia", "آسيا"),
    EUROPE("Europe", "أوروبا"),
    NORTH_AMERICA("North America", "أمريكا الشمالية"),
    SOUTH_AMERICA("South America", "أمريكا الجنوبية"),
    OCEANIA("Oceania", "أوقيانوسيا")
}

/**
 * Primary strategic resource endowment
 */
data class StrategicResources(
    val energyOilGas: Double,       // 0..100 (Energy reserves)
    val mineralsMetals: Double,     // 0..100 (Raw industrial inputs)
    val agricultureFood: Double,    // 0..100 (Food self-sufficiency)
    val industrialCapacity: Double, // 0..100 (Factories & assembly)
    val technologyChips: Double     // 0..100 (Semiconductors & tech)
)

/**
 * Country data model for each of the 200 nations in the simulation.
 */
data class Country(
    val id: String,                 // ISO 3-letter code or unique identifier (e.g. USA, CHN, EGY)
    val name: String,               // Display name (English/Universal)
    val nameArabic: String,         // Display name (Arabic)
    val flagEmoji: String,          // National flag emoji
    val continent: Continent,       // Continent
    val capital: String,            // Capital city
    val capitalArabic: String,      // Capital city in Arabic
    val populationMillions: Double, // Population in millions (e.g. 335.0)
    val gdpBillionUsd: Double,      // Nominal GDP in Billion USD
    val currencyName: String,       // Currency name (e.g. US Dollar)
    val currencyCode: String,       // Currency symbol/code (e.g. USD)
    val govtBudgetBillionUsd: Double,// Annual state budget
    val militaryRating: Int,        // 1..100 composite military strength
    val stabilityIndex: Int,        // 1..100 political stability
    val techLevel: Int,             // 1..10 tech tier
    val mapLatitude: Float,         // Coordinates for map projection (-90 to +90)
    val mapLongitude: Float,        // Coordinates for map projection (-180 to +180)
    val resources: StrategicResources,
    val isEstimatedData: Boolean = false // Marker for transparency on estimated microstate figures
)
