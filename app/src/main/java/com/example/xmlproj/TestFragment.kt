package com.example.xmlproj

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView


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
        Log.d("FRAG", "onViewCreated")

        val textView = view.findViewById<TextView>(R.id.txtFragment)

        val message = arguments?.getString(ARG_MESSAGE)
        if (message != null) {
            textView.text = message
        }

        val btn1 = view.findViewById<Button>(R.id.btnNext)
        btn1?.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .setCustomAnimations(
                    android.R.anim.slide_in_left,
                    android.R.anim.slide_out_right,
                    android.R.anim.slide_in_left,
                    android.R.anim.slide_out_right
                )
                .replace(R.id.fragment_container, Test2Fragment())
                .addToBackStack(null)
                .commit()
        }
        val btn2 = view.findViewById<Button>(R.id.btnSendToActivity)
        btn2.setOnClickListener {
            listener?.onMessageFromFragment("Hi Activity, from Fragment!")
        }
    }
    companion object {
        private const val ARG_MESSAGE = "arg_message"

        fun newInstance(message: String): TestFragment {
            val fragment = TestFragment()
            val bundle = Bundle()
            bundle.putString(ARG_MESSAGE, message)
            fragment.arguments = bundle
            return fragment
        }
    }
    interface OnFragmentInteractionListener {
        fun onMessageFromFragment(message: String)
    }

    private var listener: OnFragmentInteractionListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException("$context must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }
}