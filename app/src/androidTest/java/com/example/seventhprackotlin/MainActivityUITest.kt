package com.example.seventhprackotlin

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.IdlingResource
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.RootMatchers.withDecorView
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.hamcrest.CoreMatchers.not
import org.junit.*
import org.junit.runner.RunWith
import java.util.concurrent.Executor
import java.util.concurrent.atomic.AtomicInteger

@RunWith(AndroidJUnit4::class)
class MainActivityUITestTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    private lateinit var decorView: android.view.View
    private lateinit var idlingResource: ExecutorIdlingResource

    @Before
    fun setUp() {
        activityRule.scenario.onActivity { activity ->
            decorView = activity.window.decorView
            // Получаем executor и используем его в ExecutorIdlingResource
            idlingResource = ExecutorIdlingResource(TrackingExecutor(activity.executor))
            IdlingRegistry.getInstance().register(idlingResource)
        }
    }

    @After
    fun tearDown() {
        if (::idlingResource.isInitialized) {
            IdlingRegistry.getInstance().unregister(idlingResource)
        }
    }

    // Тест 1: Проверка загрузки и отображения изображения при вводе корректного URL
    @Test
    fun testValidImageUrlDownload() {
        val validUrl = "https://via.placeholder.com/150"

        onView(withId(R.id.urlInput)).perform(typeText(validUrl), closeSoftKeyboard())
        onView(withId(R.id.downloadButton)).perform(click())

        // Проверяем, что ImageView стал видимым
        onView(withId(R.id.imageView)).check(matches(isDisplayed()))

        // Проверяем отображение тоста об успешной загрузке
        onView(withText("Изображение успешно загружено и сохранено"))
            .inRoot(withDecorView(not(decorView)))
            .check(matches(isDisplayed()))
    }

    // Тест 2: Проверка отображения ошибки при вводе некорректного URL
    @Test
    fun testInvalidImageUrlDownload() {
        val invalidUrl = "https://invalid.url/image.jpg"

        onView(withId(R.id.urlInput)).perform(typeText(invalidUrl), closeSoftKeyboard())
        onView(withId(R.id.downloadButton)).perform(click())

        // Проверяем отображение тоста об ошибке загрузки
        onView(withText("Ошибка загрузки или сохранения изображения"))
            .inRoot(withDecorView(not(decorView)))
            .check(matches(isDisplayed()))
    }

    // Тест 3: Проверка сообщения об ошибке при пустом поле URL
    @Test
    fun testEmptyUrlInput() {
        onView(withId(R.id.urlInput)).perform(clearText(), closeSoftKeyboard())
        onView(withId(R.id.downloadButton)).perform(click())

        // Проверяем отображение тоста с просьбой ввести URL
        onView(withText("Введите URL"))
            .inRoot(withDecorView(not(decorView)))
            .check(matches(isDisplayed()))
    }

    // Тест 4: Проверка видимости ImageView после успешной загрузки
    @Test
    fun testImageViewVisibility() {
        val validUrl = "https://via.placeholder.com/150"

        // Убеждаемся, что ImageView изначально скрыт
        onView(withId(R.id.imageView)).check(matches(withEffectiveVisibility(Visibility.GONE)))

        onView(withId(R.id.urlInput)).perform(typeText(validUrl), closeSoftKeyboard())
        onView(withId(R.id.downloadButton)).perform(click())

        // Проверяем, что ImageView стал видимым
        onView(withId(R.id.imageView)).check(matches(isDisplayed()))
    }

    // Тест 5: Проверка обработки некорректного формата URL
    @Test
    fun testInvalidUrlFormat() {
        val invalidUrlFormat = "not a url"

        onView(withId(R.id.urlInput)).perform(typeText(invalidUrlFormat), closeSoftKeyboard())
        onView(withId(R.id.downloadButton)).perform(click())

        // Проверяем отображение тоста об ошибке загрузки
        onView(withText("Ошибка загрузки или сохранения изображения"))
            .inRoot(withDecorView(not(decorView)))
            .check(matches(isDisplayed()))
    }
}


class TrackingExecutor(private val executor: Executor) : Executor {

    private var taskCounter: AtomicInteger? = null

    fun setTaskCounter(counter: AtomicInteger) {
        this.taskCounter = counter
    }

    override fun execute(command: Runnable) {
        taskCounter?.incrementAndGet()
        executor.execute {
            try {
                command.run()
            } finally {
                taskCounter?.decrementAndGet()
            }
        }
    }
}

class ExecutorIdlingResource(private val executor: TrackingExecutor) : IdlingResource {

    @Volatile
    private var callback: IdlingResource.ResourceCallback? = null
    private val activeTasks = AtomicInteger(0)

    init {
        executor.setTaskCounter(activeTasks)
    }

    override fun getName(): String = "ExecutorIdlingResource"

    override fun isIdleNow(): Boolean {
        val idle = activeTasks.get() == 0
        if (idle) {
            callback?.onTransitionToIdle()
        }
        return idle
    }

    override fun registerIdleTransitionCallback(callback: IdlingResource.ResourceCallback) {
        this.callback = callback
    }
}
