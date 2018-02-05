package server;

import java.io.Serializable;
import java.util.ArrayList;

import interfaces.Assessment;

public class Student implements Serializable{

	private int studentID;
	private String password;
	private String sessionID;
	private ArrayList<Assessment> assessments;
	
	public Student(int studentID, String password) {
		this.studentID = studentID;
		this.password = password; 
	}
	public int getstudentID() {
		return studentID;
	}
	public void setstudentID(int studentID) {
		this.studentID = studentID;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getSessionID() {
		return sessionID;
	}
	public void setSessionID(String sessionID) {
		this.sessionID = sessionID;
	}
	public ArrayList<Assessment> getAssessments() {
		return assessments;
	}
	public void setAssessments(ArrayList<Assessment> assessments) {
		this.assessments = assessments;
	}
}
