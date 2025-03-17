package com.example.demo;


import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class DemoListener implements ServletContextListener {


    @Override
    public void contextInitialized(ServletContextEvent sce) {
        LOGGER.info("Application started");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        LOGGER.info("Application stopped");
    }

}
