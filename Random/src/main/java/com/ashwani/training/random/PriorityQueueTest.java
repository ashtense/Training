package com.ashwani.training.random;

import java.util.PriorityQueue;

public class PriorityQueueTest {

	public static void main(String[] args) {
		final PriorityQueue<Integer> queue = new PriorityQueue<>();
		queue.add(11);
		queue.add(10);
		queue.add(22);
		queue.add(5);
		queue.add(12);
		queue.add(2);

		while (!queue.isEmpty()) {
			System.out.printf("%d", queue.remove());
		}
	}
}
