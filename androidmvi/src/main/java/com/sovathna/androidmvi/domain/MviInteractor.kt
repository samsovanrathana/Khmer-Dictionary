package com.sovathna.androidmvi.domain

import com.sovathna.androidmvi.intent.MviIntent
import com.sovathna.androidmvi.result.MviResult
import io.reactivex.ObservableTransformer

abstract class MviInteractor<I : MviIntent, R : MviResult> {

  abstract val intentsProcessor: ObservableTransformer<I, R>

}
