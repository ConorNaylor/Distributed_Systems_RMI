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
