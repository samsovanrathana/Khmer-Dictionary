package com.sovathna.khmerdictionary.data.repository

import com.sovathna.khmerdictionary.data.local.AppDatabase
import com.sovathna.khmerdictionary.data.local.LocalDatabase
import com.sovathna.khmerdictionary.domain.model.BookmarkEntity
import com.sovathna.khmerdictionary.domain.model.Definition
import com.sovathna.khmerdictionary.domain.model.HistoryEntity
import com.sovathna.khmerdictionary.domain.model.Word
import com.sovathna.khmerdictionary.domain.repository.AppRepository
import io.reactivex.Observable
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppRepositoryImpl @Inject constructor(
  db: AppDatabase,
  local: LocalDatabase
) : AppRepository {

  private val wordDao = db.wordDao()
  private val historyDao = local.historyDao()
  private val bookmarkDao = local.bookmarkDao()

  override fun getMainWordList(offset: Int, pageSize: Int): Observable<List<Word>> {
    return wordDao
      .getMainWordList(offset, pageSize)
      .map { entities ->
        entities.map { entity -> entity.toWord() }
      }
      .toObservable()
  }

  override fun addHistory(word: Word): Observable<Long> {
    return historyDao
      .add(HistoryEntity(word.name, word.id))
      .toObservable()
  }

  override fun getHistories(offset: Int, pageSize: Int): Observable<List<Word>> {
    return historyDao
      .all(offset, pageSize)
      .map { entities ->
        entities.map { entity -> entity.toWord() }
      }
      .toObservable()
  }

  override fun getSearchWords(
    searchTerm: String,
    offset: Int,
    pageSize: Int
  ): Observable<List<Word>> {
    return wordDao
      .getFilterWordList("$searchTerm%", offset, pageSize)
      .map { entities ->
        entities.map { entity -> entity.toWord() }
      }
      .toObservable()
  }

  override fun getDefinition(id: Long): Observable<Definition> =
    wordDao
      .getDefinition(id)
      .map { Definition(it.word, it.definition) }
      .toObservable()

  override fun checkBookmark(id: Long): Observable<Boolean> =
    bookmarkDao
      .get(id).map { true }
      .onErrorReturn { false }
      .toObservable()

  override fun addBookmark(entity: BookmarkEntity): Observable<Long> =
    bookmarkDao
      .add(entity)
      .toObservable()

  override fun deleteBookmark(id: Long): Observable<Int> =
    bookmarkDao
      .delete(id)
      .toObservable()

}