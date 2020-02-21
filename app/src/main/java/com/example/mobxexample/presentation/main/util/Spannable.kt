package com.example.mobxexample.presentation.main.util

import android.text.Spannable
import android.text.Spanned
import android.text.style.StrikethroughSpan

fun Spannable.setStrikeThroughSpan() {
    this.setSpan(StrikethroughSpan(), 0, this.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
}
