package com.example.sportikitochka.presentation.main.rating

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.sportikitochka.R

class RatingFragment : Fragment() {

    companion object {
        fun newInstance() = RatingFragment()
    }

    private lateinit var viewModel: RatingViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_rating, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(RatingViewModel::class.java)
        // TODO: Use the ViewModel
    }

}