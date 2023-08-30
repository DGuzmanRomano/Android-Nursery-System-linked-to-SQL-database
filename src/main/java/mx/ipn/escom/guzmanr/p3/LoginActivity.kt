package mx.ipn.escom.guzmanr.p3

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import retrofit2.Call
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Response
import retrofit2.Callback


class LoginActivity : AppCompatActivity() {

    private lateinit var edit_text_password: EditText
    private lateinit var edit_text_username: EditText
    private lateinit var button_login: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_login)

        edit_text_password = findViewById(R.id.edit_text_password)
        edit_text_username = findViewById(R.id.edit_text_username)
        button_login = findViewById(R.id.button_login)


        button_login.setOnClickListener {
            val username = edit_text_username.text.toString()
            val password = edit_text_password.text.toString()



            val apiService = RetrofitClient.instance()
            val call = apiService.login(mapOf("username" to username, "password" to password))
            call.enqueue(object : Callback<Map<String, Any>> {
                override fun onResponse(call: Call<Map<String, Any>>, response: Response<Map<String, Any>>) {
                    if (response.isSuccessful) {
                        val responseBody = response.body()
                        if (responseBody?.get("success") == true) {
                            val role = responseBody["role"] as String
                            val id = responseBody["id"] as Double
                            Log.d("LoginActivity", "database ParentID: $id")

                            if (role == "parent") {
                                val intent = Intent(this@LoginActivity, ParentActivity::class.java)
                                intent.putExtra("parentId", id.toInt())
                                intent.putExtra("role", role)
                                startActivity(intent)
                                finish()

                            } else if (role == "teacher") {
                                val intent = Intent(this@LoginActivity, TeacherActivity::class.java)
                                intent.putExtra("teacherId", id.toInt())
                                intent.putExtra("role", role)
                                startActivity(intent)
                            }
                        } else {
                            Toast.makeText(this@LoginActivity, "Invalid username or password", Toast.LENGTH_SHORT).show()
                        }
                    }
                }

                override fun onFailure(call: Call<Map<String, Any>>, t: Throwable) {
                    Toast.makeText(this@LoginActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }
}
