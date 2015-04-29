package org.amikhalev.sprinklers.service.impl;

import org.amikhalev.sprinklers.service.Section;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by alex on 5/1/15.
 */
public class GpioSection extends Section {
    private int port;
    private static final Logger logger = LoggerFactory.getLogger(GpioSection.class);

    public GpioSection(String name, int port) {
        super(name);
        this.port = port;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    @Override
    public void initialize() {
        logger.info("initialize section '{}', port '{}'", getName(), port);
    }

    @Override
    public void cleanup() {
        logger.info("cleanup section '{}', port '{}'", getName(), port);
    }

    @Override
    public void on() {
        logger.info("on section '{}', port '{}'", getName(), port);
    }

    @Override
    public void off() {
        logger.info("off section '{}', port '{}'", getName(), port);
    }
}
