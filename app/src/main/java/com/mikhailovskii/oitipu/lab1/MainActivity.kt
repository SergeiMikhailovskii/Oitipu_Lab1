package com.mikhailovskii.oitipu.lab1

import android.content.res.Configuration
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels()

    private val addDigitToInputExpression = View.OnClickListener {
        viewModel.input.value = viewModel.input.value + (it as AppCompatButton).text
    }

    private val addSignToInputExpression = View.OnClickListener {
        viewModel.input.value = viewModel.input.value + " ${(it as AppCompatButton).text} "
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tv_input_text.apply {
            movementMethod = ScrollingMovementMethod()
        }.setHorizontallyScrolling(true)

        if (isPortraitOrientation()) {
            initPortraitOrientationListeners()
        } else {
            initLandscapeOrientationListeners()
        }
    }

    private fun initLandscapeOrientationListeners() {
        btn_left_bracket.setOnClickListener(addDigitToInputExpression)
        btn_right_bracket.setOnClickListener(addDigitToInputExpression)
        btn_seven.setOnClickListener(addDigitToInputExpression)
        btn_eight.setOnClickListener(addDigitToInputExpression)
        btn_nine.setOnClickListener(addDigitToInputExpression)
        btn_four.setOnClickListener(addDigitToInputExpression)
        btn_five.setOnClickListener(addDigitToInputExpression)
        btn_six.setOnClickListener(addDigitToInputExpression)
        btn_one.setOnClickListener(addDigitToInputExpression)
        btn_two.setOnClickListener(addDigitToInputExpression)
        btn_three.setOnClickListener(addDigitToInputExpression)
        btn_point.setOnClickListener(addDigitToInputExpression)

        btn_clear.setOnClickListener {
            viewModel.input.value = ""
            viewModel.output.value = ""
        }

        btn_equals_hor?.setOnClickListener {
            viewModel.calculateWithLib()
        }

        viewModel.input.observe(this, Observer<String> {
            tv_input_text.text = it.replace(" ", "")
        })

        viewModel.output.observe(this, Observer<String> {
            tv_output_text.text = it
        })
    }

    private fun initPortraitOrientationListeners() {
        btn_left_bracket.setOnClickListener(addDigitToInputExpression)
        btn_right_bracket.setOnClickListener(addDigitToInputExpression)
        btn_seven.setOnClickListener(addDigitToInputExpression)
        btn_eight.setOnClickListener(addDigitToInputExpression)
        btn_nine.setOnClickListener(addDigitToInputExpression)
        btn_four.setOnClickListener(addDigitToInputExpression)
        btn_five.setOnClickListener(addDigitToInputExpression)
        btn_six.setOnClickListener(addDigitToInputExpression)
        btn_one.setOnClickListener(addDigitToInputExpression)
        btn_two.setOnClickListener(addDigitToInputExpression)
        btn_three.setOnClickListener(addDigitToInputExpression)
        btn_point.setOnClickListener(addDigitToInputExpression)

        btn_multiply.setOnClickListener(addSignToInputExpression)
        btn_divide.setOnClickListener(addSignToInputExpression)
        btn_substract.setOnClickListener(addSignToInputExpression)
        btn_add.setOnClickListener(addSignToInputExpression)

        btn_equals?.setOnClickListener {
            viewModel.calculatePln()
        }

        btn_clear.setOnClickListener {
            viewModel.input.value = ""
            viewModel.output.value = ""
        }

        viewModel.input.observe(this, Observer<String> {
            tv_input_text.text = it.replace(" ", "")
        })

        viewModel.output.observe(this, Observer<String> {
            tv_output_text.text = it
        })

    }

    private fun isPortraitOrientation() =
        resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT


}