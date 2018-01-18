package com.ashwani.demo.thread.blockingqueue;

import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class App {

	private static BlockingQueue queue = new ArrayBlockingQueue<>(10);

	public static void main(String[] args) throws InterruptedException {
		Thread thread1 = new Thread(() -> {
			producer();
		});
		Thread thread2 = new Thread(() -> {
			try {
				consumer();
			} catch (InterruptedException e) {
			}
		});
		
		thread1.start();
		thread2.start();
		
		thread1.join();
		thread2.join();
	}

	private static void producer() {
		Random random = new Random();
		while (true) {
			try {
				queue.put(random.nextInt(100));
			} catch (InterruptedException e) {
			}
		}
	}

	private static void consumer() throws InterruptedException {
		Random random = new Random();
		while (true) {
			Thread.sleep(100);
			if (random.nextInt(10) == 0) {
				Integer value = (Integer) queue.take();
				System.out.println("Taken value: " + value + "; Queue size is: " + queue.size());
			}
		}
	}
}
