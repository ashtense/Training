package com.ashwani.demo.reentrant.locks;

import java.util.Scanner;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Runner {

	private int count = 0;
	private Lock lock = new ReentrantLock();
	private Condition condition = this.lock.newCondition();

	private void increment() {
		for (int i = 0; i < 10000; i++) {
			this.count++;
		}
	}

	public void firstThread() throws InterruptedException {
		this.lock.lock();

		/**
		 * condition.await will halt the execution of this thread unless the
		 * signal method on the same condition object is called. Pretty useful
		 * stuff to use if you want to avoid using wait-notify by yourself.
		 * Similar effects could be achieved via wait-notify for an object lock
		 * in a synchronized block. But java API provides these mechanisms in
		 * java.concurrent API so that one can develope code without getting
		 * into the troubles of synchronized blocks and all
		 */
		System.out.println("Going into hibernation");
		this.condition.await();
		System.out.println("Woken up");

		/**
		 * Never use lock.unLock without a try-finally. If your code runs into
		 * exception before the unlock is called program will run into huge
		 * problems. Memory wastage and what not to name a few.
		 */
		try {
			increment();
		} finally {
			this.lock.unlock();
		}
	}

	public void secondThread() throws InterruptedException {
		Thread.sleep(1000);
		this.lock.lock();

		/**
		 * Condition.signal will get all threads from the current lock instance
		 * and will work kinda notify.
		 */
		System.out.println("Press Enter!..");
		new Scanner(System.in).nextLine();
		System.out.println("Got Enter!..");
		this.condition.signal();
		try {
			increment();
		} finally {
			this.lock.unlock();
		}
	}

	public void finished() {
		System.out.println("Count is: " + this.count);
	}
}
