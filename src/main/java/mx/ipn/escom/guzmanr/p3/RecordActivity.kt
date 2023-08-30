package mx.ipn.escom.guzmanr.p3

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import retrofit2.Call
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Response
import retrofit2.Callback
import java.text.SimpleDateFormat
import java.util.*


class RecordActivity : AppCompatActivity() {
    private lateinit var recordLayout: LinearLayout  // Define a LinearLayout for the records
    private lateinit var name: TextView
    private lateinit var fecha: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_record)
        recordLayout = findViewById(R.id.recordLayout)
        name = findViewById(R.id.tvNombre)
        fecha = findViewById(R.id.tvFecha)
        fetchRecords()
    }

    private fun fetchRecords() {
        val kidID = intent.getIntExtra("KidID", -1)
        val kidFullName = intent.getStringExtra("KidName")

        name.text = kidFullName


        val call = RetrofitClient.instance().getRecords()
        call.enqueue(object: Callback<List<Record>> {
            override fun onResponse(call: Call<List<Record>>, response: Response<List<Record>>) {
                if(response.isSuccessful && response.body() != null) {
                    val recordsList = response.body()!!

                    // Filtra la fecha


                    val filteredRecordsList = recordsList.filter {
                        it.KidID == kidID && it.Date == if (selectedDate.isNotEmpty()) {
                            selectedDate
                        } else {
                            getFormattedDate()
                        }
                    }

                    if (selectedDate.isNotEmpty()) {
                        fecha.text = selectedDate
                    } else {
                        fecha.text =getFormattedDate()
                    }


                    // Iterate through each record and add its view to the recordLayout
                    for (record in filteredRecordsList) {
                        val recordView = createRecordView(record)
                        recordLayout.addView(recordView)
                    }
                }
            }

            override fun onFailure(call: Call<List<Record>>, t: Throwable) {
                // Handle failure
            }
        })
    }


    private fun createRecordView(record: Record): View {
        val inflater = LayoutInflater.from(this)
        val recordView = inflater.inflate(R.layout.record_item, null)

        val recordDate: TextView = recordView.findViewById(R.id.record_date)
        val enterHour: TextView = recordView.findViewById(R.id.enter_hour)
        val exitHour: TextView = recordView.findViewById(R.id.exit_hour)

        val mealMenu: TextView = recordView.findViewById(R.id.meal_menu)
        val recordComment: TextView = recordView.findViewById(R.id.record_comment)
        val mealInfo: TextView = recordView.findViewById(R.id.meal_info)
        val clothesChangeCount: TextView = recordView.findViewById(R.id.clothes_change_count)
        val evacuationCount: TextView = recordView.findViewById(R.id.evacuation_count)
        val urinationCount: TextView = recordView.findViewById(R.id.urination_count)
        val classIncident: TextView = recordView.findViewById(R.id.class_incident)
        val medicIncident: TextView = recordView.findViewById(R.id.medic_incident)

        recordDate.text = record.Date
        enterHour.text = "Hora de entrada: ${record.EnterTime}"
        exitHour.text = "Hora de salida: ${record.ExitTime}"

        mealMenu.text = " ${record.Menu}"
        recordComment.text = "${record.Comment}"
        mealInfo.text = "${record.MealInfo}"
        clothesChangeCount.text = "${record.ChangeClothes}"
        evacuationCount.text = "${record.Evacuations}"
        urinationCount.text = "${record.Urinations}"
        classIncident.text = "${record.ClassIncident}"
        medicIncident.text = "${record.MedicIncident}"

        return recordView
    }

    private fun getFormattedDate(): String {
        val currentDate = Calendar.getInstance()
        val formattedDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(currentDate.time)

        return formattedDate
    }


}
