package mx.ipn.escom.guzmanr.p3

import android.annotation.SuppressLint
import android.os.Bundle
import retrofit2.Call
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Response
import retrofit2.Callback
import java.text.SimpleDateFormat
import java.util.*


class PutRecordActivity : AppCompatActivity() {
    private lateinit var recordLayout: LinearLayout  // Define a LinearLayout for the records

    private lateinit var date: TextView
    private lateinit var name: TextView
    private var formattedDate = ""

    private lateinit var enterHour: TimePicker
    private lateinit var exitHour: TimePicker
    private lateinit var mealCount: EditText
    private lateinit var bathroomCount: EditText
    private lateinit var mealMenu: EditText
    private lateinit var saveButton: Button
    private lateinit var comment: EditText
    private lateinit var mealInfo: RadioGroup
    private lateinit var clothesChangeCount: TextView
    private lateinit var evacuations: TextView
    private lateinit var urinations: TextView
    private lateinit var classIncident: EditText
    private lateinit var medicIncident: EditText

    private lateinit var evacuationsButton: Button
    private lateinit var urinationsButton: Button
    private lateinit var clothesChangeButton: Button
    private lateinit var minusButton: Button
    private lateinit var minusButton2: Button
    private lateinit var minusButton3: Button

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_put_record)

        name = findViewById(R.id.nombreForm)


        date = findViewById(R.id.enter_record_date)
        setDateToToday(date)

        enterHour = findViewById(R.id.enter_enter_hour)
        exitHour = findViewById(R.id.enter_exit_hour)
       //mealCount = findViewById(R.id.enter_meal_count)
       //bathroomCount = findViewById(R.id.enter_bathroom_count)
        mealMenu = findViewById(R.id.enter_meal_menu)
        saveButton = findViewById(R.id.save_button) // Add this button in your XML
        comment = findViewById(R.id.comment)
        mealInfo = findViewById(R.id.meal_info)

        /////////////////////////////////////
        clothesChangeCount = findViewById(R.id.clothes_change_count)
        evacuations = findViewById(R.id.evacuations)
        urinations = findViewById(R.id.urinations)

        evacuationsButton = findViewById(R.id.buttonCountevID)
        urinationsButton = findViewById(R.id.buttonCountmiID)
        clothesChangeButton = findViewById(R.id.buttonCountCambiosRopaID)
        minusButton = findViewById(R.id.minusButton)
        minusButton2 = findViewById(R.id.minusButton2)
        minusButton3 = findViewById(R.id.minusButton3)


        evacuationsButton.setOnClickListener {
            incrementTextViewValue(evacuations)
        }

        urinationsButton.setOnClickListener {
            incrementTextViewValue(urinations)
        }

        clothesChangeButton.setOnClickListener {
            incrementTextViewValue(clothesChangeCount)
        }

        minusButton.setOnClickListener {
            decrementTextViewValue(evacuations)
            decrementTextViewValue(urinations)
            decrementTextViewValue(clothesChangeCount)
        }
        minusButton2.setOnClickListener {

            decrementTextViewValue(urinations)

        }
        minusButton3.setOnClickListener {

            decrementTextViewValue(clothesChangeCount)
        }
    }

    private fun incrementTextViewValue(textView: TextView) {
        val currentValue = textView.text.toString().toInt()
        textView.text = (currentValue + 1).toString()
    }

    private fun decrementTextViewValue(textView: TextView) {
        val currentValue = textView.text.toString().toInt()
        if (currentValue > 0) {
            textView.text = (currentValue - 1).toString()
        }

        /////////////////////////
        classIncident = findViewById(R.id.class_incident)
        medicIncident = findViewById(R.id.medic_incident)


        saveButton.setOnClickListener {
            addRecord()
        }


    }

    private fun setDateToToday(dateTextView: TextView): String {
        val currentDate = Calendar.getInstance()
        val formattedDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(currentDate.time)
        dateTextView.text = formattedDate
        val kidFullName = intent.getStringExtra("KidName")

        name.text = kidFullName
      return formattedDate
    }


    private fun addRecord() {
        val kidID = intent.getIntExtra("KidID", -1)


        val selectedMealInfoId = mealInfo.checkedRadioButtonId
        val selectedMealInfo: RadioButton = findViewById(selectedMealInfoId)
        val mealInfoText = selectedMealInfo.text.toString()

        val record = Record(

            KidID = kidID,
            Date = getFormattedDate(),
            EnterTime = getFormattedTimeFromTimePicker(enterHour),
            ExitTime = getFormattedTimeFromTimePicker(exitHour),
      //      MealCount = mealCount.text.toString().toInt(),
       //     BathroomCount = bathroomCount.text.toString().toInt(),
            Menu = mealMenu.text.toString(),
            Comment = comment.text.toString(),
            MealInfo = mealInfoText,
            ChangeClothes = clothesChangeCount.text.toString().toIntOrNull(),
            Evacuations = evacuations.text.toString().toIntOrNull(),
            Urinations = urinations.text.toString().toIntOrNull(),
            ClassIncident = classIncident.text.toString(),
            MedicIncident = medicIncident.text.toString()
        )

        val call = RetrofitClient.instance().addRecord(record)
        call.enqueue(object: Callback<Record> {
            override fun onResponse(call: Call<Record>, response: Response<Record>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@PutRecordActivity, "Record added", Toast.LENGTH_LONG).show()

                } else {
                    Toast.makeText(this@PutRecordActivity, "Failed to add record", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<Record>, t: Throwable) {
                // Handle failure
                Toast.makeText(this@PutRecordActivity, "Failed to add record", Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun getFormattedTimeFromTimePicker(timePicker: TimePicker): String {
        val hour = timePicker.hour
        val minute = timePicker.minute
        return String.format("%02d:%02d", hour, minute)
    }

    private fun getFormattedDate(): String {
        val currentDate = Calendar.getInstance()
        val formattedDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(currentDate.time)

        return formattedDate
    }


}

