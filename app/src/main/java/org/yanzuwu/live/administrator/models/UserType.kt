package org.yanzuwu.live.administrator.models

/**
 * User type 用户类型
 * 描述所有员工的类型
 */
enum class UserType {
    /** Not_arrow 不允许 */
    NOT_ARROW,
    /** High level task worker 高级任务工作者 */
    HighLevelTaskWorker,
    /** Task worker 任务工作者 */
    TaskWorker,
    /** Money manager 金钱管理者 */
    MoneyManager,
    /** Personal manager 人事部 */
    PersonalManager,
}