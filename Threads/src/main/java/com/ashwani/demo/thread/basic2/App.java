package com.ashwani.demo.thread.basic2;

class Runner implements Runnable {

	@Override
	public void run() {
		for(int i =0 ; i< 10; i++){
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("Runner " + i);
		}
	}

}

public class App {

	public static void main(String[] args) {
		Thread t1 = new Thread(new Runner());
		Thread t2 = new Thread(new Runner());
		t1.start();
		t2.start();
		
		Thread newOne = new Thread(new Runnable() {
			
			@Override
			public void run() {
				
			}
		});
	}
}
