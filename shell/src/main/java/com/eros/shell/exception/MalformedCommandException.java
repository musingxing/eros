package com.eros.shell.exception;

import org.apache.commons.cli.ParseException;

/**
 * Malformed Command Exception.
 *
 * @author Eros
 * @since   2020-01-03 10:58
 */
public class MalformedCommandException extends CommandException{

    /**
     * Constructs a new exception with the specified detail message.  The
     * cause is not initialized, and may subsequently be initialized by
     * a call to {@link #initCause}.
     *
     * @param   message   the detail message. The detail message is saved for
     *          later retrieval by the {@link #getMessage()} method.
     */
    public MalformedCommandException(String message) {
        super(message);
    }
}
