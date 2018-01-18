package com.ashwani.demo.thread.basic;

public class RunnableDemo implements Runnable {

	private String threadName;

	RunnableDemo(String name) {
		this.threadName = name;
		System.out.println("Creating instance of " + this.threadName);
	}

	public void run() {
		System.out.println("Execution of " + this.threadName + " starts.");
		try {
			for (int i = 0; i < 5; i++) {
				System.out.println(this.threadName + " executing itself");
				Thread.sleep(500);
			}
		} catch (InterruptedException e) {
			System.out.println("Thread " + this.threadName + " interrupted via an exception.");
		}
	}

}
