package com.gft.bench.pojo;

/**
 * to model the message carrying the name of the root directory.
 * this class is a plain old java object with rootDirectoryPath
 * and getRootDirectoryPath.
 * <p>
 * The service accept messages containing a rootDirectoryPath
 * in a STOMP message whose body is a JSON object. If the
 * rootDirectoryPath given is "/root", the message might look
 * something like this:
 * <p>
 * {"rootDirectoryPath" : "/root"}
 */
public class IncomingMessage {


    private String rootDirectoryPath;

    public IncomingMessage() {

    }

    public IncomingMessage(String rootDirectoryPath) {
        this.rootDirectoryPath = rootDirectoryPath;
    }

    public String getRootDirectoryPath() {
        return rootDirectoryPath;
    }

    public void setRootDirectoryPath(String rootDirectoryPath) {
        this.rootDirectoryPath = rootDirectoryPath;
    }
}
