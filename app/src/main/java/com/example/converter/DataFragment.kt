package com.example.converter

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context.CLIPBOARD_SERVICE
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.text.method.ScrollingMovementMethod
import android.view.*
import android.widget.*
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import java.lang.reflect.Field
import java.math.BigDecimal



// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [DataFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DataFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var editTextInput : EditText
    var unitsOfGroupInt: Int = R.array.distanceUnits
    lateinit var converterViewModel : ConverterViewModel

    private var myClipboard: ClipboardManager? = null
    private var myClip: ClipData? = null

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
        return inflater.inflate(R.layout.fragment_data, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        editTextInput = view.findViewById(R.id.editTextInputDecimal)
        editTextInput.setShowSoftInputOnFocus(false)
        editTextInput.maxLines = 1
        editTextInput.isVerticalScrollBarEnabled = true
        editTextInput.movementMethod = ScrollingMovementMethod()

        editTextInput.customSelectionActionModeCallback = object : ActionMode.Callback {
            override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean {
                return false
            }

            override fun onDestroyActionMode(mode: ActionMode?) {}
            override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {
                return false
            }

            override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean {
                return false
            }
        }
        editTextInput.isLongClickable = false
        editTextInput.setTextIsSelectable(false)

        val textViewOutput = view.findViewById<TextView>(R.id.text_view_output)
        // textViewOutput.isVerticalScrollBarEnabled = true
        textViewOutput.isHorizontalScrollBarEnabled = true
        textViewOutput.movementMethod = ScrollingMovementMethod()
        textViewOutput.maxLines = 1

        val unitsOfSpinner = resources.getStringArray(unitsOfGroupInt)
        val inputSpinner = view.findViewById<Spinner>(R.id.spinnerInput)
        if(inputSpinner != null)
        {
            val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, unitsOfSpinner)
            inputSpinner.adapter = adapter
            inputSpinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    converterViewModel.setUnitFrom(UnitRepository.getUnitById(
                        getStringResourceId(unitsOfSpinner[position])))
                }

                override fun onNothingSelected(parent: AdapterView<*>) {

                }
            }
        }

        val outputSpinner = view.findViewById<Spinner>(R.id.spinner_output)
        if(outputSpinner != null)
        {
            val adapter = ArrayAdapter(requireActivity(), android.R.layout.simple_spinner_item, unitsOfSpinner)
            outputSpinner.adapter = adapter
            outputSpinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    converterViewModel.setUnitTo(UnitRepository.getUnitById(
                        getStringResourceId(unitsOfSpinner[position])))
                }

                override fun onNothingSelected(parent: AdapterView<*>) {

                }
            }
        }


        editTextInput.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {
                val str = s.toString()

            }

            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int
            ) {
                //if (str == ".")
                //   editText.setText( "0")


            }

            override fun onTextChanged(
                s: CharSequence, start: Int,
                before: Int, count: Int
            ) {
                var str = s.toString()
                //if (str == ".")
                //   editText.setText( "0")
                val cursorPosition: Int = editTextInput.selectionStart
                if(str.startsWith('.') && cursorPosition!= 0)
                {
                    editTextInput.setText("0".plus(s))
                    editTextInput.setSelection(editTextInput.text.length)
                }

                if(str.startsWith('0') && str.length >= 2 && str[1] != '.')
                {
                    str = str.substring(1)
                    /*Toast.makeText(
                        this@ConverterActivity,str, Toast.LENGTH_SHORT
                    ).show()*/
                    Toast.makeText(
                        requireActivity(), "leading zeros were removed", Toast.LENGTH_SHORT
                    ).show()
                    editTextInput.setText(str)
                    editTextInput.setSelection(editTextInput.text.length)
                }

                /* if(str.startsWith("0") && str.contains('.'))
                 {
                     str = str.substring(1)
                     Toast.makeText(
                         this@ConverterActivity, "here", Toast.LENGTH_SHORT
                     ).show()
                    // editText.setSelection(str.length)
                 }
                     //if(!str.contains('.')) {
                     //    str.trimStart('0')
                     //editText.setText(str)}
                // }*/
                val convertedNum : BigDecimal =
                    if (str.isEmpty() || str.toBigDecimalOrNull() == null)
                        BigDecimal("0")
                    else
                        BigDecimal(str)
                //editText.setText(convertedNum.toString())
                converterViewModel.setValueFrom(convertedNum)
                converterViewModel.convert()

                /* Toast.makeText(
                     this@ConverterActivity,"valueFrom" + mainViewModel.unitFrom.value?.let {
                         getString(
                             it.Name)
                     } + "valueto" + mainViewModel.unitTo.value?.Name?.let { getString(it) }, Toast.LENGTH_SHORT
                 ).show()*/


            }
        })

        converterViewModel.valueTo.observe(requireActivity(), Observer {
            if(editTextInput.text.toString() == "") {
                textViewOutput.text = ""
            }
            else {
                textViewOutput.text = it.toPlainString()
            }
        })

        myClipboard = requireContext().getSystemService(CLIPBOARD_SERVICE) as ClipboardManager?;

        fun copyText() {
            val sdk = Build.VERSION.SDK_INT
            if (sdk < Build.VERSION_CODES.HONEYCOMB) {
                val clipboard = requireContext().getSystemService(CLIPBOARD_SERVICE) as android.text.ClipboardManager
                clipboard.text = textViewOutput.text.toString()
            } else {
                val clipboard = requireContext().getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
                val clip = ClipData.newPlainText("text label", textViewOutput.text)
                clipboard.setPrimaryClip(clip)
            }
            Toast.makeText(requireContext(), "Number Copied", Toast.LENGTH_SHORT).show();
        }

        // on click paste button
        fun pasteText() {
            val abc = myClipboard?.getPrimaryClip()
            val item = abc?.getItemAt(0)
            val itemStr = item?.text.toString()
            if(itemStr.toBigDecimalOrNull() != null) {
                editTextInput.setText(itemStr)
                editTextInput.setSelection(editTextInput.length())
                Toast.makeText(
                    requireContext(), "Number pasted",
                    Toast.LENGTH_SHORT
                ).show()
            }
            else {
                Toast.makeText(
                    requireContext(),
                    "Not allowed format to paste",
                    Toast.LENGTH_SHORT
                ).show()
            }

        }

        val pasteButton = view.findViewById<Button>(R.id.button_paste)
        pasteButton.setOnClickListener {
            pasteText()
        }

        val copyButton = view.findViewById<Button>(R.id.button_copy)
        copyButton.setOnClickListener {
            copyText()
        }

        val swapButton = view.findViewById<Button>(R.id.button_swap)
        swapButton.setOnClickListener {
            converterViewModel.swap()
            val spinner1Index: Int = inputSpinner.getSelectedItemPosition()
            inputSpinner.setSelection(outputSpinner.selectedItemPosition)
            outputSpinner.setSelection(spinner1Index)

        }
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment DataFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            DataFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    fun getStringResourceId(stringToSearch: String): Int {
        val fields: Array<Field> = R.string::class.java.fields
        for (field in fields) {
            val id = field.getInt(field)
            val str = resources.getString(id)
            if (str == stringToSearch) {
                return id
            }
        }
        return -1
    }
}