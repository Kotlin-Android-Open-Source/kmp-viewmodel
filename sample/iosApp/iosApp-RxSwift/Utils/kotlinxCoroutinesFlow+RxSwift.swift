//
//  kotlinxCoroutinesFlow+RxSwift.swift
//  iosApp
//
//  Created by Petrus Nguyen Thai Hoc on 2/12/23.
//  Copyright © 2023 orgName. All rights reserved.
//

import Foundation
import shared
import RxSwift

private func unconfinedScope() -> CoroutineScope {
  CoroutineScopeKt.CoroutineScope(
    context: Dispatchers.shared
      .Unconfined
      .plus(context: SupervisorKt.SupervisorJob(parent: nil))
  )
}

extension Flow {
  // MARK: - Flow<T>

  func asNonNullObservable<T: AnyObject>(_ type: T.Type = T.self) -> Observable<T> {
    Observable<T>.create { observer in
      let wrapper = NonNullFlowWrapperKt.wrap(self) as! NonNullFlowWrapper<T>

      let closable = wrapper.subscribe(
        scope: unconfinedScope(),
        onValue: observer.onNext,
        onError: { t in observer.onError(t.asNSError()) },
        onComplete: observer.onCompleted
      )

      return Disposables.create(with: closable.close)
    }
  }

  // MARK: - Flow<T?>

  func asNullableObservable<T: AnyObject>(_ type: T.Type = T.self) -> Observable<T?> {
    Observable<T?>.create { observer in
      let wrapper = NullableFlowWrapperKt.wrap(self) as! NullableFlowWrapper<T>

      let closable = wrapper.subscribe(
        scope: unconfinedScope(),
        onValue: observer.onNext,
        onError: { t in observer.onError(t.asNSError()) },
        onComplete: observer.onCompleted
      )

      return Disposables.create(with: closable.close)
    }
  }
}
