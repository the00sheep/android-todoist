package com.bawp.todoister.model;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SharedViewModel extends ViewModel {
    private final MutableLiveData<Task> selecteItem = new MutableLiveData<>();

    public void setSelecteItem(Task task) {
        selecteItem.setValue(task);
    }

    public LiveData<Task> getSelectedItem(){
        return selecteItem;
    }

}
