package com.sovathna.khmerdictionary.domain.model.state

import com.sovathna.androidmvi.state.MviState
import com.sovathna.khmerdictionary.domain.model.Definition

data class DefinitionState(
  val isInit: Boolean = true,
  val definition: Definition? = null,
  val isBookmark: Boolean? = null
) : MviState