package com.ovidiomirada.playwright.toolshop.fixtures;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import java.util.Arrays;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

public abstract class PlaywrightTestCase {

  protected static ThreadLocal<Playwright> playwright = ThreadLocal.withInitial(() -> {
    Playwright playwright = Playwright.create();
    playwright.selectors().setTestIdAttribute("data-test");
    return playwright;
  });

  protected static ThreadLocal<Browser> browser = ThreadLocal.withInitial(
      () -> playwright.get().chromium().launch(new BrowserType.LaunchOptions().setHeadless(false)
          .setArgs(Arrays.asList("--no-sandbox", "--disable-extensions", "--disable-gpu"))));

  protected BrowserContext browserContext;

  protected Page page;

  @BeforeEach
  void setUpBrowserContext() {
    browserContext = browser.get().newContext();
    page = browserContext.newPage();
  }

  @AfterEach
  void closeContext() {
    browserContext.close();
  }

  @AfterAll
  static void tearDown() {
    browser.get().close();
    browser.remove();
    playwright.get().close();
    playwright.remove();
  }
}
