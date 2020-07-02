package ru.lionzxy.securitytools.cmd

import java.io.File

class ApkToolCompile : ITerminalCommand {
    override fun getOutputFile(inputFile: File?): File? {
        return File(inputFile, "build/dist/output.apk")
    }

    override fun buildCommand(inputFile: File?, targetFile: File?): ProcessBuilder? {
        val folder = inputFile ?: throw RuntimeException("Request source folder")
        val apkFile = targetFile ?: throw RuntimeException("WTF")
        return ProcessBuilder().command("apktool", "b", folder.absolutePath, "-o", apkFile.absolutePath)
    }

    override fun checkAvailability(): ProcessBuilder? {
        return ProcessBuilder().command("apktool", "--help")
    }
}
