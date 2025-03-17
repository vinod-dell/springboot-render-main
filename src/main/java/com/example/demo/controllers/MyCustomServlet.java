/*package com.example;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

// Optionally use @WebServlet annotation, or define in web.xml
@WebServlet("/myServlet")
public class MyCustomServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            Thread.sleep(80000);
        } catch(Exception e) {
            e.printStackTrace();
        }
        resp.getWriter().write("After thread sleep");
    }
}*/
