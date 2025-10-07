package com.estanesti.isogen

import adapters.AdapterRecyclerIso
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.estanesti.isogen.databinding.ActivityMainBinding
import helpers.IsoHelper
import models.Codec
import viewmodels.ModeViewModel

class MainActivity : AppCompatActivity() {
    private val viewModel: ModeViewModel by viewModels()
    private lateinit var b: ActivityMainBinding
    private val codecs = mutableListOf<Codec>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityMainBinding.inflate(layoutInflater)
        setContentView(b.root)

        observe()

        b.btGenerate.setOnClickListener { viewModel.mode.value = ModeViewModel.Mode.GENERATE }
        b.btParse.setOnClickListener { viewModel.mode.value = ModeViewModel.Mode.PARSE }

        b.rvIso.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true)
        b.rvIso.adapter = AdapterRecyclerIso().apply { submitList(codecs) }

        b.btConvert.setOnClickListener {
            if (viewModel.mode.value == ModeViewModel.Mode.GENERATE) {
                b.etCard.error = null
                b.etAmount.error = null
                var hasError = false

                val cardNumber = b.etCard.editableText.toString()
                if (cardNumber.length < 16) {
                    b.etCard.error = "Invalid Card Number"
                    hasError = true
                }

                val amount = b.etAmount.editableText.toString()
                if (amount.isBlank()) {
                    b.etAmount.error = "Invalid Amount"
                    hasError = true
                }

                if (!hasError) {
                    val message = IsoHelper.buildMessage(cardNumber, amount)
                    codecs.add(Codec(message, true))
                    b.rvIso.adapter = AdapterRecyclerIso().apply { submitList(codecs) }
                }
            } else {
                b.etIso.error = null
                val iso = b.etIso.editableText.toString()
                if (iso.isBlank()) {
                    b.etIso.error = "Invalid ISO8583 Message"
                } else {
                    val message = IsoHelper.parseMessage(iso, this)
                    codecs.add(Codec(message, false))
                    b.rvIso.adapter = AdapterRecyclerIso().apply { submitList(codecs) }
                }
            }
        }
    }

    private fun observe() {
        viewModel.mode.observe(this) { mode ->
            val colorSelected = ColorStateList.valueOf(ContextCompat.getColor(this@MainActivity, R.color.purple_500))
            val colorUnselected = ColorStateList.valueOf(ContextCompat.getColor(this@MainActivity, R.color.purple_200))
            when (mode) {
                ModeViewModel.Mode.GENERATE -> {
                    b.btGenerate.backgroundTintList = colorSelected
                    b.btParse.backgroundTintList = colorUnselected
                    b.cvCard.visibility = View.VISIBLE
                    b.cvAmount.visibility = View.VISIBLE
                    b.cvIso.visibility = View.GONE
                }

                ModeViewModel.Mode.PARSE    -> {
                    b.btGenerate.backgroundTintList = colorUnselected
                    b.btParse.backgroundTintList = colorSelected
                    b.cvCard.visibility = View.GONE
                    b.cvAmount.visibility = View.GONE
                    b.cvIso.visibility = View.VISIBLE
                }
            }
        }
    }
}