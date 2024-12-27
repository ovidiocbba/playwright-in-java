package com.ovidiomirada.playwright;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.AriaRole;
import java.util.Arrays;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class AddingItemsToTheCartTest {

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
    browserContext = browser.newContext(
        new Browser.NewContextOptions().setViewportSize(1920, 1080));
    page = browserContext.newPage();
  }

  @AfterAll
  public static void tearDown() {
    browser.close();
    playwright.close();
  }

  @Test
  void shouldShowSearchTermsInTheTitle() {
    page.navigate("https://practicesoftwaretesting.com");
    page.getByPlaceholder("Search").fill("Pliers");
    page.getByPlaceholder("Search").press("Enter");
    page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Search")).click();

    assertThat(page.locator(".card")).hasCount(4);

    List<String> productNames = page.locator("[data-test='product-name']").allTextContents();
    Assertions.assertThat(productNames).allMatch(name -> name.contains("Pliers"));

    Locator outOfStockItem = page.locator(".card")
        .filter(new Locator.FilterOptions().setHasText("Out of stock"))
        .locator("[data-test='product-name']");

    assertThat(outOfStockItem).hasCount(1);
    assertThat(outOfStockItem).hasText("Long Nose Pliers");
  }
}
