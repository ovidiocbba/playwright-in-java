package com.ovidiomirada.playwright;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import java.util.Arrays;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

public class PlaywrightAssertionsTest {

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

  @DisplayName("Making assertions about the contents of a field")
  @Nested
  class LocatingElementsUsingCSS {

    @BeforeEach
    void openContactPage() {
      page.navigate("https://practicesoftwaretesting.com/contact");
    }

    @DisplayName("Checking the value of a field")
    @Test
    void fieldValues() {
      var firstNameField = page.getByLabel("First name");

      firstNameField.fill("Sarah-Jane");

      assertThat(firstNameField).hasValue("Sarah-Jane");

      assertThat(firstNameField).not().isDisabled();
      assertThat(firstNameField).isVisible();
      assertThat(firstNameField).isEditable();
    }
  }
}
