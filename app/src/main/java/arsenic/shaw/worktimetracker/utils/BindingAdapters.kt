package arsenic.shaw.worktimetracker.utils

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import arsenic.shaw.worktimetracker.database.Work
import arsenic.shaw.worktimetracker.mainScreeenFragment.WorksAdapter

@BindingAdapter("listAllWorks")
fun listAllWorks(recyclerView: RecyclerView, works: List<Work>?){
    val adapter = recyclerView.adapter as WorksAdapter

    if (works != null) {
        adapter.setData(works)
    }
}