/*******************************************
 * Copyright (c) 2017, Copyright ADP Inc.
 * All Rights Reserved.
 ******************************************/
package com.ashwani.demo.thread.producerconsumer;

import java.util.LinkedList;
import java.util.Random;

public class Processor {

	private final LinkedList<Integer> list = new LinkedList<>();
	private final int LIMIT = 10;
	private final Object lock = new Object();

	public static Processor valueOf(final Processor processor) {
		return processor;
	}

	public void producer() throws InterruptedException {
		while (true) {
			synchronized (lock) {
				/*
				 * If list's size reaches its limit thread is sent into wait
				 * stage unless consumer removes some from list.
				 */
				while (list.size() == LIMIT) {
					lock.wait();
				}
				// list.add(value++);
				/*
				 * Notifies to other threads waiting that producer has finished
				 * adding elements in the list.
				 */
				lock.notify();
			}
		}
	}

	public void consumer() throws InterruptedException {
		while (true) {
			synchronized (lock) {
				/**
				 * If list is empty consumer cant consume thus will wait unless
				 * producer inserts some new records in the list.
				 */
				while (list.size() == 0) {
					lock.wait();
				}
				System.out.println("List size: " + list.size());
				final int value = list.removeFirst();
				System.out.println("; value is: " + value);
				/*
				 * Notifies that the consumption is over and producer which was
				 * in waiting stage can insert new elements.
				 */
				lock.notify();
			}

			Thread.sleep(new Random().nextInt(1000));
		}
	}
}
