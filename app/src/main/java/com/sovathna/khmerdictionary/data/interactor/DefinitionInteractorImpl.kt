package com.sovathna.khmerdictionary.data.interactor

import com.sovathna.khmerdictionary.domain.interactor.DefinitionInteractor
import com.sovathna.khmerdictionary.domain.model.BookmarkEntity
import com.sovathna.khmerdictionary.domain.model.intent.DefinitionIntent
import com.sovathna.khmerdictionary.domain.model.result.DefinitionResult
import com.sovathna.khmerdictionary.domain.repository.AppRepository
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class DefinitionInteractorImpl @Inject constructor(
  private val repository: AppRepository
) : DefinitionInteractor() {

  override val getDefinition =
    ObservableTransformer<DefinitionIntent.Get, DefinitionResult> {
      it
        .flatMap { intent ->
          Observable
            .merge(
              repository
                .getDefinition(intent.id)
                .map(DefinitionResult::Success),
              repository
                .checkBookmark(intent.id)
                .map(DefinitionResult::BookmarkChecked)
            )
            .subscribeOn(Schedulers.io())
        }
    }

  override val bookmark =
    ObservableTransformer<DefinitionIntent.Bookmark, DefinitionResult> {
      it
        .flatMap { intent ->
          repository
            .checkBookmark(intent.word.id)
            .flatMap {
              if (it) {
                repository
                  .deleteBookmark(intent.word.id)
                  .map { DefinitionResult.BookmarkChecked(false) }
              } else {
                repository
                  .addBookmark(BookmarkEntity(intent.word.name, intent.word.id))
                  .map { DefinitionResult.BookmarkChecked(true) }
              }
            }
            .subscribeOn(Schedulers.io())
        }
    }
}