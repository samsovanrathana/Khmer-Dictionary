package com.sovathna.khmerdictionary.domain.model

sealed class FilterType {
  object All : FilterType()
  object Bookmark : FilterType()
  object History : FilterType()
}