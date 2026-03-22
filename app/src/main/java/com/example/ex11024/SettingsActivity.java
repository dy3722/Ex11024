package com.example.ex11024;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

/**
 * @author David Yusupov <dy3722@bs.amalnet.k12.il>
 * @version 1.0
 * @since 22/3/2026
 * Settings Activity
 */
public class SettingsActivity extends AppCompatActivity {

    private Intent siCred;
    private EditText etNickName;
    private String nickName;
    private AlertDialog.Builder adb;

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
        setContentView(R.layout.activity_settings);

        siCred = new Intent(this,CreditsActivity.class);

        etNickName = findViewById(R.id.etNickName);

        SharedPreferences userInfo = getSharedPreferences("USER_INFO", MODE_PRIVATE);
        nickName = userInfo.getString("nickName", "NoName");

        etNickName.setText(nickName);
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
     * This implementation checks if the selected item is the "Main" or "Credits" menu item
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

        return super.onOptionsItemSelected(item);
    }

    public void toSave(View view) {
        nickName = etNickName.getText().toString();
        SharedPreferences userInfo = getSharedPreferences("USER_INFO", MODE_PRIVATE);
        SharedPreferences.Editor editor = userInfo.edit();
        editor.putString("nickName", nickName);
        editor.commit();

        finish();
    }

    public void toResetBest(View view) {
        adb = new AlertDialog.Builder(this);

        adb.setCancelable(false);
        adb.setTitle("Are you sure?");
        adb.setMessage("It's will reset your best score.");
        adb.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                SharedPreferences userInfo = getSharedPreferences("USER_INFO", MODE_PRIVATE);
                SharedPreferences.Editor editor = userInfo.edit();
                editor.putInt("bestScore", 0);

                editor.commit();
                Toast.makeText(SettingsActivity.this, "Reset!", Toast.LENGTH_SHORT).show();
            }
        });
        adb.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });

        AlertDialog ad = adb.create();
        ad.show();
    }
}