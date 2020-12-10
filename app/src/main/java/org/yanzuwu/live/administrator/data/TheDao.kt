package org.yanzuwu.live.administrator.data


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
    fun checkPhoneOnLogged(phone:String?):UserType = phone?.let { UserType.TaskWorker } ?: UserType.NOT_ARROW
    fun getUserNameByID(id:Int) = "刘大奔"
}