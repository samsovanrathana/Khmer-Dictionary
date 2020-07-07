package com.sovathna.khmerdictionary.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Word(
  val id: Long,
  val name: String
) : Parcelable