package sample;

import org.junit.Test;

import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.*;

public class ObservableTest {

    @Test
    public void singleObserver() {

        Observer o1 = (o, arg) -> System.out.println(
                Thread.currentThread().getName() +
                        " / observable=" + o.toString() +
                        " / arg=" + arg);

        IntObservable intObservable = new IntObservable();
        intObservable.addObserver(o1);

        ExecutorService executors = Executors.newSingleThreadExecutor();
        executors.submit(intObservable);

        System.out.println(Thread.currentThread().getName() + " / EXIT");
    }

    @Test
    public void multipleObserver() {

        Observer o1 = (o, arg) -> System.out.println(
                Thread.currentThread().getName() +
                        " / observable=" + o.toString() +
                        " / arg=" + arg);

        Observer o2 = (o, arg) -> System.out.println(
                Thread.currentThread().getName() +
                        " just hello");

        IntObservable intObservable = new IntObservable();
        intObservable.addObserver(o1);
        intObservable.addObserver(o2);

        ExecutorService executors = Executors.newSingleThreadExecutor();
        executors.submit(intObservable);

        System.out.println(Thread.currentThread().getName() + " / EXIT");
    }

    static class IntObservable extends Observable implements Runnable {

        @Override
        public void run() {

            for (int i = 0; i < 10; i++) {
                setChanged();
                notifyObservers(i);
            }
        }
    }
}
