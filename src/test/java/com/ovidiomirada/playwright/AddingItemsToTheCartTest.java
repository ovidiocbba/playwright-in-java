package com.ovidiomirada.playwright;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.junit.UsePlaywright;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@UsePlaywright(HeadlessChromeOptions.class)
public class AddingItemsToTheCartTest {

  @DisplayName("Search for pliers")
  @Test
  void searchForPliers(Page page) {
    page.onConsoleMessage(msg -> System.out.println(msg.text()));

    page.navigate("https://practicesoftwaretesting.com");
    page.getByPlaceholder("Search").fill("Pliers");
    page.getByPlaceholder("Search").press("Enter");

    page.waitForLoadState();
    page.waitForCondition(() -> page.getByTestId("product-name").count() > 0);

    List<String> products = page.getByTestId("product-name").allTextContents();
    Assertions.assertThat(products.get(0)).containsIgnoringCase("Pliers");

    assertThat(page.locator(".card")).hasCount(4);

    List<String> productNames = page.getByTestId("product-name").allTextContents();
    Assertions.assertThat(productNames).allMatch(name -> name.contains("Pliers"));

    Locator outOfStockItem = page.locator(".card")
        .filter(new Locator.FilterOptions().setHasText("Out of stock")).getByTestId("product-name");

    assertThat(outOfStockItem).hasCount(1);
    assertThat(outOfStockItem).hasText("Long Nose Pliers");
  }
}
