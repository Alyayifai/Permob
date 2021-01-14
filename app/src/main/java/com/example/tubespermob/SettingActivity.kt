package com.example.tubespermob

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.setting.*


class SettingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.setting)

        logout.setOnClickListener {
            val dialogBuilder = AlertDialog.Builder(this@SettingActivity)
            dialogBuilder.setMessage("Do you really want to logout?")
                .setCancelable(false)
                .setPositiveButton("Yes", DialogInterface.OnClickListener{
                    dialog, id -> logoutFunction()
                })
                .setNegativeButton("No", DialogInterface.OnClickListener{
                    dialog, id -> dialog.cancel()
                })
            val alert = dialogBuilder.create()
            alert.setTitle("Logout?")
            alert.show()
        }
    }

    private fun logoutFunction() {
        // CLEAR DATA
        val prefs = getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)
        val editor = prefs.edit()

        editor.clear()
        editor.apply()

        // GO TO MAIN
        val intent = Intent(this@SettingActivity, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }
}
