package com.pactera.monitoring.utils.ssh;

import com.jcraft.jsch.JSchException;

import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * @author 84483
 */
public class QueryContext<T> {
    private AbstractAssessRemoteServer<T> abstractAssessRemoteServer;

    public QueryContext(AbstractAssessRemoteServer<T> abstractAssessRemoteServer) {
        this.abstractAssessRemoteServer = abstractAssessRemoteServer;
    }

    public List<T> getCollection(Date date) throws JSchException, IOException {
        return abstractAssessRemoteServer.getCollection(date);
    }
    public T getObject(Date date) throws JSchException, IOException {
        return abstractAssessRemoteServer.getObject(date);
    }
    public void close(){
        abstractAssessRemoteServer.close();
    }
}
