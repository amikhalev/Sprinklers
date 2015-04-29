package org.amikhalev.sprinklers.service.impl;

import org.amikhalev.sprinklers.service.Section;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by alex on 4/21/15.
 */
public class TestSection extends Section {
    private static final Logger logger = LoggerFactory.getLogger(TestSection.class);

    public TestSection(String name) {
        super(name);
    }

    @Override
    public void initialize() {
        logger.info("initialize section {}", getName());
    }

    @Override
    public void cleanup() {
        logger.info("cleanup section {}", getName());
    }

    @Override
    public void on() {
        logger.info("turn section {} on", getName());
    }

    @Override
    public void off() {
        logger.info("turn section {} off", getName());
    }
}
