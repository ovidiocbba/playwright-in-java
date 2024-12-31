package com.ovidiomirada.playwright;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.AriaRole;
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
public class PlaywrightPageObjectTest {

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

  @BeforeEach
  void openHomePage() {
    page.navigate("https://practicesoftwaretesting.com");
  }

  @Nested
  class WhenSearchingProductsByKeyword {

    @DisplayName("Without Page Objects")
    @Test
    void withoutPageObjects() {
      page.waitForResponse("**/products/search?q=tape", () -> {
        page.getByPlaceholder("Search").fill("tape");
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Search")).click();
      });
      List<String> matchingProducts = page.getByTestId("product-name").allInnerTexts();
      Assertions.assertThat(matchingProducts)
          .contains("Tape Measure 7.5m", "Measuring Tape", "Tape Measure 5m");
    }

    @DisplayName("With Page Objects")
    @Test
    void withPageObjects() {
      SearchComponent searchComponent = new SearchComponent(page);
      ProductList productList = new ProductList(page);

      searchComponent.searchBy("tape");

      var matchingProducts = productList.getProductNames();

      Assertions.assertThat(matchingProducts)
          .contains("Tape Measure 7.5m", "Measuring Tape", "Tape Measure 5m");
    }
  }

  class SearchComponent {

    private final Page page;

    SearchComponent(Page page) {
      this.page = page;
    }

    public void searchBy(String keyword) {
      page.waitForResponse("**/products/search?q=" + keyword, () -> {
        page.getByPlaceholder("Search").fill(keyword);
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Search")).click();
      });
    }
  }

  class ProductList {

    private final Page page;

    ProductList(Page page) {
      this.page = page;
    }

    public List<String> getProductNames() {
      return page.getByTestId("product-name").allInnerTexts();
    }

    public void viewProductDetails(String productName) {
      page.locator(".card").getByText(productName).click();
    }
  }
}
