package com.ashwani.demo.thread.countdown.latches;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class Processor implements Runnable {

	private CountDownLatch latch;

	public Processor(CountDownLatch latch) {
		this.latch = latch;
	}

	public void run() {
		System.out.println("Started...");

		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("Decreasing count");
		this.latch.countDown();
	}

}

public class App {

	/**
	 * @param args
	 *            It passes the instance of countdownlatch initialized in main
	 *            program to the thread for decrementing. After the countdown
	 *            latches countdown mechanism is taken care of by individual
	 *            threads we just set the main program to wait for the countdown
	 *            to stop by using latch.await As soon as the main thread's
	 *            latche's count reaches zero main is released from latch.await
	 *            and execution starts. Thing to notice is, this could have been
	 *            a simple integer increment variable but in the util we get a
	 *            thread safe and prepared object to control main thread
	 *            otherwise I would have to write the conditional logic of
	 *            stopping threads and all.
	 */
	public static void main(String[] args) {
		CountDownLatch latch = new CountDownLatch(3);

		ExecutorService executor = Executors.newFixedThreadPool(3);

		for (int i = 0; i < 3; i++) {
			executor.submit(new Processor(latch));
		}
		try {
			latch.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("Completed:");
	}
}
