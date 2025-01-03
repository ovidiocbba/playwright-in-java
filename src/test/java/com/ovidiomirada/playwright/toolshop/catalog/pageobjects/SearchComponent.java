package com.ovidiomirada.playwright.toolshop.catalog.pageobjects;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import io.qameta.allure.Step;

public class SearchComponent {

  private final Page page;

  public SearchComponent(Page page) {
    this.page = page;
  }

  @Step("Search for keyword")
  public void searchBy(String keyword) {
    page.waitForResponse("**/products/search?q=**", () -> {
      page.getByPlaceholder("Search").fill(keyword);
      page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Search")).click();
    });
  }

  @Step("Clear the search criteria")
  public void clearSearch() {
    page.waitForResponse("**/products**", () -> {
      page.getByTestId("search-reset").click();
    });
  }

  public void filterBy(String filterName) {
    page.waitForResponse("**/products?**by_category=**", () -> {
      page.getByLabel(filterName).click();
    });
  }

  public void sortBy(String sortFilter) {
    page.waitForResponse("**/products?sort=**", () -> {
      page.getByTestId("sort").selectOption(sortFilter);
    });
    page.waitForTimeout(250);
  }
}
