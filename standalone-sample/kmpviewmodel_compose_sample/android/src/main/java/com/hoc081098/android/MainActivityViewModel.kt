package com.hoc081098.android

import com.hoc081098.kmp.viewmodel.SavedStateHandle
import com.hoc081098.kmp.viewmodel.ViewModel

class MainActivityViewModel(
  savedStateHandle: SavedStateHandle,
) : ViewModel() {
  init {
    val currentCount = savedStateHandle.get<Int>("count") ?: 0
    savedStateHandle["count"] = currentCount + 1
    println("MainActivityViewModel init $currentCount")

    addCloseable { println("MainActivityViewModel close") }
  }
}
