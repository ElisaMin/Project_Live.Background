package org.yanzuwu.live.administrator.data.beans

/**
 * Result 结果表单
 *
 * @property isDone
 * @constructor Create empty Result
 */
sealed class Result (
    open var isDone:Boolean=false
) {
    class NotDoneYet():Result(false)
    data class FixDevicesTaskResult (
        override var isDone : Boolean,
        val reason:String?=null,
        val theWayYouFixIt:String?=null,
        val isSomethingReplaced:Boolean?=null,
        val replacedDeviceInfo:Map<String,Int>?=null,
        val images:List<String>?=null
    ): Result(isDone)
}
