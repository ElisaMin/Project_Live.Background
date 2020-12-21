package org.yanzuwu.live.administrator.repositories

import android.util.Log
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import org.yanzuwu.live.administrator.Main
import org.yanzuwu.live.administrator.utils.dataclassess.Task
import kotlin.random.Random

/**
 * Task repository
 * 用于处理任务的仓库
 */
object TaskRepository {
    val tasks by lazy {
        MutableStateFlow(Task())
    }
    private var lastTaskTimeThePoint:Long= 0
    private var taskPointForID= 0
    suspend fun getTaskByID(id : String) {
        repeat(9) {
            delay(100* Random.nextInt(10).toLong())
            Log.i(Main.TAG, "getTaskByID: called and $it")
            tasks.emit(Task(
                    id=taskPointForID++,
            ).also { lastTaskTimeThePoint = it.generateTime })
        }
    }
}