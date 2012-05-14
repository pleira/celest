package be.angelcorp.libs.celest.frames;

import be.angelcorp.libs.util.exceptions.GenericException;

public class ReferenceFrameTransformationException extends GenericException {

	public ReferenceFrameTransformationException(String format, Object... args) {
		super(format, args);
	}

	public ReferenceFrameTransformationException(Throwable e, String format, Object... args) {
		super(e, format, args);
	}

}
