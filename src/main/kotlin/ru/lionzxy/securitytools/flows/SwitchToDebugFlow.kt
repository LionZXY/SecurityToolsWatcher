package ru.lionzxy.securitytools.flows

import ru.lionzxy.securitytools.cmd.ApkToolCompile
import ru.lionzxy.securitytools.cmd.ApkToolDecompile
import ru.lionzxy.securitytools.cmd.AxmlSwitchToDebug
import ru.lionzxy.securitytools.cmd.SignApk
import ru.lionzxy.securitytools.executors.BaseExecutor
import java.io.File

object SwitchToDebugFlow : IFlow {
    override fun run(input: File, executor: BaseExecutor): File? {
        val decompileFolder = executor.execute(ApkToolDecompile(), input)
        executor.execute(AxmlSwitchToDebug(), File(decompileFolder, "AndroidManifest.xml"))
        val apk = executor.execute(ApkToolCompile(), decompileFolder)
        return executor.execute(SignApk(), apk)
    }

}
