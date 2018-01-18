package com.ashwani.demo.deadlock.oldway;

public class Account {
	private int balance = 10000;

	public void deposit(int amount) {
		this.balance += amount;
	}

	public void withdraw(int amount) {
		this.balance -= amount;
	}

	public int getBalance() {
		return this.balance;
	}

	public static void transfer(Account account1, Account account2, int amount) {
		account1.withdraw(amount);
		account2.deposit(amount);
	}
}