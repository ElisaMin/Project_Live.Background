package org.yanzuwu.live.administrator.models

import java.io.Serializable

/**
 * TaskResult 结果表单
 *
 * @property type 表单类型
 * @property isDone 任务是否完成
 * @constructor 封闭类
 */
sealed class TaskResult (
    open val type:String,
    open var isDone:Boolean=false
):Serializable {
    /**
     * Not done yet 翻译:然而并没有完成
     *
     * @constructor Create a taskResult
     */
    class NotDoneYet(): TaskResult("NotDone",false)

    /**
     * Fix devices task taskResult 修理设备返回表单
     * @property isDone 是否完成
     * @property reason 造成设备损坏的原因
     * @property theWayThatFixIt 维修方法
     * @property isSomethingReplaced 是否替换了某些东西
     * @property replacedDeviceInfo [Map]类型 替换信息
     * @property images 图片
     * @constructor Create empty Fix devices task taskResult
     */
    data class FixDevicesTaskTaskResult (
        override var isDone : Boolean,
        val reason:String?=null,
        val theWayThatFixIt:String?=null,
        val isSomethingReplaced:Boolean?=null,
        val replacedDeviceInfo:Map<String,Int>?=null,
        val images:List<String>?=null
    ): TaskResult("fixDevice",isDone)
}
