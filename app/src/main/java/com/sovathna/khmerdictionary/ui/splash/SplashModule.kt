package com.sovathna.khmerdictionary.ui.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sovathna.khmerdictionary.di.ViewModelKey
import com.sovathna.khmerdictionary.di.scope.SplashScope
import com.sovathna.khmerdictionary.domain.model.intent.SplashIntent
import com.sovathna.khmerdictionary.util.LogUtil
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import io.reactivex.subjects.PublishSubject
import javax.inject.Named

@Module
class SplashModule {

  @Provides
  @SplashScope
  @Named("instance")
  fun viewModel(activity: SplashActivity, factory: ViewModelProvider.Factory) =
    ViewModelProvider(activity, factory)[SplashViewModel::class.java]

  @Provides
  @SplashScope
  fun checkDatabaseIntent() = PublishSubject.create<SplashIntent.CheckDatabase>()

}