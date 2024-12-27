package com.ovidiomirada.playwright;

import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.junit.Options;
import com.microsoft.playwright.junit.OptionsFactory;
import com.microsoft.playwright.junit.UsePlaywright;
import java.util.Arrays;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@UsePlaywright(AnAnnotatedPlaywrightTest.MyOptions.class)
public class AnAnnotatedPlaywrightTest {

  public static class MyOptions implements OptionsFactory {

    @Override
    public Options getOptions() {
      return new Options()
          .setHeadless(false)
          .setLaunchOptions(new BrowserType.LaunchOptions().setArgs(
              Arrays.asList("--no-sandbox", "--disable-gpu")));
    }
  }

  @Test
  void shouldShowThePageTitle(Page page) {
    page.navigate("https://practicesoftwaretesting.com");
    String title = page.title();
    Assertions.assertTrue(title.contains("Practice Software Testing"));
  }

  @Test
  void shouldShowSearchTermsInTheTitle(Page page) {
    page.navigate("https://practicesoftwaretesting.com");
    page.locator("[placeholder=Search]").fill("Pliers");
    page.locator("button:has-text('Search')").click();

    int matchingProductCount = page.locator(".card-title").count();

    Assertions.assertTrue(matchingProductCount > 0);
  }
}
