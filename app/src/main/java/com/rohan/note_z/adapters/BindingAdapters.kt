package com.rohan.note_z.adapters

import androidx.databinding.BindingAdapter
import androidx.navigation.findNavController
import com.rohan.note_z.R
import com.google.android.material.floatingactionbutton.FloatingActionButton

class BindingAdapters {

    companion object {

        @BindingAdapter("android.navigateToAddFragment")
        @JvmStatic
        fun navigateToAddFragment(view: FloatingActionButton, isNavigate: Boolean) {
            view.setOnClickListener {
                if (isNavigate) {
                    view.findNavController().navigate(R.id.action_listFragment_to_addFragment)
                }
            }
        }
    }
}