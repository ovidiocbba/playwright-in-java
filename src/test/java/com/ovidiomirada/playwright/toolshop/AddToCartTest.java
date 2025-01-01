package com.ovidiomirada.playwright.toolshop;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.junit.UsePlaywright;
import com.ovidiomirada.playwright.HeadlessChromeOptions;
import com.ovidiomirada.playwright.toolshop.catalog.pageobjects.CartLineItem;
import com.ovidiomirada.playwright.toolshop.catalog.pageobjects.CheckoutCart;
import com.ovidiomirada.playwright.toolshop.catalog.pageobjects.NavBar;
import com.ovidiomirada.playwright.toolshop.catalog.pageobjects.ProductDetails;
import com.ovidiomirada.playwright.toolshop.catalog.pageobjects.ProductList;
import com.ovidiomirada.playwright.toolshop.catalog.pageobjects.SearchComponent;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

@UsePlaywright(HeadlessChromeOptions.class)
public class AddToCartTest {

  SearchComponent searchComponent;
  ProductList productList;
  ProductDetails productDetails;
  NavBar navBar;
  CheckoutCart checkoutCart;

  @BeforeEach
  void openHomePage(Page page) {
    page.navigate("https://practicesoftwaretesting.com");
  }

  @BeforeEach
  void setUp(Page page) {
    searchComponent = new SearchComponent(page);
    productList = new ProductList(page);
    productDetails = new ProductDetails(page);
    navBar = new NavBar(page);
    checkoutCart = new CheckoutCart(page);
  }

  @Test
  void whenCheckingOutASingleItem() {
    searchComponent.searchBy("pliers");
    productList.viewProductDetails("Combination Pliers");

    productDetails.increaseQuanityBy(2);
    productDetails.addToCart();

    navBar.openCart();

    List<CartLineItem> lineItems = checkoutCart.getLineItems();

    Assertions.assertThat(lineItems).hasSize(1).first().satisfies(item -> {
      Assertions.assertThat(item.title()).contains("Combination Pliers");
      Assertions.assertThat(item.quantity()).isEqualTo(3);
      Assertions.assertThat(item.total()).isEqualTo(item.quantity() * item.price());
    });
  }

  @Test
  void whenCheckingOutMultipleItems() {
    navBar.openHomePage();
    productList.viewProductDetails("Bolt Cutters");
    productDetails.increaseQuanityBy(2);
    productDetails.addToCart();

    navBar.openHomePage();
    productList.viewProductDetails("Slip Joint Pliers");
    productDetails.addToCart();

    navBar.openCart();

    List<CartLineItem> lineItems = checkoutCart.getLineItems();

    Assertions.assertThat(lineItems).hasSize(2);
    List<String> productNames = lineItems.stream().map(CartLineItem::title).toList();
    Assertions.assertThat(productNames).contains("Bolt Cutters", "Slip Joint Pliers");

    Assertions.assertThat(lineItems).allSatisfy(item -> {
      Assertions.assertThat(item.quantity()).isGreaterThanOrEqualTo(1);
      Assertions.assertThat(item.price()).isGreaterThan(0.0);
      Assertions.assertThat(item.total()).isGreaterThan(0.0);
      Assertions.assertThat(item.total()).isEqualTo(item.quantity() * item.price());
    });
  }
}
