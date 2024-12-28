package com.ovidiomirada.playwright;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.junit.UsePlaywright;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@UsePlaywright(HeadlessChromeOptions.class)
public class PlaywrightFormsTest {

  @DisplayName("Interacting with text fields")
  @Nested
  class WhenInteractingWithTextFields {

    @BeforeEach
    void openContactPage(Page page) {
      page.navigate("https://practicesoftwaretesting.com/contact");
    }

    @DisplayName("Complete the form")
    @Test
    void completeForm(Page page) throws URISyntaxException {
      var firstNameField = page.getByLabel("First name");
      var lastNameField = page.getByLabel("Last name");
      var emailNameField = page.getByLabel("Email");
      var messageField = page.getByLabel("Message");
      var subjectField = page.getByLabel("Subject");
      var uploadField = page.getByLabel("Attachment");

      Path fileToUpload = Paths.get(ClassLoader.getSystemResource("data/sample-data.txt").toURI());

      page.setInputFiles("#attachment", fileToUpload);

      firstNameField.fill("Sarah-Jane");
      lastNameField.fill("Smith");
      emailNameField.fill("sarah-jane@example.com");
      messageField.fill("Hello, world!");
      subjectField.selectOption("Warranty");

      assertThat(firstNameField).hasValue("Sarah-Jane");
      assertThat(lastNameField).hasValue("Smith");
      assertThat(emailNameField).hasValue("sarah-jane@example.com");
      assertThat(messageField).hasValue("Hello, world!");
      assertThat(subjectField).hasValue("warranty");

      String uploadedFile = uploadField.inputValue();
      org.assertj.core.api.Assertions.assertThat(uploadedFile).endsWith("sample-data.txt");
    }
  }
}
