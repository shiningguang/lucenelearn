package org.itat.message.exception;

public class MessageException extends RuntimeException {

	private static final long serialVersionUID = -8412165961002835819L;

	public MessageException() {
		super();
	}

	public MessageException(String message, Throwable cause) {
		super(message, cause);
	}

	public MessageException(String message) {
		super(message);
	}

	public MessageException(Throwable cause) {
		super(cause);
	}
	
}
