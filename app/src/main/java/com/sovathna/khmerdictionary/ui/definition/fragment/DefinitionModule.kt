package com.sovathna.khmerdictionary.ui.definition.fragment

import androidx.lifecycle.ViewModelProvider
import com.sovathna.khmerdictionary.domain.model.intent.DefinitionIntent
import dagger.Module
import dagger.Provides
import io.reactivex.subjects.PublishSubject
import javax.inject.Named

@Module
class DefinitionModule {

  @Provides
  @Named("instance")
  fun viewModel(fragment: DefinitionFragment, factory: ViewModelProvider.Factory) =
    ViewModelProvider(fragment, factory)[DefinitionViewModel::class.java]

}