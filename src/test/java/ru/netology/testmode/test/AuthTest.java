package ru.netology.testmode.test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import static com.codeborne.selenide.Selenide.$x;
import static com.codeborne.selenide.Selenide.open;

import static ru.netology.testmode.data.DataGenerator.Registration.getRegisteredUser;
import static ru.netology.testmode.data.DataGenerator.Registration.getUser;
import static ru.netology.testmode.data.DataGenerator.getRandomLogin;
import static ru.netology.testmode.data.DataGenerator.getRandomPassword;

class AuthTest {
    @BeforeEach
    void setup() {
        open("http://localhost:9999");
        Configuration.holdBrowserOpen = false;
    }

    @Test
    @DisplayName("Should successfully login with active registered user")
    void shouldSuccessfulLoginIfRegisteredActiveUser() {

        var registeredUser = getRegisteredUser("active");
        $x("//*[@name='login']").setValue(registeredUser.getLogin());
        $x("//*[@name='password']").setValue(registeredUser.getPassword());
        $x("//div[contains(@class, 'form-field')]/button[@role='button']").click();
        $x("//h2[contains(@class,'heading')]").shouldHave(Condition.text("Личный кабинет"), Duration.ofSeconds(15)).shouldBe(Condition.visible);
    }

    @Test
    @DisplayName("Should get error message if login with not registered user")
    void shouldGetErrorIfNotRegisteredUser() {
        var notRegisteredUser = getUser("active");
        $x("//*[@name='login']").setValue(notRegisteredUser.getLogin());
        $x("//*[@name='password']").setValue(notRegisteredUser.getPassword());
        $x("//div[contains(@class, 'form-field')]/button[@role='button']").click();
        $x("//div[contains(@class,'notification__content')]").shouldHave(Condition.text("Неверно указан логин или пароль"), Duration.ofSeconds(15)).shouldBe(Condition.visible);
    }

    @Test
    @DisplayName("Should get error message if login with blocked registered user")
    void shouldGetErrorIfBlockedUser() {
        var blockedUser = getRegisteredUser("blocked");

        $x("//*[@name='login']").setValue(blockedUser.getLogin());
        $x("//*[@name='password']").setValue(blockedUser.getPassword());
        $x("//div[contains(@class, 'form-field')]/button[@role='button']").click();
        $x("//div[contains(@class,'notification__content')]").shouldHave(Condition.text("Пользователь заблокирован"), Duration.ofSeconds(15)).shouldBe(Condition.visible);
    }

    @Test
    @DisplayName("Should get error message if login with wrong login")
    void shouldGetErrorIfWrongLogin() {
        var registeredUser = getRegisteredUser("active");
        var wrongLogin = getRandomLogin();

        $x("//*[@name='login']").setValue(wrongLogin);
        $x("//*[@name='password']").setValue(registeredUser.getPassword());
        $x("//div[contains(@class, 'form-field')]/button[@role='button']").click();
        $x("//div[contains(@class,'notification__content')]").shouldHave(Condition.text("Неверно указан логин или пароль"), Duration.ofSeconds(15)).shouldBe(Condition.visible);
    }

    @Test
    @DisplayName("Should get error message if login with wrong password")
    void shouldGetErrorIfWrongPassword() {
        var registeredUser = getRegisteredUser("active");
        var wrongPassword = getRandomPassword();

        $x("//*[@name='login']").setValue(registeredUser.getLogin());
        $x("//*[@name='password']").setValue(wrongPassword);
        $x("//div[contains(@class, 'form-field')]/button[@role='button']").click();
        $x("//div[contains(@class,'notification__content')]").shouldHave(Condition.text("Неверно указан логин или пароль"), Duration.ofSeconds(15)).shouldBe(Condition.visible);
    }
}
