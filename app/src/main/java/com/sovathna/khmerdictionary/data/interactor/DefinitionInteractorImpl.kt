package com.sovathna.khmerdictionary.data.interactor

import com.sovathna.khmerdictionary.data.interactor.base.DefinitionInteractor
import com.sovathna.khmerdictionary.model.entity.BookmarkEntity
import com.sovathna.khmerdictionary.model.intent.DefinitionIntent
import com.sovathna.khmerdictionary.model.result.DefinitionResult
import com.sovathna.khmerdictionary.data.repository.base.AppRepository
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class DefinitionInteractorImpl @Inject constructor(
  private val repository: AppRepository
) : DefinitionInteractor() {

  override val getDefinition =
    ObservableTransformer<DefinitionIntent.GetDefinition, DefinitionResult> {
      it.flatMap { intent ->
        Observable
          .merge(
            repository
              .getDefinition(intent.word.id)
              .map(DefinitionResult::Success),
            repository
              .checkBookmark(intent.word.id)
              .map(DefinitionResult::CheckBookmarkSuccess),
            repository
              .addHistory(intent.word)
              .map(DefinitionResult::AddHistorySuccess)
          )
          .subscribeOn(Schedulers.io())
      }
    }

  override val getQuickDefinition =
    ObservableTransformer<DefinitionIntent.GetQuickDefinition, DefinitionResult> {
      it.flatMap { intent ->
        repository
          .getDefinition(intent.id)
          .subscribeOn(Schedulers.io())
          .map(DefinitionResult::QuickSuccess)
      }
    }

  override val addDeleteBookmark =
    ObservableTransformer<DefinitionIntent.AddDeleteBookmark, DefinitionResult> {
      it.flatMap { intent ->
        repository
          .checkBookmark(intent.word.id)
          .subscribeOn(Schedulers.io())
          .flatMap {
            if (it) {
              repository
                .deleteBookmark(intent.word.id)
                .map { DefinitionResult.BookmarkSuccess(false) }
            } else {
              repository
                .addBookmark(
                  BookmarkEntity(
                    intent.word.name,
                    intent.word.id
                  )
                )
                .map { DefinitionResult.BookmarkSuccess(true) }
            }
          }
      }
    }
}