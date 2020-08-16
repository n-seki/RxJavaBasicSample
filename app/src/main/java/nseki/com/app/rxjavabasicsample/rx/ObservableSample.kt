package nseki.com.app.rxjavabasicsample.rx

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.kotlin.toObservable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class ObservableSample {

    fun basic(
        values: List<Int>,
        scheduler: Scheduler = Schedulers.io()
    ): Observable<Int> {
        return values.toObservable()
            .subscribeOn(scheduler)
    }

    fun filter(
        values: List<Int>,
        test: (Int) -> Boolean,
        scheduler: Scheduler = Schedulers.io()
    ): Observable<Int> {
        return values.toObservable()
            .filter(test)
            .subscribeOn(scheduler)
    }

    fun fromPublisher(
        values: List<Int>,
        scheduler: Scheduler = Schedulers.io()
    ): Observable<Int> {
        return Observable.fromPublisher<Int> { subscriber ->
            values.forEach {
                subscriber.onNext(it)
            }
            subscriber.onComplete()
        }.subscribeOn(scheduler)
    }

    fun interval(
        count: Int,
        scheduler: Scheduler = Schedulers.io()
    ): Observable<Int> {
        return Observable.interval(100, TimeUnit.MILLISECONDS, scheduler)
            .take(count.toLong())
            .map { it.toInt() }
    }

    fun scan(
        values: List<Int>,
        biFunc: (Int, Int) -> Int,
        scheduler: Scheduler = Schedulers.io()
    ): Observable<Int> {
        return values.toObservable()
            .scan { t1, t2 -> biFunc.invoke(t1, t2) }
            .subscribeOn(scheduler)
    }

    fun delay(
        values: List<Int>,
        scheduler: Scheduler = Schedulers.io()
    ): Observable<Int> {
        return values.toObservable()
            .delay(5000, TimeUnit.MILLISECONDS, scheduler)
            .subscribeOn(scheduler)
    }
}
