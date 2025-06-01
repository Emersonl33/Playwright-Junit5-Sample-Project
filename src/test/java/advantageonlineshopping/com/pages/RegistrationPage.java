package advantageonlineshopping.com.pages;

import advantageonlineshopping.com.GlobalRegisterData;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static advantageonlineshopping.com.utils.ToolsUtils.highlightAndClick;
import static advantageonlineshopping.com.utils.ToolsUtils.inputFill;

public class RegistrationPage {
    private static final Logger log = LoggerFactory.getLogger(HomePage.class);
    private final Page page;

    public RegistrationPage(Page page) {
        this.page = page;
    }

    private Locator usernameInput() {
        return page.locator("[name='usernameRegisterPage']");
    }

    private Locator passwordInput() {
        return page.locator("[name='passwordRegisterPage']");
    }

    private Locator confirmPasswordInput() {
        return page.locator("[name='confirm_passwordRegisterPage']");
    }

    private Locator emailInput() {
        return page.locator("[name='emailRegisterPage']");
    }

    private Locator firstnameInput() {
        return page.locator("[name='first_nameRegisterPage']");
    }

    private Locator lastnameInput() {
        return page.locator("[name='last_nameRegisterPage']");
    }

    private Locator phoneNumberInput() {
        return page.locator("[name='phone_numberRegisterPage']");
    }

    private Locator cityInput() {
        return page.locator("[name='cityRegisterPage']");
    }

    private Locator adressInput() {
        return page.locator("[name='addressRegisterPage']");
    }

    private Locator countryComboBox() {
        return page.locator("[name='countryListboxRegisterPage']");
    }

    private Locator stateInput() {
        return page.locator("[name='state_/_province_/_regionRegisterPage']");
    }

    private Locator postalCodeInput() {
        return page.locator("[name='postal_codeRegisterPage']");
    }

    private Locator agreeCheckBox() {
        return page.locator("[name='i_agree']");
    }

    private Locator registerButton(){
        return page.locator("#register_btn");
    }

    public void usernameInputFill(String username) {
        GlobalRegisterData.USERNAME = username;
        inputFill(usernameInput(), username);
    }

    public void emailInputFill(String email) {
        GlobalRegisterData.EMAIL = email;
        inputFill(emailInput(), email);
    }

    public void passwordInputFill(String password) {
        GlobalRegisterData.PASSWORD = password;
        inputFill(passwordInput(), password);
    }

    public void confirmPasswordInputFill(String password) {
        GlobalRegisterData.CONFIRM_PASSWORD = password;
        inputFill(confirmPasswordInput(), password);
    }

    public void firstnameInputFill(String firstname) {
        GlobalRegisterData.FIRSTNAME = firstname;
        inputFill(firstnameInput(), firstname);
    }

    public void lastnameInputFill(String lastname) {
        GlobalRegisterData.LASTNAME = lastname;
        inputFill(lastnameInput(), lastname);
    }

    public void phoneNumberFill(String phoneNumber) {
        GlobalRegisterData.PHONE_NUMBER = phoneNumber;
        inputFill(phoneNumberInput(), phoneNumber);
    }

    public void countrySelect(String country) {
        GlobalRegisterData.COUNTRY = country;
        countryComboBox().selectOption(country);
    }

    public void cityInputFill(String city) {
        GlobalRegisterData.CITY = city;
        inputFill(cityInput(), city);
    }

    public void adressInputFill(String adress) {
        GlobalRegisterData.ADDRESS = adress;
        inputFill(adressInput(), adress);
    }

    public void stateInputFill(String state) {
        GlobalRegisterData.STATE = state;
        inputFill(stateInput(), state);
    }

    public void postalCodeFill(String postalCode) {
        GlobalRegisterData.POSTAL_CODE = postalCode;
        inputFill(postalCodeInput(), postalCode);
    }

    public void clickAgreeCheckBox() {
        highlightAndClick(agreeCheckBox());
    }

    public void clickRegisterButton() {
        highlightAndClick(registerButton());
    }


}
