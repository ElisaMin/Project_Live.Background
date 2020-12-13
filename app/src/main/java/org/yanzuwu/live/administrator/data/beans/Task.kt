package org.yanzuwu.live.administrator.data.beans

import java.util.*

/**
 * Task 任务
 *
 * @property id
 * @property generateTime
 * @property processorName
 * @property type
 * @property roomID
 * @property result
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
        val type:TaskType=TaskType.PlumbingRepair,

        /** 房间编号 */
        val roomID:Int?=903,

        /** 任务结果 */
        var result:Result = Result.NotDoneYet(),

        /** 是否完成 */
        var isDone:Boolean = false
) {

    enum class TaskType (
            id : Int,name:String
    ){
        /**
         * 水管维修
         */
        PlumbingRepair(0,"水管维修")
    }
}
