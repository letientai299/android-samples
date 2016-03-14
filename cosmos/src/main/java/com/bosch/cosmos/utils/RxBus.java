package com.bosch.cosmos.utils;

import rx.Observable;
import rx.subjects.PublishSubject;
import rx.subjects.SerializedSubject;
import rx.subjects.Subject;

/**
 * courtesy: https://gist.github.com/benjchristensen/04eef9ca0851f3a5d7bf
 */
public class RxBus {
  private static RxBus singleBus;
  // If multiple threads are going to emit events to this
  // then it must be made thread-safe like this instead
  private final Subject<Object, Object> _bus =
      new SerializedSubject<>(PublishSubject.create());

  private RxBus() {
    // Singleton
  }

  public static RxBus getBus() {
    if (singleBus == null) {
      singleBus = new RxBus();
    }
    return singleBus;
  }

  public void send(Object o) {
    _bus.onNext(o);
  }

  public Observable<Object> toObserverable() {
    return _bus;
  }

  public boolean hasObservers() {
    return _bus.hasObservers();
  }
}
