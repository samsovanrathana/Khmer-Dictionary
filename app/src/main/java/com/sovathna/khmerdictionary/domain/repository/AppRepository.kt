package com.sovathna.khmerdictionary.domain.repository

import com.sovathna.khmerdictionary.domain.model.BookmarkEntity
import com.sovathna.khmerdictionary.domain.model.Definition
import com.sovathna.khmerdictionary.domain.model.Word
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
}