package ru.lionzxy.securitytools.cmd

import ru.lionzxy.securitytools.utils.FileUtils
import java.io.File

class ApkToolDecompile(private val withResources: Boolean = false) :
    ITerminalCommand {
    override fun getOutputFile(inputFile: File?): File? {
        val fileName = inputFile?.nameWithoutExtension ?: return null
        return File(File(FileUtils.getTemporaryFolder(), fileName), "decompile")
    }

    override fun buildCommand(inputFile: File?, targetFile: File?): ProcessBuilder? {
        val apkFile = inputFile ?: throw RuntimeException("Request apk file to input")
        val folder = targetFile ?: throw RuntimeException("Request target folder")
        if (folder.exists()) {
            folder.deleteRecursively()
        }
        if (apkFile.extension != "apk") {
            throw RuntimeException("Request apk file! Not ${apkFile.extension}")
        }

        val command = ArrayList<String>()
        command.add("apktool")
        if (!withResources) {
            command.add("-r")
            //command.add("--force-manifest")
        }
        command.add("d")
        command.add(apkFile.absolutePath)
        command.add("-o")
        command.add(folder.absolutePath)
        return ProcessBuilder().command(command)
    }

    override fun checkAvailability(): ProcessBuilder? {
        return ProcessBuilder().command("apktool", "--help")
    }

}
