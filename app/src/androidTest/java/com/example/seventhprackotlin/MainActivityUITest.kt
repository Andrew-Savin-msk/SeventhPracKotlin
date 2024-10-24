package com.example.seventhprackotlin

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.matcher.ViewMatchers.*
import org.hamcrest.CoreMatchers.not
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import androidx.test.espresso.IdlingRegistry
import org.junit.Before
import org.junit.After
import android.view.View
import androidx.test.core.app.ActivityScenario.launch

@RunWith(AndroidJUnit4::class)
class MainActivityUITest {

    @Rule @JvmField
    var activityRule = ActivityScenarioRule(MainActivity::class.java)

    private var decorView: View? = null

    @Before
    fun setUp() {
        IdlingRegistry.getInstance().register(MainActivity.idlingResource)

        activityRule.scenario.onActivity { activity ->
            decorView = activity.window.decorView
        }
    }

    @After
    fun tearDown() {
        IdlingRegistry.getInstance().unregister(MainActivity.idlingResource)
    }

    @Test
    fun testSwipeOnImageView() {
        onView(withId(R.id.urlInput))
            .perform(typeText("https://avatars.mds.yandex.net/i?id=2fef24696ae8ffeeb7b88889efc91e4bb62dfcbe-12524916-images-thumbs&n=13"))
        closeSoftKeyboard()

        onView(withId(R.id.downloadButton)).perform(click())

        Thread.sleep(4000)

        onView(withId(R.id.imageView)).check(matches(isDisplayed()))

        onView(withId(R.id.imageView)).perform(swipeUp())

        onView(withId(R.id.imageView)).check(matches(isDisplayed()))
    }


    @Test
    fun testImageViewBecomesVisibleAfterDownload() {
        onView(withId(R.id.urlInput)).perform(typeText("https://avatars.mds.yandex.net/i?id=2fef24696ae8ffeeb7b88889efc91e4bb62dfcbe-12524916-images-thumbs&n=13"))
        closeSoftKeyboard()

        onView(withId(R.id.downloadButton)).perform(click())

        Thread.sleep(4000)

        onView(withId(R.id.imageView)).check(matches(isDisplayed()))
    }

//    @Test
//    fun testDownloadButtonWorksWithValidUrl() {
//        onView(withId(R.id.urlInput)).perform(typeText("https://avatars.mds.yandex.net/i?id=2fef24696ae8ffeeb7b88889efc91e4bb62dfcbe-12524916-images-thumbs&n=13"))
//        closeSoftKeyboard()
//
//        onView(withId(R.id.downloadButton)).perform(click())
//
//        Thread.sleep(4000)
//
//        onView(withId(R.id.imageView)).check(matches(isDisplayed()))
//    }

    @Test
    fun testImageViewVisibilityChangesAfterDownload() {
        onView(withId(R.id.imageView)).check(matches(not(isDisplayed())))

        onView(withId(R.id.urlInput)).perform(typeText("https://avatars.mds.yandex.net/i?id=2fef24696ae8ffeeb7b88889efc91e4bb62dfcbe-12524916-images-thumbs&n=13"))
        closeSoftKeyboard()

        onView(withId(R.id.downloadButton)).perform(click())

        Thread.sleep(2000)

        onView(withId(R.id.imageView)).check(matches(isDisplayed()))
    }

    @Test
    fun testImageViewIsHiddenInitially() {
        launch(MainActivity::class.java)

        onView(withId(R.id.imageView)).check(matches(not(isDisplayed())))
    }

    @Test
    fun testDownloadButtonIsClickable() {
        launch(MainActivity::class.java)

        onView(withId(R.id.downloadButton)).check(matches(isClickable()))
    }
}
