package com.ovidiomirada.playwright.toolshop.catalog.pageobjects;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import com.ovidiomirada.playwright.toolshop.fixtures.ScreenshotManager;
import io.qameta.allure.Step;

public class ProductDetails {

  private final Page page;

  public ProductDetails(Page page) {
    this.page = page;
  }

  @Step("Increase quantity")
  public void increaseQuantityBy(int increment) {
    for (int i = 1; i <= increment; i++) {
      page.getByTestId("increase-quantity").click();
    }
    ScreenshotManager.takeScreenshot(page, "Quantity increased by " + increment);
  }

  @Step("Add to cart")
  public void addToCart() {
    page.waitForResponse(
        response -> response.url().contains("/carts") && response.request().method().equals("POST"),
        () -> {
          page.getByText("Add to cart").click();
          page.getByRole(AriaRole.ALERT).click();
        });
    ScreenshotManager.takeScreenshot(page, "Added to cart");
  }

}
