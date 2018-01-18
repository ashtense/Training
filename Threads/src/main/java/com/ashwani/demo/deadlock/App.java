package com.ashwani.demo.deadlock;

public class App {

	public static void main(String[] args) {
		Runner runner = new Runner();

		long startTime = System.currentTimeMillis();

		Thread thread1 = new Thread(() -> {
			try {
				runner.firstThread();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		});

		Thread thread2 = new Thread(() -> {
			try {
				runner.secondThread();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		});

		thread1.start();
		thread2.start();

		try {
			thread1.join();
			thread2.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		runner.finished();

		long endTime = System.currentTimeMillis();

		System.out.println("Time taken: " + (endTime - startTime));
	}
}
