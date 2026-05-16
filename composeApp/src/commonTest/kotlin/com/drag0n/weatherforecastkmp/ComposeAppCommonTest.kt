package com.drag0n.weatherforecastkmp

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest

class ComposeAppCommonTest {

    @Test
    fun testSimpleKmpLogic() {
        val cityName = "Moscow"
        
        // Проверяем простейшую логику работы со строками, которая одинакова и для iOS, и для Android
        assertEquals(6, cityName.length)
    }
}
