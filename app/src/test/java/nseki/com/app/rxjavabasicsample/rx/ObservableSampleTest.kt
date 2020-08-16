package nseki.com.app.rxjavabasicsample.rx

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.schedulers.TestScheduler
import org.junit.Test
import java.util.concurrent.TimeUnit

class ObservableSampleTest {

    private val observableSample = ObservableSample()

    @Test
    fun testBasic1() {
        val values = (1..5).toList()
        observableSample.basic(values, Schedulers.trampoline()).test()
            .assertComplete()
            .assertValueSequence(values)
    }

    @Test
    fun testFilter() {
        val values = (1..5).toList()
        val isEven: (Int) -> Boolean = { x -> x % 2 == 0  }
        observableSample.filter(values, isEven, Schedulers.trampoline()).test()
            .assertComplete()
            .assertValueSequence(listOf(2, 4))
    }

    @Test
    fun testFromPublisher() {
        val values = (1..5).toList()
        observableSample.fromPublisher(values, Schedulers.trampoline()).test()
            .assertComplete()
            .assertValueSequence(values)
    }

    @Test
    fun testInterval() {
        val scheduler = TestScheduler()
        val testObservable = observableSample.interval(5, scheduler).test()
        scheduler.advanceTimeTo(500, TimeUnit.MILLISECONDS)
        testObservable.awaitCount(5).assertValueSequence(listOf(0, 1, 2, 3, 4))
    }

    @Test
    fun testScan() {
        val values = (1..5).toList()
        val add: (Int, Int) -> Int = { v1, v2 -> v1 + v2 }
        observableSample.scan(values, add, Schedulers.trampoline()).test()
            .assertValueSequence(listOf(1, 3, 6, 10, 15))
    }

    @Test
    fun testDelay() {
        val values = (1..5).toList()
        val scheduler = TestScheduler()
        val testObservable = observableSample.delay(values, scheduler).test()
        scheduler.advanceTimeTo(5000, TimeUnit.MILLISECONDS)
        testObservable.await().assertValueSequence(values)
    }

    @Test
    fun testAmbWith() {
        val scheduler1 = TestScheduler()
        val scheduler2 = TestScheduler()
        val scheduler3 = TestScheduler()

        val observable1 = Observable.interval(100, TimeUnit.MILLISECONDS, scheduler1).take(3)
        val observable2 = Observable.interval(100, TimeUnit.MILLISECONDS, scheduler2).take(3).map { it + 10 }
        val observable3 = Observable.interval(100, TimeUnit.MILLISECONDS, scheduler3).take(3).map { it + 20 }
        val ambObserver = observable1.ambWith(observable2).ambWith(observable3).test()

        scheduler2.advanceTimeTo(300, TimeUnit.MILLISECONDS) // observe observable2 only
        scheduler1.advanceTimeTo(300, TimeUnit.MILLISECONDS)
        scheduler3.advanceTimeTo(300, TimeUnit.MILLISECONDS)

        ambObserver.assertComplete().assertResult(10 ,11, 12)
    }
}
