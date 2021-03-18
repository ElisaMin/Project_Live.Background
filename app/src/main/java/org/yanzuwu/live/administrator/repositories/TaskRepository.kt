package org.yanzuwu.live.administrator.repositories

import android.content.Context
import android.util.Log
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import org.yanzuwu.live.administrator.Main
import org.yanzuwu.live.administrator.models.Task
import org.yanzuwu.live.administrator.models.TaskResult
import kotlin.random.Random


/**
 * Task repository
 * 用于处理任务的仓库
 */
class TaskRepository (
    var context: Context
) {
    val isTaskSender = false
    val tasks by lazy {
        MutableStateFlow(Task())
    }
    private var lastTaskTimeThePoint:Long= 0
    private var taskPointForID= 0


    /**
     * 通过ID获取id
     */
    suspend fun getTaskByID(id : String) {
        repeat(9) {
            delay(300* Random.nextInt(10).toLong())
            Log.i(Main.TAG, "getTaskByID: called and $it")
            tasks.emit(Task(
                    id=taskPointForID,roomID = 900+taskPointForID++
            ).also { lastTaskTimeThePoint = it.generateTime })
        }
    }




    /**
     * 提交任务完成表单
     * @return 是否提交成功
     */
    suspend fun submitTask(id:Int,result: TaskResult):Boolean = true
}