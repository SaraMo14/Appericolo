package com.example.appericolo

import android.util.Log
import com.example.appericolo.sharelocation.LocationUpdatesReceiverActivity
import com.example.appericolo.utils.MapsUtil
import com.google.android.gms.maps.model.LatLng
import org.junit.After
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before
import java.util.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class UnitTest {



    /**
     * test del funzionamento del metodo di verifica se un orario è maggiore o uguale all'ora attuale
     */
    @Test
    fun isTimeExpiredTest() {
        val time = Calendar.getInstance()
         time.set(Calendar.HOUR, 10)

        if( Calendar.getInstance()[Calendar.HOUR_OF_DAY] < time.get(Calendar.HOUR)){
            assertFalse(MapsUtil.isArrivalTimeExpired("10:00"))
        }
        else{
            assertTrue(MapsUtil.isArrivalTimeExpired("10:00"))
      }

    }


    /**
     * test del funzionamento del metodo di verifica se l'utente si trova vicino alla destinazione o meno
     */
    @Test
    fun locationsDistanceTest() {
        val currentPosition_1 = LatLng(43.6162624,13.5163337) //Ancona - Ufficio postale
        val destination = LatLng(43.6161534,13.5171037) //Ancona - Caffe diana
        assertTrue(LocationUpdatesReceiverActivity.isCloseToDestination(currentPosition_1, destination))

    }

}