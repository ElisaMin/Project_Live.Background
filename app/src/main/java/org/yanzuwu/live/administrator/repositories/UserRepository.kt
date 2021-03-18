package org.yanzuwu.live.administrator.repositories


import android.content.Context
import android.util.Log
import kotlinx.coroutines.delay
import org.yanzuwu.live.administrator.Main.Companion.TAG
import org.yanzuwu.live.administrator.models.Preferences
import org.yanzuwu.live.administrator.models.UserType
import kotlin.random.Random


/**
 * User repository
 * 用于处理用户信息的仓库
 * 包括登入等
 */
class UserRepository (
    var context:Context,
    val preferences: Preferences
) {
    /**
     * 检查是否为员工
     * @param phone 手机号码
     * @return [UserType] 用户类型
     */
    suspend fun checkPhoneOnLogged(phone:String?): UserType {
        delay(2000)
        Log.i(TAG, "checkPhoneOnLogged: phone$phone")
        val type =  when(phone){
            null -> UserType.NOT_ARROW
            "111111111" -> UserType.MoneyManager
            else->UserType.TaskWorker
        }
        if (type != UserType.NOT_ARROW) savePhone(phone)
        return type
    }

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
    private fun sendCode() {}

    fun savePhone(phone: String?)  {
        preferences.phone = phone
    }

}