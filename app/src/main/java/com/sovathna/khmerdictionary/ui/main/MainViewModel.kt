package com.sovathna.khmerdictionary.ui.main

import androidx.lifecycle.LiveDataReactiveStreams
import androidx.lifecycle.ViewModel
import io.reactivex.BackpressureStrategy
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

class MainViewModel @Inject constructor() : ViewModel() {

  val page = PublishSubject.create<String>()

  val pageLiveData =
    LiveDataReactiveStreams
      .fromPublisher(
        page.toFlowable(BackpressureStrategy.BUFFER)
      )

}