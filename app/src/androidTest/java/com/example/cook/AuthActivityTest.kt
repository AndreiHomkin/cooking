package com.example.cook

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.rule.ActivityTestRule
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class AuthActivityTest {

    @Rule
    @JvmField
    val activityRule = ActivityTestRule(AuthActivity::class.java)

    @Before
    fun setUp() {
        ActivityScenario.launch(AuthActivity::class.java)
        Intents.init()
    }
    @After
    fun tearDownIntents() {
        Intents.release()
    }

    @Test
    fun testDisplayAuthScreen() {
        onView(withId(R.id.user_login_auth)).check(matches(isDisplayed()))
        onView(withId(R.id.user_pass_auth)).check(matches(isDisplayed()))
        onView(withId(R.id.button_auth)).check(matches(isDisplayed()))
        onView(withId(R.id.link_to_reg)).check(matches(isDisplayed()))
    }
    @Test
    fun testNavigateToRegisterActivity() {
        ActivityScenario.launch(AuthActivity::class.java).use {
            onView(withId(R.id.link_to_reg)).perform(click())

            intended(hasComponent(RegisterActivity::class.java.name))
        }
    }
    @Test
    fun testLoginButtonWithInvalidCredentials() {
        onView(withId(R.id.user_login_auth)).perform(typeText("invalidUser"))
        onView(withId(R.id.user_pass_auth)).perform(typeText("invalidPass"))
        onView(withId(R.id.button_auth)).perform(click())
    }
}