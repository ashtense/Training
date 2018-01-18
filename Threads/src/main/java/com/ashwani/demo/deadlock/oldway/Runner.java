package com.ashwani.demo.deadlock.oldway;

import java.util.Random;

import com.ashwani.demo.deadlock.Account;

public class Runner {
	private Account account1 = new Account();
	private Account account2 = new Account();
	private Object lockingObject1 = new Object();
	private Object lockingObject2 = new Object();

	public void firstThread() throws InterruptedException {
		synchronized (this.lockingObject1) {
			Random random = new Random();
			for (int i = 0; i < 10000; i++) {
				Account.transfer(this.account1, this.account2, random.nextInt(100));
			}
		}
	}

	public void secondThread() throws InterruptedException {
		synchronized (this.lockingObject2) {
			Random random = new Random();
			for (int i = 0; i < 10000; i++) {
				Account.transfer(this.account2, this.account1, random.nextInt(100));
			}
		}
	}

	public void finish() {
		System.out.println("Account 1 balance: " + this.account1.getBalance());
		System.out.println("Account 2 balance: " + this.account2.getBalance());
		System.out.println("Total balance is: " + (this.account1.getBalance() + this.account2.getBalance()));
	}
}
