package com.gft.bench.pojo;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * to model the message carrying the name of the root directory.
 * this class is a plain old java object with path
 * and getPath.
 * <p>
 * The service accept messages containing a path
 * in a STOMP message whose body is a JSON object. If the
 * path given is "/root", the message might look
 * something like this:
 * <p>
 * {"path" : "/root"}
 */
public class IncomingMessage {

    private String path;

    public IncomingMessage() {
    }

    public IncomingMessage(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    @Autowired
    public void setPath(String path) {
        this.path = path;
    }
}
