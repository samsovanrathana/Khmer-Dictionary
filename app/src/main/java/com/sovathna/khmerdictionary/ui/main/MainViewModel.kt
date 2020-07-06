package com.sovathna.khmerdictionary.ui.main

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel @ViewModelInject constructor() : ViewModel() {

  var searchTerm = ""

  val titleLiveData = MutableLiveData<String?>()

  var title: String? = null
    set(value) {
      field = value
      titleLiveData.value = value
    }

}