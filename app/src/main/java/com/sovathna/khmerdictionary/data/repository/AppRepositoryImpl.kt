package com.sovathna.khmerdictionary.data.repository

import com.sovathna.khmerdictionary.data.local.AppDatabase
import com.sovathna.khmerdictionary.data.local.LocalDatabase
import com.sovathna.khmerdictionary.domain.model.Definition
import com.sovathna.khmerdictionary.domain.model.FilterType
import com.sovathna.khmerdictionary.domain.model.Word
import com.sovathna.khmerdictionary.domain.repository.AppRepository
import io.reactivex.Single
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

  override fun filterWordList(
    filterType: FilterType,
    searchTerm: String?,
    offset: Int,
    pageSize: Int
  ): Single<List<Word>> {
    return when (filterType) {
      FilterType.All -> {
        when {
          searchTerm.isNullOrEmpty() -> wordDao.getWordList(offset, pageSize)
          else -> wordDao.getFilterWordList("$searchTerm%", offset, pageSize)
        }.map { it.map { tmp -> Word(tmp.id, tmp.word) } }
      }
      FilterType.History -> {
        when {
          searchTerm.isNullOrEmpty() -> historyDao.all(offset, pageSize)
          else -> historyDao.search("$searchTerm%", offset, pageSize)
        }.map { it.map { tmp -> Word(tmp.wordId, tmp.word) } }
      }
      FilterType.Bookmark -> {
        when {
          searchTerm.isNullOrEmpty() -> bookmarkDao.all(offset, pageSize)
          else -> bookmarkDao.search("$searchTerm%", offset, pageSize)
        }.map { it.map { tmp -> Word(tmp.wordId, tmp.word) } }
      }
    }
  }

  override fun getDefinition(id: Long): Single<Definition> {
    return wordDao.getDefinition(id).map { Definition(it.word, it.definition) }
  }
}