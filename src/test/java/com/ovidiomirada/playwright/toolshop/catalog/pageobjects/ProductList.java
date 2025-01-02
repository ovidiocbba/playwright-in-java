package com.ovidiomirada.playwright.toolshop.catalog.pageobjects;

import com.microsoft.playwright.Page;
import com.ovidiomirada.playwright.toolshop.domain.ProductSummary;
import com.ovidiomirada.playwright.toolshop.fixtures.ScreenshotManager;
import io.qameta.allure.Step;
import java.util.List;

public class ProductList {

  private final Page page;

  public ProductList(Page page) {
    this.page = page;
  }


  public List<String> getProductNames() {
    return page.getByTestId("product-name").allInnerTexts();
  }

  public List<ProductSummary> getProductSummaries() {
    return page.locator(".card").all().stream().map(productCard -> {
      String productName = productCard.getByTestId("product-name").textContent().strip();
      String productPrice = productCard.getByTestId("product-price").textContent();
      return new ProductSummary(productName, productPrice);
    }).toList();
  }

  @Step("View product details")
  public void viewProductDetails(String productName) {
    ScreenshotManager.takeScreenshot(page, "View product details for " + productName);
    page.locator(".card").getByText(productName).click();
  }

  public String getSearchCompletedMessage() {
    return page.getByTestId("search_completed").textContent();
  }
}
