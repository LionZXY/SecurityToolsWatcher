package ru.lionzxy.securitytools.cmd

import ru.lionzxy.securitytools.utils.FileUtils
import java.io.File

private const val KEY_PASSWORD = "GXgWX9U9Fofn8h[m(9sjJPi" // Not secure information

class SignApk : ITerminalCommand {
    override fun getOutputFile(inputFile: File?): File? {
        return inputFile
    }

    override fun buildCommand(inputFile: File?, targetFile: File?): ProcessBuilder? {
        val apkFile = inputFile ?: throw RuntimeException("Request apk file to input")
        val keyStoreFile = FileUtils.getKeyStoreFile()
        if (!keyStoreFile.exists()) {
            throw RuntimeException("Not found keystore file")
        }

        return ProcessBuilder().command(
            "jarsigner",
            "-verbose",
            "-sigalg",
            "SHA1withRSA",
            "-digestalg",
            "SHA1",
            "-keystore",
            keyStoreFile.absolutePath,
            "-storepass",
            KEY_PASSWORD,
            apkFile.absolutePath,
            "apksignerkey"
        )
    }

    override fun checkAvailability(): ProcessBuilder? {
        return ProcessBuilder().command("jarsigner", "--help")
    }
}
