<<<<<<< HEAD
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
=======
package server;

import interfaces.Question;

public class MCQQuestion implements Question{

	@Override
	public int getQuestionNumber() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getQuestionDetail() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[] getAnswerOptions() {
		// TODO Auto-generated method stub
		return null;
	}

}
>>>>>>> 67c7a0f94fe9d03e9bad2cbdcf81d380d27d00eb
