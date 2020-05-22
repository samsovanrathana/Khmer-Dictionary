package com.sovathna.androidmvi

import timber.log.Timber

class Logger {
  companion object {
    private var TAG: String = "Logger==>"

    fun init(tag: String = "Logger==>") {
      TAG = tag
      if (BuildConfig.DEBUG)
        Timber.plant(Timber.DebugTree())
    }

    fun d(throwable: Throwable) {
      Timber.tag(TAG).d(throwable)
    }

    fun d(message: String, vararg args: Any) {
      Timber.tag(TAG).d(message, args)
    }

    fun d(throwable: Throwable, message: String, vararg args: Any) {
      Timber.tag(TAG).d(throwable, message, args)
    }

    fun e(throwable: Throwable) {
      Timber.tag(TAG).e(throwable)
    }

    fun e(message: String, vararg args: Any) {
      Timber.tag(TAG).e(message, args)
    }

    fun e(throwable: Throwable, message: String, vararg args: Any) {
      Timber.tag(TAG).e(throwable, message, args)
    }

    fun i(throwable: Throwable) {
      Timber.tag(TAG).i(throwable)
    }

    fun i(message: String, vararg args: Any) {
      Timber.tag(TAG).i(message, args)
    }

    fun i(throwable: Throwable, message: String, vararg args: Any) {
      Timber.tag(TAG).i(throwable, message, args)
    }

    fun v(throwable: Throwable) {
      Timber.tag(TAG).v(throwable)
    }

    fun v(message: String, vararg args: Any) {
      Timber.tag(TAG).v(message, args)
    }

    fun v(throwable: Throwable, message: String, vararg args: Any) {
      Timber.tag(TAG).v(throwable, message, args)
    }

    fun wtf(throwable: Throwable) {
      Timber.tag(TAG).wtf(throwable)
    }

    fun wtf(message: String, vararg args: Any) {
      Timber.tag(TAG).wtf(message, args)
    }

    fun wtf(throwable: Throwable, message: String, vararg args: Any) {
      Timber.tag(TAG).wtf(throwable, message, args)
    }

  }
}