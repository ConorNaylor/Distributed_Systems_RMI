<<<<<<< HEAD
package server;

import java.util.ArrayList;

public class Student {
	
	private int id;
	private String password;

	public static ArrayList<Student> students;
	
	public Student(int id, String pw) {
		this.id = id;
		this.password = pw;
		students.add(this);
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getPassword() {
		return password;
	}
}
=======
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
>>>>>>> 67c7a0f94fe9d03e9bad2cbdcf81d380d27d00eb
