package com.example.tubespermob

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.tubespermob.api.Register
import kotlinx.android.synthetic.main.signup.*
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.signup)

        var email = findViewById(R.id.email) as EditText
        var password = findViewById(R.id.pass) as EditText

        btn_signUp.setOnClickListener {
            val emailValue = email.text.toString().trim()
            val passwordValue = password.text.toString().trim()

            if(emailValue.isEmpty()) {
                email.requestFocus()
                Toast.makeText(this@RegisterActivity, "Email Required", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            } else if (passwordValue.isEmpty()) {
                password.requestFocus()
                Toast.makeText(this@RegisterActivity, "Password Required", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            } else {
                makeApiRequest(emailValue, passwordValue)
            }
        }
    }

    private fun makeApiRequest(emailValue: String, passwordValue: String) {
        val api = Retrofit.Builder()
            .baseUrl("https://reqres.in")
            .addConverterFactory(GsonConverterFactory.create())
            .client(OkHttpClient.Builder().build())
            .build()
            .create(APIRequest::class.java)

        api.createUser(email = emailValue, password = passwordValue)
            .enqueue(object : Callback<Register> {
                override fun onFailure(call: Call<Register>, t: Throwable) {
                    Toast.makeText(applicationContext, t.message, Toast.LENGTH_LONG).show()
                }

                override fun onResponse(call: Call<Register>, response: Response<Register>) {
                    if(response.code() == 200) {
                        // SAVE DATA
                        val prefs = getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)
                        val editor = prefs.edit()
                        editor.apply {
                            putBoolean("isLoggedIn", true)
                            putString("token", response.body()?.token)
                        }.apply()

                        // GO TO HOME ACTIVITY
                        val intent = Intent(this@RegisterActivity, HomeActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)
                    } else {
                        Toast.makeText(applicationContext, "Harap periksa email dan password Anda kembali!", Toast.LENGTH_LONG).show()
                    }
                }

            })
    }

    override fun onStart() {
        super.onStart()

        val prefs = getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)
        val isLoggedIn = prefs.getBoolean("isLoggedIn", false)

        if(isLoggedIn) {
            val intent = Intent(this@RegisterActivity, HomeActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
    }
}