package com.example.converter

import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.util.SparseArray
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.ExtractedTextRequest
import android.view.inputmethod.InputConnection
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [KeyboardFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class KeyboardFragment : Fragment(), View.OnClickListener, View.OnLongClickListener {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    // keyboard keys (buttons)

    private lateinit var button1: Button
    private lateinit var button2: Button
    private lateinit var button3: Button
    private lateinit var button4: Button
    private lateinit var button5: Button
    private lateinit var button6: Button
    private lateinit var button7: Button
    private lateinit var button8: Button
    private lateinit var button9: Button
    private lateinit var button0: Button
    private lateinit var buttonDot: Button
    private lateinit var buttonDel: Button

    private var buttonIdToValue = SparseArray<String>()
    private lateinit var inputConnection: InputConnection
    private lateinit var currContext: Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_keyboard, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        button1 = view.findViewById(R.id.button_1) as Button
        button2 = view.findViewById(R.id.button_2) as Button
        button3 = view.findViewById(R.id.button_3) as Button
        button4 = view.findViewById(R.id.button_4) as Button
        button5 = view.findViewById(R.id.button_5) as Button
        button6 = view.findViewById(R.id.button_6) as Button
        button7 = view.findViewById(R.id.button_7) as Button
        button8 = view.findViewById(R.id.button_8) as Button
        button9 = view.findViewById(R.id.button_9) as Button
        button0 = view.findViewById(R.id.button_0) as Button
        buttonDot = view.findViewById(R.id.button_dot) as Button
        buttonDel = view.findViewById(R.id.button_del) as Button

        button1.setOnClickListener(this)
        button2.setOnClickListener(this)
        button3.setOnClickListener(this)
        button4.setOnClickListener(this)
        button5.setOnClickListener(this)
        button6.setOnClickListener(this)
        button7.setOnClickListener(this)
        button8.setOnClickListener(this)
        button9.setOnClickListener(this)
        button0.setOnClickListener(this)
        buttonDot.setOnClickListener(this)
        buttonDel.setOnClickListener(this)
        buttonDel.setOnLongClickListener(this)

        buttonIdToValue.put(R.id.button_1, "1")
        buttonIdToValue.put(R.id.button_2, "2")
        buttonIdToValue.put(R.id.button_3, "3")
        buttonIdToValue.put(R.id.button_4, "4")
        buttonIdToValue.put(R.id.button_5, "5")
        buttonIdToValue.put(R.id.button_6, "6")
        buttonIdToValue.put(R.id.button_7, "7")
        buttonIdToValue.put(R.id.button_8, "8")
        buttonIdToValue.put(R.id.button_9, "9")
        buttonIdToValue.put(R.id.button_0, "0")
        buttonIdToValue.put(R.id.button_dot, ".")

        val editTextInput: EditText = requireActivity().findViewById(R.id.editTextInputDecimal)
        inputConnection = editTextInput.onCreateInputConnection(EditorInfo())
        currContext = requireActivity().applicationContext
    }

    override fun onClick(view: View)
    {
        // do nothing if the InputConnection has not been set yet
        // if (inputConnection == null) return
        // if(iConnection!!.getTextBeforeCursor(1,0 ) == "")
        // mButtonDot?.isClickable = false



        // Delete text or input key value
        // All communication goes through the InputConnection
        if (view.id == R.id.button_del) {
            val selectedText = inputConnection.getSelectedText(0)
            if (TextUtils.isEmpty(selectedText)) {
                // no selection, so delete previous character
                inputConnection.deleteSurroundingText(1, 0)
                //if(TextUtils.isEmpty(iConnection!!.getTextBeforeCursor(1,0)))
                //buttonAccessible(mButtonDot,false)
            } else {
                // delete the selection
                inputConnection.commitText("", 1)
                //if(TextUtils.isEmpty(iConnection!!.getTextBeforeCursor(1,0)))
                //buttonAccessible(mButtonDot,false)
            }
        }
        else {
            if(view.id == R.id.button_dot) {
                val currentText: CharSequence =
                    inputConnection.getExtractedText(ExtractedTextRequest(), 0).text
                if (currentText.toString().contains('.'))
                    Toast.makeText(requireActivity(), "you can't enter second dot", Toast.LENGTH_SHORT).show()
            }
            val value = buttonIdToValue[view.id]
            /* val currentText: CharSequence =
                 iConnection!!.getExtractedText(ExtractedTextRequest(), 0).text
             if(currentText == "0"){
                 iConnection!!.deleteSurroundingText(1, 0)
                 if(TextUtils.isEmpty(iConnection!!.getTextBeforeCursor(1,0)))
                     buttonAccessible(mButtonDot,false)
             }*/


            inputConnection.commitText(value, 1)
            // buttonAccessible(buttonDot,true)

        }
    }

    override fun onLongClick(v: View?): Boolean {
//        if (inputConnection == null)
//            return false
        val text = inputConnection.getExtractedText(ExtractedTextRequest(), 0).text

        inputConnection.deleteSurroundingText(text.length, 0)
        return true
    }

    private fun buttonAccessible(button: Button?, b:Boolean)
    {
        button?.isClickable = b
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment KeyboardFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            KeyboardFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}