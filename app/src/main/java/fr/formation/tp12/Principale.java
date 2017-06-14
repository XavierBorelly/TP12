package fr.formation.tp12;

import android.content.Intent;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.support.design.widget.FloatingActionButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import fr.formation.tp12.database.datasource.DataSource;
import fr.formation.tp12.database.modele.User;

public class Principale extends AppCompatActivity {

    DataSource<User> dataSource;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principale);

        Intent i = getIntent();
        String newPatient = "";
        newPatient = i.getStringExtra("name");


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Principale.this, add_new_user.class);
                startActivity(intent);
            }
        });

        // Create or retrieve the database
        try {
            dataSource = new DataSource<>(this, User.class);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // open the database
        openDB();

        if(newPatient != null) {
            // Insert a new record
            // -------------------
            User user = new User();
            user.setNom(newPatient);
            try {
                insertRecord(user);
            } catch (Exception e) {
                e.printStackTrace();
            }

            Toast.makeText(this, newPatient,
                    Toast.LENGTH_LONG).show();

        }

        // Query that line
        // ---------------
        queryTheDatabase();
    }

    @Override
    protected void onResume() {
        super.onResume();
        openDB();
    }

    @Override
    protected void onPause() {
        super.onPause();
        closeDB();
    }

    public void openDB() throws SQLiteException {
        dataSource.getDB();
    }

    public void closeDB() {
        dataSource.close();
    }

    private long insertRecord(User user) throws Exception {

        // Insert the line in the database
        long rowId = dataSource.insert(user);

        // Test to see if the insertion was ok
        if (rowId == -1) {
            Toast.makeText(this, "Error when creating an User",
                    Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "User created and stored in database",
                    Toast.LENGTH_LONG).show();
        }
        return rowId;
    }

    /**
     * * Update a record
     *
     * @return the updated row id
     */
    private long updateRecord(User user) throws Exception {

        int rowId = dataSource.update(user);

        // test to see if the insertion was ok
        if (rowId == -1) {
            Toast.makeText(this, "Error when updating an User",
                    Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "User updated in database", Toast.LENGTH_LONG)
                    .show();
        }
        return rowId;
    }

    private void deleteRecord(User user) {
        long rowId = dataSource.delete(user);
        if (rowId == -1) {
            Toast.makeText(this, "Error when deleting an User",
                    Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "User deleted in database", Toast.LENGTH_LONG)
                    .show();
        }
    }

    /**
     * Query a the database
     */
    private void queryTheDatabase() {
        List<User> users = dataSource.readAll();
        displayResults(users);
    }

    private void displayResults(List<User> users) {

        List<String> listPatient = new ArrayList<>();
        int count = 0;
        for (User user : users
                ) {
            //Toast.makeText(this,"Utilisateur :" + user.getNom() + "("+ user.getId() + ")", Toast.LENGTH_LONG).show();
            count++;

            listPatient.add("Patien : " + user.getNom());
        }
        //Toast.makeText(this, "The number of elements retrieved is " + count, Toast.LENGTH_LONG).show();

        RecyclerView patients = (RecyclerView) findViewById(R.id.patient);
        RecyclerSimpleViewAdapter adapter = new RecyclerSimpleViewAdapter(listPatient, android.R.layout.simple_list_item_1);
        patients.setAdapter(adapter);
        patients.setLayoutManager(new LinearLayoutManager(this));
    }

}
