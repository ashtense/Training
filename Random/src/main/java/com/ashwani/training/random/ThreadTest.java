package com.ashwani.training.random;

public class ThreadTest {

	public static void main(String[] args) {
		Thread runnableThread = new Thread(() -> {
			System.err.println("Hello");
			try {
				Thread.sleep(1000);
			} catch (Exception e) {
			}
			System.err.println("I would be a separate instruction.");
		});
		runnableThread.start();

		ThreadDemo threadDemo = new ThreadDemo();
		threadDemo.start();
	}
}

class ThreadDemo extends Thread {

	@Override
	public synchronized void start() {
		super.start();
		System.err.println("Thread starts now");
		try {
			super.wait(1000);
		} catch (InterruptedException e) {
		}
	}

	@Override
	public void run() {
		super.run();
		System.err.println("This is me in the running state.");
	}

}