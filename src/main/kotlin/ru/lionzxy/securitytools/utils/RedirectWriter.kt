package ru.lionzxy.securitytools.utils

import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.Reader
import java.io.Writer
import java.util.concurrent.Executors

class RedirectWriter(writer: Writer) : BufferedWriter(writer) {
    private val executor = Executors.newCachedThreadPool()
    private val readers = ArrayList<BufferedReader>()

    fun copyFrom(reader: Reader) {
        var bufferedReader = if (reader is BufferedReader) {
            reader
        } else {
            BufferedReader(reader)
        }
        readers.add(bufferedReader)

        executor.submit {
            try {
                var line: String? = bufferedReader.readLine()
                while (line != null) {
                    write(line + "\n")
                    line = bufferedReader.readLine()
                }
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }
    }

    override fun write(line: String) {
        synchronized(this) {
            super.write(line)
            super.flush()
        }
    }

    override fun close() {
        readers.forEach {
            it.close()
        }
    }
}
