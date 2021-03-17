package org.yanzuwu.live.administrator.repositories

import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import me.heizi.kotlinx.android.default
import org.yanzuwu.live.administrator.Main.Companion.TAG
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
        val notDoneYet: List<BillOfRoom>
    )
    //id maxGen
    private val notDoneYetCounts = hashMapOf<Int,Int>()
    suspend fun getPageNeedingData(lastPeriod:Int = this.lastPeriod,pagesCount: Int = 6) : ArrayList<SinglePageContent> {
        var i = lastPeriod
        val result:ArrayList<SinglePageContent> = arrayListOf()
        repeat(pagesCount) {
            val overviews = getOverviews(i)
            val (count,not) = overviews[Bills.All]!!
            notDoneYetCounts[i] = count - not
            SinglePageContent(getPeriodName(i), overviews,getAllNotSuccessBills(i)).let(result::add)
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
        val masterPhone:String ,
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
    suspend fun getAllNotSuccessBills(periodID: Int): List<BillOfRoom>   {
        val task = genBills(periodID)
        gen().join()
        val rooms = ArrayList<BillOfRoom>()
        Log.i(TAG, "getAllNotSuccessBills: task await")
        val bills = task.await()
        repeat(bills.size) {
            rooms.add(
                BillOfRoom(it,roomNames[it],names[it],(13900000000+Random(System.currentTimeMillis()).nextInt(10000)).toString(),bills[it])
            )
        }
        return rooms.filter { billOfRoom -> billOfRoom.bills.isNotEmpty() }
    }
    private var roomNames = ArrayList<String>()
    private var names = ArrayList<String>()
    fun genBills(periodID: Int)= scope.async {
        val types = Bills.values()
        fun getFlout() = Random.nextDouble(30.0,500.0).pow(2).toFloat()
        val maxGen = notDoneYetCounts[periodID]!!
        val hashmaps = ArrayList<HashMap<Bills,Pair<Float,Float>>>()
        repeat(maxGen) {
            val map = HashMap<Bills,Pair<Float,Float>>()
            repeat(Random.nextInt(6)) {
                map [types.random()] = getFlout() to getFlout()
            }
            map.remove(Bills.All)
            hashmaps.add(map)
        }
        hashmaps
    }
    var gens = 5
    suspend fun gen() = scope.default {
        fun <T> messup(list:List<T>):ArrayList<T> {
            val count = list.size
            val revers = ArrayList(list.reversed())
            val normal = ArrayList(list)
            repeat(count*gens++) {
                val ramdom = Random(it).nextInt(count)
                val reversed = revers[ramdom]
                val normar = normal[ramdom]
                revers[ramdom] = normar
                normal[ramdom] = reversed
            }

            return ArrayList(revers)
        }
        if (roomNames.isEmpty()) default {
            for ( i in arrayOf("A","B","C")) {
                repeat(2) { a ->
                    repeat(25) { l->
                        repeat(4){h->
                            roomNames.add("$i$a-${l}0$h")
                        } } } }
            roomNames=ArrayList(messup(roomNames).subList(1,All+10))
        } else default {
            roomNames=messup(roomNames)
        }
        if(names.isEmpty()) default {
            repeat(All) {
                names.add(firstNames.random()+jieXing.random()+firstNames.random())
            }
        }
        names = messup(names.drop(1))
    }
}