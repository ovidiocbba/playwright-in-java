package com.ovidiomirada.playwright;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.LoadState;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import org.assertj.core.api.Assertions;
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

    @DisplayName("Making assertions about data values")
    @Nested
    class MakingAssertionsAboutDataValues {

      @BeforeEach
      void openHomePage() {
        page.navigate("https://practicesoftwaretesting.com");
        page.waitForCondition(() -> page.getByTestId("product-name").count() > 0);
      }

      @Test
      void allProductPricesShouldBeCorrectValues() {
        List<Double> prices = page.getByTestId("product-price")
            .allInnerTexts()
            .stream()
            .map(price -> Double.parseDouble(price.replace("$","")))
            .toList();

        Assertions.assertThat(prices)
            .isNotEmpty()
            .allMatch(price -> price > 0)
            .doesNotContain(0.0)
            .allMatch(price -> price < 1000)
            .allSatisfy(price ->
                Assertions.assertThat(price)
                    .isGreaterThan(0.0)
                    .isLessThan(1000.0));
      }

      @Test
      void shouldSortInAlphabeticalOrder() {
        page.getByLabel("Sort").selectOption("Name (A - Z)");
        page.waitForLoadState(LoadState.NETWORKIDLE);

        List<String> productNames = page.getByTestId("product-name").allTextContents();

        Assertions.assertThat(productNames).isSortedAccordingTo(String.CASE_INSENSITIVE_ORDER);
      }

      @Test
      void shouldSortInReverseAlphabeticalOrder() {
        page.getByLabel("Sort").selectOption("Name (Z - A)");
        page.waitForLoadState(LoadState.NETWORKIDLE);

        List<String> productNames = page.getByTestId("product-name").allTextContents();

        Assertions.assertThat(productNames).isSortedAccordingTo(Comparator.reverseOrder());
      }
    }
  }
}
