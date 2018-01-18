package com.ashwani.demo.thread.basic1;

class Runner extends Thread {

	@Override
	public void run() {
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		for (int i = 0; i < 10; i++) {
			System.out.println("Hello " + i);
		}
	}

}

public class App {

	public static void main(String[] args) {
		Runner runner1 = new Runner();
		runner1.start(); // I call the start method and not call run directly
							// because if I call run directly then it will be
							// executed in the main thread of the application
							// but when I call start a new parallel thread is
							// started.

		Runner runner2 = new Runner();
		runner2.start();
	}
}
