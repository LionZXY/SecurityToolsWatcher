package ru.lionzxy.securitytools.utils

import pxb.android.axml.NodeVisitor

public class ChangeNodeVisitor(
    nv: NodeVisitor?, private val nodeName: String?,
    val changer: NodeChanger,
    currentLevel: Int = 0
) : NodeVisitor(nv) {
    private val level = currentLevel + 1
    override fun child(ns: String?, name: String?): NodeVisitor {
        return ChangeNodeVisitor(super.child(ns, name), name, changer, level)
    }

    override fun attr(ns: String?, name: String?, resourceId: Int, type: Int, result: Any?) {
        changer.attr(
            level,
            nodeName,
            ns,
            name,
            resourceId,
            type,
            result
        ) { nsOut, nameOut, resourceIdOut, typeOut, resultOut ->
            super.attr(nsOut, nameOut, resourceIdOut, typeOut, resultOut)
        }
    }

    override fun end() {
        super.end()
    }
}

@FunctionalInterface
public interface NodeChanger {
    fun attr(
        level: Int, nodeName: String?, ns: String?, name: String?, resourceId: Int, type: Int, result: Any?,
        out: (nsOut: String?, nameOut: String?, resourceIdOut: Int, typeOut: Int, resultOut: Any?) -> Unit
    )
}
