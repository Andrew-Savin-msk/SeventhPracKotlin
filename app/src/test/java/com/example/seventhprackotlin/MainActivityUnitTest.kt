package com.example.seventhprackotlin

import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.*
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import java.io.File
import java.io.FileOutputStream

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [31], manifest = "src/main/AndroidManifest.xml", packageName = "com.example.seventhprackotlin")
class MainActivityUnitTest {

    @Test
    fun testDownloadImageWithValidUrl() = runBlocking {
        val activity = MainActivity()
        val validUrl = "https://via.placeholder.com/150"
        // Здесь вы можете использовать мокирование для имитации загрузки
        try {
            activity.downloadImage(validUrl)
        } catch (e: Exception) {
            fail("Метод выбросил исключение с валидным URL")
        }
    }

    @Test
    fun testDownloadImageWithInvalidUrl() = runBlocking {
        val activity = MainActivity()
        val invalidUrl = "https://invalidurl"

        try {
            activity.downloadImage(invalidUrl)
        } catch (e: Exception) {
            // Ожидаем, что метод обработает исключение внутри себя
            fail("Метод не должен выбрасывать исключение наружу")
        }
    }

    @Test
    fun testSaveImageSuccess() {
        val activity = MainActivity()
        val imageBytes = ByteArray(1024) // Мок данные изображения

        val mockDirectory =  mock(File::class.java)
        val mockFile = mock(File::class.java)
        val mockFos = mock(FileOutputStream::class.java)

        `when`(mockDirectory.path).thenReturn("/mock/path")
        `when`(mockDirectory.exists()).thenReturn(true)
        `when`(mockDirectory.isDirectory).thenReturn(true)
        `when`(mockDirectory.canWrite()).thenReturn(true)
        `when`(mockFile.exists()).thenReturn(false)
        `when`(mockFile.path).thenReturn("/mock/path/downloaded_image.jpg")
        `when`(mockFos.write(any(ByteArray::class.java))).thenReturn(Unit)

        val result = activity.saveImage(imageBytes, mockDirectory)
        assertTrue(result)
    }

    @Test
    fun testSaveImageFailure() {
        val activity = MainActivity()
        val imageBytes = ByteArray(1024) // Мок данные изображения

        val mockDirectory = mock(File::class.java)
        `when`(mockDirectory.canWrite()).thenReturn(false)

        val result = activity.saveImage(imageBytes, mockDirectory)
        assertFalse(result)
    }

    @Test
    fun testExecutorInitialization() {
        val activity = MainActivity()
        assertNotNull(activity.executor)
    }
}