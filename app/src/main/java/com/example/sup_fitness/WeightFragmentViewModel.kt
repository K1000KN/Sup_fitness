package com.example.sup_fitness

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.sup_fitness.db.RoomAppDb
import com.example.sup_fitness.db.UserEntity

class WeightFragmentViewModel(app: Application): AndroidViewModel(app) {

    private var  allUsers : MutableLiveData<List<UserEntity>> = MutableLiveData()
    var list = mutableListOf<UserEntity>()

    init {
        getAllUsers()
        allUsers.value = mutableListOf()
    }

    fun getAllUsersObservers(): MutableLiveData<List<UserEntity>> {
        return allUsers
    }

    fun getAllUsers() {
        val userDao = RoomAppDb.getAppDatabase((getApplication()))?.userDao()
        list = userDao?.getAllUserInfo() as MutableList<UserEntity>

        allUsers.postValue(list)
        Log.i("allGet",list.toString())
    }

    fun insertUserInfo(entity: UserEntity) {
        val userDao = RoomAppDb.getAppDatabase(getApplication())?.userDao()
        userDao?.insertUser(entity)
        getAllUsers()
    }

    fun deleteUserInfo(entity: UserEntity) {
        val userDao = RoomAppDb.getAppDatabase(getApplication())?.userDao()
        userDao?.deleteUser(entity)
        getAllUsers()
    }


}