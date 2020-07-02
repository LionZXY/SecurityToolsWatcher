package ru.lionzxy.securitytools.flows

import ru.lionzxy.securitytools.executors.BaseExecutor
import java.io.File

interface IFlow {
    fun run(input: File, executor: BaseExecutor): File?
}
