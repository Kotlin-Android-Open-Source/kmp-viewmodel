/*
 * Copyright (C) 2015 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.hoc081098.kmp.viewmodel

/**
 * Denotes that the annotated method should only be called on the main thread. If the annotated
 * element is a class, then all methods in the class should be called on the main thread.
 *
 * Example:
 * ```
 * @MainThread
 * public void deliverResult(D data) { ... }
 * ```
 *
 * **Note:** Ordinarily, an app's main thread is also the UI thread. However, under special
 * circumstances, an app's main thread might not be its UI thread; for more information, see
 * [Thread annotations](https://developer.android.com/studio/write/annotations#thread-annotations).
 */
@MustBeDocumented
@Retention(AnnotationRetention.SOURCE)
@Target(
  AnnotationTarget.FUNCTION,
  AnnotationTarget.PROPERTY_GETTER,
  AnnotationTarget.PROPERTY_SETTER,
  AnnotationTarget.CONSTRUCTOR,
  AnnotationTarget.ANNOTATION_CLASS,
  AnnotationTarget.CLASS,
  AnnotationTarget.VALUE_PARAMETER,
)
public expect annotation class MainThread()
