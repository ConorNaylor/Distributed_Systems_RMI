package server;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import exceptions.InvalidOptionNumber;
import exceptions.InvalidQuestionNumber;
import interfaces.Assessment;
import interfaces.Question;

public class MCQAssessment implements Assessment{

	private ArrayList<Question> questions = new ArrayList<>();
	private int studentId;
	private String name;
	private String courseCode;
	private Date date; 
	
	public MCQAssessment(int studentId, String courseCode, String name, Date date) {
		this.name = name;
		this.courseCode = courseCode;
		this.studentId = studentId;
		this.date = date;
	}
	
	public void setQuestion(MCQQuestion q) {
		questions.add(q);
	}
	
	@Override
	public String getInformation() {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		return "Assessment " + name + " for course " + this.courseCode + " for student " + studentId + " with closing date " + sdf.format(date); 
	}

	@Override
	public Date getClosingDate() {
		return this.date;
	}

	@Override
	public List<Question> getQuestions() {
		return this.questions;
	}

	@Override
	public Question getQuestion(int questionNumber) throws InvalidQuestionNumber {
		return this.questions.get(questionNumber);
	}

	@Override
	public void selectAnswer(int questionNumber, int optionNumber) throws InvalidQuestionNumber, InvalidOptionNumber {
		((MCQQuestion) this.getQuestion(questionNumber)).select(optionNumber);	
	}

	@Override
	public int getSelectedAnswer(int questionNumber) {
		return ((MCQQuestion) this.questions.get(questionNumber)).getSelected();
	}

	@Override
	public int getAssociatedID() {
		return this.studentId;
	}
	
	public String getCourseCode() {
		return this.courseCode;
	}
	
	public String getName() {
		return this.name;
	}

}