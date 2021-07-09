package sg.edu.rp.c346.id19023702.demodatabase;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class MainActivity extends AppCompatActivity {
    Button btnInsert, btnGetTasks;
    EditText etLesson, etDate;
    ListView listView;
    TextView tvResult;
    Spinner spinner;
    ArrayAdapter<Task> aa;
    ArrayList<Task> al;
    ArrayAdapter<String> aa1;
    ArrayList<String> up;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnInsert = findViewById(R.id.btnInsert);
        btnGetTasks = findViewById(R.id.btnGetTask);
        tvResult = findViewById(R.id.tvResult);
        etLesson = findViewById(R.id.edit_lesson);
        etDate = findViewById(R.id.edit_date);
        listView = findViewById(R.id.lv);
        spinner = findViewById(R.id.spinner);

        al = getFromDB(false);
        aa = new TaskAdapter(this, R.layout.row, al);
        listView.setAdapter(aa);


        up = new ArrayList<String>();
        up.add(new String("ASCENDING"));
        up.add(new String("DESCENDING"));

        aa1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, up);
        spinner.setAdapter(aa1);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });

        btnInsert.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                // Create the DBHelper object, passing in the
                // activity's Context
                DBHelper db = new DBHelper(MainActivity.this);

                // Insert a task
                db.close();

                // ListView

                if (checkAllFields()) {
                    DBHelper dbHelper = new DBHelper(MainActivity.this);
                    String description = etLesson.getText().toString();
                    String date = etDate.getText().toString();

                    Task newTask = new Task(description, date);

                    long statusId = dbHelper.insertTask(newTask.getDescription(), newTask.getDate());
                    dbHelper.close();
                    if (statusId != -1) {
                        Toast.makeText(MainActivity.this, "Inserted Description and Date successfully.", Toast.LENGTH_SHORT).show();
                        clearAllFields();
                    }
                }
            }
        });

        btnGetTasks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Create the DBHelper object, passing in the activity's content
                DBHelper db = new DBHelper(MainActivity.this);
//
//                // insert a task
                ArrayList<String> data = db.getTaskContent();
                db.close();

                String txt = "";
                for (int i = 0; i < data.size(); i++) {
                    Log.d("Database content", i + ". " + data.get(i));
                    txt += i + ". " + data.get(i) + "\n";
                }
                tvResult.setText(txt);

                // ListView

                DBHelper dbh = new DBHelper(MainActivity.this);
                al.clear();
                al.addAll(dbh.getTasks());

                aa.notifyDataSetChanged();
                dbh.close();
            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String getUp = spinner.getItemAtPosition(position).toString();
                if (getUp.equals("ASCENDING")) {
                    sortArrayList();
                } else if (getUp.equals("DESCENDING")) {
                    sortArrayList();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    private boolean checkAllFields() {
        ArrayList<String> problems = new ArrayList<>();
        if (etLesson.getText().toString().trim().isEmpty()) {
            problems.add("Description");
        }
        if (etLesson.getText().toString().trim().isEmpty()) {
            problems.add("Date");
        }
        if (problems.size() > 0) {
            String message = "Ensure that: ";

            if (problems.size() == 1) {
                message += problems.get(0) + " ";
            }
            else if (problems.size() > 1) {
                for (int i = 0; i < problems.size(); i++) {
                    if (i == problems.size() - 1) {
                        message += "and " + problems.get(i) + " ";
                    }
                    else {
                        message += problems.get(i) + ", ";
                    }
                }
            }

            message += "are filled/selected correctly.";
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        }

        return problems.isEmpty();
    }

    private void clearAllFields() {
        etLesson.setText("");
        etDate.setText("");
    }

    private ArrayList<Task> getFromDB(boolean b) {
        ArrayList<Task> tasks;
        DBHelper helper = new DBHelper(this);
        tasks = helper.getTasks();
        helper.close();
        return tasks;
    }

    private void sortArrayList() {
        Collections.sort(al, new Comparator<Task>() {
            @Override
            public int compare(Task o1, Task o2) {
                return o1.getDescription().compareToIgnoreCase(o2.getDescription());
            }
        });
    }

}