package com.goazzi.mycompose.util

import timber.log.Timber

fun String.d(message: String = "") = Timber.tag(this).d(message = message)

fun String.e(message: String = "", e: Exception? = null) =
    Timber.tag(this).e(message = "$message: ${e?.message ?: ""}")