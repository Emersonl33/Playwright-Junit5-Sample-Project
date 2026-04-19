package advantageonlineshopping.com.tests;

import Hooks;
import advantageonlineshopping.com.data.GlobalRegisterDataShopping;
import advantageonlineshopping.com.pages.HomePage;
import advantageonlineshopping.com.pages.RegistrationPage;
import utils.AIUtils;
import utils.AllureUtils;
import utils.MongoDBUtils;
import com.microsoft.playwright.options.LoadState;
import io.qameta.allure.Step;
import io.qameta.allure.junit5.AllureJunit5;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Map;

import static utils.AllureUtils.screenshot;

@Slf4j
@ExtendWith(AllureJunit5.class)
public class RegisterTest extends Hooks {

    RegistrationPage register = new RegistrationPage(page);
    HomePage home = new HomePage(page);

    @Test
    void userSignup() throws Exception {
        MongoDBUtils.init("local");
        stepGenerateRegisterData();
        MongoDBUtils.saveRegisterData("credentialsTests");
        stepNavigateToHomePage();
        home = new HomePage(page);
        register = new RegistrationPage(page);
        stepNavigateToRegister();
        stepRegisterInfos();
        stepConfirmRegister();
        MongoDBUtils.close();
    }

    @Step("Generate register data")
    void stepGenerateRegisterData() throws Exception {
        String model = "nvidia/nemotron-3-super-120b-a12b:free";
        String systemMsg = "You are a registration data generator. Be creative, avoid using generic names in username to prevent duplication!";
        String userMsg = "Generate a pure JSON, without additional text, containing the fields username, email," +
                " password <Use 4 characters or longer, Use maximum 12 characters, Including at least one lowercase letter, Including at least one uppercase letter, Including at least one number>," +
                " confirmPassword, firstname, lastname, phoneNumber, country <real name in English for all geographic info>, city, address, state, postalCode. The username need have maximum 13 characters.";

        Map<String, String> userData = AIUtils.generateUserData(model, systemMsg, userMsg);
        AIUtils.populateGlobalRegisterData(userData);
    }

    @Step("Navigate to home page")
    void stepNavigateToHomePage() {
        AllureUtils.attachLog("Execution logs", "Starting the test...\nStep 1 executed successfully");
        page.navigate("https://advantageonlineshopping.com/#/");
        page.waitForLoadState(LoadState.DOMCONTENTLOADED);
        screenshot(page, "Entry in home page");
    }

    @Step("Navigate to register page")
    void stepNavigateToRegister() {
        home.userIconClick();
        AllureUtils.screenshot(page, "Enter register page");
        home.createNewAccountLabelClick();
    }

    @Step("Fill register infos")
    void stepRegisterInfos() {
        register.usernameInputFill(GlobalRegisterDataShopping.USERNAME);
        register.emailInputFill(GlobalRegisterDataShopping.EMAIL);
        register.passwordInputFill(GlobalRegisterDataShopping.PASSWORD);
        register.confirmPasswordInputFill(GlobalRegisterDataShopping.CONFIRM_PASSWORD);
        register.firstnameInputFill(GlobalRegisterDataShopping.FIRSTNAME);
        register.lastnameInputFill(GlobalRegisterDataShopping.LASTNAME);
        register.phoneNumberFill(GlobalRegisterDataShopping.PHONE_NUMBER);
        register.countrySelect(GlobalRegisterDataShopping.COUNTRY);
        register.cityInputFill(GlobalRegisterDataShopping.CITY);
        register.adressInputFill(GlobalRegisterDataShopping.ADDRESS);
        register.stateInputFill(GlobalRegisterDataShopping.STATE);
        register.postalCodeFill(GlobalRegisterDataShopping.POSTAL_CODE);
        AllureUtils.screenshot(page, "Register fill");
    }

    @Step("Confirm register")
    void stepConfirmRegister() {
        register.clickAgreeCheckBox();
        register.clickRegisterButton();
        screenshot(page, "Register confirmation");

        String usernameUserIcon = home.userIconGetText();

        Assertions.assertEquals(
                GlobalRegisterDataShopping.USERNAME, usernameUserIcon,
                "Register failed. The username displayed in the icon does not match the one used during registration.\n" +
                        "Username found: " + usernameUserIcon + " | Expected: " + GlobalRegisterDataShopping.USERNAME
        );

        log.info("Registration completed successfully.");
        screenshot(page, "Register confirmation");
    }

}
