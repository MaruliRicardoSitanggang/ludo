package com.ludoelite.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Maps the standard Spring Boot error response body so we can extract
 * human-readable messages from 4xx / 5xx responses.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ApiError {

    private int    status;
    private String error;
    private String message;
    private String path;

    public ApiError() {}

    public int    getStatus()               { return status; }
    public void   setStatus(int status)     { this.status = status; }

    public String getError()               { return error; }
    public void   setError(String error)   { this.error = error; }

    public String getMessage()                 { return message; }
    public void   setMessage(String message)   { this.message = message; }

    public String getPath()                { return path; }
    public void   setPath(String path)     { this.path = path; }
}
