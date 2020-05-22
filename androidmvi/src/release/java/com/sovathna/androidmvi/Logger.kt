package com.sovathna.androidmvi

class Logger {
  companion object {

    fun init(tag: String = "Logger==>") {}

    fun d(throwable: Throwable) {}

    fun d(message: String, vararg args: Any) {}

    fun d(throwable: Throwable, message: String, vararg args: Any) {}

    fun e(throwable: Throwable) {}

    fun e(message: String, vararg args: Any) {}

    fun e(throwable: Throwable, message: String, vararg args: Any) {}

    fun i(throwable: Throwable) {}

    fun i(message: String, vararg args: Any) {}

    fun i(throwable: Throwable, message: String, vararg args: Any) {}

    fun v(throwable: Throwable) {}

    fun v(message: String, vararg args: Any) {}

    fun v(throwable: Throwable, message: String, vararg args: Any) {}

    fun wtf(throwable: Throwable) {}

    fun wtf(message: String, vararg args: Any) {}

    fun wtf(throwable: Throwable, message: String, vararg args: Any) {}

  }
}