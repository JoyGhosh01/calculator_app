package com.example.calculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import com.example.calculator.databinding.ActivityMainBinding
import net.objecthunter.exp4j.Expression
import net.objecthunter.exp4j.ExpressionBuilder

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    var lastNumeric = false
    var stateError = false
    var lastDot = false
    private lateinit var expression: Expression

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }

    fun onAllClearClick(view: View) {
        binding.dataTv.text = ""
        binding.resultTv.text = ""
        stateError = false
        lastDot = false
        lastNumeric = false
        binding.resultTv.visibility = View.GONE
    }

    fun onEqualClick(view: View) {
        onEqual()
        binding.dataTv.text = binding.resultTv.text.toString().drop(1)
    }

    fun onOperatorClick(view: View) {
        if(!stateError && lastNumeric){
            binding.dataTv.append((view as Button).text)
            lastDot = false
            lastNumeric = false
            onEqual()
        }
    }

    fun onEraseClick(view: View) {
        binding.dataTv.text = binding.dataTv.text.toString().dropLast(1)
        try {
            var lastchar = binding.dataTv.text.toString().last()
            if(lastchar.isDigit()){
                onEqual()
            }
        }
        catch (e: java.lang.Exception){
            binding.resultTv.text = ""
            binding.resultTv.visibility = View.GONE
            Log.e("last char error", e.toString())
        }
    }

    fun onClearClick(view: View) {
        binding.dataTv.text = ""
        lastNumeric = false
    }


    fun onDigitClick(view: View) {
        if(stateError){
            binding.dataTv.text = (view as Button).text
            stateError = false
        }
        else{
            binding.dataTv.append((view as Button).text)
        }
        lastNumeric = true
        onEqual()
    }

    fun onEqual(){
        if(lastNumeric && !stateError){
            val txt = binding.dataTv.text.toString()
            expression = ExpressionBuilder(txt).build()
            try {
                val result = expression.evaluate()
                binding.resultTv.visibility = View.VISIBLE
                binding.resultTv.text = "= " + result.toString()
            }
            catch (ex: java.lang.ArithmeticException){
                Log.e( "evaluate error",ex.toString())
                binding.resultTv.text = "error"
                stateError = true
                lastNumeric = false
            }
        }
    }
}