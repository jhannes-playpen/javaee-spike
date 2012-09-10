package com.exilesoft.javaee;

import static org.fest.assertions.Assertions.assertThat;

import javax.sql.DataSource;

import org.h2.jdbcx.JdbcConnectionPool;
import org.junit.BeforeClass;
import org.junit.Test;

public class JdbcPersonRepositoryTest {

    private PersonRepository personRepository = new JdbcPersonRepository(dataSource);

    @Test
    public void should_save_people() {
        Person person = Person.withName("Johannes Brodwall");
        personRepository.savePerson(person);
        assertThat(personRepository.findPeople(null))
            .contains(person);
    }

    @Test
    public void should_filter_people() {
        Person person = Person.withName("Darth Vader");
        Person nonMatching = Person.withName("Anakin Skywalker");
        personRepository.savePerson(person);
        personRepository.savePerson(nonMatching);

        assertThat(personRepository.findPeople("vade"))
            .contains(person).excludes(nonMatching);
    }


    private static DataSource dataSource;

    @BeforeClass
    public static void createDataSource() {
        dataSource = JdbcConnectionPool.
                create("jdbc:h2:mem:repoTest;DB_CLOSE_DELAY=-1", "sa", "sa");
        JdbcPersonRepository.createDatabaseSchema(dataSource);
    }

}
