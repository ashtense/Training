package com.ashwani.demo.thread.basic;

public class TestThread {

	public static void main(String[] args) throws InterruptedException {
		Thread thread1 = new Thread(new RunnableDemo("GERONIMO1"));
		thread1.setPriority(7);
		
		Thread thread2 = new Thread(new RunnableDemo("GERONIMO2"));
		thread2.setPriority(4);
		
		thread1.start();
		thread1.join(); // This command makes it execute thread1 before any other thread is executed.
		thread2.start();
		
		Runnable thread3 = new RunnableDemo("GERONIMO3");
		thread3.run();
	}
}