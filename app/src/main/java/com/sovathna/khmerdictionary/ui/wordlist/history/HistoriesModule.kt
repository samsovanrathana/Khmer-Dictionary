package com.sovathna.khmerdictionary.ui.wordlist.history

import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sovathna.khmerdictionary.domain.model.intent.HistoriesIntent
import com.sovathna.khmerdictionary.ui.main.MainActivity
import dagger.Module
import dagger.Provides
import io.reactivex.subjects.PublishSubject
import javax.inject.Named

@Module
class HistoriesModule {

  @Provides
  @Named("instance")
  fun viewModel(fragment: HistoriesFragment, factory: ViewModelProvider.Factory) =
    ViewModelProvider(fragment, factory)[HistoriesViewModel::class.java]

  @Provides
  fun getHistories() = PublishSubject.create<HistoriesIntent.GetWords>()

  @Provides
  fun update() = PublishSubject.create<HistoriesIntent.Update>()

  @Provides
  fun layoutManager(activity: MainActivity): RecyclerView.LayoutManager =
    LinearLayoutManager(activity)

}