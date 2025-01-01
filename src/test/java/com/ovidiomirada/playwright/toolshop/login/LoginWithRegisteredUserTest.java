package com.ovidiomirada.playwright.toolshop.login;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.junit.UsePlaywright;
import com.ovidiomirada.playwright.HeadlessChromeOptions;
import com.ovidiomirada.playwright.toolshop.domain.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@UsePlaywright(HeadlessChromeOptions.class)
public class LoginWithRegisteredUserTest {

  @Test
  @DisplayName("Should be able to login with a registered user")
  void should_login_with_registered_user(Page page) {
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
  void should_reject_user_with_invalid_password(Page page) {
    User user = User.randomUser();
    UserAPIClient userAPIClient = new UserAPIClient(page);
    userAPIClient.registerUser(user);

    LoginPage loginPage = new LoginPage(page);
    loginPage.open();
    loginPage.loginAs(user.withPassword("wrong-password"));

    assertThat(loginPage.loginErrorMessage()).isEqualTo("Invalid email or password");
  }
}
