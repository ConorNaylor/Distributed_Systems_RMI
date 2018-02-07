package server;

import interfaces.Question;

public class MCQQuestion implements Question{

	private int selected;
	private int correctAnswer;
	private String[] answers;
	private String detail;
	private int questionNumber;
	
	
	public MCQQuestion(String detail, String[] answers, int questionNumber, int correctAnswer) {
		this.detail = detail;
		this. answers = answers;
		this.correctAnswer = correctAnswer;
	}
	
	@Override
	public int getQuestionNumber() {
		return this.questionNumber;
	}

	@Override
	public String getQuestionDetail() {
		return this.detail;
	}

	@Override
	public String[] getAnswerOptions() {
		return this.answers;
	}

	public void select(int optionNumber) {
		this.selected = optionNumber;
	}
	
	public int getSelected() {
		return this.selected;
	}

}