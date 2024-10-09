package com.example.seventhprackotlin

import android.graphics.BitmapFactory
import android.os.Looper
import android.view.View
import android.widget.ImageView
import org.junit.Assert
import org.junit.*
import org.junit.runner.RunWith
import org.mockito.*
import org.mockito.Mockito.*
import org.robolectric.*
import org.robolectric.Shadows.shadowOf
import org.robolectric.annotation.Config
import org.robolectric.shadows.ShadowToast
import java.io.File

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class MainActivityUnitTest {

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun testDownloadImageSuccess() {
        val activity = Robolectric.buildActivity(MainActivity::class.java).create().get()
        val imageUrl = "https://example.com/test.jpg"

        activity.downloadImage(imageUrl) { isSuccess ->
            Assert.assertTrue(isSuccess)

            val imageView = activity.findViewById<ImageView>(R.id.imageView)
            Assert.assertTrue(imageView.visibility == View.VISIBLE)
            Assert.assertNotNull(imageView.drawable)
        }

        shadowOf(Looper.getMainLooper()).idle()
    }

    @Test
    fun testEmptyUrlHandling() {
        val activity = Robolectric.buildActivity(MainActivity::class.java).create().get()

        activity.downloadImage("") { isSuccess ->
            Assert.assertFalse(isSuccess)
            val toastMessage = ShadowToast.getTextOfLatestToast()
            Assert.assertEquals("Введите URL", toastMessage)
        }

        shadowOf(Looper.getMainLooper()).idle()
    }

    @Test
    fun testSaveImageSuccess() {
        val activity = Robolectric.buildActivity(MainActivity::class.java).create().get()
        val imageBytes = ByteArray(1024)

        val result = activity.saveImage(imageBytes)
        Assert.assertTrue(result)
    }

    @Test
    fun testSaveImageCalled() {
        val activity = Robolectric.buildActivity(MainActivity::class.java).create().get()
        val imageBytes = ByteArray(1024)

        val mockFile = File(activity.filesDir, "")

        val result = activity.saveImage(imageBytes, mockFile)
        Assert.assertTrue(result)
        Assert.assertTrue(mockFile.exists())

        mockFile.delete()
    }

    @Test
    fun testImageIsDisplayedAndSaved() {
        val activity = Robolectric.buildActivity(MainActivity::class.java).create().get()

        // Подготавливаем URL изображения
        val imageUrl = "https://example.com/test.jpg"
        val imageBytes = ByteArray(1024) // Симулированные данные изображения

        // Мокаем загрузку изображения
        activity.executor.execute {
            activity.runOnUiThread {
                activity.imageView.setImageBitmap(BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size))
                activity.imageView.visibility = ImageView.VISIBLE
            }

            // Сохраняем изображение
            val isSaved = activity.saveImage(imageBytes)
            Assert.assertTrue(isSaved)

            // Проверяем, что изображение отображено
            val imageView = activity.findViewById<ImageView>(R.id.imageView)
            Assert.assertEquals(View.VISIBLE, imageView.visibility)
            Assert.assertNotNull(imageView.drawable)

            // Проверяем, что файл существует
            val savedFile = File(activity.filesDir, "downloaded_image.jpg")
            Assert.assertTrue(savedFile.exists())

            // Удаляем тестовый файл после проверки
            savedFile.delete()
        }

        // Ожидаем завершения всех задач в основном потоке
        shadowOf(Looper.getMainLooper()).idle()
    }

}