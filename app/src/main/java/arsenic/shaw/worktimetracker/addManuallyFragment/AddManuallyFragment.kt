package arsenic.shaw.worktimetracker.addManuallyFragment

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import arsenic.shaw.worktimetracker.databinding.FragmentAddManuallyBinding


class AddManuallyFragment : Fragment() {

    private lateinit var viewModel: AddManuallyViewModel
    private lateinit var binding: FragmentAddManuallyBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddManuallyBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this)[AddManuallyViewModel::class.java]

        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        setObservers()
        setClickListeners()

        return binding.root
    }

    private fun setClickListeners() {
        binding.backButton.setOnClickListener{
            findNavController().popBackStack()
        }
    }

    private fun setObservers() {
        viewModel.startDateDialogShowing.observe(viewLifecycleOwner){
            if(it){
                val datePickerDialog = DatePickerDialog(requireContext(), viewModel.startDateChangeListener, 2021,12,1)
                datePickerDialog.show()
            }
        }

        viewModel.endDateDialogShowing.observe(viewLifecycleOwner){
            if (it){
                val datePickerDialog = DatePickerDialog(requireContext(), viewModel.endDateChangeListener, 2021, 12, 1)
                datePickerDialog.show()
            }
        }

        viewModel.startTimeDialogShowing.observe(viewLifecycleOwner){
            if (it){
                val timePickerDialog = TimePickerDialog(requireContext(), viewModel.startTimeChangeListener, 0, 0, true)
                timePickerDialog.show()
            }
        }

        viewModel.endTimeDialogShowing.observe(viewLifecycleOwner){
            if (it){
                val timePickerDialog = TimePickerDialog(requireContext(), viewModel.endTimeChangeListener, 0, 0, true)
                timePickerDialog.show()
            }
        }

        viewModel.workAdded.observe(viewLifecycleOwner){
            if (it){
                findNavController().popBackStack()
            }
        }
    }
}