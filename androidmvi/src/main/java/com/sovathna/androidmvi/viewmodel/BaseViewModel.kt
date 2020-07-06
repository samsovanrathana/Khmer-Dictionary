package com.sovathna.androidmvi.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.sovathna.androidmvi.intent.MviIntent
import com.sovathna.androidmvi.state.MviState
import io.reactivex.Observable

abstract class BaseViewModel<I : MviIntent, S : MviState> : ViewModel() {
  abstract fun init(intents: Observable<I>)

  abstract val stateLiveData: LiveData<S>
}