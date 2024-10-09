package com.example.seventhprackotlin

import android.view.View
import android.widget.ImageView
import org.junit.Assert
import org.junit.*
import org.junit.runner.RunWith
import org.mockito.*
import org.mockito.Mockito.*
import org.robolectric.*
import org.robolectric.annotation.Config
import org.robolectric.shadows.ShadowToast
import java.io.File

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class MainActivityUnitTest {

    @Mock
    lateinit var mainActivity: MainActivity

    @Before
    fun setUp() {
        // Initialize mock objects
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun testDownloadImageSuccess() {
        val activity = Robolectric.buildActivity(MainActivity::class.java).create().get()
        val imageUrl = "https://yandex-images.clstorage.net/gK4aW7286/05d9dfsXCtYU/COV9FZelSrezb4s1hxKT3yNeU625GbpEzlfmE2DgLpchaxHz5f32kl7igoWSY55dtop44tuslruJhigKttqnNHnxuaGmEJ-D0hrLtbdCsgr3b0JoGVxr4OuSYjOlgI4HZwijpLfLEVFcYOAy8-PLPaPr7EOa2mAKrlYuD5MrZnQ7d0x0wsVbdddfO3V1YHMZ0hxg-xsfaFepAhP3t_WbcWV58CaXypDQrqHgY4DQ-H-hSwqDmtrTv9-0v40xqDQ0yLY-dcMFCEZU3EvmJgNDWfFOwKhorro2yQDD5WWXieA1aRAXNHpjYj5AgYKj45tc4RgKkPja8Vws1L2fIbgmFOiUTBTwNsp3xK9KZlcx0skx_tOruz8YpJij0qVFFhgQdlsDpIRK04NvwHLgIyDL7WKbedOr--Ed7oR9zoJ6RmYKBez0Y6caphacaLdWQ8FrAh1gmFt8GjQa0mAGxxYpk2WpYfYHWIEgzCIg4MFA-5_yqShgeuuBLc4Gfn-Q2jXUOQQ9trJ2iXbnvbnmZaKQObM-EKmoDzm0uCJiJwUXa7A3ybKGlKoh8g-QM-LBMcn-Q4iqghso4u799s5O40p15Ju0P3fyNuiG1O1JdmQQcGkDDRL56u4YRnnBwXRE58gjVihAJuU6IoC9U_JRkqMJXXHIWfMrysJ8_YefjxDqlwbZtw9mInUIJNXvG8enIdPK0S8DeQouiIRK0MHlJGZbMfRJsqWUeAGDTuGjwHESe1yz2OogKqkxLA-0Ld0z2jYXW9ZcJvEUCAZHHGt1ltHQ6YEdgFob_cuU2QDx9xf12fMHW_L05Ygisl-AsGBzILsPsfn5Mvub4Y6cZr_NYlilhCvH76ehBsmEp86Z10cwcDtyn2KYeI7b1CoRk7ZWxami1ukiJfe7cqM_8aLhUEOY_2KqWnIamsDN34ScfXK79BaqNf61whV5pHfOeZZWAvBqwq0CusqMCpXrIyIUU"

        // Call the image download method directly
        activity.downloadImage(imageUrl)

        // Check if the image is set in ImageView (simulate successful download)
        val imageView = activity.findViewById<ImageView>(R.id.imageView)
        Assert.assertTrue(imageView.visibility == View.VISIBLE)
        Assert.assertNotNull(imageView.drawable)
    }

    @Test
    fun testEmptyUrlHandling() {
        val activity = Robolectric.buildActivity(MainActivity::class.java).create().get()

        // Call the method directly with an empty URL
        activity.downloadImage("")

        // Check if nothing happened or if an error message was displayed (if applicable)
        val toastMessage = ShadowToast.getTextOfLatestToast()
        Assert.assertEquals("Введите URL", toastMessage)
    }

    @Test
    fun testSaveImageSuccess() {
        val activity = Robolectric.buildActivity(MainActivity::class.java).create().get()
        val imageBytes = ByteArray(1024) // Simulated image data

        // Call the image saving method
        val result = activity.saveImage(imageBytes)

        // Verify that the image was successfully saved
        Assert.assertTrue(result)
        val toastMessage = ShadowToast.getTextOfLatestToast()
        Assert.assertEquals("Изображение сохранено", toastMessage)
    }

    @Test
    fun testSaveImageCalled() {
        val imageBytes = ByteArray(1024) // Simulated image data
        val file = mock(File::class.java)

        // Mock the saveImage function
        `when`(mainActivity.saveImage(imageBytes, file)).thenReturn(true)

        // Call the method and verify that it was called
        mainActivity.saveImage(imageBytes, file)
        verify(mainActivity, times(1)).saveImage(imageBytes, file)
    }
}