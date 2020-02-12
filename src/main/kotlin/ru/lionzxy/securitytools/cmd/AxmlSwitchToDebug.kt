package ru.lionzxy.securitytools.cmd

import pxb.android.axml.AxmlReader
import pxb.android.axml.AxmlVisitor
import pxb.android.axml.AxmlWriter
import pxb.android.axml.NodeVisitor
import ru.lionzxy.securitytools.utils.ChangeNodeVisitor
import ru.lionzxy.securitytools.utils.NodeChanger
import java.io.File


class AxmlSwitchToDebug : ITerminalCommand, NodeChanger {
    private var addDebug = false

    override fun getOutputFile(inputFile: File?): File? {
        return inputFile
    }

    override fun buildCommand(inputFile: File?, targetFile: File?): ProcessBuilder? {
        val inputAXML = inputFile ?: throw RuntimeException("Not found axml file")
        val outputAXML = targetFile!!
        val reader = AxmlReader(inputAXML.readBytes())
        val writer = AxmlWriter()
        reader.accept(object : AxmlVisitor(writer) {
            override fun child(ns: String?, name: String?): NodeVisitor {
                return ChangeNodeVisitor(super.child(ns, name), name, this@AxmlSwitchToDebug)
            }
        })
        if (!addDebug) {
            throw RuntimeException("Can't add debuggable")
        }
        outputAXML.writeBytes(writer.toByteArray())

        return null
    }

    override fun attr(
        level: Int,
        nodeName: String?,
        ns: String?,
        name: String?,
        resourceId: Int,
        type: Int,
        result: Any?,
        out: (nsOut: String?, nameOut: String?, resourceIdOut: Int, typeOut: Int, resultOut: Any?) -> Unit
    ) {
        if (level == 2 && nodeName == "application" && !addDebug) {
            out.invoke("http://schemas.android.com/apk/res/android", "debuggable", 16842767, 18, true)
            addDebug = true
        }
        //println("$level,$nodeName, $ns,$name, $resourceId, $type, $result (${result?.javaClass})")
        out.invoke(ns, name, resourceId, type, result)
    }

    override fun checkAvailability(): ProcessBuilder? = null

}
