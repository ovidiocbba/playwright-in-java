package com.ovidiomirada.playwright.toolshop.login;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.ovidiomirada.playwright.toolshop.domain.User;
import com.ovidiomirada.playwright.toolshop.fixtures.PlaywrightTestCase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class LoginWithRegisteredUserTest extends PlaywrightTestCase {

  @Test
  @DisplayName("Should be able to login with a registered user")
  void should_login_with_registered_user() {
    // Register a user via the API
    User user = User.randomUser();
    UserAPIClient userAPIClient = new UserAPIClient(page);
    userAPIClient.registerUser(user);

    // Login via the login page
    LoginPage loginPage = new LoginPage(page);
    loginPage.open();
    loginPage.loginAs(user);

    // Check that we are on the right account page
    assertThat(loginPage.title()).isEqualTo("My account");
  }

  @Test
  @DisplayName("Should reject a user if they provide a wrong password")
  void should_reject_user_with_invalid_password() {
    User user = User.randomUser();
    UserAPIClient userAPIClient = new UserAPIClient(page);
    userAPIClient.registerUser(user);

    LoginPage loginPage = new LoginPage(page);
    loginPage.open();
    loginPage.loginAs(user.withPassword("wrong-password"));

    assertThat(loginPage.loginErrorMessage()).isEqualTo("Invalid email or password");
  }
}
