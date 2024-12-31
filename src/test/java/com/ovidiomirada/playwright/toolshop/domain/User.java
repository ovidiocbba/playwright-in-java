package com.ovidiomirada.playwright.toolshop.domain;

import net.datafaker.Faker;

public record User(
    String first_name,
    String last_name,
    String address,
    String city,
    String state,
    String country,
    String postcode,
    String phone,
    String dob,
    String password,
    String email
) {

  public static User randomUser(String firstName) {
    Faker fake = new Faker();
    return new User(
        firstName,
        fake.name().lastName(),
        fake.address().streetAddress(),
        fake.address().city(),
        fake.address().state(),
        fake.address().country(),
        fake.address().zipCode(),
        fake.phoneNumber().phoneNumber(),
        "1990-01-01",
        "Az1234£!3",
        fake.internet().emailAddress()
    );
  }

  public User withPassword(String password) {
    return new User(first_name, last_name, address, city, state, country, postcode, phone, dob,
        password, email);
  }
}
