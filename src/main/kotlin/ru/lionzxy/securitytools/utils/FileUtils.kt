package ru.lionzxy.securitytools.utils

import java.io.File

object FileUtils {
    fun getTemporaryFolder(): File {
        return File("tmp").createFolder()
    }

    fun getKeyStoreFile() = File("utils/debug.keystore")
}

fun File.createFolder(): File {
    if (parentFile != null && !parentFile.exists()) {
        parentFile.mkdirs()
    }
    if (!exists()) {
        mkdir()
    }
    return this
}

fun File.createFile(): File {
    if (parentFile != null && !parentFile.exists()) {
        parentFile.mkdirs()
    }
    if (!exists()) {
        createNewFile()
    }
    return this
}
