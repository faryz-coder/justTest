package com.faryz.testapp.first

import android.app.Application
import android.util.Log.d
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import org.json.JSONArray
import org.json.JSONException

class UserListViewModel(application: Application) : AndroidViewModel(application) {
    var json: String? = null
    val userList = mutableListOf<ListUser>()

    fun editUserList(position: String, id: String, firstName: String, lastName: String, email: String, phone: String) {
        userList[position.toInt()] = ListUser(id, firstName, lastName, email, phone)
    }

    fun addUserList(id: String, firstName: String, lastName: String, email: String, phone: String) {
        userList.add(ListUser(id, firstName, lastName, email, phone))
    }

    private val _users = MutableLiveData<MutableList<ListUser>>().apply {

        val inputStream = application.assets.open("data.json")
        if (json == null) {
            json = inputStream.bufferedReader().use{ it.readText() }
            try {
                var jsonArray = JSONArray(json)

                for (i in 0 until jsonArray.length()) {
                    var jsonObject = jsonArray.getJSONObject(i)
                    val id = jsonObject.getString("id")
                    val firstName = jsonObject.getString("firstName")
                    val lastName = jsonObject.getString("lastName")
                    var email = " "
                    if (jsonObject.has("email")) {
                        email = jsonObject.getString("email")
                    }
                    var phone = " "
                    if (jsonObject.has("phone")) {
                        phone = jsonObject.getString("phone")
                    }
                    userList.add(ListUser(id, firstName,lastName, email, phone))
                }
                value = userList
            } catch (e: JSONException) {
                d("bomoh", "json error : $e")
            }
        }
    }
    val users : LiveData<MutableList<ListUser>> = _users

}