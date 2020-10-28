package com.example.gallerydemo

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.google.gson.Gson

class GalleryViewModel(application: Application) : AndroidViewModel(application) {
    private val _photoListLive = MutableLiveData<List<PhotoItem>>();
    val photoListLive: LiveData<List<PhotoItem>>
        get() = _photoListLive;

    fun fetchData() {
        val stringRequest = StringRequest(
            Request.Method.GET,
            getUrl(),
            Response.Listener {
                _photoListLive.value = Gson().fromJson(it, Pixabay::class.java).hits.toList()
            },
            Response.ErrorListener {
                Log.d("====Hello====", it.toString())
            }
        )
        VolleySingleton.getInstance(getApplication()).requestQueue.add(stringRequest)
    }

    //拼凑URL
    private fun getUrl(): String {
        return "https://pixabay.com/api/?key=15258738-a05e29c1621e54c726437b8b1&q=${keyWords.random()}&per_page=100"
    }

    private val keyWords =
        arrayOf("apple", "dog", "car", "beauty", "phone", "computer", "flower", "animal")
}