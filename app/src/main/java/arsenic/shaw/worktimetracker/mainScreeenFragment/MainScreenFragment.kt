package arsenic.shaw.worktimetracker.mainScreeenFragment

import android.app.AlertDialog
import android.graphics.Color
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import arsenic.shaw.worktimetracker.MainActivity
import arsenic.shaw.worktimetracker.R
import arsenic.shaw.worktimetracker.databinding.FragmentMainScreenBinding

class MainScreenFragment : Fragment() {

    private lateinit var binding: FragmentMainScreenBinding
    private lateinit var viewModel: MainScreenViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainScreenBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this)[MainScreenViewModel::class.java]

        setUpToolBar()

        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        setRecyclerView()
        setClickListeners()

        return binding.root
    }

    private fun setUpToolBar() {
        binding.toolbar.setupWithNavController(
            findNavController(),
            AppBarConfiguration(findNavController().graph)
        )
        binding.toolbar.setTitleTextColor(Color.WHITE)
        (activity as MainActivity).setSupportActionBar(binding.toolbar)
        setHasOptionsMenu(true)
    }

    private fun setRecyclerView() {
        val adapter = WorksAdapter(WorkClickListener { id ->
            val dialog = AlertDialog.Builder(context)
            dialog.setTitle("Delete")
            dialog.setMessage("are you sure you want to delete this work?")
            dialog.setPositiveButton("ok") { _, _ ->
                viewModel.deleteWork(id)
            }
            dialog.setNegativeButton("cancel", null)
            dialog.setIcon(R.drawable.ic_round_delete_24)
            dialog.show()
            true
        })
        binding.worksRecyclerView.adapter = adapter
    }

    private fun setClickListeners() {
        binding.addManuallyButton.setOnClickListener {
            findNavController().navigate(MainScreenFragmentDirections.actionMainScreenFragmentToAddManuallyFragment())
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        val deleteMenuItem = menu?.add("Delete All")
        deleteMenuItem?.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.title == "Delete All"){
            viewModel.deleteAllPlant()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}