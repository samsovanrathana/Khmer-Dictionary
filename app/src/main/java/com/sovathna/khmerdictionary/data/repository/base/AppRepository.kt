package com.sovathna.khmerdictionary.data.repository.base

import androidx.paging.Pager
import com.sovathna.khmerdictionary.model.Definition
import com.sovathna.khmerdictionary.model.Word
import com.sovathna.khmerdictionary.model.entity.BookmarkEntity
import com.sovathna.khmerdictionary.model.entity.HistoryUI
import com.sovathna.khmerdictionary.model.entity.WordUI
import io.reactivex.Observable

interface AppRepository {
  fun getWords(
    offset: Int,
    pageSize: Int
  ): Observable<List<Word>>

  fun getSearches(
    searchTerm: String,
    offset: Int,
    pageSize: Int
  ): Observable<List<Word>>

  fun addHistory(
    word: Word
  ): Observable<Long>

  fun getHistoriesPager(): Observable<Pager<Int, HistoryUI>>

  fun getBookmarks(
    offset: Int,
    pageSize: Int
  ): Observable<List<Word>>

  fun getDefinition(
    id: Long
  ): Observable<Definition>

  fun checkBookmark(
    id: Long
  ): Observable<Boolean>

  fun addBookmark(
    entity: BookmarkEntity
  ): Observable<Long>

  fun deleteBookmark(
    id: Long
  ): Observable<Int>

  fun clearHistories(): Observable<Int>

  fun clearBookmarks(): Observable<Int>

  fun getWordsPager(): Observable<Pager<Int, WordUI>>

  fun selectWord(id: Long?): Observable<Int>
}