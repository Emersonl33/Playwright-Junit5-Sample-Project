package advantageonlineshopping.com.pages;

import advantageonlineshopping.com.data.GlobalRegisterDataShopping;
import utils.ToolsUtils;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static utils.ToolsUtils.highlightAndClick;

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
        GlobalRegisterDataShopping.USERNAME = username;
        ToolsUtils.highlightScrollAndFill(usernameInput(), username);
    }

    public void emailInputFill(String email) {
        GlobalRegisterDataShopping.EMAIL = email;
        ToolsUtils.highlightScrollAndFill(emailInput(), email);
    }

    public void passwordInputFill(String password) {
        GlobalRegisterDataShopping.PASSWORD = password;
        ToolsUtils.highlightScrollAndFill(passwordInput(), password);
    }

    public void confirmPasswordInputFill(String password) {
        GlobalRegisterDataShopping.CONFIRM_PASSWORD = password;
        ToolsUtils.highlightScrollAndFill(confirmPasswordInput(), password);
    }

    public void firstnameInputFill(String firstname) {
        GlobalRegisterDataShopping.FIRSTNAME = firstname;
        ToolsUtils.highlightScrollAndFill(firstnameInput(), firstname);
    }

    public void lastnameInputFill(String lastname) {
        GlobalRegisterDataShopping.LASTNAME = lastname;
        ToolsUtils.highlightScrollAndFill(lastnameInput(), lastname);
    }

    public void phoneNumberFill(String phoneNumber) {
        GlobalRegisterDataShopping.PHONE_NUMBER = phoneNumber;
        ToolsUtils.highlightScrollAndFill(phoneNumberInput(), phoneNumber);
    }

    public void countrySelect(String country) {
        GlobalRegisterDataShopping.COUNTRY = country;
        countryComboBox().selectOption(country);
    }

    public void cityInputFill(String city) {
        GlobalRegisterDataShopping.CITY = city;
        ToolsUtils.highlightScrollAndFill(cityInput(), city);
    }

    public void adressInputFill(String adress) {
        GlobalRegisterDataShopping.ADDRESS = adress;
        ToolsUtils.highlightScrollAndFill(adressInput(), adress);
    }

    public void stateInputFill(String state) {
        GlobalRegisterDataShopping.STATE = state;
        ToolsUtils.highlightScrollAndFill(stateInput(), state);
    }

    public void postalCodeFill(String postalCode) {
        GlobalRegisterDataShopping.POSTAL_CODE = postalCode;
        ToolsUtils.highlightScrollAndFill(postalCodeInput(), postalCode);
    }

    public void clickAgreeCheckBox() {
        highlightAndClick(agreeCheckBox());
    }

    public void clickRegisterButton() {
        highlightAndClick(registerButton());
    }


}
