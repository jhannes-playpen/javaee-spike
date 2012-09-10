package com.exilesoft.javaee;

public class Person {

    private String fullName;

    public static Person withName(String fullName) {
        Person person = new Person();
        person.fullName = fullName;
        return person;
    }

    public String getFullName() {
        return fullName;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Person)) return false;
        Person other = ((Person) obj);
        return equals(fullName, other.fullName);
    }

    private <T> boolean equals(T a, T b) {
        return a != null ? a.equals(b) : b == null;
    }

    @Override
    public int hashCode() {
        return fullName.hashCode();
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{eqField1=" + fullName + "}";
    }

}
