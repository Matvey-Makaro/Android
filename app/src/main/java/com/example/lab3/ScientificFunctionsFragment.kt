package com.example.lab3

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import com.example.lab3.databinding.FragmentScientificFunctionsBinding

class ScientificFunctionsFragment : Fragment() {
    lateinit var binding : FragmentScientificFunctionsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentScientificFunctionsBinding.inflate(inflater, container, false)

        binding.buttonSin.setOnClickListener {
            val result = "sin("
            setFragmentResult("requestKey", bundleOf("bundleKey" to result))
        }
        binding.buttonCos.setOnClickListener {
            val result = "cos("
            setFragmentResult("requestKey", bundleOf("bundleKey" to result))
        }
        binding.buttonTan.setOnClickListener {
            val result = "tg("
            setFragmentResult("requestKey", bundleOf("bundleKey" to result))
        }

        binding.buttonLn.setOnClickListener {
            val result = "ln("
            setFragmentResult("requestKey", bundleOf("bundleKey" to result))
        }

        binding.buttonASin.setOnClickListener {
            val result = "arcsin("
            setFragmentResult("requestKey", bundleOf("bundleKey" to result))
        }

        binding.buttonACos.setOnClickListener {
            val result = "arccos("
            setFragmentResult("requestKey", bundleOf("bundleKey" to result))
        }

        binding.buttonATan.setOnClickListener {
            val result = "arctg("
            setFragmentResult("requestKey", bundleOf("bundleKey" to result))
        }

        binding.buttonLg?.setOnClickListener {
            val result = "lg("
            setFragmentResult("requestKey", bundleOf("bundleKey" to result))
        }

        binding.buttonSinh.setOnClickListener {
            val result = "sh("
            setFragmentResult("requestKey", bundleOf("bundleKey" to result))
        }

        binding.buttonCosh.setOnClickListener {
            val result = "ch("
            setFragmentResult("requestKey", bundleOf("bundleKey" to result))
        }

        binding.buttonTanh.setOnClickListener {
            val result = "tgh("
            setFragmentResult("requestKey", bundleOf("bundleKey" to result))
        }

        binding.buttonExp.setOnClickListener {
            val result = "exp("
            setFragmentResult("requestKey", bundleOf("bundleKey" to result))
        }

        binding.buttonPi.setOnClickListener {
            val result = "pi"
            setFragmentResult("requestKey", bundleOf("bundleKey" to result))
        }

        binding.buttonFact.setOnClickListener {
            val result = "fact("
            setFragmentResult("requestKey", bundleOf("bundleKey" to result))
        }

        binding.buttonPower.setOnClickListener {
            val result = "^("
            setFragmentResult("requestKey", bundleOf("bundleKey" to result))
        }

        binding.buttonSqrt.setOnClickListener {
            val result = "sqrt("
            setFragmentResult("requestKey", bundleOf("bundleKey" to result))
        }
        return binding.root
    }
}