package com.example.seventhprackotlin

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.test.espresso.idling.CountingIdlingResource
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.net.URL
import java.util.concurrent.Executors

class MainActivity : AppCompatActivity() {

    private lateinit var urlInput: EditText
    private lateinit var downloadButton: Button
    public lateinit var imageView: ImageView
    public val executor = Executors.newFixedThreadPool(2)

    companion object {
        val idlingResource = CountingIdlingResource("ImageLoader")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        urlInput = findViewById(R.id.urlInput)
        downloadButton = findViewById(R.id.downloadButton)
        imageView = findViewById(R.id.imageView)

        downloadButton.setOnClickListener {
            val imageUrl = urlInput.text.toString()
            if (imageUrl.length != 0) {
                downloadImage(imageUrl) { isSuccess ->
                    if (isSuccess) {
                        Toast.makeText(this, "Изображение успешно загружено и сохранено", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, "Ошибка загрузки или сохранения изображения", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(this, "Введите URL", Toast.LENGTH_SHORT).show()
            }
        }
    }

    public fun downloadImage(url: String, callback: (Boolean) -> Unit) {
        executor.execute {
            try {
                val inputStream: InputStream = URL(url).openStream()
                val imageBytes = inputStream.readBytes()

                val bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)

                runOnUiThread {
                    imageView.setImageBitmap(bitmap)
                    imageView.visibility = ImageView.VISIBLE
                }

                // Сохраняем изображение во внутреннюю директорию
                executor.execute {
                    val isSaved = saveImage(imageBytes)
                    runOnUiThread {
                        callback(isSaved)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                runOnUiThread {
                    callback(false)  // Возвращаем false при ошибке загрузки
                }
            }
        }
    }

    public fun saveImage(imageBytes: ByteArray, directory: File = filesDir): Boolean {
        return try {
            val file = File(directory, "downloaded_image.jpg")
            val fos = FileOutputStream(file)
            fos.write(imageBytes)
            fos.close()
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}