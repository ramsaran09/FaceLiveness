package com.muthuram.faceliveness

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.muthuram.faceliveness.models.StudentItem
import kotlinx.coroutines.launch

class SelectionViewModel : ViewModel() {

    private val _studentList = MutableLiveData<ArrayList<StudentItem>>()
    val studentList : LiveData<ArrayList<StudentItem>> = _studentList

    fun convertStudentUrlToImage(students: ArrayList<StudentItem>) {
        viewModelScope.launch {
            val studentList = arrayListOf<StudentItem>()
            students.map {
                studentList.add(
                    StudentItem(
                        userId = it.userId,
                        name = it.name,
                        status = "pending",
                        faceUrl = it.faceUrl,
                        studentImage = it.faceUrl,
                    )
                )
            }
            _studentList.value = studentList
        }
    }



    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer { SelectionViewModel() }
        }
    }
}