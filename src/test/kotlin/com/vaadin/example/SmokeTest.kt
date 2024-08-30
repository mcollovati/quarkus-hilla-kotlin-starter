package com.vaadin.example

import java.lang.management.ManagementFactory
import java.time.Duration
import java.util.function.Function
import java.util.function.Supplier
import com.codeborne.selenide.CollectionCondition
import com.codeborne.selenide.Condition
import com.codeborne.selenide.Configuration
import com.codeborne.selenide.Selectors
import com.codeborne.selenide.Selenide
import com.codeborne.selenide.SelenideElement
import com.codeborne.selenide.junit5.BrowserPerTestStrategyExtension
import io.quarkus.test.common.http.TestHTTPResource
import io.quarkus.test.junit.QuarkusTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.openqa.selenium.WebDriver

@QuarkusTest
@ExtendWith(BrowserPerTestStrategyExtension::class)
internal class SmokeTest {
    @TestHTTPResource
    private val baseURL: String? = null

    @BeforeEach
    fun setup() {
        Configuration.headless = runHeadless()
        System.setProperty("chromeoptions.args", "--remote-allow-origins=*")
    }

    @Test
    fun rootPath_mainViewDisplayed() {
        openAndWait(Supplier { Selenide.`$`("vaadin-app-layout") })
        Selenide.`$`("section.view vaadin-text-field")
            .shouldBe(Condition.visible)
        Selenide.`$$`("section.view vaadin-button")
            .filter(Condition.text("Say Hello"))
            .first().shouldBe(Condition.visible)
    }

    @Test
    fun mainView_sayHelloButtonClicked_notificationShown() {
        openAndWait(Supplier { Selenide.`$`("vaadin-app-layout") })

        val textField = Selenide.`$`("section.view vaadin-text-field")
            .shouldBe(Condition.visible)
        val button = Selenide.`$$`("section.view vaadin-button")
            .filter(Condition.text("Say Hello"))
            .first().shouldBe(Condition.visible)

        button.click()
        val messages = Selenide.`$$`("section.view li.message")
            .shouldHave(CollectionCondition.size(1))
            .shouldHave(CollectionCondition.exactTexts("Hello stranger"))

        val name = "John"
        textField.`$`("input").setValue(name)
        button.click()

        messages.shouldHave(CollectionCondition.size(2))
            .shouldHave(
                CollectionCondition.exactTexts(
                    "Hello John",
                    "Hello stranger"
                )
            )
    }


    protected fun openAndWait(selector: Supplier<SelenideElement?>?) {
        openAndWait(baseURL, selector!!)
    }

    protected fun openAndWait(
        url: String?,
        selector: Supplier<SelenideElement?>
    ) {
        Selenide.open(url)
        waitForDevServer()
        selector.get()!!.shouldBe(Condition.visible, Duration.ofSeconds(10))
        Selenide.`$`(
            Selectors.shadowCss(
                "div.dev-tools.error",
                "vaadin-dev-tools"
            )
        ).shouldNot(Condition.exist)
        Selenide.`$`(
            Selectors.shadowCss(
                "main",
                "vite-plugin-checker-error-overlay"
            )
        ).shouldNot(Condition.exist)
    }

    protected fun waitForDevServer() {
        Selenide.Wait()
            .withTimeout(Duration.ofMinutes(20))
            .until<Boolean?>(Function { d: WebDriver? ->
                java.lang.Boolean.TRUE != Selenide.executeJavaScript<Any?>(
                    "return window.Vaadin && window.Vaadin.Flow && window.Vaadin.Flow.devServerIsNotLoaded;"
                )
            })
    }

    protected fun runHeadless(): Boolean {
        return !isJavaInDebugMode()
    }

    companion object {
        fun isJavaInDebugMode(): Boolean {
            return ManagementFactory.getRuntimeMXBean()
                .getInputArguments()
                .toString()
                .contains("jdwp")
        }
    }
}
