package org.itat.message.test;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class TestTimer {
	private static Timer timer;

	public static void main(String[] args) {
		startTimer();
	}
	
	public static void startTimer() {
		timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				System.out.println("begin");
			}
		},new Date(), 5000);
	}
	
	
}
