package com.hyperfresh.mcuniverse.server;

/**
 * Thrown when something that can only be accessed on this server
 * is accessed on an external server.
 *
 * @author Octopod - octopodsquad@gmail.com
 */
public class ExternalServerException extends RuntimeException
{
    public ExternalServerException(String message)
    {
        super(message);
    }
}
