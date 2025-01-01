package com.ovidiomirada.playwright.toolshop.contact;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.junit.UsePlaywright;
import com.ovidiomirada.playwright.HeadlessChromeOptions;
import com.ovidiomirada.playwright.toolshop.catalog.pageobjects.NavBar;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@UsePlaywright(HeadlessChromeOptions.class)
public class ContactFormTest {

  ContactForm contactForm;
  NavBar navigate;

  @BeforeEach
  void openContactPage(Page page) {
    contactForm = new ContactForm(page);
    navigate = new NavBar(page);
    navigate.toTheContactPage();
  }

  @DisplayName("Customers can use the contact form to contact us")
  @Test
  void completeForm(Page page) throws URISyntaxException {
    contactForm.setFirstName("Sarah-Jane");
    contactForm.setLastName("Smith");
    contactForm.setEmail("sarah@example.com");
    contactForm.setMessage(
        "A very long message to the warranty service about a warranty on a product!");
    contactForm.selectSubject("Warranty");

    Path fileToUpload = Paths.get(ClassLoader.getSystemResource("data/sample-data.txt").toURI());
    contactForm.setAttachment(fileToUpload);

    contactForm.submitForm();

    assertThat(contactForm.alertMessage()).isVisible();
    assertThat(contactForm.alertMessage()).hasText(
        "Thanks for your message! We will contact you shortly.");
  }

  @DisplayName("First name, last name, email and message are mandatory")
  @ParameterizedTest(name = "{arguments} is a mandatory field")
  @ValueSource(strings = {"First name", "Last name", "Email", "Message"})
  void mandatoryFields(String fieldName, Page page) {
    // Fill in the field values
    contactForm.setFirstName("Sarah-Jane");
    contactForm.setLastName("Smith");
    contactForm.setEmail("sarah@example.com");
    contactForm.setMessage(
        "A very long message to the warranty service about a warranty on a product!");
    contactForm.selectSubject("Warranty");

    // Clear one of the fields
    contactForm.clearField(fieldName);
    contactForm.submitForm();

    // Check the error message for that field
    assertThat(contactForm.alertMessage()).isVisible();
    assertThat(contactForm.alertMessage()).hasText(fieldName + " is required");
  }

  @DisplayName("The message must be at least 50 characters long")
  @Test
  void messageTooShort(Page page) {

    contactForm.setFirstName("Sarah-Jane");
    contactForm.setLastName("Smith");
    contactForm.setEmail("sarah@example.com");
    contactForm.setMessage("A short long message.");
    contactForm.selectSubject("Warranty");

    contactForm.submitForm();

    assertThat(contactForm.alertMessage()).isVisible();
    assertThat(contactForm.alertMessage()).hasText("Message must be minimal 50 characters");
  }

  @DisplayName("The email address must be correctly formatted")
  @ParameterizedTest(name = "'{arguments}' should be rejected")
  @ValueSource(strings = {"not-an-email", "not-an.email.com", "notanemail"})
  void invalidEmailField(String invalidEmail, Page page) {
    contactForm.setFirstName("Sarah-Jane");
    contactForm.setLastName("Smith");
    contactForm.setEmail(invalidEmail);
    contactForm.setMessage(
        "A very long message to the warranty service about a warranty on a product!");
    contactForm.selectSubject("Warranty");

    contactForm.submitForm();

    assertThat(contactForm.alertMessage()).isVisible();
    assertThat(contactForm.alertMessage()).hasText("Email format is invalid");
  }
}
