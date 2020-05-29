package com.sovathna.khmerdictionary.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import javax.inject.Inject

class MainViewModel @Inject constructor() : ViewModel() {

  var searchTerm = ""

  val titleLiveData = MutableLiveData<String?>()

  var title: String? = null
    set(value) {
      field = value
      titleLiveData.value = value
    }

}