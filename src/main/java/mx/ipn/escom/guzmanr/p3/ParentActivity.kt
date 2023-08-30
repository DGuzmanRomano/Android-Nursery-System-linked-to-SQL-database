package mx.ipn.escom.guzmanr.p3

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.DatePicker
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*


var selectedDate: String = ""

class ParentActivity : AppCompatActivity() {
    private lateinit var kidAdapter: KidAdapter
    private lateinit var recyclerView: RecyclerView
    private var parentID: Int = 0
    private var role: String = ""



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_parent)

        fetchLastAnnouncement()

        parentID = intent.getIntExtra("parentId", -1)
        role = intent.getStringExtra("role") ?: "" // Get the role here

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)



        val datePicker: DatePicker = findViewById(R.id.datePicker)
        val confirmButton: Button = findViewById(R.id.confirmButton)
        confirmButton.setOnClickListener {
            val day: Int = datePicker.dayOfMonth
            val month: Int = datePicker.month + 1 // Months are indexed from 0.
            val year: Int = datePicker.year

            selectedDate = String.format("%04d-%02d-%02d", year, month, day)

            Toast.makeText(this, "Fecha Seleccionada", Toast.LENGTH_SHORT).show()
            Log.d("Parent", "Global: $selectedDate")

        }



        fetchKids()
    }

    private fun fetchKids() {
        val call = RetrofitClient.instance().getKids()
        call.enqueue(object: Callback<List<Kid>> {
            override fun onResponse(call: Call<List<Kid>>, response: Response<List<Kid>>) {
                if(response.isSuccessful && response.body() != null) {
                    val kidsList = response.body()!!

                    // Filter the kids list to include only kids with ParentID
                    val filteredKidsList = kidsList.filter { it.ParentID == parentID }

                    kidAdapter = KidAdapter(filteredKidsList, this@ParentActivity, role) // Pass the role to KidAdapter

                    recyclerView.adapter = kidAdapter
                }
            }

            override fun onFailure(call: Call<List<Kid>>, t: Throwable) {
                // Handle failure
            }
        })
    }


    private fun fetchLastAnnouncement() {
        val call = RetrofitClient.instance().getLastAnnouncement()
        call.enqueue(object : Callback<Announcement> {
            override fun onResponse(call: Call<Announcement>, response: Response<Announcement>) {
                if (response.isSuccessful) {
                    val announcement: Announcement? = response.body()
                    // Assuming tvAnnouncement is the id of your TextView
                    val textView: TextView = findViewById(R.id.tvAnnouncements)
                    textView.text = "Anuncios:  ${announcement?.Announcement}"
                } else {
                    // Handle the error
                }
            }

            override fun onFailure(call: Call<Announcement>, t: Throwable) {
                // Handle failure
            }
        })
    }



}
