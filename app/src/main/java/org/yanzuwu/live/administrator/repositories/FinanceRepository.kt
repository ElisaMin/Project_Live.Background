package org.yanzuwu.live.administrator.repositories

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import me.heizi.kotlinx.android.default
import kotlin.math.pow
import kotlin.random.Random

/**
 * Finance repository
 *
 * 金融管理的数据仓库
 */
class FinanceRepository(

) {
    companion object {
        const val All = 524

    }

    lateinit var scope : CoroutineScope
    private val randomAround get() = All- Random(System.currentTimeMillis()).nextInt(100)
    private val round get() = All- Random(System.currentTimeMillis()).nextInt(10)

    val lastPeriod = 14

    /**
     * 单页数据
     *
     * @property name xx月xx期
     * @property overviews 预览的所有数据
     * @property notDoneYet 没有完成的数据
     */
    data class SinglePageContent(
        val name:String,
        val overviews: HashMap<Bills, Pair<Int, Int>>,
        val notDoneYet: ArrayList<BillOfRoom>
    )

    suspend fun getPageNeedingData(lastPeriod:Int = this.lastPeriod,pagesCount: Int = 2) : ArrayList<SinglePageContent> {
        var i = lastPeriod
        val result:ArrayList<SinglePageContent> = arrayListOf()
        repeat(pagesCount) {
            SinglePageContent(getPeriodName(i),getOverviews(i),getAllNotSuccessBills(i))
            i--
        }
        return result
    }

    fun getPeriodName(periodID: Int):String {
        if (periodID<=0) throw  IllegalArgumentException("ID不存在")
        val periodSun = (periodID%12).takeIf { it!=0 } ?: 12
        return "${2020 + (periodID/12)}年${periodSun}月"

    }
    /** 账单类型 */
    enum class Bills(val translate:String) {
        Elect("电费"),Water("水费"),
        Gas("天然气"),Trash("垃圾处理费"),
        Safer("保安保护费"),Room("月租"),
        All("总和")
    }
    /**
     * 获取本期的所有账单总预览
     *
     * @return 类型/账单总量/已完成
     */
    suspend fun getOverviews(periodID: Int): HashMap<Bills, Pair<Int, Int>> {
        val map = hashMapOf(
            Bills.Elect to (round to randomAround),
            Bills.Water to (round to randomAround),
            Bills.Gas to (round to randomAround),
            Bills.Safer to (round to randomAround),
            Bills.Trash to (round to randomAround),
            Bills.Room to (round to randomAround),
        )
        map[Bills.All] = All to map.values.minOf { it.second }
        return map
    }

    /**
     * },{
     *     id:roomId,
     *     name:roomName,
     *     bills:[{
     *         type:billType,
     *         shouldPay:,
     *         payed:,
     *     }]
     * },{
     */
    data class BillOfRoom(
        val roomId:Int,
        val roomName:String,
        val roomMaster:String,
        val bills:HashMap<Bills,Pair<Float,Float>>
    )

    private val firstNames by lazy {arrayOf("赵","钱","孙","李","周","吴","郑","王","冯","陈","褚","卫","蒋","沈","韩","杨","朱","秦","尤","许","何","吕","施","张")}
    private val jieXing  by lazy { arrayOf("子","家","晓","明","永","五","","","","","","","","","","","建","下","国","里","娜","花","","","") }

    /**
     * 获取所有本月未完成账单
     *
     * @param periodID
     * @return
     */
    suspend fun getAllNotSuccessBills(periodID: Int): ArrayList<BillOfRoom>   {
        fun getFlout() = Random.nextDouble(30.0,500.0).pow(2).toFloat()
        val rooms = ArrayList<BillOfRoom>()
        val ABC = arrayOf("A","B","C")
        var roomNames = ArrayList<String>()
        val names = ArrayList<String>()
        val hashmaps = ArrayList<HashMap<Bills,Pair<Float,Float>>>()
        val types = Bills.values()
        scope.launch {
            default {
                repeat(All) {
                    val map = HashMap<Bills,Pair<Float,Float>>()
                    repeat(Random.nextInt(6)) {
                        map [types.random()] = getFlout() to getFlout()
                    }
                    map.remove(Bills.All)
                    hashmaps.add(map)
                }
            }
            default {
                for ( i in ABC) {
                    repeat(2) { a ->
                        repeat(25) { l->
                            repeat(4){h->
                                roomNames.add("$i$a-${l}0$h")
                            } } } }
                val count = roomNames.size
                val revers = ArrayList(roomNames.reversed())
                val normal = roomNames
                repeat(count*3) {
                    val ramdom = Random(it).nextInt(count)
                    val reversed = revers[ramdom]
                    val normar = normal[ramdom]
                    revers[ramdom] = normar
                    normal[ramdom] = reversed
                }
                roomNames = revers
            }
            default {
                repeat(All) {
                    names.add(firstNames.random()+jieXing.random()+firstNames.random())
                }
            }
        }.join()
        repeat(All- Random.nextInt(All)) {
            rooms.add(
                BillOfRoom(it,roomNames[it],names[it],hashmaps[it])
            )
        }
        return rooms
    }
}