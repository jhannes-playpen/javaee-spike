package com.exilesoft.javaee;

import java.util.ArrayList;
import java.util.List;

public class FakePersonRepository implements PersonRepository {

    private List<Person> people = new ArrayList<>();

    @Override
    public void savePerson(Person person) {
        people.add(person);
    }

    @Override
    public List<Person> findPeople(String nameQuery) {
        ArrayList<Person> result = new ArrayList<>();
        for (Person person : people) {
            if (nameQuery == null || person.getFullName().toLowerCase().contains(nameQuery.toLowerCase())) {
                result.add(person);
            }
        }
        return result;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }

}
