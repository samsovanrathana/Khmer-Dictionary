package com.sovathna.khmerdictionary.model.state

import com.sovathna.androidmvi.livedata.Event
import com.sovathna.androidmvi.state.MviState
import com.sovathna.khmerdictionary.model.Definition

data class DefinitionState(
  val isInit: Boolean = true,
  val definition: Definition? = null,
  val quickDef: Event<Definition>? = null,
  val isBookmark: Boolean? = null,
  val isBookmarkEvent: Event<Boolean>? = null
) : MviState