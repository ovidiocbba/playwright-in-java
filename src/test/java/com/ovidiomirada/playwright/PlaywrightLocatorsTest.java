package com.ovidiomirada.playwright;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.assertions.PlaywrightAssertions;
import com.microsoft.playwright.options.LoadState;
import java.util.Arrays;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

@Execution(ExecutionMode.SAME_THREAD)
public class PlaywrightLocatorsTest {

  protected static Playwright playwright;
  protected static Browser browser;
  protected static BrowserContext browserContext;

  Page page;

  @BeforeAll
  static void setUpBrowser() {
    playwright = Playwright.create();
    browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false)
        .setArgs(Arrays.asList("--no-sandbox", "--disable-extensions", "--disable-gpu")));
  }

  @BeforeEach
  void setUp() {
    browserContext = browser.newContext();
    page = browserContext.newPage();
  }

  @AfterEach
  void closeContext() {
    browserContext.close();
  }

  @AfterAll
  static void tearDown() {
    browser.close();
    playwright.close();
  }

  @DisplayName("Locating elements by text")
  @Nested
  class LocatingElementsByText {

    @BeforeEach
    void openTheCatalogPage() {
      openPage();
    }

    @DisplayName("Locating an element by text contents")
    @Test
    void byText() {
      page.getByText("Bolt Cutters").click();

      PlaywrightAssertions.assertThat(page.getByText("MightyCraft Hardware")).isVisible();
    }
  }

  private void openPage() {
    page.navigate("https://practicesoftwaretesting.com");
    page.waitForLoadState(LoadState.NETWORKIDLE);
  }
}
