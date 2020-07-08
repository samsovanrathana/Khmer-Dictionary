package com.sovathna.khmerdictionary.data.interactor.base

import com.sovathna.androidmvi.domain.MviInteractor
import com.sovathna.khmerdictionary.model.intent.SplashIntent
import com.sovathna.khmerdictionary.model.result.SplashResult
import io.reactivex.ObservableTransformer

abstract class SplashInteractor : MviInteractor<SplashIntent, SplashResult>() {

  protected abstract val checkDatabase:
      ObservableTransformer<SplashIntent.CheckDatabase, SplashResult>

  override val intentsProcessor =
    ObservableTransformer<SplashIntent, SplashResult> {
      it.publish { intent ->
        intent
          .ofType(SplashIntent.CheckDatabase::class.java)
          .compose(checkDatabase)
      }
    }
}