package org.amikhalev.sprinklers.beans;

import org.hsqldb.persist.HsqlProperties;
import org.hsqldb.server.Server;
import org.hsqldb.server.ServerAcl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.context.Lifecycle;

import java.io.IOException;

import static org.hsqldb.server.ServerConstants.SERVER_STATE_ONLINE;

/**
 * Created by alex on 4/30/15.
 */
public class HsqlDbServerFactoryBean implements Lifecycle, FactoryBean<Server> {
    private static final String KEY_ADDRESS = "server.address";
    private static final String KEY_PORT = "server.port";
    private static final String KEY_DATABASE_PATH = "server.database.";
    private static final String KEY_DATABASE_NAME = "server.dbname.";
    private static final String KEY_SILENT = "server.silent";
    private static final String KEY_TRACE = "server.trace";
    private static final String KEY_TLS = "server.tls";
    private static final String KEY_DAEMON = "server.daemon";
    private static final String KEY_REMOTE_OPEN = "server.remote_open";
    private static final Logger logger = LoggerFactory.getLogger(HsqlDbServerFactoryBean.class);
    private HsqlProperties serverProperties = new HsqlProperties();
    private Server server;

    public HsqlDbServerFactoryBean() {
    }

    public void setAddress(String address) {
        serverProperties.setProperty(KEY_ADDRESS, address);
    }

    public void setPort(int port) {
        serverProperties.setProperty(KEY_PORT, port);
    }

    public void setDefaultDatabasePath(String path) {
        setDatabasePath(0, path);
    }

    public void setDatabasePath(int databaseNumber, String path) {
        serverProperties.setProperty(KEY_DATABASE_PATH + databaseNumber, path);
    }

    public void setDefaultDatabaseName(String name) {
        setDatabaseName(0, name);
    }

    public void setDatabaseName(int databaseNumber, String name) {
        serverProperties.setProperty(KEY_DATABASE_NAME + databaseNumber, name);
    }

    public void setSilent(boolean silent) {
        serverProperties.setProperty(KEY_SILENT, silent);
    }

    public void setTrace(boolean trace) {
        serverProperties.setProperty(KEY_TRACE, trace);
    }

    public void setTls(boolean tls) {
        serverProperties.setProperty(KEY_TLS, tls);
    }

    public void setDaemon(boolean daemon) {
        serverProperties.setProperty(KEY_DAEMON, daemon);
    }

    public void setRemoteOpen(boolean remoteOpen) {
        serverProperties.setProperty(KEY_REMOTE_OPEN, remoteOpen);
    }

    @Override
    public void start() {
        createServer();
        logger.info("Starting hsqldb server...");
        server.start();
    }

    @Override
    public void stop() {
        if (server != null) {
            logger.info("Stopping hsqldb server...");
            server.stop();
        }
    }

    @Override
    public boolean isRunning() {
        return server != null && server.getState() == SERVER_STATE_ONLINE;
    }

    @Override
    public Server getObject() throws Exception {
        createServer();
        return server;
    }

    private void createServer() {
        if (server == null) {
            logger.info("Creating hsqldb server...");
            server = new Server();
            try {
                server.setProperties(serverProperties);
            } catch (IOException | ServerAcl.AclFormatException e) {
                logger.error("Error while creating hsqldb server", e);
                throw new Error("Error while creating hsqldb server", e);
            }
        }
    }

    @Override
    public Class<?> getObjectType() {
        return Server.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}
