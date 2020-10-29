package com.example.pagerGallery

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.google.gson.Gson
import kotlin.math.ceil

const val DATA_STATUS_CAN_LOAD_MORE = 0
const val DATA_STATUS_NO_MORE = 1
const val DATA_STATUS_NETWORK_ERROR = 2

class GalleryViewModel(application: Application) : AndroidViewModel(application) {
    private var currentPage = 1
    private var totalPage = 1
    private var currentKey = "love"
    private var isNewQuery = true
    private var isLoading = false
    private val perPage = 100
    var needToScrollTop = true
    private val keyWords =
        arrayOf(
            "apple",
            "dog",
            "car",
            "beauty",
            "phone",
            "computer",
            "flower",
            "animal",
            "love"
        )

    //拼凑URL
    private fun getUrl(): String {
        return "https://pixabay.com/api/?key=15258738-a05e29c1621e54c726437b8b1&q=${currentKey}&per_page=${perPage}&page=${currentPage}"
    }

    private val _photoListLive = MutableLiveData<List<PhotoItem>>();
    val photoListLive: LiveData<List<PhotoItem>> get() = _photoListLive;
    private val _dataStatusLive = MutableLiveData<Int>()
    val dataStatusLive: LiveData<Int> get() = _dataStatusLive

    init {
        resetQuery()
    }

    fun resetQuery() {
        currentPage = 1
        totalPage = 1
        currentKey = keyWords.random()
        isNewQuery = true
        needToScrollTop = true
        fetchData()
    }

    fun fetchData() {
        if (isLoading) return
        if (currentPage > totalPage) {
            _dataStatusLive.value = DATA_STATUS_NO_MORE
            return
        }
        isLoading = true
        val stringRequest = StringRequest(
            Request.Method.GET,
            getUrl(),
            Response.Listener {
                with(Gson().fromJson(it, com.example.pagerGallery.Pixabay::class.java)) {
                    totalPage = ceil(totalHits.toDouble() / perPage).toInt()
                    if (isNewQuery) {
                        _photoListLive.value = hits.toList()
                    } else {
                        //新请求的数据和原数据进行合并，并返回一个新的集合
                        _photoListLive.value =
                            arrayListOf(_photoListLive.value!!, hits.toList()).flatten()
                    }
                }
                _dataStatusLive.value = DATA_STATUS_CAN_LOAD_MORE
                isLoading = false
                isNewQuery = false
                currentPage++
                //_photoListLive.value = Gson().fromJson(it, Pixabay::class.java).hits.toList()
            },
            Response.ErrorListener {
                _dataStatusLive.value = DATA_STATUS_NETWORK_ERROR
                isLoading = false
                Log.d("====Hello====", it.toString())
            }
        )
        VolleySingleton.getInstance(getApplication()).requestQueue.add(stringRequest)
    }


}