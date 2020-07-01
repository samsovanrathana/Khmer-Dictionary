package com.sovathna.khmerdictionary.ui.definition

import androidx.lifecycle.MutableLiveData
import com.sovathna.androidmvi.livedata.Event
import com.sovathna.khmerdictionary.di.scope.DefinitionScope
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
  @DefinitionScope
  fun fab() = PublishSubject.create<Boolean>()

  @Provides
  @DefinitionScope
  fun selectedLiveData() = MutableLiveData<Word>()

  @Provides
  @DefinitionScope
  fun menuItemClick() =MutableLiveData<Event<String>>()

  @Provides
  @DefinitionScope
  fun bookmarkedLiveData() = MutableLiveData<Boolean>()
}