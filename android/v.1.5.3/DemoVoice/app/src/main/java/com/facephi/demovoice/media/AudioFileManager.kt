package com.facephi.demovoice.media

import android.content.Context
import io.github.aakira.napier.Napier
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

@Suppress("MagicNumber")
object AudioFileManager {

    fun saveWavToInternalStorage(
        context: Context,
        byteArray: ByteArray,
        fileName: String
    ): String? {
        val directory: File = context.getDir("appData", Context.MODE_PRIVATE)
        val outputFile = File(directory, "$fileName.wav")
        var fos: FileOutputStream? = null
        val dir: String?
        try {
            fos = FileOutputStream(outputFile)
            fos.write(byteArray)
        } catch (e: Exception) {
            Napier.d("APP: Save WAV error: $e")
        } finally {
            dir = outputFile.absolutePath
            try {
                fos?.close()
            } catch (e: IOException) {
                Napier.d(e.localizedMessage)
            }
        }
        return dir
    }
}
