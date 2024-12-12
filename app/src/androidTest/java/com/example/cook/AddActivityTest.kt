package com.example.cook

import android.app.Activity
import android.app.Instrumentation
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.IBinder
import android.provider.MediaStore
import android.view.WindowManager
import android.widget.EditText
import android.widget.Spinner
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Root
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.hamcrest.Description
import org.hamcrest.Matchers.allOf
import org.hamcrest.TypeSafeMatcher
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AddActivityTest {

    @get:Rule
    val intentsTestRule = IntentsTestRule(AddActivity::class.java)

    @Before
    fun setUp() {
    }

    @After
    fun tearDown() {
        onView(withId(R.id.receiptName)).check(matches(isDisplayed()))
        onView(withId(R.id.receiptIngr)).check(matches(isDisplayed()))
        onView(withId(R.id.receiptDesc)).check(matches(isDisplayed()))
        onView(withId(R.id.spinner)).check(matches(isDisplayed()))
        onView(withId(R.id.spinnerSub)).check(matches(isDisplayed()))
        onView(withId(R.id.spinnerPrivacy)).check(matches(isDisplayed()))
        onView(withId(R.id.receiptImg)).check(matches(isDisplayed()))
    }

    @Test
    fun testFillFormAndSubmit() {
        onView(withId(R.id.receiptName)).perform(replaceText("Pasta"))
        onView(withId(R.id.receiptIngr)).perform(replaceText("Pasta, Sauce"))
        onView(withId(R.id.receiptDesc)).perform(replaceText("A delicious pasta dish"))

        onView(withId(R.id.spinner)).perform(click())
        onView(withText(R.string.lunch)).perform(click())

        onView(withId(R.id.spinnerSub)).perform(click())
        onView(withText(R.string.main_dishes)).perform(click())

        onView(withId(R.id.spinnerPrivacy)).perform(click())
        onView(withText(R.string.public123)).perform(click())

        onView(withId(R.id.createItem)).perform(click())
    }
    @Test
    fun testEmptyFieldsValidation() {
        onView(withId(R.id.createItem)).perform(click())
    }

    @Test
    fun testImageSelection() {
        // Mock the Intent that the image picker would launch
        val expectedIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        Intents.intending(IntentMatchers.hasAction(Intent.ACTION_PICK)).respondWith(
            Instrumentation.ActivityResult(Activity.RESULT_OK, expectedIntent)
        )

        onView(withId(R.id.receiptImg)).perform(click())

        Intents.intended(IntentMatchers.hasAction(Intent.ACTION_PICK))
    }

    @Test
    fun testBackgroundChangeBasedOnDarkMode() {
        val sharedPreferences: SharedPreferences = ApplicationProvider.getApplicationContext<Context>()
            .getSharedPreferences("Settings", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putBoolean("isDarkMode", true)
        editor.apply()

        ActivityScenario.launch(AddActivity::class.java)
    }
}