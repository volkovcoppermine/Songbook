package org.volkov.songbook_fts.util

import android.content.Context
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

fun fileFromAsset(context: Context, assetName: String): File {
    val outFile = File(context.cacheDir, assetName)
    if (assetName.contains("/")) {
        outFile.parentFile?.mkdirs()
    }
    copy(context.assets.open(assetName), outFile)
    return outFile
}

private fun copy(inputStream: InputStream?, output: File?) {
    FileOutputStream(output).use {
        var read = 0
        val bytes = ByteArray(1024)
        if (inputStream != null) {
            while (inputStream.read(bytes).also { read = it } != -1) {
                it.write(bytes, 0, read)
            }
        }
    }
}