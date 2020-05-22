package com.sovathna.khmerdictionary.domain.repository

import com.sovathna.khmerdictionary.domain.model.*
import io.reactivex.Observable

interface AppRepository {

  fun getWords(tableName: String): Observable<List<MainWordEntity>>

  fun filterWordList(
    filterType: FilterType,
    searchTerm: String?,
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
}