package com.example.xmlproj

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button


class TestFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("FRAG", "onCreate")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d("FRAG", "onCreateView")
        return inflater.inflate(R.layout.fragment_test, container, false)
    }

    override fun onStart() {
        super.onStart()
        Log.d("FRAG", "onStart")
    }

    override fun onResume() {
        super.onResume()
        Log.d("FRAG", "onResume")
    }

    override fun onPause() {
        super.onPause()
        Log.d("FRAG", "onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.d("FRAG", "onStop")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d("FRAG", "onDestroyView")
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btn = view.findViewById<Button>(R.id.btnNext)
        btn.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, Test2Fragment())
                .addToBackStack(null)
                .commit()
        }
    }
}