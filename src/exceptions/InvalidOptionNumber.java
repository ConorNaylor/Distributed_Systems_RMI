package exceptions;

public class InvalidOptionNumber extends Exception {

	public InvalidOptionNumber(String optionNumber) {
		super(optionNumber);
	}
}

