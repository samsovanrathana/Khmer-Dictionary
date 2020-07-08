package com.sovathna.khmerdictionary.data.repository.base

import androidx.paging.Pager
import com.sovathna.khmerdictionary.model.Definition
import com.sovathna.khmerdictionary.model.Word
import com.sovathna.khmerdictionary.model.entity.BookmarkEntity
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

  fun getHistories(
    offset: Int,
    pageSize: Int
  ): Observable<List<Word>>

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

  fun getPagingWords(
    offset: Int
  ): Observable<Pager<Int, Word>>
}