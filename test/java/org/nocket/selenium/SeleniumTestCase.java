package org.nocket.selenium;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.nocket.selenium.infrastructure.SeleniumProperties;
import org.nocket.selenium.infrastructure.WebDriverFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

public abstract class SeleniumTestCase {

	private static final int DEFAULT_PORT = new Integer(
			SeleniumProperties.getProperty(SeleniumProperties.WEBDRIVER_PORT)).intValue();
	private static final String BASE_URL = SeleniumProperties.getProperty(SeleniumProperties.WEBDRIVER_URL);
	private static StringBuffer verificationErrors = new StringBuffer();
	protected static WebDriver driver;

	@BeforeClass
	public static void createDriver() throws Exception {
		String webDriverName = SeleniumProperties.getProperty(SeleniumProperties.WEBDRIVER);
		driver = WebDriverFactory.getInstance(webDriverName, null, null, false);
	}

	@AfterClass
	public static void afterClass() {
		tearDown();
	}

	protected static void getSite(String siteUrl) {
		driver.get(BASE_URL + DEFAULT_PORT + siteUrl);
	}

	protected void setFieldValue(String fieldID, String value) {
		driver.findElement(By.id(fieldID)).clear();
		driver.findElement(By.id(fieldID)).sendKeys(value);
	}

	protected void selectComboboxValue(String comboboxID, String value) {
		Select combobox = new Select(driver.findElement(By.id(comboboxID)));
		combobox.selectByValue(value);
	}

	protected void selectMultipleChoiceByValue(String multiplechoiceID, String... value) {
		Select multiplechoice = new Select(driver.findElement(By.id(multiplechoiceID)));
		for (int i = 0; i < value.length; i++) {
			multiplechoice.selectByValue(value[i]);
		}
	}

	protected void selectMultipleChoiceByVisibleText(String multiplechoiceID, String... visibleText) {
		Select multiplechoice = new Select(driver.findElement(By.id(multiplechoiceID)));
		for (int i = 0; i < visibleText.length; i++) {
			multiplechoice.selectByVisibleText(visibleText[i]);
		}
	}

	protected String[] getAllMultiplechoiceOptions(String multiplechoiceID) {
		Select multiplechoice = new Select(driver.findElement(By.id(multiplechoiceID)));
		List<WebElement> optionElements = multiplechoice.getOptions();
		String[] optionText = new String[optionElements.size()];

		for (int i = 0; i < optionText.length; i++) {
			optionText[i] = optionElements.get(i).getText();
		}
		return optionText;
	}

	protected void selectCheckbox(String checkboxID) {
		if (!driver.findElement(By.id(checkboxID)).isSelected()) {
			driver.findElement(By.id(checkboxID)).click();
		}
	}

	protected void deselectCheckbox(String checkboxID) {
		if (driver.findElement(By.id(checkboxID)).isSelected()) {
			driver.findElement(By.id(checkboxID)).click();
		}
	}

	protected void clickButtonByXpath(String xpath) {
		driver.findElement(By.xpath(xpath)).click();
	}

	// compares expected errormessage with current errormessage
	protected void assertErrorMessage(String errorID, String errorMessage) {
		assertEquals(errorMessage, driver.findElement(By.id(errorID)).getText());
	}

	protected void assertNoError(String errorID) {
		assertTrue("Error-Element '" + errorID + "' is visible!", !isElementPresent(By.id(errorID)));
	}

	protected void assertErrorMessageNotNull(String errorID) {
		if (isElementPresent(By.id(errorID))) {
			assertTrue("Error visible, but no error message implemented!", isErrorNotNull(errorID));
		}
	}

	private boolean isErrorNotNull(String errorID) {
		if (driver.findElement(By.id(errorID)).getText().equals("")) {
			return false;
		}
		return true;
	}

	protected boolean isElementPresent(By by) {
		try {
			driver.findElement(by);
			return true;
		} catch (NoSuchElementException e) {
			return false;
		}
	}

	private static void tearDown() {
		driver.quit();
		String verificationErrorString = verificationErrors.toString();
		if (!"".equals(verificationErrorString)) {
			fail(verificationErrorString);
		}
	}
}