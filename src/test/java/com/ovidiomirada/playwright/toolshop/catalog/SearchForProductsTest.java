package com.ovidiomirada.playwright.toolshop.catalog;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.junit.UsePlaywright;
import com.ovidiomirada.playwright.HeadlessChromeOptions;
import com.ovidiomirada.playwright.toolshop.catalog.pageobjects.ProductList;
import com.ovidiomirada.playwright.toolshop.catalog.pageobjects.SearchComponent;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("Searching for products")
@Feature("Product Catalog")
@UsePlaywright(HeadlessChromeOptions.class)
public class SearchForProductsTest {

  @BeforeEach
  void openHomePage(Page page) {
    page.navigate("https://practicesoftwaretesting.com");
  }

  @Nested
  @DisplayName("Searching by keyword")
  @Story("Searching for products")
  class SearchingByKeyword {

    @Test
    @DisplayName("When there are matching results")
    void whenSearchingByKeyword(Page page) {
      SearchComponent searchComponent = new SearchComponent(page);
      ProductList productList = new ProductList(page);

      searchComponent.searchBy("tape");

      var matchingProducts = productList.getProductNames();

      Assertions.assertThat(matchingProducts)
          .contains("Tape Measure 7.5m", "Measuring Tape", "Tape Measure 5m");
    }

    @Test
    @DisplayName("When there are no matching results")
    void whenThereIsNoMatchingProduct(Page page) {
      SearchComponent searchComponent = new SearchComponent(page);
      ProductList productList = new ProductList(page);
      searchComponent.searchBy("unknown");

      var matchingProducts = productList.getProductNames();

      Assertions.assertThat(matchingProducts).isEmpty();
      Assertions.assertThat(productList.getSearchCompletedMessage())
          .contains("There are no products found.");
    }

    @Test
    @DisplayName("When the user clears a previous search results")
    void clearingTheSearchResults(Page page) {
      SearchComponent searchComponent = new SearchComponent(page);
      ProductList productList = new ProductList(page);
      searchComponent.searchBy("saw");

      var matchingFilteredProducts = productList.getProductNames();
      Assertions.assertThat(matchingFilteredProducts).hasSize(2);

      searchComponent.clearSearch();

      var matchingProducts = productList.getProductNames();
      Assertions.assertThat(matchingProducts).hasSize(9);
    }
  }
}
