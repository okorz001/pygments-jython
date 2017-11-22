package org.korz.pygments;

import org.python.core.PyException;
import org.python.core.PyObject;

/**
 * Thrown when a lexer or formatter cannot be constructed.
 * <p>
 * This class wraps pygments.util.ClassNotFound into a Java exception.
 */
public class ClassNotFoundException extends RuntimeException {
    /**
     * Creates a new exception.
     * @param cause The cause.
     */
    public ClassNotFoundException(PyException cause) {
        super(PythonUtil.getMessage(cause), cause);
    }
}
