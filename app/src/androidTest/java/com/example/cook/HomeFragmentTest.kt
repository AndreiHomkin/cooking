package com.example.cook

import android.content.Context
import android.content.SharedPreferences
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.contrib.RecyclerViewActions.scrollToPosition
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.hamcrest.Matcher
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class HomeFragmentTest {

    @Before
    fun setUp() {
        val homeFragment = HomeFragment()
        ActivityScenario.launch(MainActivity::class.java)
        Intents.init()
        ActivityScenario.launch(MainActivity::class.java).onActivity { activity ->
            activity.supportFragmentManager.beginTransaction()
                .replace(R.id.frame_layout, homeFragment)
                .commit()
        }
    }

    @After
    fun tearDown() {
        Intents.release()
    }

    @Test
    fun testTextViewsAreDisplayed() {
        onView(withId(R.id.textView)).check(matches(isDisplayed()))
        onView(withId(R.id.textView2)).check(matches(isDisplayed()))
        onView(withId(R.id.todayMenuTxt)).check(matches(isDisplayed()))
    }

    @Test
    fun testDarkModeTextColor() {
        val sharedPreferences: SharedPreferences = ApplicationProvider.getApplicationContext<Context>()
            .getSharedPreferences("Settings", Context.MODE_PRIVATE)
        sharedPreferences.edit().putBoolean("isDarkMode", true).apply()

        ActivityScenario.launch(MainActivity::class.java)
        val homeFragment = HomeFragment()
        ActivityScenario.launch(MainActivity::class.java).onActivity { activity ->
            activity.supportFragmentManager.beginTransaction()
                .replace(R.id.frame_layout, homeFragment)
                .commit()
        }

        onView(withId(R.id.targetsList))
            .check(matches(isDisplayed()))
    }

    @Test
    fun testLanguageChange() {
        val preferences: SharedPreferences = ApplicationProvider.getApplicationContext<Context>()
            .getSharedPreferences("language", Context.MODE_PRIVATE)
        preferences.edit().putString("selected_language", "en").apply()

        ActivityScenario.launch(MainActivity::class.java)
        val homeFragment = HomeFragment()
        ActivityScenario.launch(MainActivity::class.java).onActivity { activity ->
            activity.supportFragmentManager.beginTransaction()
                .replace(R.id.frame_layout, homeFragment)
                .commit()
        }

        onView(withId(R.id.textView))
            .check(matches(withText("Menu")))
    }

    @Test
    fun testNavigationToDetailsFragment() {
        onView(withId(R.id.targetsList))
            .perform(actionOnItemAtPosition<RecyclerView.ViewHolder>(0, clickChildViewWithId(R.id.item_list_button)))

        onView(withId(R.id.item_show_title))
            .check(matches(isDisplayed()))
    }


    fun clickChildViewWithId(id: Int): ViewAction {
        return object : ViewAction {
            override fun getConstraints(): Matcher<View> {
                return isAssignableFrom(View::class.java)
            }

            override fun getDescription(): String {
                return "Click on a child view with specified ID."
            }

            override fun perform(uiController: UiController?, view: View?) {
                val childView = view?.findViewById<View>(id)
                childView?.performClick()
            }
        }
    }

}