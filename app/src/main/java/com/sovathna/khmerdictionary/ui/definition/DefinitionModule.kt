package com.sovathna.khmerdictionary.ui.definition

import androidx.lifecycle.MutableLiveData
import com.sovathna.khmerdictionary.domain.model.Word
import dagger.Module
import dagger.Provides
import io.reactivex.subjects.PublishSubject

@Module(
  includes = [
    DefinitionFragmentsModule::class
  ]
)
class DefinitionModule {
  @Provides
  fun fab() = PublishSubject.create<Boolean>()

  @Provides
  fun selectedLiveData() = MutableLiveData<Word>()
}