package com.example.demo;

import org.springframework.boot.web.servlet.ServletContextInitializer;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import org.apache.tomcat.util.descriptor.web.WebXml;
import org.apache.tomcat.util.descriptor.web.WebXmlParser;
import org.apache.tomcat.util.descriptor.web.ServletDef;
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
        WebXml webXml = parseWebXml(servletContext);

        registerServlets(webXml, servletContext);
        registerFilters(webXml, servletContext);  // Implement if necessary
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
        Map<String, ServletDef> servletDefs = webXml.getServlets();

        for (Map.Entry<String, ServletDef> entry : servletDefs.entrySet()) {
            String servletName = entry.getKey();
            ServletDef servletDef = entry.getValue();

            // Extract the servlet class
            String servletClass = servletDef.getServletClass();

            // Find servlet mappings for this servlet
            Map<String, String> servletMappings = webXml.getServletMappings();
            String servletMapping = servletMappings.get(servletName);

            if (servletMapping != null) {
                // Register servlet with servlet context
                servletContext.addServlet(servletName, servletClass).addMapping(servletMapping);
            } else {
                throw new IllegalStateException("No mapping defined for servlet " + servletName);
            }
        }
    }

    private void registerListeners(WebXml webXml, ServletContext servletContext) {
        for (String listenerClassName : webXml.getListeners()) {
            try {
                // Load the listener class dynamically
                Class<?> listenerClass = Class.forName(listenerClassName);

                // Instantiate the listener and add it to the context
                servletContext.addListener(listenerClass.getDeclaredConstructor().newInstance());
            } catch (ClassNotFoundException | ReflectiveOperationException e) {
                throw new IllegalStateException("Error adding listener: " + listenerClassName, e);
            }
        }
    }
}
