package com.bawp.todoister;

import android.annotation.SuppressLint;
import android.os.Bundle;

import com.bawp.todoister.adapter.OnTodoClickListener;
import com.bawp.todoister.adapter.RecyclerViewAdapter;
import com.bawp.todoister.model.Priority;
import com.bawp.todoister.model.SharedViewModel;
import com.bawp.todoister.model.Task;
import com.bawp.todoister.model.TaskViewModel;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;

import android.view.Menu;
import android.view.MenuItem;

import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnTodoClickListener{
    private static final String TAG =  "item";
    private TaskViewModel taskViewModel;
    private RecyclerView recyclerView;
    private RecyclerViewAdapter recyclerViewAdapter;
    private int counter;
    private SharedViewModel sharedViewModel;


    BottomSheetFragment bottomSheetFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //toolbar widget
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        counter = 0;

        //instantiate BottomSheet
        bottomSheetFragment = new BottomSheetFragment();
        ConstraintLayout constraintLayout = findViewById(R.id.bottomSheet);

        BottomSheetBehavior<ConstraintLayout> bottomSheetBehavior = BottomSheetBehavior.from(constraintLayout);
        //bottomSheet always hidden
        bottomSheetBehavior.setPeekHeight(BottomSheetBehavior.STATE_HIDDEN);


        //recycler view setup
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        taskViewModel = new ViewModelProvider.AndroidViewModelFactory(
                MainActivity.this.getApplication())
                .create(TaskViewModel.class);


        //create a list of tasks
        taskViewModel.getAllTasks().observe(this, tasks -> {
            /* for (Task task : tasks){
                Log.d(TAG, "onCreate: " + task.getTaskId());
            }*/

            //pass the tasks to recycler view adapter
            recyclerViewAdapter = new RecyclerViewAdapter(tasks, this);
            recyclerView.setAdapter(recyclerViewAdapter);


        });

        //instantiate shared view model
        sharedViewModel = new ViewModelProvider(this).get(SharedViewModel.class);


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

//                Task task = new Task("Task" + counter++, Priority.MEDIUM, Calendar.getInstance().getTime(), Calendar.getInstance().getTime(), false );
//
//                TaskViewModel.insert(task);

                showBottomSheetDialog();
            }
        });
    }

    private void showBottomSheetDialog() {
        bottomSheetFragment.show(getSupportFragmentManager(), bottomSheetFragment.getTag());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTodoClick(Task task) {
        sharedViewModel.setSelecteItem(task);

        showBottomSheetDialog();

    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onTodoRadioButtonClick(Task task) {
        Log.d("Click Delete", "onTodoClick: " +task.getTask() );
        TaskViewModel.delete(task);
        recyclerViewAdapter.notifyDataSetChanged();
    }
}