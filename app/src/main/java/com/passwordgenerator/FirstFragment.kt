package com.passwordgenerator

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.passwordgenerator.databinding.FragmentFirstBinding

class FirstFragment : Fragment() {

    lateinit var checkBoxUpperCase: CheckBox
    lateinit var checkBoxLowerCase: CheckBox
    lateinit var checkBoxSpace: CheckBox
    lateinit var checkBoxDigits: CheckBox
    lateinit var checkBoxSpecial: CheckBox
    lateinit var checkBoxBrackets: CheckBox
    lateinit var checkBoxMinus: CheckBox
    lateinit var checkBoxUnderscore: CheckBox

    lateinit var editTextGeneratedPassword: EditText
    lateinit var textViewOutput : TextView

    private val lowerCaseChars = "abcdefghijklmnopqrstuvwxyz"
    private val upperCaseChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
    private val digitChars = "0123456789"
    private val spaceChars = " "
    private val specialChars = "\\^-=\$!|?*+."
    private val bracketsChars = "([{}])"
    private val minusChars = "-"
    private val underscoreChars = "_"
    lateinit var clip: ClipData
    lateinit var clipboard: ClipboardManager
    lateinit var chars: StringBuilder
    private var _binding: FragmentFirstBinding? = null

    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!
    private var showText = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        val view = binding.root

        binding.numberpicker.minValue = 3
        binding.numberpicker.maxValue = 128
        binding.numberpicker.value = 20

        checkBoxUpperCase = binding.checkboxuppercase
        checkBoxLowerCase = binding.checkboxlowercase
        checkBoxSpace = binding.checkboxspace
        checkBoxSpecial = binding.checkboxspecial
        checkBoxDigits = binding.checkboxdigits
        checkBoxBrackets = binding.checkboxbrackets
        checkBoxMinus = binding.checkboxminus
        checkBoxUnderscore = binding.checkboxunderscore

        editTextGeneratedPassword = binding.editTextGeneratedPassword
        editTextGeneratedPassword.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {}

            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence, start: Int,
                before: Int, count: Int
            ) {

                calculatePasswordStrength()
            }
        })

        binding.buttonShowHide.setOnClickListener {
            buttonShowHideClicked();
        }

        binding.buttonCopyToClipboard.setOnClickListener {
            buttonCopyToClipboardClicked();
        }
        binding.buttonGenerate.setOnClickListener { generatePasswordClicked() }

        textViewOutput = binding.textViewOutput

        clipboard = requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun calculatePasswordStrength() {

        //if(editTextGeneratedPassword.getText().toString().length == 0) return
        textViewOutput.text = getPasswordStrengthMessage(editTextGeneratedPassword.getText().toString())
    }

    private fun buttonCopyToClipboardClicked() {
        clip = ClipData.newPlainText("RANDOM UUID", editTextGeneratedPassword.getText())
        clipboard.setPrimaryClip(clip);
    }

    private fun buttonShowHideClicked() {
        if (showText) {
            editTextGeneratedPassword.transformationMethod =
                HideReturnsTransformationMethod.getInstance()
        } else {
              editTextGeneratedPassword.transformationMethod =
                PasswordTransformationMethod.getInstance()
        }
        showText = !showText
    }

    private fun generatePasswordClicked(): Unit {
        chars = StringBuilder()
        var password: StringBuilder = StringBuilder()

        if (checkBoxUpperCase.isChecked)
            chars.append(upperCaseChars)
        if (checkBoxLowerCase.isChecked)
            chars.append(lowerCaseChars)
        if (checkBoxSpace.isChecked)
            chars.append(spaceChars)
        if (checkBoxDigits.isChecked)
            chars.append(digitChars)
        if (checkBoxMinus.isChecked)
            chars.append(minusChars)
        if (checkBoxSpecial.isChecked)
            chars.append(specialChars)
        if (checkBoxUnderscore.isChecked)
            chars.append(underscoreChars)
        if (checkBoxBrackets.isChecked)
            chars.append(bracketsChars)

        if (chars.length == 0)
            password.append("")
        else {
            for (i in 0 until binding.numberpicker.value) {
                password.append(chars[Math.floor(Math.random() * chars.length).toInt()])
            }
        }
        editTextGeneratedPassword.setText(password)
    }
}
