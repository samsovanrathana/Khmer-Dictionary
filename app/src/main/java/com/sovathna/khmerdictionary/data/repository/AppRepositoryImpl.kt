package com.sovathna.khmerdictionary.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.room.EmptyResultSetException
import com.sovathna.khmerdictionary.Const
import com.sovathna.khmerdictionary.data.interactor.HistoriesRemoteMediator
import com.sovathna.khmerdictionary.data.interactor.WordsRemoteMediator
import com.sovathna.khmerdictionary.data.local.db.AppDatabase
import com.sovathna.khmerdictionary.data.local.db.LocalDatabase
import com.sovathna.khmerdictionary.data.repository.base.AppRepository
import com.sovathna.khmerdictionary.model.Definition
import com.sovathna.khmerdictionary.model.Word
import com.sovathna.khmerdictionary.model.entity.BookmarkEntity
import com.sovathna.khmerdictionary.model.entity.HistoryEntity
import com.sovathna.khmerdictionary.model.entity.HistoryUI
import com.sovathna.khmerdictionary.model.entity.WordUI
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppRepositoryImpl @Inject constructor(
  private val db: AppDatabase,
  private val local: LocalDatabase
) : AppRepository {

  private val wordDao = db.wordDao()
  private val historyDao = local.historyDao()
  private val bookmarkDao = local.bookmarkDao()
  private val wordUIDao = local.wordUIDao()

  override fun getWords(offset: Int, pageSize: Int): Observable<List<Word>> {
    return wordDao
      .get(offset, pageSize)
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

  override fun getHistoriesPager(): Observable<Pager<Int, HistoryUI>> {
    return Observable.just(Pager(
      config = PagingConfig(pageSize = Const.PAGE_SIZE),
      remoteMediator = HistoriesRemoteMediator(local),
      pagingSourceFactory = { local.historyUIDao().get() }
    ))
  }

  override fun getBookmarks(offset: Int, pageSize: Int): Observable<List<Word>> {
    return bookmarkDao
      .get(offset, pageSize)
      .map { entities ->
        entities.map { entity -> entity.toWord() }
      }
      .toObservable()
  }

  override fun getSearches(
    searchTerm: String,
    offset: Int,
    pageSize: Int
  ): Observable<List<Word>> {
    return wordDao
      .search("$searchTerm%", offset, pageSize)
      .map { entities ->
        entities.map { entity -> entity.toWord() }
      }
      .toObservable()
  }

  override fun getDefinition(id: Long): Observable<Definition> =
    wordDao
      .get(id)
      .map {
        Definition(
          it.word,
          it.definition
            .replace("<\"", "<a href=\"")
            .replace("/a", "</a>")
            .replace("\\n", "<br><br>")
            .replace(" : ", " : ឧ. ")
            .replace("ន.", "<span style=\"color:#D50000\">ន.</span>")
            .replace("កិ. វិ.", "<span style=\"color:#D50000\">កិ. វិ.</span>")
            .replace("កិ.វិ.", "<span style=\"color:#D50000\">កិ.វិ.</span>")
            .replace("កិ.", "<span style=\"color:#D50000\">កិ.</span>")
            .replace("និ.", "<span style=\"color:#D50000\">និ.</span>")
            .replace("គុ.", "<span style=\"color:#D50000\">គុ.</span>")
        )
      }
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

  override fun clearHistories(): Observable<Int> =
    historyDao
      .clear()
      .toObservable()

  override fun clearBookmarks(): Observable<Int> =
    bookmarkDao
      .clear()
      .toObservable()

  override fun getWordsPager(): Observable<Pager<Int, WordUI>> {
    return Observable.just(Pager(
      config = PagingConfig(pageSize = Const.PAGE_SIZE),
      remoteMediator = WordsRemoteMediator(db, local),
      pagingSourceFactory = { local.wordUIDao().get() }
    ))
  }

  override fun selectWord(id: Long?): Observable<Int> {
    val tmp = wordUIDao.getSelected()
      .subscribeOn(Schedulers.io())
      .onErrorReturn {
        if (it is EmptyResultSetException) 0 else throw it
      }
      .toFlowable()
    return if (id != null) {
      tmp
        .flatMap {
          Single.merge(
            wordUIDao.updateSelected(it, false)
              .subscribeOn(Schedulers.io()),
            wordUIDao.updateSelected(id, true)
              .subscribeOn(Schedulers.io())
          )
        }
//        .flatMap {
//          wordUIDao.updateSelected(id, true)
//            .subscribeOn(Schedulers.io())
//        }
    } else {
      tmp
        .flatMap {
          wordUIDao
            .updateSelected(it, false)
            .subscribeOn(Schedulers.io())
            .toFlowable()
        }
    }.toObservable()
  }
}