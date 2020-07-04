package com.sovathna.khmerdictionary.ui.splash

import androidx.lifecycle.ViewModelProvider
import com.sovathna.khmerdictionary.di.scope.SplashScope
import com.sovathna.khmerdictionary.domain.model.intent.SplashIntent
import dagger.Module
import dagger.Provides
import io.reactivex.subjects.PublishSubject
import javax.inject.Named

@Module
class SplashModule {

  @Provides
  @SplashScope
  @Named("instance")
  fun viewModel(
    activity: SplashActivity,
    factory: ViewModelProvider.Factory
  ): SplashViewModel =
    ViewModelProvider(activity, factory)[SplashViewModel::class.java]

}