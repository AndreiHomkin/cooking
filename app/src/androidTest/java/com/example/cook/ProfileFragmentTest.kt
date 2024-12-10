package com.example.cook


import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.fragment.app.testing.FragmentScenario
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.hamcrest.Matchers.not
import org.junit.After
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ProfileFragmentTest {
    private lateinit var sharedPreferences: SharedPreferences
    @Before
    fun setUp() {
        sharedPreferences = ApplicationProvider.getApplicationContext<Context>()
            .getSharedPreferences("Settings", Context.MODE_PRIVATE)

        sharedPreferences.edit().clear().apply()
        val profileFragment = ProfileFragment()
        ActivityScenario.launch(MainActivity::class.java)
        Intents.init()
        ActivityScenario.launch(MainActivity::class.java).onActivity { activity ->
            activity.supportFragmentManager.beginTransaction()
                .replace(R.id.frame_layout, profileFragment)
                .commit()
        }
    }
    @After
    fun tearDownIntents() {
        Intents.release()
    }
    @Test
    fun testFragmentInitialization() {
        onView(withId(R.id.profileBack)).check(matches(isDisplayed()))
        onView(withId(R.id.account_name)).check(matches(isDisplayed()))
        onView(withId(R.id.btnLogin)).check(matches(isDisplayed()))
        onView(withId(R.id.btnRegister)).check(matches(isDisplayed()))
    }
    @Test
    fun testButtonVisibilityWhenLoggedOut() {
        sharedPreferences.edit().putString("userName", "You are not logged in yet").apply()

        onView(withId(R.id.btnExit)).check(matches(not(isDisplayed())))
        onView(withId(R.id.btnLogin)).check(matches(isDisplayed()))
        onView(withId(R.id.btnRegister)).check(matches(isDisplayed()))
    }
    @Test
    fun testChangeThemeUpdatesUI() {
        onView(withId(R.id.changeTheme)).perform(click())
        onView(withId(R.id.profileBack)).check(matches(isDisplayed()))
    }
    @Test
    fun testRegisterButtonOpensRegisterActivity() {
        onView(withId(R.id.btnRegister)).perform(click())
        Intents.intended(IntentMatchers.hasComponent(RegisterActivity::class.java.name))
    }
    @Test
    fun testAuthButtonOpensAuthActivity() {
        onView(withId(R.id.btnLogin)).perform(click())
        Intents.intended(IntentMatchers.hasComponent(AuthActivity::class.java.name))
    }

}