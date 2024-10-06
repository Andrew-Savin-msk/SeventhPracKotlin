package com.example.seventhprackotlin

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.net.URL
import java.util.concurrent.Executors

class MainActivity : AppCompatActivity() {

    public lateinit var urlInput: EditText
    public lateinit var downloadButton: Button
    public lateinit var imageView: ImageView
    public val executor = Executors.newFixedThreadPool(2)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        urlInput = findViewById(R.id.urlInput)
        downloadButton = findViewById(R.id.downloadButton)
        imageView = findViewById(R.id.imageView)

        downloadButton.setOnClickListener {
            val imageUrl = urlInput.text.toString()
            if (imageUrl.isNotEmpty()) {
                downloadImage(imageUrl)
            } else {
                Toast.makeText(this, "Введите URL", Toast.LENGTH_SHORT).show()
            }
        }
    }

    public fun downloadImage(url: String) {
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
                    saveImage(imageBytes)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                runOnUiThread {
                    Toast.makeText(this, "Ошибка загрузки изображения", Toast.LENGTH_SHORT).show()
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
            runOnUiThread {
                Toast.makeText(this, "Изображение сохранено", Toast.LENGTH_SHORT).show()
            }
            true
        } catch (e: Exception) {
            e.printStackTrace()
            runOnUiThread {
                Toast.makeText(this, "Ошибка сохранения изображения", Toast.LENGTH_SHORT).show()
            }
            false
        }
    }
}
