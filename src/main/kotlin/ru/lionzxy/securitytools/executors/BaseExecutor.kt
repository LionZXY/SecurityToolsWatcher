package ru.lionzxy.securitytools.executors

import ru.lionzxy.securitytools.cmd.ITerminalCommand
import ru.lionzxy.securitytools.utils.RedirectWriter
import java.io.File
import java.io.InputStreamReader
import java.io.OutputStream
import java.io.OutputStreamWriter

class BaseExecutor(
    logFileStream: OutputStream
) {
    private val logWriter = OutputStreamWriter(logFileStream)

    fun execute(command: ITerminalCommand, inputFile: File?): File? {
        logWriter.write("[EXECUTOR] Execute ${command.javaClass.simpleName}\n")
        val targetFile = command.getOutputFile(inputFile)
        val builder = command.buildCommand(inputFile, targetFile) ?: return targetFile
        logWriter.write("[EXECUTOR] Execute ${builder.command().joinToString(" ")}\n")
        val process = builder.start()
        val writer = RedirectWriter(logWriter)
        writer.copyFrom(InputStreamReader(process.inputStream))
        writer.copyFrom(InputStreamReader(process.errorStream))
        val returnCode = process.waitFor()
        if (returnCode != 0) {
            throw RuntimeException("Process $command return $returnCode")
        }
        logWriter.write("[EXECUTOR] Command ${command.javaClass.simpleName} return code $returnCode\n")

        return targetFile
    }
}
