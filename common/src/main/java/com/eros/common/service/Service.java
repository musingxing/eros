package com.eros.common.service;

/**
 * Service interface
 *
 * @author Eros
 * @since   2020-01-02 15:58
 */
public interface Service extends Stoppable {

    /**
     * @return service name
     */
    public String serviceName();

    /**
     * Service startup
     */
    public void startup();

    /**
     * @return True if {@link #startup()} has been called.
     */
    boolean isStartup();
}
