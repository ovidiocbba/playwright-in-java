package com.ovidiomirada.playwright;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ASimplePlaywrightTest {

  Playwright playwright;
  Browser browser;
  Page page;

  @BeforeEach
  void setup() {
    playwright = Playwright.create();
    browser = playwright.chromium().launch();
    page = browser.newPage();
  }

  @AfterEach
  void teardown() {
    browser.close();
    playwright.close();
  }

  @Test
  void shouldShowThePageTitle() {
    page.navigate("https://practicesoftwaretesting.com");
    String title = page.title();

    Assertions.assertTrue(title.contains("Practice Software Testing"));
  }

  @Test
  void shouldShowSearchTermsInTheTitle() {
    page.navigate("https://practicesoftwaretesting.com");
    page.locator("[placeholder=Search]").fill("Pliers");
    page.locator("button:has-text('Search')").click();

    int matchingProductCount = page.locator(".card").count();

    Assertions.assertTrue(matchingProductCount > 0);
  }
}
