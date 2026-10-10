package com.example

import com.example.data.WorldDataCatalog
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class WorldCatalogTest {

    @Test
    fun testAllCountriesCountIsExactly200() {
        val countries = WorldDataCatalog.allCountries
        assertEquals("The world database must contain exactly 200 countries", 200, countries.size)
    }

    @Test
    fun testCountryIdsAreUnique() {
        val countries = WorldDataCatalog.allCountries
        val uniqueIds = countries.map { it.id }.toSet()
        assertEquals("All 200 country IDs must be unique", 200, uniqueIds.size)
    }

    @Test
    fun testEveryCountryHasValidAttributes() {
        for (c in WorldDataCatalog.allCountries) {
            assertTrue("Country ID cannot be blank: ${c.name}", c.id.isNotBlank())
            assertTrue("Country name cannot be blank: ${c.id}", c.name.isNotBlank())
            assertTrue("Capital cannot be blank: ${c.id}", c.capital.isNotBlank())
            assertTrue("Population must be positive: ${c.id}", c.populationMillions > 0)
            assertTrue("GDP must be positive: ${c.id}", c.gdpBillionUsd > 0)
            assertTrue("Latitude within bounds: ${c.id}", c.mapLatitude in -90f..90f)
            assertTrue("Longitude within bounds: ${c.id}", c.mapLongitude in -180f..180f)
        }
    }
}
