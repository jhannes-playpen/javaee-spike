package com.exilesoft.javaee;

import static org.fest.assertions.Assertions.assertThat;

import org.junit.Test;

public class PersonTest {

  @Test
  public void equality() {
    assertThat(Person.withName("Darth Vader"))
      .isEqualTo(Person.withName("Darth Vader"))
      .isNotEqualTo(Person.withName("Anakin Skywalker"))
      .isNotEqualTo(new Object())
      .isNotEqualTo(null);
  }
}
