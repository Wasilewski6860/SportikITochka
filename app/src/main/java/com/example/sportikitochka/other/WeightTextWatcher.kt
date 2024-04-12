package com.example.sportikitochka.other

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText

class WeightTextWatcher(private val editText: EditText) : TextWatcher {

    private var isFormatting = false

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

    override fun afterTextChanged(editable: Editable?) {
        if (isFormatting) {
            return
        }

        isFormatting = true

        val originalString = editable.toString()

        val stringToRemove = originalString
        // Удаляем все нецифровые символы из исходной строки
        val onlyNumbers = stringToRemove.replace("[^\\d.]".toRegex(), "")

        val formattedString = StringBuilder()

        if (!onlyNumbers.startsWith(".")) formattedString.append(onlyNumbers)

//
//
//        if (onlyNumbers.length<2){
//            formattedString.append(originalString)
//        }
//        if (onlyNumbers.length >= 2) {
//            formattedString.append(onlyNumbers.substring(0, 2))
//            formattedString.append(".")
//        }
//        if (onlyNumbers.length == 3) {
//            formattedString.append(onlyNumbers.substring(2, 3))
//        }
//        if (onlyNumbers.length >= 4) {
//            formattedString.append(onlyNumbers.substring(2, 4))
//            formattedString.append(".")
//        }
//        if (onlyNumbers.length == 5) {
//            formattedString.append(onlyNumbers.substring(4, 5))
//        }
//        if (onlyNumbers.length >= 6) {
//            formattedString.append(onlyNumbers.substring(4, 6))
//        }

        editText.removeTextChangedListener(this)
        editText.setText(formattedString.toString())
        editText.setSelection(formattedString.length)
        editText.addTextChangedListener(this)

        isFormatting = false

        // Если была введена последняя цифра для года (6 символов), запрещаем ввод
//        if (originalString.length >= 6) {
//            editText.isEnabled = false
//        }
    }
}