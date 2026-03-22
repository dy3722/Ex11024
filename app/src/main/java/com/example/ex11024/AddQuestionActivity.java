package com.example.ex11024;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

/**
 * @author David Yusupov <dy3722@bs.amalnet.k12.il>
 * @version 1.0
 * @since 22/3/2026
 * Add-Question Activity
 */
public class AddQuestionActivity extends AppCompatActivity {
    private Intent siCred, siSettings;
    private EditText etNewQuestion, etNewAns1, etNewAns2, etNewAns3, etNewAns4;
    private TextView tvIsEmpty;
    private final String NEW_QUESTION = "new_question.txt";

    /**
     * Initializes the activity and sets up the UI components.
     * <p>
     * This method sets the content view to activity_main, initializes the Intent for
     * the credits screen, and links the layout and TextView variables to their XML IDs.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously
     * being shut down then this Bundle contains the data it most recently supplied.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_question);

        siCred = new Intent(this,CreditsActivity.class);
        siSettings = new Intent(this,SettingsActivity.class);

        etNewQuestion = findViewById(R.id.etNewQuestion);
        etNewAns1 = findViewById(R.id.etNewAns1);
        etNewAns2 = findViewById(R.id.etNewAns2);
        etNewAns3 = findViewById(R.id.etNewAns3);
        etNewAns4 = findViewById(R.id.etNewAns4);
        tvIsEmpty = findViewById(R.id.tvIsEmpty);
    }

    /**
     * Initialize the contents of the Activity's standard options menu.
     * <p>
     * This method inflates the menu resource (R.menu.main) into the provided Menu
     * object and adds the items to the action bar.
     *
     * @param menu The options menu in which you place your items.
     * @return You must return true for the menu to be displayed; if you return false it will not be shown.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main,menu);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * This hook is called whenever an item in your options menu is selected.
     * <p>
     * This implementation checks if the selected item is the "Main" or "Credits" or "Setting" menu item
     * and, if so, starts the activity defined by the Intent 'si'.
     *
     * @param item The menu item that was selected.
     * @return boolean Return false to allow normal menu processing to proceed,
     * true to consume it here.
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menuMain)
        {
            finish();
        }
        else if (id == R.id.menuCredits)
        {
            finish();
            startActivity(siCred);
        }
        else if (id == R.id.menuSettings)
        {
            finish();
            startActivity(siSettings);
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Saves a new user-defined question to internal storage.
     * <p>
     * This method validates that all input fields (question and 4 answers) are not empty.
     * If valid, it appends the new question and answers to the "new_question.txt" file in
     * internal storage using a FileOutputStream and BufferedWriter. It then closes the activity.
     * If any field is empty, it displays a warning message.
     *
     * @param view The view that was clicked to trigger this method.
     */
    public void toSave(View view) {
        if (!etNewQuestion.getText().toString().isEmpty() && !etNewAns2.getText().toString().isEmpty() && !etNewAns3.getText().toString().isEmpty() && !etNewAns4.getText().toString().isEmpty() && !etNewAns1.getText().toString().isEmpty())
        {
            try
            {
                FileOutputStream fOS = openFileOutput(NEW_QUESTION, MODE_APPEND);
                OutputStreamWriter oSW = new OutputStreamWriter(fOS);
                BufferedWriter bW = new BufferedWriter(oSW);

                bW.write(etNewQuestion.getText().toString() + "\n" + etNewAns1.getText().toString() + "\n" + etNewAns2.getText().toString() + "\n" + etNewAns3.getText().toString() + "\n" + etNewAns4.getText().toString() + "\n");

                bW.close();
                oSW.close();
                fOS.close();
            }
            catch (Exception e)
            {
                Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
            tvIsEmpty.setVisibility(View.INVISIBLE);
            finish();
        }
        else
        {
            tvIsEmpty.setVisibility(View.VISIBLE);
        }
    }
}