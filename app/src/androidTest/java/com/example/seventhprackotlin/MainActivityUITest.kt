package com.example.seventhprackotlin

import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.espresso.Espresso.closeSoftKeyboard
import androidx.test.espresso.matcher.RootMatchers.withDecorView
import org.hamcrest.CoreMatchers.not
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainActivityUITest {

    @get:Rule
    var activityScenarioRule = ActivityScenarioRule(MainActivity::class.java)

    // Тест 1: Проверка отображения сообщения при пустом URL
    @Test
    fun testEmptyUrlShowsToast() {
        // Нажимаем на кнопку загрузки без ввода URL
        onView(withId(R.id.downloadButton)).perform(click())

        // Получаем activity для работы с toast
        activityScenarioRule.scenario.onActivity { activity ->
            // Проверяем, что показано сообщение "Введите URL"
            onView(withText("Введите URL"))
                .inRoot(withDecorView(not(activity.window.decorView)))
                .check(matches(isDisplayed()))
        }
    }

    // Тест 2: Проверка отображения изображения после загрузки
    @Test
    fun testImageViewBecomesVisibleAfterDownload() {
        // Вводим корректный URL изображения
        onView(withId(R.id.urlInput)).perform(typeText("https://example.com/image.jpg"))
        closeSoftKeyboard()

        // Нажимаем на кнопку загрузки
        onView(withId(R.id.downloadButton)).perform(click())

        // Проверяем, что изображение отображено
        onView(withId(R.id.imageView)).check(matches(isDisplayed()))
    }

    // Тест 3: Проверка успешного сохранения изображения
    @Test
    fun testToastAfterSuccessfulImageSave() {
        // Вводим корректный URL изображения
        onView(withId(R.id.urlInput)).perform(typeText("https://example.com/image.jpg"))
        closeSoftKeyboard()

        // Нажимаем на кнопку загрузки
        onView(withId(R.id.downloadButton)).perform(click())

        // Получаем activity для работы с toast
        activityScenarioRule.scenario.onActivity { activity ->
            // Проверяем, что показано сообщение "Изображение успешно загружено и сохранено"
            onView(withText("Изображение успешно загружено и сохранено"))
                .inRoot(withDecorView(not(activity.window.decorView)))
                .check(matches(isDisplayed()))
        }
    }

    // Тест 4: Проверка ошибки при неудачной загрузке
    @Test
    fun testToastOnFailedImageDownload() {
        // Вводим некорректный URL изображения
        onView(withId(R.id.urlInput)).perform(typeText("https://invalid-url.com"))
        closeSoftKeyboard()

        // Нажимаем на кнопку загрузки
        onView(withId(R.id.downloadButton)).perform(click())

        // Получаем activity для работы с toast
        activityScenarioRule.scenario.onActivity { activity ->
            // Проверяем, что показано сообщение "Ошибка загрузки или сохранения изображения"
            onView(withText("Ошибка загрузки или сохранения изображения"))
                .inRoot(withDecorView(not(activity.window.decorView)))
                .check(matches(isDisplayed()))
        }
    }

    // Тест 5: Проверка работы кнопки с корректным URL
    @Test
    fun testDownloadButtonWorksWithValidUrl() {
        // Вводим корректный URL изображения
        onView(withId(R.id.urlInput)).perform(typeText("https://example.com/valid-image.jpg"))
        closeSoftKeyboard()

        // Нажимаем на кнопку загрузки
        onView(withId(R.id.downloadButton)).perform(click())

        // Проверяем, что изображение отображено
        onView(withId(R.id.imageView)).check(matches(isDisplayed()))
    }
}
