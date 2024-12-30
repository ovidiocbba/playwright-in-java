package com.ovidiomirada.playwright;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.Route;
import java.util.Arrays;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

public class PlaywrightRestAPITest {

  protected static Playwright playwright;
  protected static Browser browser;
  protected static BrowserContext browserContext;

  Page page;

  @BeforeAll
  static void setUpBrowser() {
    playwright = Playwright.create();
    playwright.selectors().setTestIdAttribute("data-test");
    browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false)
        .setArgs(Arrays.asList("--no-sandbox", "--disable-extensions", "--disable-gpu")));
  }

  @BeforeEach
  void setUp() {
    browserContext = browser.newContext(
        new Browser.NewContextOptions().setViewportSize(1920, 1080));
    page = browserContext.newPage();

    page.navigate("https://practicesoftwaretesting.com");
    page.getByPlaceholder("Search").waitFor();
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

  @DisplayName("Playwright allows us to mock out API responses")
  @Nested
  class MockingAPIResponses {

    @Test
    @DisplayName("When a search returns a single product")
    void whenASingleItemIsFound() {
      page.route("**/products/search?q=pliers", route -> route.fulfill(
          new Route.FulfillOptions().setBody(MockSearchResponses.RESPONSE_WITH_A_SINGLE_ENTRY)
              .setStatus(200)));
      page.getByPlaceholder("Search").fill("pliers");
      page.getByPlaceholder("Search").press("Enter");
      assertThat(page.getByTestId("product-name")).hasCount(1);
      assertThat(page.getByTestId("product-name")).hasText("Super Pliers - Ovidio");
    }

    @Test
    @DisplayName("When a search returns no products")
    void whenNoItemsAreFound() {
      page.route("**/products/search?q=pliers", route -> route.fulfill(
          new Route.FulfillOptions().setBody(MockSearchResponses.RESPONSE_WITH_NO_ENTRIES)
              .setStatus(200)));
      var searchBox = page.getByPlaceholder("Search");
      searchBox.fill("pliers");
      searchBox.press("Enter");

      assertThat(page.getByTestId("product-name")).isHidden();
      assertThat(page.getByTestId("search_completed")).hasText("There are no products found.");
    }
  }
}
