package org.yanzuwu.live.administrator.ui.finance.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import me.heizi.kotlinx.android.default
import me.heizi.kotlinx.android.io
import org.yanzuwu.live.administrator.Main.Companion.TAG
import org.yanzuwu.live.administrator.repositories.FinanceRepository
import java.util.*
import javax.inject.Inject
import kotlin.collections.HashMap
import kotlin.math.roundToInt

@HiltViewModel
class FinanceHomeViewModel @Inject constructor (
    private val repository: FinanceRepository
) : ViewModel() {

    /** period id
     *  -bills
     *      all
     *      not done yet*/
//    private val loadedPages: List<FinanceRepository.SinglePageContent> get() = _loadedPages!!
    val isLoading get() = _isLoading.asStateFlow()
    var updateLoading
        set(value) { _isLoading.value = value }
        get() = isLoading.value
    val tabTitles get() = _loadedPages
        ?.map { it.name }
        ?.toTypedArray()
    val pages get() = _loadedPages
        ?.map {
            SinglePeriodViewModel(
                periodName = it.name,
                overView = it.overviews.map { bills ->
                    BillOverviewViewModel(
                        bills.key.translate,
                        roomCount = bills.value.first,
                        done = bills.value.second,
                    )
                },toDone = it.notDoneYet
                    .map { billOfRoom ->
                    ToDoneViewModel(title= "${ billOfRoom.roomMaster } | ${billOfRoom.roomName}",billOfRoom.masterPhone,toPays = HashMap<FinanceRepository.Bills,Float>().apply {
                        billOfRoom.bills.keys.forEach { bill ->
                            put(bill , billOfRoom.bills[bill]!!.first)
                        }
                    })
                }
            )
        }

    private var _loadedPages: ArrayList<FinanceRepository.SinglePageContent>? = null
    private val _isLoading = MutableStateFlow(true)
    private val status by lazy { MutableStateFlow<Status>(Status.Waiting) }

    fun start() = default {
        default {
            status.collect(::collecting)
        }
        default {
            status.emit(Status.StartTask)
        }
    }

    private suspend fun collecting(status: Status) {when(status) {
        is Status.StartTask -> io {
            Log.i(TAG, "collecting: called")
            _loadedPages = repository.getPageNeedingData()
            Log.i(TAG, "collecting: ${_loadedPages?.size}")
        }

    }
        Log.i(TAG, "collecting: $status")
    }

    init {
        repository.scope = viewModelScope
    }

    sealed class Status {
        object Waiting:Status()
        object StartTask:Status()
        class ChangeMountStart():Status()
    }
    inner class SinglePeriodViewModel(
        val periodName:String,
        val overView:List<BillOverviewViewModel>,
        val toDone:List<ToDoneViewModel>,
    ) {
        val titleFirst:String get() = "${periodName}总览"
        val titleSecond:String get() = "${periodName}待处理"
        val isLoading =  _isLoading.asStateFlow()
    }
    class BillOverviewViewModel(
        val billName:String,
        val roomCount:Int,
        val done:Int
    ) {
        val percent = (done.toFloat()/roomCount.toFloat()*100).roundToInt()
    }

    /**
     * To done view model
     *
     * @property title
     * @property phone
     * @property toPays type should pay
     * @property wechat
     * @constructor Create empty To done view model
     */
    class ToDoneViewModel(
        val title: String,
        val phone:String,
        val toPays:Map<FinanceRepository.Bills,Float>,
        val wechat:String?=null
    )



}