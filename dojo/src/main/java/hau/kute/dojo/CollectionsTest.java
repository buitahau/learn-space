package hau.kute.dojo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class CollectionsTest {

    public static void main(String[] args) throws InterruptedException {
        final List<String> list = new ArrayList<>();
        final List<String> notSynList = new ArrayList<>();
        final List<String> synList = Collections.synchronizedList(list);
        CountDownLatch countDownLatch = new CountDownLatch(2);

        Thread tOne = new Thread(() -> {
            for (int i = 0; i < 100; i++) {
                System.out.println(synList.add("add tOne " + i) + " tOne");
                notSynList.add("add tOne " + i);
            }
//            countDownLatch.countDown();
        });

        Thread tTwo = new Thread(() -> {
            for (int i = 0; i < 100; i++) {
                System.out.println(synList.add("add tTwo " + i) + " tTwo");
                notSynList.add("add tTwo " + i);
            }
//            countDownLatch.countDown();
        });

        tOne.start();
        tTwo.start();

        Thread.sleep(2000);
//        countDownLatch.await();
        System.out.println("------------------");
        System.out.println(synList);
        System.out.println("------------------");
        System.out.println(notSynList);
    }
}
