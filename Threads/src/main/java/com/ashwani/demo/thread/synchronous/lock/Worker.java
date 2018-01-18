package com.ashwani.demo.thread.synchronous.lock;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Worker {

	private Random randomNumber = new Random();
	private List<Integer> list1 = new ArrayList<>();
	private List<Integer> list2 = new ArrayList<>();
	private Object lockingObject1 = new Object();
	private Object lockingObject2 = new Object();

	public void stageOne() {
		 synchronized (this.lockingObject1) {
		try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			this.list1.add(this.randomNumber.nextInt(1));
		}
	}

	public void stageTwo() {
		synchronized (this.lockingObject2) {
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			this.list2.add(this.randomNumber.nextInt(1));
		}
	}

	public void process() {
		for (int i = 0; i < 1000; i++) {
			stageOne();
			stageTwo();
		}
	}

	public void main() {
		System.err.println("Starting... ");
		long start = System.currentTimeMillis();
		Thread thread1 = new Thread(new Runnable() {

			@Override
			public void run() {
				process();
			}
		});

		thread1.start();

		Thread thread2 = new Thread(new Runnable() {

			@Override
			public void run() {
				process();
			}
		});

		thread2.start();

		try {
			thread1.join();
			thread2.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		long end = System.currentTimeMillis();
		System.err.println("Time taken: " + (end - start));
		System.out.println("Sizes are \n List1: " + this.list1.size() + " List2: " + this.list2.size());
	}
}
