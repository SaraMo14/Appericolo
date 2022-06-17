package com.example.appericolo

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.appericolo.databinding.ActivityInfoFakeCallBinding
import java.util.*


class InfoFakeCallActivity : AppCompatActivity() {
    private lateinit var binding: ActivityInfoFakeCallBinding
    private lateinit var calendar: Calendar

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityInfoFakeCallBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.scheduleCallButton.isEnabled =false


        val mcurrentTime = Calendar.getInstance()
        calendar = Calendar.getInstance()

        /*val datePicker = DatePickerDialog(this,
            { _, year, month, dayOfMonth -> binding.callDate.setText(String.format("%d / %d / %d", dayOfMonth, month + 1, year))
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                calendar.set(Calendar.MONTH, month)
                calendar.set(Calendar.YEAR, year)
            }, mcurrentTime.get(Calendar.YEAR), mcurrentTime.get(Calendar.MONTH), mcurrentTime.get(Calendar.DAY_OF_MONTH))
        datePicker.datePicker.minDate= mcurrentTime.timeInMillis
        binding.selectDate.setOnClickListener{
            datePicker.show()
        }*/


        val timePicker = TimePickerDialog(this,
            { _, hourOfDay, minute -> binding.callTime.setText(String.format("%d : %d", hourOfDay, minute))
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                calendar.set(Calendar.MINUTE, minute)
            }, mcurrentTime.get(Calendar.HOUR_OF_DAY), mcurrentTime.get(Calendar.MINUTE), false)

        binding.selectTime.setOnClickListener{
            timePicker.show()
        }



        val watcher: TextWatcher = object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                //YOUR CODE
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                //YOUR CODE
            }

            override fun afterTextChanged(s: Editable) {

                binding.scheduleCallButton.isEnabled = inputValidation()
            }
        }

        binding.callName.addTextChangedListener(watcher)
        binding.callTime.addTextChangedListener(watcher)
        //binding.callDate.addTextChangedListener(watcher)


        binding.scheduleCallButton.setOnClickListener{
            //scheduleCall()
            val name = binding.callName.text.toString()
            val number= binding.callNumber.text.toString()
            //schedule notification
            NotificationUtils().setNotification(calendar.timeInMillis, this, name, number) /* notification time, */
            Toast.makeText(applicationContext, "Chiamata pianificata alle ore " + calendar.get(Calendar.HOUR_OF_DAY)+":"+calendar.get(Calendar.MINUTE), Toast.LENGTH_LONG).show()
            finish()
        }
    }

    private fun inputValidation(): Boolean{
        if((binding.callName.text.isEmpty()) or (binding.callNumber.text.isEmpty()) ){
            return false
        }
        return true
    }

    @RequiresApi(Build.VERSION_CODES.P)
    private fun scheduleCall(){
        //versione 1
        /*val intent = Intent(this, IncomingCallActivity::class.java)
        intent.putExtra("nominativo", binding.callName.text.toString())
        intent.putExtra("cellulare",  binding.callNumber.text.toString())
        val pendingIntent =PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT)
        val alarmManager = applicationContext.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
        */
        val intent = Intent(this, FakeCallReceiver::class.java)
        intent.putExtra("nominativo", binding.callName.text.toString())
        intent.putExtra("cellulare",  binding.callNumber.text.toString())
        intent.putExtra("orario",  "${calendar.get(Calendar.HOUR_OF_DAY)}:${calendar.get(Calendar.MINUTE)}")
        intent.action="com.tester.alarmmanager"
        val pendingIntent =PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_ONE_SHOT)
        val alarmManager = applicationContext.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
    }

    //metodi per creazione di Options Menu
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.delete_call_menu,menu)
        return super.onCreateOptionsMenu(menu)
    }
    //metodo che produce un dialog con le informazioni sulla call programmata se l'utente seleziona la relativa voce nel menu
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
            R.id.settings ->{


            val builder = AlertDialog.Builder(this)
            //verifica se ci sono chiamate programmate
                val alarmUp = PendingIntent.getBroadcast(this, 0,
                    Intent(this, FakeCallReceiver::class.java), PendingIntent.FLAG_NO_CREATE)
                val scheduled_time: String?
                if (alarmUp!=null) {
                    //intent esistente
                        scheduled_time = intent.getStringExtra("orario")
                    builder.setMessage("C'è una chiamata programmata. Eliminarla?")
                    //performing negative action
                    builder.setPositiveButton("No"){dialogInterface, which ->
                        Toast.makeText(applicationContext,"clicked No",Toast.LENGTH_LONG).show()
                    }
                    //performing cancel action
                    builder.setNeutralButton("Elimina") { dialogInterface, which ->
                        val notificationManager =
                            this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                        alarmUp.cancel()
                        notificationManager.cancel(HeadsUpNotificationService.mNotificationId)
                        Toast.makeText(applicationContext, "Chiamata cancellata", Toast.LENGTH_LONG)
                            .show()
                    }
                }else{
                    //intent non esistente
                    builder.setMessage("Nessuna chiamata programmata")
                    //performing closing action
                    builder.setNegativeButton("Chiudi"){ dialogInterface, _ ->
                }
            //set title for alert dialog
            builder.setTitle(R.string.dialogTitle)
            builder.setIcon(android.R.drawable.ic_dialog_dialer)
            }
                // Create the AlertDialog
                val alertDialog: AlertDialog = builder.create()
            // Set other dialog properties
            alertDialog.setCancelable(false)
                    alertDialog.show()
        }
    }
        return super.onOptionsItemSelected(item)
    }
}