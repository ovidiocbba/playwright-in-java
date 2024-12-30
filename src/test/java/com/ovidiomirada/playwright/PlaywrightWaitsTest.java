package com.ovidiomirada.playwright;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import java.util.Arrays;
import java.util.List;
import org.assertj.core.api.Assertions;
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
public class PlaywrightWaitsTest {

  protected static Playwright playwright;
  protected static Browser browser;
  protected static BrowserContext browserContext;

  Page page;

  @BeforeAll
  static void setUpBrowser() {
    playwright = Playwright.create();
    playwright.selectors().setTestIdAttribute("data-test");
    browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(true)
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

  @Nested
  class WaitingForState {

    @BeforeEach
    void openHomePage() {
      page.navigate("https://practicesoftwaretesting.com");
      page.waitForSelector(".card-img-top");
    }

    @Test
    void shouldShowAllProductNames() {
      List<String> productNames = page.getByTestId("product-name").allInnerTexts();
      Assertions.assertThat(productNames).contains("Pliers", "Bolt Cutters", "Hammer");
    }

    @Test
    void shouldShowAllProductImages() {
      List<String> productImageTitles = page.locator(".card-img-top").all().stream()
          .map(img -> img.getAttribute("alt")).toList();

      Assertions.assertThat(productImageTitles).contains("Pliers", "Bolt Cutters", "Hammer");
    }
  }

  @Nested
  class AutomaticWaits {

    @BeforeEach
    void openHomePage() {
      page.navigate("https://practicesoftwaretesting.com");
    }

    // Automatic wait
    @Test
    @DisplayName("Should wait for the filter checkbox options to appear before clicking")
    void shouldWaitForTheFilterCheckboxes() {

      var screwdriverFilter = page.getByLabel("Screwdriver");

      screwdriverFilter.click();

      assertThat(screwdriverFilter).isChecked();
    }
  }
}
