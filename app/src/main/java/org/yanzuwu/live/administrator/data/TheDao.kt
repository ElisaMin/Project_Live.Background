package org.yanzuwu.live.administrator.data

import kotlin.random.Random


/**
 * The dao
 * 假Dao
 * 后期可直接接入数据
 *
 * @constructor Create empty The dao
 */
class TheDao {

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
    suspend fun getUserNameByID(id:Int) = "刘大奔"

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
}