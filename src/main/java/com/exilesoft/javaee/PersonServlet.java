package com.exilesoft.javaee;

import java.io.IOException;
import java.io.PrintWriter;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

public class PersonServlet extends HttpServlet {

    private PersonRepository personRepository;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("text/html");
        PrintWriter writer = resp.getWriter();
        if (req.getPathInfo().equals("/create.html")) {
            writer.write("<html><form method='post'>" +
                    "<input type='text' name='full_name'/>" +
                    "<input type='submit' name='create_person'/>" +
                    "</form></html>");
        } else {
            String nameQuery = req.getParameter("name_query");
            writer.append("<html><form method='get'>");
            writer.append("<input type='text' name='name_query' value='");
            writer.append(nameQuery);
            writer.append("'/>");
            writer.append("<input type='submit' name='find_people'/>");
            writer.append("</form>");
            writer.append("<ul id='people'>");
            for (Person person : personRepository.findPeople(nameQuery)) {
                writer.append("<li>").append(person.getFullName()).append("</li>");
            }
            writer.append("</ul>");
            writer.append("</html>");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        personRepository.savePerson(Person.withName(req.getParameter("full_name")));
        resp.sendRedirect("/");
    }

    public void setPersonRepository(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @Override
    public void init() throws ServletException {
        try {
            DataSource dataSource = (DataSource) new InitialContext().lookup("jdbc/primaryDs");
            setPersonRepository(new JdbcPersonRepository(dataSource));
        } catch (NamingException e) {
            throw new ServletException(e);
        }
    }

}
