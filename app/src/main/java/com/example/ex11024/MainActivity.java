package com.example.ex11024;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
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
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

/**
 * @author David Yusupov <dy3722@bs.amalnet.k12.il>
 * @version 1.0
 * @since 22/3/2026
 * Main Activity
 */
public class MainActivity extends AppCompatActivity {
    private final String STARTED_QUESTIONS = "started_questions.txt";
    private final String NEW_QUESTION = "new_question.txt";
    private Intent siCred, siSettings, siAddQuestion;
    private String line;
    private TextView tvQuestion, tvInformation;
    private Button btnAns1, btnAns2, btnAns3, btnAns4;
    private int score = 0, bestScore = 0, numOfQuestion = 1, index = 0;
    private ArrayList<String> questionList = new ArrayList<>();
    private LinearLayout llBTN1and2, llBTN3and4;

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
        setContentView(R.layout.activity_main);

        siCred = new Intent(this, CreditsActivity.class);
        siSettings = new Intent(this, SettingsActivity.class);
        siAddQuestion = new Intent(this, AddQuestionActivity.class);

        tvQuestion = findViewById(R.id.tvQuestion);
        tvInformation = findViewById(R.id.tvInformation);
        btnAns1 = findViewById(R.id.btnAns1);
        btnAns2 = findViewById(R.id.btnAns2);
        btnAns3 = findViewById(R.id.btnAns3);
        btnAns4 = findViewById(R.id.btnAns4);
        llBTN1and2 = findViewById(R.id.llBTN1and2);
        llBTN3and4 = findViewById(R.id.llBTN3and4);

        SharedPreferences userInfo = getSharedPreferences("USER_INFO", MODE_PRIVATE);
        bestScore = userInfo.getInt("bestScore", 0);
        tvInformation.setText("Score: " + score + "\n" + "Best score: " +bestScore);

        try
        {
            String fileNameStartedQuestions = STARTED_QUESTIONS.substring(0, STARTED_QUESTIONS.length() - 4);
            int startedQuestionsId = this.getResources().getIdentifier(fileNameStartedQuestions, "raw", this.getPackageName());
            InputStream iS = this.getResources().openRawResource(startedQuestionsId);
            InputStreamReader iSR = new InputStreamReader(iS);
            BufferedReader bR = new BufferedReader(iSR);

            addRaw();

            putQuestion();

            btnAns1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (numOfQuestion <= 3) badAns(bR);
                    else goodAns(userInfo,bR);
                }
            });

            btnAns2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    badAns(bR);
                }
            });

            btnAns3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {    //correct
                    if (numOfQuestion == 1 || numOfQuestion == 3) goodAns(userInfo,bR);
                    else badAns(bR);
                }
            });

            btnAns4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (numOfQuestion == 2) goodAns(userInfo,bR);
                    else badAns(bR);
                }
            });

            bR.close();
            iSR.close();
            iS.close();
        }
        catch (Exception e)
        {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        score = 0;
        index = 0;
        numOfQuestion = 1;

        SharedPreferences userInfo = getSharedPreferences("USER_INFO", MODE_PRIVATE);
        bestScore = userInfo.getInt("bestScore", 0);
        tvInformation.setText("Score: " + score + "\n" + "Best score: " +bestScore);
        tvInformation.setTextColor(Color.parseColor("#03A9F4"));
        llBTN1and2.setVisibility(View.VISIBLE);
        llBTN3and4.setVisibility(View.VISIBLE);

        addNewQuestions();
        putQuestion();
    }

    private void addRaw()
    {
        try {
            String fileNameStartedQuestions = STARTED_QUESTIONS.substring(0, STARTED_QUESTIONS.length() - 4);
            int startedQuestionsId = this.getResources().getIdentifier(fileNameStartedQuestions, "raw", this.getPackageName());
            InputStream iS = this.getResources().openRawResource(startedQuestionsId);
            InputStreamReader iSR = new InputStreamReader(iS);
            BufferedReader bR = new BufferedReader(iSR);

            line = bR.readLine();
            while (line != null) {
                questionList.add(line);
                line = bR.readLine();
            }

            bR.close();
            iSR.close();
            iS.close();
        }
        catch (Exception e)
        {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
        }
    }

    private void addNewQuestions()
    {
        ArrayList<String> newQuestionList = new ArrayList<>();
        try {
            FileInputStream fIS = openFileInput(NEW_QUESTION);
            InputStreamReader iSR = new InputStreamReader(fIS);
            BufferedReader bR = new BufferedReader(iSR);

            line = bR.readLine();
            while (line != null)
            {
                newQuestionList.add(line);
                line = bR.readLine();
            }

            bR.close();
            iSR.close();
            fIS.close();
        }
        catch (Exception e)
        {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
        }

        if (questionList.isEmpty()) {
            questionList.addAll(newQuestionList);
        } else {
            if (newQuestionList.size() > questionList.size()) {
                questionList.addAll(newQuestionList.subList(questionList.size(), newQuestionList.size()));
            }
        }
    }

    public void goodAns(SharedPreferences userInfo, BufferedReader bR)
    {
        score++;
        if (score > bestScore)
        {
            bestScore = score;
            SharedPreferences.Editor editor = userInfo.edit();
            editor.putInt("bestScore", bestScore);

            editor.commit();
        }

        tvInformation.setTextColor(Color.GREEN);
        tvInformation.setText("Score: " + score + "\n" + "Best score: " +bestScore);

        numOfQuestion++;

        putQuestion();
    }

    public void badAns(BufferedReader bR)
    {
        tvInformation.setTextColor(Color.RED);
        numOfQuestion++;
        putQuestion();
    }

    public void putQuestion()
    {
        if (questionList == null || index + 3 >= questionList.size())
        {
            tvQuestion.setText("Quiz Finished!");
            llBTN1and2.setVisibility(View.INVISIBLE);
            llBTN3and4.setVisibility(View.INVISIBLE);
            tvInformation.setText("Score: " + score + "\n" + "Best score: " + bestScore);
            return;
        }
        tvQuestion.setText(questionList.get(index++));
        btnAns1.setText(questionList.get(index++));
        btnAns2.setText(questionList.get(index++));
        btnAns3.setText(questionList.get(index++));
        btnAns4.setText(questionList.get(index++));
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
     * This implementation checks if the selected item is the "Credits" or "Setting" menu item
     * and, if so, starts the activity defined by the Intent 'si'.
     *
     * @param item The menu item that was selected.
     * @return boolean Return false to allow normal menu processing to proceed,
     * true to consume it here.
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menuCredits)
        {
            startActivity(siCred);
        }
        else if (id == R.id.menuSettings)
        {
            startActivity(siSettings);
        }

        return super.onOptionsItemSelected(item);
    }

    public void toAddQuestion(View view) {
        startActivity(siAddQuestion);
    }
}