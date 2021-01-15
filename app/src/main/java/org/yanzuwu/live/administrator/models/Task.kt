package org.yanzuwu.live.administrator.models

import org.yanzuwu.live.administrator.utils.TimeFormatter.toShortFormat
import java.io.Serializable
import java.util.*

/**
 * Task 任务
 *
 * @property id
 * @property generateTime
 * @property processorName
 * @property type
 * @property roomID
 * @property taskResult
 * @property isDone
 * @constructor Create empty Task
 */
data class Task (

    val id:Int?=null,

    /** 生成日期 */
        val generateTime:Long = Date().time,

    /** 派发者 */
        val processorName:String="系统默认",

    /** 任务类型 */
        val type: TaskType = TaskType.PlumbingRepair,

    /** 房间编号 */
        val roomID:Int?=903,

    /** 任务结果 */
        var taskResult: TaskResult = TaskResult.NotDoneYet(),

    /** 是否完成 */
        var isDone:Boolean = false
):Serializable {
    companion object {
        const val serialVersionUID = 32L
    }
    val title get() = "$roomID ${type.typeName}"
    val time get() = generateTime.toShortFormat()

    enum class TaskType (
            val id : Int,val typeName:String
    ){
        /**
         * 水管维修
         */
        PlumbingRepair(0,"水管维修")
    }
}
