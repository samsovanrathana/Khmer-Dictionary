package com.sovathna.khmerdictionary.data.interactor

import androidx.paging.rxjava2.RxPagingSource
import com.sovathna.khmerdictionary.data.local.db.AppDatabase
import com.sovathna.khmerdictionary.model.Word
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

class WordsPagingSource(
  db: AppDatabase
) : RxPagingSource<Int, Word>() {
  private val wordDao = db.wordDao()
  override fun loadSingle(params: LoadParams<Int>): Single<LoadResult<Int, Word>> {
    val offset = params.key ?: 0
    return wordDao
      .get(offset, params.loadSize)
      .subscribeOn(Schedulers.io())
      .map { entities ->
        val tmp = entities.map { entity -> entity.toWord() }
        LoadResult.Page(
          tmp,
          null,
          if (tmp.size >= params.loadSize) offset + params.loadSize else null
        ) as LoadResult<Int, Word>
      }.onErrorReturn { LoadResult.Error(it) }
  }
}