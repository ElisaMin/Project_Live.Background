package org.yanzuwu.live.administrator.ui.finance.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import me.heizi.kotlinx.android.io
import org.yanzuwu.live.administrator.repositories.FinanceRepository
import java.util.*
import javax.inject.Inject
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
    val tabTitles get() = _loadedPages
        ?.sortedBy { it.name }
        ?.map { it.name }
        ?.toTypedArray()
    private var _loadedPages: ArrayList<FinanceRepository.SinglePageContent>? = null
    private val _isLoading = MutableStateFlow(true)
    private fun collecting(status: Status) {when(status) {
        is Status.StartTask -> io {
            _loadedPages = repository.getPageNeedingData()
        }

    }}

    init {
        repository.scope = viewModelScope
    }

    sealed class Status {
        object StartTask:Status()
        class ChangeMountStart():Status()
    }
    inner class SinglePeriodViewModel(
        val titleFirst:String,
        val titleSecond:String,
        val overView:List<BillOverviewViewModel>,
        val toDone:List<ToDoneViewModel>,
    ) {
        val isLoading =  _isLoading.asStateFlow()
    }
    class BillOverviewViewModel(
        val billName:String,
        val roomCount:Int,
        val done:Int
    ) {
        val percent = (roomCount.toFloat()/done.toFloat()*100).roundToInt()
    }
    class ToDoneViewModel(
        val title: String,
        val phone:String,
        val toPays:Map<FinanceRepository.Bills,Int>,
        val wechat:String?=null
    )



}