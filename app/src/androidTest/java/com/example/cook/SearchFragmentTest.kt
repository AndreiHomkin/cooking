package com.example.cook

import android.content.Context
import android.content.SharedPreferences
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.hamcrest.Matcher
import org.hamcrest.Matchers.not
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SearchFragmentTest {

    @Before
    fun setUp() {
        val searchFragment = SearchFragment()
        ActivityScenario.launch(MainActivity::class.java)
        Intents.init()
        ActivityScenario.launch(MainActivity::class.java).onActivity { activity ->
            activity.supportFragmentManager.beginTransaction()
                .replace(R.id.frame_layout, searchFragment)
                .commit()
        }
    }

    @After
    fun tearDown() {
        Intents.release()
    }

    @Test
    fun recyclerView_isDisplayed() {
        onView(withId(R.id.recyclerView))
            .check(matches(isDisplayed()))
    }

    @Test
    fun searchView_filtersItems() {
        onView(withId(R.id.searchView))
            .perform(click())
        Thread.sleep(500)
        onView(withId(R.id.searchView))
            .perform(setSearchViewQuery("Гуакамоле", false))
        onView(withId(R.id.recyclerView))
            .check(matches(hasDescendant(withText("Гуакамоле"))))
    }

    @Test
    fun searchView_invalidQuery_showsNoItems() {
        onView(withId(R.id.searchView))
            .perform(click())
        Thread.sleep(500)
        onView(withId(R.id.searchView))
            .perform(setSearchViewQuery("NonExistent Item", false))
        onView(withId(R.id.recyclerView))
            .check(matches(not(hasDescendant(isDisplayed()))))
    }

    @Test
    fun searchView_emptyQuery_showsAllItems() {
        onView(withId(R.id.searchView))
            .perform(setSearchViewQuery("", false))
        onView(withId(R.id.recyclerView))
            .check(matches(hasChildCount(4)))
    }

    @Test
    fun darkMode_isApplied() {
        val sharedPreferences: SharedPreferences = ApplicationProvider.getApplicationContext<Context>()
            .getSharedPreferences("Settings", Context.MODE_PRIVATE)
        sharedPreferences.edit().putBoolean("isDarkMode", true).apply()

        ActivityScenario.launch(MainActivity::class.java)
        val searchFragment = SearchFragment()
        ActivityScenario.launch(MainActivity::class.java).onActivity { activity ->
            activity.supportFragmentManager.beginTransaction()
                .replace(R.id.frame_layout, searchFragment)
                .commit()
        }

        onView(withId(R.id.recyclerView))
            .check(matches(isDisplayed()))
    }

    @Test
    fun language_isApplied() {
        val sharedPreferencesLang: SharedPreferences = ApplicationProvider.getApplicationContext<Context>()
            .getSharedPreferences("language", Context.MODE_PRIVATE)
        sharedPreferencesLang.edit().putString("selected_language", "en").apply()

        ActivityScenario.launch(MainActivity::class.java)
        val searchFragment = SearchFragment()
        ActivityScenario.launch(MainActivity::class.java).onActivity { activity ->
            activity.supportFragmentManager.beginTransaction()
                .replace(R.id.frame_layout, searchFragment)
                .commit()
        }

        onView(withId(R.id.recyclerView))
            .check(matches(hasDescendant(withText("BeaverTails"))))
    }


    fun setSearchViewQuery(query: String, submit: Boolean): ViewAction {
        return object : ViewAction {
            override fun getDescription(): String {
                return "Set query on SearchView"
            }

            override fun getConstraints(): Matcher<View> {
                return isAssignableFrom(SearchView::class.java)
            }

            override fun perform(uiController: UiController, view: View) {
                val searchView = view as SearchView
                searchView.setQuery(query, submit)
            }
        }
    }
}
