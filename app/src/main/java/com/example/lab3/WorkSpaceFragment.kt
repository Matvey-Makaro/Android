package com.example.lab3

import android.content.res.Configuration
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import ch.obermuhlner.math.big.BigDecimalMath
import com.example.lab3.databinding.FragmentWorkSpaceBinding
import com.mpobjects.bdparsii.eval.*
import com.mpobjects.bdparsii.eval.Function
import kotlinx.coroutines.*
import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode

fun fact(num: Int): BigDecimal? {
    if (num == 0) { return BigDecimal.ONE }

    return fact(num - 1)?.multiply(num.toBigDecimal())
}

/*class Factorial : Function
{
    override fun getNumberOfArguments(): Int
    {
        return 1
    }

    override fun eval(args: List<Expression?>?, mathContext: MathContext?): BigDecimal?
    {
        BigDecimal num =

    }

    override fun isNaturalFunction(): Boolean {
        return true
    }
}*/

val Factorial: Function = object : UnaryFunction() {
    override fun eval(a: BigDecimal, mathContext: MathContext): BigDecimal {
        return BigDecimalMath.factorial(a.setScale(11, RoundingMode.HALF_UP), MathContextGuard.getSafeContext(mathContext)).setScale(11, RoundingMode.HALF_UP)
    }
}

val TAN: Function = object : UnaryFunction() {
    override fun eval(a: BigDecimal, mathContext: MathContext): BigDecimal {
        return BigDecimalMath.tan(a.setScale(11, RoundingMode.HALF_UP), MathContextGuard.getSafeContext(mathContext)).setScale(11, RoundingMode.HALF_UP)
    }
}

val ATAN: Function = object : UnaryFunction() {
    override fun eval(a: BigDecimal, mathContext: MathContext): BigDecimal {
        return BigDecimalMath.atan(a.setScale(11, RoundingMode.HALF_UP), MathContextGuard.getSafeContext(mathContext)).setScale(11, RoundingMode.HALF_UP)
    }
}

class WorkSpaceFragment : Fragment() {
    lateinit var binding: FragmentWorkSpaceBinding

    private fun positionIsInFunction(expr: String, pos: Int): Boolean {
        Log.d("WorkSpaceFragment", "expr: $expr")
        Log.d("WorkSpaceFragment", "pos: $pos")

        if (pos < 1 || pos > expr.length) {
            return false
        }
        if (expr[pos - 1].isLetter()) {
            return true
        }
        return false
    }

    // returns the expression with erased function
    private fun eraseFunctionByPosition(expr: String, pos: Int): Pair<Int, String> {
        var eraseStart = pos
        var eraseEnd = pos

        for (i in (pos - 1) downTo 0) {
            if (expr[i].isLetter()) {
                eraseStart--
            }
            else {
                break
            }
        }
        for (i in pos until expr.length) {
            if (expr[i].isLetter()) {
                eraseEnd++
            }
            else {
                break
            }
        }

        if (eraseStart < 0) { eraseStart = 0 }
        if (eraseEnd > expr.length) { eraseEnd = expr.length }

        return Pair(eraseStart, expr.removeRange(eraseStart, eraseEnd))
    }

    //@SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreate(savedInstanceState)
        binding = FragmentWorkSpaceBinding.inflate(inflater, container,false)

        if (savedInstanceState != null)
        {
            binding.inputText.setText(savedInstanceState.getString("inputTextData"))
            binding.resultText.text = savedInstanceState.getString("resultTextData")
        }

        binding.resultText.movementMethod = ScrollingMovementMethod()

        binding.inputText.showSoftInputOnFocus = false
        binding.inputText.setHorizontallyScrolling(true)

        val signs = "+-/*^"

