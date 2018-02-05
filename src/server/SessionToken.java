package server;

import java.io.Serializable;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;


public class SessionToken extends TimerTask implements Serializable{
	// Instance variables for each session object
	private int timeAlive;
	private Timer timer;
	private volatile boolean alive;
	private Student student;
	public int sessionId;

	// static variables to specify max session time, and timer delay
	private static final int MAX_SESSION_LENGTH = 60 * 5;
	private static final long DELAY = 1000;

	public SessionToken(Student student) {
	        //generate a random 6 digit sessionID
	        this.sessionId = (int)(Math.random()*900000)+100000;
	        this.student = student;
	        this.alive = true;
	        this.timeAlive = 0;
	        //create timer object to allow the task to be scheduled to run every second
	        this.timer = new Timer();
	        this.startTimer();
	        System.out.println(">> Session " + sessionId + " created\n");
	    }

	private void startTimer() {
		// schedule timer to run every second
		this.timer.scheduleAtFixedRate(this, new Date(System.currentTimeMillis()), DELAY);
	}

	@Override
	public void run() {
		// increment the time the session has been alive
		// updates once every second, so it represents the # of seconds the session has
		// been alive for
		this.timeAlive++;
		// if session has been alive for 5 minutes
		if (this.timeAlive == MAX_SESSION_LENGTH) {
			// set alive to false and cancel the timer
			this.alive = false;
			this.timer.cancel();
			System.out.println("\n---------------------------\nSession " + this.sessionId
					+ " terminated \n---------------------------");
			System.out.println(this);
			System.out.println("---------------------------");
		}
	}

	// Getters and Setters
	public boolean isAlive() {
		return this.alive;
	}

	public int getToken() {
		return this.sessionId;
	}

	public int getTimeAlive() {
		return this.timeAlive;
	}

	public int getMaxSessionLength() {
		return MAX_SESSION_LENGTH;
	}

	public Student getStudent() {
		return student;
	}
	
	@Override
	public String toString() {
		return "Student: " + this.student.getstudentID() + "\nSessionID: " + this.sessionId + "\nTime Alive: "
				+ this.timeAlive + "\nAlive: " + this.alive;
	}

	

}
