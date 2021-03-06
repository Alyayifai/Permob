package com.example.tubespermob

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.tubespermob.api.SignIn
import kotlinx.android.synthetic.main.sign_in.*
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.sign_in);

        // GET REFERENCE TO ALL VIEW
        var email = findViewById(R.id.editText) as EditText
        var password = findViewById(R.id.editText1) as EditText
        var buttonSubmit = findViewById(R.id.btn_signIn) as Button

        signUpDirect.setOnClickListener {
            startActivity(Intent(this@MainActivity, RegisterActivity::class.java))
        }

        buttonSubmit.setOnClickListener {
            val emailValue = email.text.toString().trim()
            val passwordValue = password.text.toString().trim()

            if(emailValue.isEmpty()) {
                email.requestFocus()
                Toast.makeText(this@MainActivity, "Email Required", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            } else if(passwordValue.isEmpty()){
                password.requestFocus()
                Toast.makeText(this@MainActivity, "Password Required", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            else {
                makeApiRequest(email = emailValue, password = passwordValue)
            }
//            Toast.makeText(this@MainActivity, email, Toast.LENGTH_LONG).show()
        }
    }

    private fun makeApiRequest(email: String, password: String) {
        val api = Retrofit.Builder()
            .baseUrl("https://reqres.in")
            .addConverterFactory(GsonConverterFactory.create())
            .client(OkHttpClient.Builder().build())
            .build()
            .create(APIRequest::class.java)



        api.loginUser(email = email, password = password)
            .enqueue(object : Callback<SignIn> {
                override fun onFailure(call: Call<SignIn>, t: Throwable) {
                    Toast.makeText(applicationContext, t.message, Toast.LENGTH_LONG).show()
                }

                override fun onResponse(call: Call<SignIn>, response: Response<SignIn>) {
                    if(response.code() == 200) {
                        // SAVE DATA
                        val prefs = getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)
                        val editor = prefs.edit()
                        editor.apply {
                            putBoolean("isLoggedIn", true)
                            putString("token", response.body()?.token)
                        }.apply()

                        // GO TO HOME ACTIVITY
                        val intent = Intent(this@MainActivity, HomeActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)
                    } else {
                        Toast.makeText(applicationContext, "Email or Password WRONG!!", Toast.LENGTH_LONG).show()
                    }
                }

            })
    }

    override fun onStart() {
        super.onStart()

        val prefs = getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)
        val isLoggedIn = prefs.getBoolean("isLoggedIn", false)

        if(isLoggedIn) {
            val intent = Intent(this@MainActivity, HomeActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()

        finish()
    }
}