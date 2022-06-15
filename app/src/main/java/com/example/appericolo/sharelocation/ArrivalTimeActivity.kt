package com.example.appericolo.sharelocation

import android.app.TimePickerDialog
import android.app.TimePickerDialog.OnTimeSetListener
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.appericolo.MainActivity
import com.example.appericolo.databinding.ActivityArrivalTimeBinding
import com.example.appericolo.sharelocation.fcm.NotificationData
import com.example.appericolo.sharelocation.fcm.PushNotification
import com.example.appericolo.sharelocation.fcm.RetrofitInstance
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.auth.FirebaseAuth
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class ArrivalTimeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityArrivalTimeBinding
    //private lateinit var timeButton: Button
    private var hour: Int = 0
    private var minute: Int = 0



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityArrivalTimeBinding.inflate(layoutInflater)

        //binding.estimatedArrivalTime.setText(Calendar.getInstance().get(Calendar.HOUR_OF_DAY).toString() + ":" + Calendar.getInstance().get(Calendar.MINUTE).toString())
        setContentView(binding.root)

        val bundle = intent.getParcelableExtra<Bundle>("LocationsBundle")
        val destinazione: LatLng? = bundle?.getParcelable("destinazione")
        //val partenza: LatLng? = bundle?.getParcelable("partenza")
        binding.backwardFab2.setOnClickListener{
            finish()
        }
        binding.forwardFab2.setOnClickListener{
            val sendIntent = Intent(this, MainActivity::class.java)
            val args = Bundle()
            args.putParcelable("destinazione", destinazione)
            //args.putParcelable("partenza", partenza)
            args.putString("orario_arrivo", binding.estimatedArrivalTime.text.toString())
            sendIntent.putExtra("LocationsBundle", args)
            sendIntent.putExtra("code","start_live_location")

            //data
            val title = "Appericolo - " //+ FirebaseAuth.getInstance().currentUser!!
            val message = FirebaseAuth.getInstance().currentUser!!.displayName + " sta condividendo la sua posizione con te"

            // These registration tokens come from the client FCM SDKs.
            val recipientsTokens = listOf(//"dCAS80DATU6chvEJ8gXXEp:APA91bFiIRG1rKXDNBn52LV2YZj9wulHqRQD9IdVAtqo31XJ9XUyxpZPGDFkqMke8_bU8GtIudhIGu-MS7oSlGJ_cN2mzCTsNGNK0gBMT5uhGe9f3PWG8xPwCVs0ivplim_Z5Fxu4Fbv",
                "dzAwqTYnTTGIYGLBJ6kAMh:APA91bG45bZNeKRcAkW_mMNJ3-Q9lpR_ACFQ_wY3eP1xqEra5xpEUMTQlP9-iNV9fCROIm2YQ6eVrvej4KwC4F0jsKhBCU881vCimPs-MxUY9n0xgAsY-_zKdTUL8MLFIEVZsIru_jnB")

            if(title.isNotEmpty()&& message.isNotEmpty() && recipientsTokens.isNotEmpty()){

                //create topic and send notification
                    for(token in recipientsTokens){
                        PushNotification(
                            NotificationData(title, message, FirebaseAuth.getInstance().currentUser!!.uid),
                            token
                            //TOPIC
                        ).also{
                            sendNotification(it)
                        }
                    }

            }
            //sendIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            //sendIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            //sendIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            sendIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(sendIntent)

            //finish()
        }
    }

    //send notification request
    private fun sendNotification(notification: PushNotification) = CoroutineScope(Dispatchers.IO).launch {
        try{
            val response = RetrofitInstance.api.postNotification(notification)
            if(response.isSuccessful){
                Log.i("ArrivalTimeActivity", "Response: ${Gson().toJson(response)}")
            }else{
                Log.e("ArrivalTimeActivity", response.errorBody().toString())
            }
        }catch (e: Exception){
            Log.e("ArrivalTimeActivity", e.toString())
        }
    }


    fun popTimePicker(view: View?) {

        val onTimeSetListener =
            OnTimeSetListener { _, selectedHour, selectedMinute ->
                hour = selectedHour
                minute = selectedMinute
                binding.estimatedArrivalTime.text = java.lang.String.format(Locale.getDefault(),
                    "%02d:%02d",
                    hour,
                    minute)
            }

        // int style = AlertDialog.THEME_HOLO_DARK;
        val timePickerDialog =
            TimePickerDialog(this,  /*style,*/onTimeSetListener, hour, minute, true)
        timePickerDialog.setTitle("Select Time")
        timePickerDialog.show()
    }

}