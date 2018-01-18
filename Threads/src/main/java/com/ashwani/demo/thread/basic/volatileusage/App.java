package com.ashwani.demo.thread.basic.volatileusage;

import java.util.Scanner;

class Processor extends Thread {

	// Volatile indicates that java wudn't cache this variable's value and every
	// update will happen in real time in memory.
	private volatile boolean running = true;

	@Override
	public void run() {
		while (this.running) {
			System.out.println("Hello");
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public void shutdown() {
		this.running = false;
	}
}

public class App {

	public static void main(String[] args) {
		Processor processor1 = new Processor();
		processor1.start();

		System.out.println("Press return to stop");
		Scanner scanner = new Scanner(System.in);
		scanner.nextLine();

		processor1.shutdown();
		scanner.close();
	}
}