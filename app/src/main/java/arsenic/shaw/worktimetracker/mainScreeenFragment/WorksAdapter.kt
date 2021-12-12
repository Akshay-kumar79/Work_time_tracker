package arsenic.shaw.worktimetracker.mainScreeenFragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import arsenic.shaw.worktimetracker.database.Work
import arsenic.shaw.worktimetracker.databinding.ListItemForWorksBinding
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList

class WorksAdapter(private val workClickListener: WorkClickListener): RecyclerView.Adapter<WorksAdapter.ViewHolder>() {

    private var works: List<Work> = ArrayList()

    fun setData(works: List<Work>){
        this.works = works
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(works[position], workClickListener)
    }

    override fun getItemCount(): Int {
        return works.size
    }

    class ViewHolder(private val binding: ListItemForWorksBinding) : RecyclerView.ViewHolder(binding.root) {

        companion object{
            fun from(parent: ViewGroup): ViewHolder{
                val inflater = LayoutInflater.from(parent.context)
                val binding = ListItemForWorksBinding.inflate(inflater, parent, false)
                return ViewHolder(binding)
            }
        }

        fun bind(work:Work, workClickListener: WorkClickListener){
            val dateFormatter = SimpleDateFormat("dd-MMM-yyy, HH:mm", Locale.getDefault())
            binding.work = work
            binding.workClickListener = workClickListener
            binding.startAt.text = dateFormatter.format(Date(work.startTime))
            binding.endAt.text = dateFormatter.format(Date(work.endTime))
            binding.timeInterval.text = milliToDHMS(work.endTime - work.startTime)
            binding.executePendingBindings()
        }

        private fun milliToDHMS(millis: Long): String{

            val s = TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis))
            val m = TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis))
            val h = TimeUnit.MILLISECONDS.toHours(millis) - TimeUnit.DAYS.toHours(TimeUnit.MILLISECONDS.toDays(millis))
            val d = TimeUnit.MILLISECONDS.toDays(millis)

            var output = ""

            if(d != 0L){
                output = output+d.toString()+"d "
            }
            if(h != 0L){
                output = output+h.toString()+"h "
            }
            if(m != 0L){
                output = output+m.toString()+"m "
            }
            if(s != 0L){
                output = output+s.toString()+"s"
            }

            return output
        }
    }
}

class WorkClickListener(val clickListener: (id: Long) -> Boolean){
    fun onClick(work: Work) = clickListener(work.id)
}