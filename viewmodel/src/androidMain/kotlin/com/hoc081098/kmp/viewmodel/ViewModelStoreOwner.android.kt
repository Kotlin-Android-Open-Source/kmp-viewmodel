package com.hoc081098.kmp.viewmodel

import androidx.lifecycle.ViewModelStore as AndroidXViewModelStore
import androidx.lifecycle.ViewModelStoreOwner as AndroidXViewModelStoreOwner

private class AndroidXToKmpViewModelStoreOwner(
  @JvmField val platform: AndroidXViewModelStoreOwner,
) : ViewModelStoreOwner {
  override val viewModelStore: ViewModelStore = platform.viewModelStore.toKmp()
}

private class KmpToAndroidXViewModelStoreOwner(
  @JvmField val kmp: ViewModelStoreOwner,
) : AndroidXViewModelStoreOwner {
  override val viewModelStore: AndroidXViewModelStore = kmp.viewModelStore.toAndroidX()
}

public fun AndroidXViewModelStoreOwner.toKmp(): ViewModelStoreOwner =
  AndroidXToKmpViewModelStoreOwner(this)

public fun ViewModelStoreOwner.toAndroidX(): AndroidXViewModelStoreOwner {
  if (this is AndroidXToKmpViewModelStoreOwner) {
    return platform
  }
  return KmpToAndroidXViewModelStoreOwner(this)
}
