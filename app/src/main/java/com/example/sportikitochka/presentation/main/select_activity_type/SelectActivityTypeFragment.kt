package com.example.sportikitochka.presentation.main.select_activity_type

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.sportikitochka.R

class SelectActivityTypeFragment : Fragment() {

    companion object {
        fun newInstance() = SelectActivityTypeFragment()
    }

    private lateinit var viewModel: SelectActivityTypeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_select_activity_type, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(SelectActivityTypeViewModel::class.java)
        // TODO: Use the ViewModel
    }

}