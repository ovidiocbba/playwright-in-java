package com.ovidiomirada.playwright;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.junit.UsePlaywright;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@UsePlaywright
public class ASimplePlaywrightTest {

  @Test
  void shouldShowThePageTitle(Page page) {
    page.navigate("https://practicesoftwaretesting.com");
    String title = page.title();

    Assertions.assertTrue(title.contains("Practice Software Testing"));
  }

  @Test
  void shouldShowSearchTermsInTheTitle(Page page) {
    page.navigate("https://practicesoftwaretesting.com");
    page.locator("[placeholder=Search]").fill("Pliers");
    page.locator("button:has-text('Search')").click();

    int matchingProductCount = page.locator(".card").count();

    Assertions.assertTrue(matchingProductCount > 0);
  }
}
