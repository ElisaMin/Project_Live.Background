package org.yanzuwu.live.administrator.data

import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import org.yanzuwu.live.administrator.Main.Companion.TAG
import org.yanzuwu.live.administrator.data.beans.Result
import org.yanzuwu.live.administrator.data.beans.Task
import java.lang.Runnable
import java.lang.reflect.Executable
import java.util.concurrent.Executor
import kotlin.random.Random


/**
 * The dao
 * 假Dao
 * 后期可直接接入数据
 *
 * @constructor Create empty The dao
 */

class TheDao : LifecycleObserver{


    val dao get() =  _dao!!
    private var _dao: CoroutineDispatcher? =null



    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun init() {
        _dao = Executor {  }.asCoroutineDispatcher()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun clean() {
        _dao = null
    }

    enum class UserType {
        /**
         * Not_arrow
         *
         * 不允许
         */
        NOT_ARROW,
        /**
         * High level task worker
         *
         * 高级任务工作者
         */
        HighLevelTaskWorker,
        /**
         * Task worker
         *
         * 任务工作者
         */
        TaskWorker,
        /**
         * Money manager
         *
         * 金钱管理者
         */
        MoneyManager,
        /**
         * Personal manager
         *
         * 人事部
         */
        PersonalManager,
    }

//    var id:String?=null

    /**
     * 检查是否为员工
     * @param phone 手机号码
     * @return [UserType] 用户类型
     */
    suspend fun checkPhoneOnLogged(phone:String?):UserType = phone?.let { UserType.TaskWorker } ?: UserType.NOT_ARROW

    /**
     * 通过ID获取该用户的名字
     * @param id
     */
    suspend fun getUserNameByID(id:String) = "刘大奔"

    companion object {
        val code = Random.nextInt(9999)
    }
    /**
     * 检查验证码
     * @param code 验证码
     */
    suspend fun checkVerificationCode(code:String) = code.runCatching { toInt() }.getOrNull() == TheDao.code

    /**
     * 发送验证码
     */
    private fun sendCode() = Unit

    val tasks by lazy {
        MutableStateFlow(Task())
    }
    private var lastTaskTimeThePoint:Long= 0
    private var taskPointForID= 0
    suspend fun getTaskByID(id : String) {
        repeat(9) {
            delay(100*Random.nextInt(10).toLong())
            Log.i(TAG, "getTaskByID: called and $it")
            tasks.emit(Task(
                    id=taskPointForID++,
            ).also { lastTaskTimeThePoint = it.generateTime })
        }
    }
}