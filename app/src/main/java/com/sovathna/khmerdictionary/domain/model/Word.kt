package com.sovathna.khmerdictionary.domain.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Word(
  val id: Long,
  val name: String
) : Parcelable