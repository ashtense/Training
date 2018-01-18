package com.ashwani.demo.thread.executorservice;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

class Processor implements Runnable {

	private int id;

	Processor(int id) {
		this.id = id;
	}

	public void run() {
		System.out.println("Starting: " + this.id);
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("Completed: " + this.id);
	}
}

public class App {

	public static void main(String[] args) throws InterruptedException {

		ExecutorService executor = Executors.newFixedThreadPool(2);
		for (int i = 0; i < 5; i++) {
			executor.submit(new Processor(i));
		}

		/*
		 * Doesn't shutdown immediately but waits for threads to complete
		 * execution.
		 */
		executor.shutdown();
		
		System.out.println("All tasks submitted. ");
		
		/*
		 * This will make the current thread wait for this particular duration
		 * to have the executor finish all its tasks. It wont kill the thread's
		 * tasks or executor instance but will simply move on.
		 */
		executor.awaitTermination(20000, TimeUnit.MILLISECONDS);
		
		System.out.println("All tasks completed");
	}
}
