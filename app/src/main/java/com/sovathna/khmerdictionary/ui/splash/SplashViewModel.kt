package com.sovathna.khmerdictionary.ui.splash

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.sovathna.androidmvi.Event
import com.sovathna.androidmvi.viewmodel.MviViewModel
import com.sovathna.khmerdictionary.domain.interactor.SplashInteractor
import com.sovathna.khmerdictionary.domain.model.intent.SplashIntent
import com.sovathna.khmerdictionary.domain.model.result.SplashResult
import com.sovathna.khmerdictionary.domain.model.state.SplashState
import com.sovathna.khmerdictionary.util.LogUtil
import io.reactivex.BackpressureStrategy
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.BiFunction
import javax.inject.Inject

class SplashViewModel @Inject constructor(
  private val interactor: SplashInteractor
) : MviViewModel<SplashIntent, SplashResult, SplashState>() {

  override val reducer = BiFunction<SplashState, SplashResult, SplashState> { state, result ->
    when (result) {
      is SplashResult.Progressing -> SplashState(isInit = false, isProgress = true)

      is SplashResult.Fail ->
        state.copy(
          error = result.throwable.message ?: "An error has occurred!",
          isProgress = false
        )
      is SplashResult.Downloading ->
        state.copy(
          isProgress = false,
          downloaded = result.downloaded,
          total = result.total
        )
      is SplashResult.Success ->
        state.copy(successEvent = Event(Unit))
    }
  }

  override val stateLiveData: LiveData<SplashState> =
    MutableLiveData<SplashState>().apply {
      intents.compose(interactor.intentsProcessor)
        .doOnSubscribe { disposables.add(it) }
        .scan(SplashState(), reducer)
        .distinctUntilChanged()
        .toFlowable(BackpressureStrategy.BUFFER)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(::setValue)
    }

  override fun onCleared() {
    super.onCleared()
    LogUtil.i("SplashViewModel cleared")
  }
}