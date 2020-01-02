package com.eros.common.service;

/**
 * Stoppable interface to stop application
 *
 * @author Eros
 * @since   2020-01-02 15:58
 */
public interface Stoppable {
    /**
     * Stop this service.
     * Implementers should favor logging errors over throwing RuntimeExceptions.
     * @param why Why we're stopping.
     */
    void stop(String why);

    /**
     * @return True if {@link #stop(String)} has been closed.
     */
    boolean isStopped();
}