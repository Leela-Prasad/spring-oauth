package exceptions;

import org.springframework.http.HttpStatus;

public class UnknownHttpErrorException extends RuntimeException {

	public UnknownHttpErrorException(String string) {
		super(string);
	}

}
