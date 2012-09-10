package com.exilesoft.javaee;

import static org.fest.assertions.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.exilesoft.common.RepositoryTestRunner;

@RunWith(RepositoryTestRunner.class)
public class PersonRepositoryTest {

    private PersonRepository personRepository;

    public PersonRepositoryTest(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

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

}
