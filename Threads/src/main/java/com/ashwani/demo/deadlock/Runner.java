package com.ashwani.demo.deadlock;

import java.util.Random;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Runner {

	private Account account1 = new Account();
	private Account account2 = new Account();
	private Lock lock1 = new ReentrantLock();
	private Lock lock2 = new ReentrantLock();

	private void acquireLocks(Lock firstLock, Lock secondLock) throws InterruptedException {
		while (true) {
			boolean gotFirstLock = false;
			boolean gotSecondLock = false;
			try {
				gotFirstLock = firstLock.tryLock();
				gotSecondLock = secondLock.tryLock();
			} finally {
				if (gotFirstLock && gotSecondLock) {
					return;
				}
				if (gotFirstLock) {
					firstLock.unlock();
				}
				if (gotSecondLock) {
					secondLock.unlock();
				}

				Thread.sleep(1);
			}
		}
	}

	public void firstThread() throws InterruptedException {
		Random random = new Random();
		for (int i = 0; i < 10000; i++) {

			acquireLocks(this.lock1, this.lock2);
			try {
				Account.transfer(this.account1, this.account2, random.nextInt(100));
			} finally {
				this.lock1.unlock();
				this.lock2.unlock();
			}
		}
	}

	public void secondThread() throws InterruptedException {
		Random random = new Random();
		for (int i = 0; i < 10000; i++) {

			acquireLocks(this.lock2, this.lock1);
			try {
				Account.transfer(this.account2, this.account1, random.nextInt(100));
			} finally {
				this.lock1.unlock();
				this.lock2.unlock();
			}
		}

	}

	public void finished() {
		System.out.println("Account 1 balance: " + this.account1.getBalance());
		System.out.println("Account 2 balance: " + this.account2.getBalance());
		System.out.println("Total balance is: " + (this.account1.getBalance() + this.account2.getBalance()));
	}
}
