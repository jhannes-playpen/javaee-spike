package com.exilesoft.javaee;

import static org.fest.assertions.Assertions.assertThat;

import org.eclipse.jetty.plus.jndi.EnvEntry;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;
import org.h2.jdbcx.JdbcConnectionPool;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

public class PersonWebTest {

    @Test
    public void should_find_saved_people() throws Exception {
        JdbcConnectionPool dataSource = JdbcConnectionPool.
                create("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1", "sa", "sa");
        JdbcPersonRepository.createDatabaseSchema(dataSource);
        new EnvEntry("jdbc/primaryDs", dataSource);

        Server server = new Server(0);
        server.setHandler(new WebAppContext("src/main/webapp", "/"));
        server.start();

        String rootUrl = "http://localhost:" + server.getConnectors()[0].getLocalPort() + "/";

        WebDriver browser = new HtmlUnitDriver() {
            @Override
            public WebElement findElement(By by) {
                try {
                    return super.findElement(by);
                } catch (NoSuchElementException e) {
                    throw new NoSuchElementException("Can't find " + by + " in " + getPageSource());
                }
            }
        };
        browser.get(rootUrl);
        browser.findElement(By.linkText("Create person")).click();
        browser.findElement(By.name("full_name")).sendKeys("Darth Vader");
        browser.findElement(By.name("create_person")).click();

        browser.findElement(By.linkText("Find people")).click();
        assertThat(browser.getPageSource())
            .contains("Darth Vader");
    }

}
