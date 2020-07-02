package ru.lionzxy.securitytools

import org.reflections.Reflections
import ru.lionzxy.securitytools.cmd.ITerminalCommand
import ru.lionzxy.securitytools.executors.BaseExecutor
import ru.lionzxy.securitytools.flows.IFlow
import ru.lionzxy.securitytools.flows.SwitchToDebugFlow
import ru.lionzxy.securitytools.utils.FileUtils
import ru.lionzxy.securitytools.utils.createFile
import ru.lionzxy.securitytools.utils.createFolder
import java.io.File
import java.io.PrintWriter
import java.io.StringWriter
import java.lang.Thread.sleep

val watcherDir = File("in")
val outDir = File("out").createFolder()
val logDir = File("log").createFolder()

fun main(args: Array<String>) {
    if (args.contains("--test")) {
        Reflections("ru.lionzxy.securitytools").getSubTypesOf(ITerminalCommand::class.java).forEach {
            println("Check $it")
            val builder = it.newInstance().checkAvailability()
            if (builder != null && builder.start().waitFor() != 0) {
                throw RuntimeException("Failed test $it")
            }
        }
        return
    }

    print("Run watcher...")

    //while (!Thread.interrupted()) {
    println("Find file with pattern: TARGET-123445.extension (example: DEBUGAPK-1234.apk)")
    checkFiles()
    sleep(1000)
    // }
}

fun checkFiles() {
    val files = watcherDir.listFiles()?.sortedBy { it.lastModified() }
        ?: throw RuntimeException("Not found directory ${watcherDir.absolutePath}")
    val file = files.firstOrNull() ?: return
    val logFile = File(logDir, "${file.nameWithoutExtension.split("-")[1]}.log").createFile()
    try {
        processFile(file, logFile)
    } catch (ex: Exception) {
        val stackTrace = StringWriter().apply {
            ex.printStackTrace(PrintWriter(this))
        }.toString()
        logFile.appendText("[ERROR] $stackTrace")
    } finally {
        // file.deleteRecursively()
    }
}

// File name pattern : TARGET-123445.extension
fun processFile(file: File, logFile: File) {
    println("Process ${file.absolutePath}")
    val fileName = file.nameWithoutExtension
    val args = fileName.split("-")
    val command = Command.valueOf(args[0])
    val taskId = args[1]

    logFile.outputStream().use {
        val output = command.flow.run(file, BaseExecutor(it))
        output?.copyTo(File(outDir, "$command-$taskId.${output.extension}"), true)
    }

    FileUtils.getTemporaryFolder().deleteRecursively()
}

enum class Command(val flow: IFlow) {
    DEBUGAPK(SwitchToDebugFlow)
}
