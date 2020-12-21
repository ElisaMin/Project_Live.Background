package org.yanzuwu.live.administrator.repositories


import org.yanzuwu.live.administrator.utils.dataclassess.UserType
import kotlin.random.Random


/**
 * User repository
 * 用于处理用户信息的仓库
 * 包括登入等
 */
object UserRepository {
    /**
     * 检查是否为员工
     * @param phone 手机号码
     * @return [UserType] 用户类型
     */
    suspend fun checkPhoneOnLogged(phone:String?): UserType = phone?.let { UserType.TaskWorker } ?: UserType.NOT_ARROW

    /**
     * 通过ID获取该用户的名字
     * @param id
     */
    suspend fun getUserNameByID(id:String) = "刘大奔"


    val code = Random.nextInt(9999)

    /**
     * 检查验证码
     * @param code 验证码
     */
    suspend fun checkVerificationCode(code:String) = code.runCatching { toInt() }.getOrNull() == this.code

    /**
     * 发送验证码
     */
    private fun sendCode() = Unit

}