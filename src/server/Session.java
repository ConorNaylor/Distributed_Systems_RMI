package server;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class Session extends TimerTask{
	
	private Student student;
	private long timestamp;
	private boolean isActive = true;
	private Timer timer;
	private long delay = 1000;
	private long sessionLength = 1000*60*5;

	public Session(Student stud) {
		this.student = stud;
		this.timestamp = System.currentTimeMillis();;
		this.timer = new Timer();
        this.startCountDown();
	}

	private void startCountDown() {
		this.timer.scheduleAtFixedRate(this, new Date(timestamp), delay);
	}

	public long getSessionNumber() {
		return this.timestamp;
	}
	
	public boolean isSessionActive() {
		return isActive;
	}

	@Override
	public void run() {
		if((System.currentTimeMillis() - timestamp) > sessionLength) {
			isActive = false;
			System.out.println("Student: " + student + ", has timed out.");
		}
		
	}
	
}
