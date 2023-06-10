package com.example.storyappsubmission.ui.customview

import android.content.Context
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.Patterns
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import com.example.storyappsubmission.R

class EmailEditText : AppCompatEditText, View.OnTouchListener {

    private lateinit var error: Drawable

    constructor(context: Context): super(context){
        init()
    }

    constructor(context : Context , attrs: AttributeSet): super(context , attrs){
        init()
    }

    constructor(context: Context , attrs: AttributeSet , defStyleAttr: Int) : super(context , attrs , defStyleAttr) {
        init()
    }

    fun isValidEmail(email: String): Boolean {
        val pattern = Patterns.EMAIL_ADDRESS
        return pattern.matcher(email).matches()
    }

    private fun init() {
        error = ContextCompat.getDrawable(context , R.drawable.ic_error) as Drawable
        setOnTouchListener(this)

        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                s : CharSequence? ,
                start : Int ,
                count : Int ,
                after : Int
            ) {
            }

            override fun onTextChanged(
                s : CharSequence? ,
                start : Int ,
                before : Int ,
                count : Int
            ) {
                if (s.toString().isNotEmpty()){
                    if (!isValidEmail(s.toString())) {
                        setError("Email Must Valid")
                    } else {
                        setButtonDrawables()
                    }
                }
            }

            override fun afterTextChanged(s : Editable?) {
            }

        })
    }

    private fun setButtonDrawables(startOfTheText: Drawable? = null, topOfTheText:Drawable? = null, endOfTheText:Drawable? = null, bottomOfTheText: Drawable? = null){
        setCompoundDrawablesWithIntrinsicBounds(startOfTheText, topOfTheText, endOfTheText, bottomOfTheText)
    }

    override fun onTouch(v : View? , event : MotionEvent?) : Boolean {
        return false
    }
}