        setFragmentResultListener("requestKey") { _, bundle ->
            val button = bundle.getString("bundleKey")
            var changeText = StringBuilder(binding.inputText.text.toString())
            var positionStart = binding.inputText.selectionStart
            var position = binding.inputText.selectionEnd

            if (button == "clear") {
                if (binding.inputText.length() == 0) binding.resultText.text = ""

                binding.inputText.text.clear()
                return@setFragmentResultListener
            }
            else if (button == "delete") {
                if (position != 0 && (positionStart != position)) {


                    if (positionIsInFunction(changeText.toString(), positionStart)) {
                        val result = eraseFunctionByPosition(changeText.toString(), positionStart)
                        val delta = changeText.length - result.second.length
                        changeText = StringBuilder(result.second)
                        positionStart = result.first
                        position -= delta
                    }
                    if (positionIsInFunction(changeText.toString(), position)) {
                        val result = eraseFunctionByPosition(changeText.toString(), position)
                        //val delta = changeText.length - result.second.length
                        changeText = StringBuilder(result.second)
                        position = result.first
                    }

                    changeText = changeText.delete(positionStart, position)
                    binding.inputText.setText(changeText)
                    binding.inputText.setSelection(positionStart)
                }
                else if (position != 0) {
                    val newSelection: Int

                    if (positionIsInFunction(changeText.toString(), position)) {
                        val result = eraseFunctionByPosition(changeText.toString(), position)
                        changeText = StringBuilder(result.second)
                        newSelection = result.first
                    }
                    else {
                        changeText = changeText.deleteAt(position - 1)
                        newSelection = position - 1
                    }

                    binding.inputText.setText(changeText)
                    binding.inputText.setSelection(newSelection)
                }
                if (binding.inputText.text.isEmpty()) {
                    return@setFragmentResultListener
                }
                else if (binding.inputText.text[0] == '.')
                {
                    binding.inputText.setText("0${binding.inputText.text}")
                }
            }
            else if (button == ".") {
                changeText = changeText.apply { insert(position, button) }
                val list = changeText.toString().split('+', '-', '*', '/', '^', '(', ')')
                for (item in list) {
                    if (item.count { it == '.' } > 1) {
                        Toast.makeText(activity, "already has one dot!", Toast.LENGTH_SHORT).show()
                        return@setFragmentResultListener
                    }
                }
                binding.inputText.setText(changeText)
                binding.inputText.setSelection(position + 1)
            }
            else if (button?.let { signs.contains(it) } == true) {
                val pos = binding.inputText.selectionEnd
                if (pos == 0) {
                    return@setFragmentResultListener
                }
                if (pos != binding.inputText.length()) {
                    if (signs.contains(binding.inputText.text[pos])) {
                        Toast.makeText(activity, "cannot have two signs in a row!", Toast.LENGTH_SHORT).show()
                        return@setFragmentResultListener
                    }
                }
                if (binding.inputText.length() > 0) {
                    if (signs.contains(binding.inputText.text[pos-1])) {
                        Toast.makeText(activity, "cannot have two signs in a row!", Toast.LENGTH_SHORT).show()
                        return@setFragmentResultListener
                    }
                }
                changeText = changeText.apply { insert(position, button) }
                binding.inputText.setText(changeText)
                binding.inputText.setSelection(position + 1)
            }
            else if (button == ")") {
                var lCount = 0
                var rCount = 0
                for (element in changeText) {
                    if (element == '(') lCount++
                    else if (element == ')') rCount++
                }
                if (rCount >= lCount) {
                    Toast.makeText(activity, "no leading bracket!", Toast.LENGTH_SHORT).show()
                    return@setFragmentResultListener
                }
                changeText = changeText.apply { insert(position, button) }
                binding.inputText.setText(changeText)
                binding.inputText.setSelection(position + 1)
            }
            else if (button == "=") {
                var lCount = 0
                var rCount = 0
                for (element in changeText) {
                    if (element == '(') lCount++
                    else if (element == ')') rCount++
                }
                if (lCount > rCount) {
                    val count = lCount - rCount
                    binding.inputText.text.insert(binding.inputText.text.length, ")".repeat(count))
                    changeText = StringBuilder(binding.inputText.text.toString())
                    //Toast.makeText(activity, "added missing brackets", Toast.LENGTH_SHORT).show()
                    //return@setFragmentResultListener
                }

                Parser.registerFunction("tg", TAN)
                Parser.registerFunction("sh", Functions.SINH)
                Parser.registerFunction("ch", Functions.COSH)
                Parser.registerFunction("tgh", Functions.TANH)
                Parser.registerFunction("arcsin", Functions.ASIN)
                Parser.registerFunction("arccos", Functions.ACOS)
                Parser.registerFunction("arctg", ATAN)
                Parser.registerFunction("lg", Functions.LOG)
                Parser.registerFunction("fact", Factorial)

//                try {
//                    val list = changeText.toString().split(Regex("(?<=[()+/*-^])|(?=[()+/*-^])"))
//                    val newList = ArrayList<String>()
//                    for (item in list) {
//                        if (item.contains('!')) {
//                            if (item.dropLast(1).toBigDecimal() > BigDecimal(500)) throw ArithmeticException()
//                            newList.add(fact(item.dropLast(1).toInt()).toString())
//                        }
//                        else {
//                            newList.add(item)
//                        }
//                    }
//                    changeText = StringBuilder(TextUtils.join("", newList))
//                }
//                catch(e: ArithmeticException) {
//                    Toast.makeText(activity, "cannot evaluate this expression!", Toast.LENGTH_SHORT).show()
//                }
//                catch(e: Exception) {
//                    Toast.makeText(activity, "invalid expression!", Toast.LENGTH_SHORT).show()
//                }

                val scope = Scope()
                scope.mathContext = MathContext(170)

                try {
                    val userExpression = changeText.toString()
                    val exp = Parser.parse(userExpression, scope)
                    var resultNumber = BigDecimal("0", scope.mathContext)

                    val startTime = System.currentTimeMillis()

                    val job = CoroutineScope(SupervisorJob()).launch { withContext(Dispatchers.IO) {
                        try {
                            resultNumber = exp.evaluate().setScale(11, RoundingMode.HALF_UP).stripTrailingZeros()
                            // resultNumber = exp.evaluate().stripTrailingZeros()
                        }
                        catch(e: Exception) {

                        }

                    } }
                    runBlocking {
                        launch { withContext(Dispatchers.IO) {
                            while (job.isActive) {
                                if (System.currentTimeMillis() - startTime > 500){
                                    job.cancel()
                                    throw ArithmeticException("Сan't count. Too big expression.")
                                }
                            }
                        }
                        }
                    }

                    // val result = resultNumber.toString()
                    val result = resultNumber.toPlainString()
                    if (result.length > 1029) throw ArithmeticException()
                    binding.resultText.text = result
                    //binding.inputText.setText(result)
                    //binding.inputText.setSelection(binding.inputText.text.length)
                }
                catch(e: ArithmeticException) {
                    if(e.message == null)
                        Toast.makeText(activity, "Cannot evaluate this expression!", Toast.LENGTH_SHORT).show()
                    else Toast.makeText(activity, e.message, Toast.LENGTH_SHORT).show()
                }
                catch(e: Exception) {
                    Toast.makeText(activity, "Invalid expression!", Toast.LENGTH_SHORT).show()
                }
            }
            else if (button == "switch_mode") {
                val orientation = resources.configuration.orientation
                if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    Toast.makeText(activity, "cannot switch modes in landscape!", Toast.LENGTH_SHORT).show()
                    return@setFragmentResultListener
                }

                val scientificFunctionsFragment: View =
                    requireActivity().findViewById(R.id.fragment_scientific_functions)!!
                val weight: Float = (scientificFunctionsFragment.layoutParams as LinearLayout.LayoutParams).weight
                var newWeight = 1.0f

                if (weight > 0.0f) {
                    newWeight = 0.0f
                }

                val params = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    0,
                    newWeight
                )
                scientificFunctionsFragment.layoutParams = params
            }
            else {
                val newPos = button?.length
                changeText = changeText.apply { insert(position, button) }
                binding.inputText.setText(changeText)
                binding.inputText.setSelection(position + newPos!!)
            }
        }

        return binding.root
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString("inputTextData", binding.inputText.text.toString())
        outState.putString("resultTextData", binding.resultText.text.toString())
        super.onSaveInstanceState(outState)
    }
}