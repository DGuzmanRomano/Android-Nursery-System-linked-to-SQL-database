package mx.ipn.escom.guzmanr.p3

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView

class KidAdapter(private val kids: List<Kid>, private val context: Context,private val role: String) :
    RecyclerView.Adapter<KidAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val kidName: TextView = itemView.findViewById(R.id.kid_name)
        val kidGroupID: TextView = itemView.findViewById(R.id.kid_group_id)
        val cardView: CardView = itemView.findViewById(R.id.card_view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_kid, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val kid = kids[position]
        holder.kidName.text = "${kid.FirstName} ${kid.LastName}"
        holder.kidGroupID.text = "Grupo: ${kid.GroupID}"
        val kidFullName = "${kid.FirstName} ${kid.LastName}"
        holder.cardView.setOnClickListener {

            val intent = when(role) {
                "parent" -> Intent(context, RecordActivity::class.java)
                "teacher" -> Intent(context, PutRecordActivity::class.java)
                else -> null
            }
            intent?.putExtra("KidID", kid.KidID)
            intent?.putExtra("KidName", kidFullName)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = kids.size
}
