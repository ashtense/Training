package com.ashwani.demo.thread.waitnotify;

import java.util.Scanner;

public class Processor {

	public void produce() throws InterruptedException {
		synchronized (this) {
			System.out.println("Producer thread running ....");
			wait(); // Can only be used from a synchronized block.
			System.out.println("Resumed...");
		}
	}

	public void consume() throws InterruptedException {
		@SuppressWarnings("resource")
		Scanner scanner = new Scanner(System.in);

		Thread.sleep(2000);
		synchronized (this) {
			System.out.println("Waiting for the return key...");
			scanner.nextLine();
			System.out.println("Return key pressed...");
			notify(); // Can only be used from a synchronized block.
			Thread.sleep(5000); // To show that notify doesn't hand over the
								// controls of a thread.
		}
	}
}
