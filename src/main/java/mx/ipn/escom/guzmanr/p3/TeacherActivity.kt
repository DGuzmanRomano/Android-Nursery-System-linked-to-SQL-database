package mx.ipn.escom.guzmanr.p3

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

class TeacherActivity : AppCompatActivity() {
    private lateinit var kidAdapter: KidAdapter
    private lateinit var recyclerView: RecyclerView
    private var teacherID: Int = 0
    private var role: String = "" // Declare role variable here

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_teacher)

        val announcementEditText: EditText = findViewById(R.id.announcementEditText)
        val postAnnouncementButton: Button = findViewById(R.id.postAnnouncementButton)

        postAnnouncementButton.setOnClickListener {
            val announcementText = announcementEditText.text.toString().trim()
            if(announcementText.isNotEmpty()) {
                postAnnouncement(announcementText)
            } else {
                Toast.makeText(this, "Please enter an announcement", Toast.LENGTH_SHORT).show()
            }
        }


        teacherID = intent.getIntExtra("teacherId", -1)
        role = intent.getStringExtra("role") ?: "" // Get the role here

        recyclerView = findViewById(R.id.recyclerView2)
        recyclerView.layoutManager = LinearLayoutManager(this)

        fetchKids()
    }

    private fun fetchKids() {
        val call = RetrofitClient.instance().getKids()
        call.enqueue(object: Callback<List<Kid>> {
            override fun onResponse(call: Call<List<Kid>>, response: Response<List<Kid>>) {
                if(response.isSuccessful && response.body() != null) {
                    val kidsList = response.body()!!

                    // Filter the kids list to include only kids with ParentID = 1
                    val filteredKidsList = kidsList.filter { it.GroupID == teacherID }

                    kidAdapter = KidAdapter(filteredKidsList, this@TeacherActivity, role)

                    recyclerView.adapter = kidAdapter
                }
            }

            override fun onFailure(call: Call<List<Kid>>, t: Throwable) {
                // Handle failure
            }
        })
    }

    private fun postAnnouncement(announcementText: String) {
        val announcement = Announcement(0, announcementText) // Here, you can ignore the ID as the server generates it.
        val call = RetrofitClient.instance().addAnnouncement(announcement)
        call.enqueue(object: Callback<Announcement> {
            override fun onResponse(call: Call<Announcement>, response: Response<Announcement>) {
                if(response.isSuccessful) {
                    Toast.makeText(applicationContext, "Announcement added successfully", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(applicationContext, "Failed to add announcement", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<Announcement>, t: Throwable) {
                Toast.makeText(applicationContext, "An error occurred: ${t.message}", Toast.LENGTH_LONG).show()
            }
        })
    }





}
