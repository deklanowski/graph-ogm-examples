package de.jotschi.example;

public class InvalidArgumentException extends Exception {

	private static final long serialVersionUID = 2438001531364467934L;

	public InvalidArgumentException(String msg) {
		super(msg);
	}

}
