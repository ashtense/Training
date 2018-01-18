package com.ashwani.demo.thread.basic.synchronoususage;

public class App {

	private int count = 0;

	/*
	 * synchronized keyword makes a method to be usable only after an intrinsic
	 * lock. No thread then can use this method without acquiring an intrinsic
	 * lock on this method. Thing about intrinsic lock is that only one thread
	 * can hold that lock in one time.
	 */
	public synchronized void increment() { // Intrinsic lock on this method
		count++;
	}

	public static void main(String[] args) {
		App app = new App();
		app.doWork();
	}

	public void doWork() {
		Runnable task1 = () -> {
			for (int i = 0; i < 10000; i++) {
				increment();
			}
		};

		Runnable task2 = () -> {
			for (int i = 0; i < 10000; i++) {
				increment();
			}
		};

		Thread thread1 = new Thread(task1);
		Thread thread2 = new Thread(task2);

		thread1.start();
		thread2.start();

		// Join stops the execution of the parent thread unless the child thread
		// stops executing.
		try {
			thread1.join();
			thread2.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		System.out.println("Count is " + count);
	}
} 