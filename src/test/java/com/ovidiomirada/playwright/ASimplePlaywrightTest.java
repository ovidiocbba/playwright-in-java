package com.ovidiomirada.playwright;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import java.util.Arrays;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ASimplePlaywrightTest {

  private static Playwright playwright;
  private static Browser browser;
  private static BrowserContext browserContext;

  Page page;

  @BeforeAll
  public static void setUpBrowser() {
    playwright = Playwright.create();
    browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false)
        .setArgs(Arrays.asList("--no-sandbox", "--disable-extensions", "--disable-gpu")));
    browserContext = browser.newContext();
  }

  @BeforeEach
  public void setUp() {
    page = browserContext.newPage();
  }

  @AfterAll
  public static void tearDown() {
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
