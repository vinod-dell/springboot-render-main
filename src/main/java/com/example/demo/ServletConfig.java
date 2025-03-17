
/*package com.example.demo;

import jakarta.servlet.Servlet;
import jakarta.servlet.annotation.WebServlet;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.example.demo.MyCustomServlet;

@Configuration
public class ServletConfig {

    @Bean
    public ServletRegistrationBean<Servlet> myServlet() {
        ServletRegistrationBean<Servlet> servletBean = new ServletRegistrationBean<>(new MyCustomServlet(), "/myServlet");
        servletBean.setLoadOnStartup(1); // optional: to ensure the servlet is loaded on startup
        return servletBean;
    }
}*/
