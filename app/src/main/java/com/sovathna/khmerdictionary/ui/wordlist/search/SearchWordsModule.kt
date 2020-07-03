package com.sovathna.khmerdictionary.ui.wordlist.search

import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sovathna.khmerdictionary.domain.model.intent.SearchWordsIntent
import com.sovathna.khmerdictionary.ui.main.MainActivity
import dagger.Module
import dagger.Provides
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject
import javax.inject.Named

@Module
class SearchWordsModule {

  @Provides
  @Named("instance")
  fun viewModel(fragment: SearchWordsFragment, factory: ViewModelProvider.Factory) =
    ViewModelProvider(fragment, factory)[SearchWordsViewModel::class.java]

  @Provides
  @Named("local")
  fun getWordsIntent()= PublishSubject.create<SearchWordsIntent.GetWords>()

  @Provides
  fun layoutManager(activity: MainActivity): RecyclerView.LayoutManager =
    LinearLayoutManager(activity)

}