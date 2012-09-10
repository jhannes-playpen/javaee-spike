package com.exilesoft.javaee;

import java.util.List;

public interface PersonRepository {

    void savePerson(Person person);

    List<Person> findPeople(String nameQuery);

}
