package com.ovidiomirada.playwright.toolshop.cucumber.stepdefinitions;

import com.ovidiomirada.playwright.toolshop.catalog.pageobjects.NavBar;
import com.ovidiomirada.playwright.toolshop.catalog.pageobjects.ProductList;
import com.ovidiomirada.playwright.toolshop.catalog.pageobjects.SearchComponent;
import com.ovidiomirada.playwright.toolshop.fixtures.ProductSummary;
import io.cucumber.java.Before;
import io.cucumber.java.DataTableType;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.util.List;
import java.util.Map;
import org.assertj.core.api.Assertions;

public class ProductCatalogStepDefinitions {

  NavBar navBar;
  SearchComponent searchComponent;
  ProductList productList;

  @Before
  public void setupPageObjects() {
    navBar = new NavBar(PlaywrightCucumberFixtures.getPage());
    searchComponent = new SearchComponent(PlaywrightCucumberFixtures.getPage());
    productList = new ProductList(PlaywrightCucumberFixtures.getPage());
  }

  @Given("Sally is on the home page")
  public void sally_is_on_the_home_page() {
    navBar.openHomePage();
  }

  @When("she searches for {string}")
  public void she_searches_for(String searchTerm) {
    searchComponent.searchBy(searchTerm);
  }

  @Then("the {string} product should be displayed")
  public void the_product_should_be_displayed(String productName) {
    var matchingProducts = productList.getProductNames();
    Assertions.assertThat(matchingProducts).contains(productName);
  }

  @Then("the following products should be displayed:")
  public void theFollowingProductsShouldBeDisplayed(List<String> expectedProduct) {
    var matchingProducts = productList.getProductNames();
    Assertions.assertThat(matchingProducts).containsAll(expectedProduct);
  }
}
