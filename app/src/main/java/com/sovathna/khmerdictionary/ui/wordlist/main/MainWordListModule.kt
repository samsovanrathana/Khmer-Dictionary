package com.sovathna.khmerdictionary.ui.wordlist.main

import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sovathna.khmerdictionary.domain.model.intent.MainWordListIntent
import com.sovathna.khmerdictionary.ui.main.MainActivity
import dagger.Module
import dagger.Provides
import io.reactivex.subjects.PublishSubject
import javax.inject.Named

@Module
class MainWordListModule {

  @Provides
  @Named("instance")
  fun viewModel(fragment: MainWordListFragment, factory: ViewModelProvider.Factory) =
    ViewModelProvider(fragment, factory)[MainWordListViewModel::class.java]

  @Provides
  fun getWordsIntent() = PublishSubject.create<MainWordListIntent.GetWords>()

  @Provides
  fun layoutManager(activity: MainActivity): RecyclerView.LayoutManager =
    LinearLayoutManager(activity)

}