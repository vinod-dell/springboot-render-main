package com.example.demo;

import org.springframework.boot.web.servlet.ServletContextInitializer;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import org.apache.tomcat.util.descriptor.web.WebXml;
import org.apache.tomcat.util.descriptor.web.WebXmlParser;
import org.xml.sax.InputSource;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

public class WebXmlBridge implements ServletContextInitializer {

    private static final String WEB_XML_PATH = "/WEB-INF/web.xml";

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        // Parse web.xml and register servlets, filters, and listeners
        WebXml webXml = parseWebXml(servletContext);

        registerServlets(webXml, servletContext);
        registerFilters(webXml, servletContext);
        registerListeners(webXml, servletContext);
    }

    private WebXml parseWebXml(ServletContext servletContext) {
        URL resource;
        try {
            resource = servletContext.getResource(WEB_XML_PATH);
        } catch (MalformedURLException e) {
            throw new IllegalStateException(e);
        }

        WebXml webXml = new WebXml();
        WebXmlParser parser = new WebXmlParser(false, false, false);
        parser.setClassLoader(WebXmlBridge.class.getClassLoader());

        try (InputStream is = resource.openStream()) {
            boolean success = parser.parseWebXml(new InputSource(is), webXml, false);
            if (!success) {
                throw new IllegalStateException("Error parsing " + WEB_XML_PATH);
            }
        } catch (IOException e) {
            throw new IllegalStateException("Error reading " + WEB_XML_PATH, e);
        }

        return webXml;
    }

    private void registerServlets(WebXml webXml, ServletContext servletContext) {
        // Register servlets defined in web.xml
        Map<String, String> servletMappings = webXml.getServletMappings();

        for (Map.Entry<String, String> entry : webXml.getServlets().entrySet()) {
            String servletName = entry.getKey();
            String servletClass = entry.getValue();

            // Find servlet mappings for this servlet
            String servletMapping = servletMappings.get(servletName);

            if (servletMapping != null) {
                // Register servlet with servlet context
                servletContext.addServlet(servletName, servletClass).addMapping(servletMapping);
            } else {
                throw new IllegalStateException("No mapping defined for servlet " + servletName);
            }
        }
    }

    private void registerFilters(WebXml webXml, ServletContext servletContext) {
        // Register filters defined in web.xml
        // Implement similar to registerServlets if needed
    }

    private void registerListeners(WebXml webXml, ServletContext servletContext) {
        // Register listeners defined in web.xml
        for (String listenerClass : webXml.getListeners()) {
            try {
                Class<?> listener = Class.forName(listenerClass);
                servletContext.addListener(listener);
            } catch (ClassNotFoundException e) {
                throw new IllegalStateException("Listener class not found: " + listenerClass, e);
            }
        }
    }
}
