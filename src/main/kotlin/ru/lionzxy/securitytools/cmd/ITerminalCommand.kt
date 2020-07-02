package ru.lionzxy.securitytools.cmd

import java.io.File

interface ITerminalCommand {
    fun getOutputFile(inputFile: File?): File?
    fun buildCommand(inputFile: File?, targetFile: File?): ProcessBuilder?
    fun checkAvailability(): ProcessBuilder?
}

