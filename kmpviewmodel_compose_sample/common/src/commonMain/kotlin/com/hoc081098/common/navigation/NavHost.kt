package com.hoc081098.common.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveableStateHolder
import androidx.compose.runtime.staticCompositionLocalOf
import com.hoc081098.common.navigation.internal.DefaultNavigator
import com.hoc081098.common.navigation.internal.NavEntry
import com.hoc081098.common.navigation.internal.NavStoreViewModel
import com.hoc081098.common.navigation.internal.rememberDefaultNavigator
import com.hoc081098.kmp.viewmodel.SavedStateHandle
import com.hoc081098.kmp.viewmodel.compose.SavedStateHandleFactoryProvider
import com.hoc081098.kmp.viewmodel.compose.ViewModelStoreOwnerProvider
import com.hoc081098.kmp.viewmodel.compose.kmpViewModel
import com.hoc081098.kmp.viewmodel.createSavedStateHandle

const val EXTRA_ROUTE: String = "com.hoc081098.common.navigation.ROUTE"

fun <T : Route> SavedStateHandle.requireRoute(): T {
  return requireNotNull(get<T>(EXTRA_ROUTE)) {
    "SavedStateHandle doesn't contain Route data in \"$EXTRA_ROUTE\""
  }
}

val LocalNavigator = staticCompositionLocalOf<Navigator> {
  error("Can't use Navigator outside of a navigator NavHost")
}

@Composable
fun NavHost(
  initialRoute: Route,
  contents: List<RouteContent<*>>,
) {
  val saveableStateHolder = rememberSaveableStateHolder()

  val navStoreViewModel = kmpViewModel(
    factory = {
      NavStoreViewModel(
        globalSavedStateHandle = createSavedStateHandle()
      )
    }
  )

  val navigator = rememberDefaultNavigator(
    initialRoute = initialRoute,
    contents = contents,
    onStackEntryRemoved = remember(navStoreViewModel, saveableStateHolder) {
      { entry ->
        navStoreViewModel.removeEntry(entry.id)
        saveableStateHolder.removeState(entry.id)
      }
    },
    navStoreViewModel = navStoreViewModel,
  )

  SystemBackHandlingEffect(navigator)

  val navEntry = navigator.visibleEntryState.value
  CompositionLocalProvider(LocalNavigator provides navigator) {
    saveableStateHolder.SaveableStateProvider(key = navEntry.id) {
      NavEntryContent(
        navEntry,
        navStoreViewModel
      )
    }
  }
}

@Composable
private fun <T : Route> NavEntryContent(
  navEntry: NavEntry<T>,
  navStoreViewModel: NavStoreViewModel
) {
  ViewModelStoreOwnerProvider(
    navStoreViewModel provideViewModelStoreOwner navEntry.id
  ) {
    SavedStateHandleFactoryProvider(
      navStoreViewModel provideSavedStateHandleFactory navEntry
    ) {
      navEntry.content.Content(route = navEntry.route)
    }
  }
}

@Composable
internal expect fun SystemBackHandlingEffect(navigator: DefaultNavigator)
