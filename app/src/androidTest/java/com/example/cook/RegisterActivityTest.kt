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


class RegisterActivityTest {

    @Rule
    @JvmField
    val activityRule = ActivityTestRule(RegisterActivity::class.java)

    @Before
    fun setUp() {
        ActivityScenario.launch(RegisterActivity::class.java)
        Intents.init()
    }

    @After
    fun tearDownIntents() {
        Intents.release()
    }

    @Test
    fun testDisplayAuthScreen() {
        onView(withId(R.id.user_login)).check(matches(isDisplayed()))
        onView(withId(R.id.user_pass)).check(matches(isDisplayed()))
        onView(withId(R.id.user_email)).check(matches(isDisplayed()))
        onView(withId(R.id.button_reg)).check(matches(isDisplayed()))
        onView(withId(R.id.link_to_auth)).check(matches(isDisplayed()))
    }

    @Test
    fun testNavigateToAuthActivity() {
        ActivityScenario.launch(RegisterActivity::class.java).use {
            onView(withId(R.id.link_to_auth)).perform(click())

            intended(hasComponent(AuthActivity::class.java.name))
        }
    }
    @Test
    fun testLoginButtonWithInvalidCredentials() {
        onView(withId(R.id.user_login)).perform(typeText("invalidUser"))
        onView(withId(R.id.user_email)).perform(typeText("invalidEmail@gmail.com"))
        onView(withId(R.id.user_pass)).perform(typeText("invalidPass"), closeSoftKeyboard())

        onView(withId(R.id.button_reg)).perform(click())
    }
}