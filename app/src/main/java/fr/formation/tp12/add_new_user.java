package fr.formation.tp12;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

public class add_new_user extends AppCompatActivity {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_user);
    }

    public void onValidate(View view) {

        Intent intent = new Intent(add_new_user.this, Principale.class);

        EditText nameUser = (EditText) findViewById(R.id.nameUser);

        intent.putExtra("name", nameUser.getText().toString());
        startActivity(intent);
        finish();
    }

}
