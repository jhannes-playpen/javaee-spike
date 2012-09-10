package com.exilesoft.javaee;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.exilesoft.common.RepositoryTestRunner;

@RunWith(RepositoryTestRunner.class)
public class PersonServletTest {

    private PersonServlet servlet = new PersonServlet();
    private HttpServletRequest req = mock(HttpServletRequest.class);
    private HttpServletResponse resp = mock(HttpServletResponse.class);
    private StringWriter html = new StringWriter();
    private PersonRepository personRepository = new FakePersonRepository();

    public PersonServletTest(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @Test
    public void should_display_create_page() throws IOException {
        when(req.getPathInfo()).thenReturn("/create.html");
        servlet.doGet(req, resp);

        verify(resp).setContentType("text/html");

        Document document = Parser.parse(html.toString(), "");
        assertThat(document.select("form input[name=full_name]").attr("type")).isEqualTo("text");
        assertThat(document.select("form input[name=create_person]").attr("type")).isEqualTo("submit");
    }

    @Test
    public void should_save_person() throws IOException {
        when(req.getParameter("full_name")).thenReturn("Darth Vader");

        servlet.doPost(req, resp);
        verify(resp).sendRedirect("/");

        assertThat(personRepository.findPeople(null)).contains(Person.withName("Darth Vader"));
    }

    @Test
    public void should_show_search_page() throws IOException {
        when(req.getPathInfo()).thenReturn("/find.html");
        servlet.doGet(req, resp);

        Document document = Parser.parse(html.toString(), "");
        assertThat(document.select("form input[name=name_query]").attr("type")).isEqualTo("text");
        assertThat(document.select("form input[name=find_people]").attr("type")).isEqualTo("submit");
    }

    @Test
    public void should_search_for_people() throws IOException {
        personRepository.savePerson(Person.withName("Darth Vader"));

        when(req.getPathInfo()).thenReturn("/find.html");
        when(req.getParameter("name_query")).thenReturn("vader");

        servlet.doGet(req, resp);
        Document document = Parser.parse(html.toString(), "");
        assertThat(document.select("form input[name=name_query]").val()).isEqualTo("vader");
        assertThat(document.select("ul#people li").first().text()).isEqualTo("Darth Vader");
    }

    @Before
    public void captureOutput() throws IOException {
        when(resp.getWriter()).thenReturn(new PrintWriter(html));
    }

    @Before
    public void setupServlet() {
        servlet.setPersonRepository(personRepository);
    }
}
