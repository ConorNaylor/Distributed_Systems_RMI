package exceptions;

public class InvalidQuestionNumber extends Exception {

	public InvalidQuestionNumber(String questionNumber) {
		super(questionNumber);
	}
}

