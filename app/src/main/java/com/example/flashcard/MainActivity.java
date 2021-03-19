package com.example.flashcard;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    //initiate the database variable outside of onCreate method, otherwise getApplicationContext ...
    //..will error out inside of the oneCreate method
    FlashcardDatabase flashcardDatabase;
    List<Flashcard> allFlashcards;
    int currentCardDisplayIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //declare the database variable
        flashcardDatabase = new FlashcardDatabase(getApplicationContext());
        allFlashcards = flashcardDatabase.getAllCards();

            super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

            TextView flashcardQuestion = findViewById(R.id.flashcard_question);
            TextView flashcardAnswer = findViewById(R.id.flashcard_answer);
            ImageView addCardButton = findViewById(R.id.floatingActionButton);
            ImageView nextCardButton = findViewById(R.id.nextRightButton);

        if (allFlashcards != null && allFlashcards.size() > 0) {
            ((TextView) findViewById(R.id.flashcard_question)).setText(allFlashcards.get(0).getQuestion());
            ((TextView) findViewById(R.id.flashcard_answer)).setText(allFlashcards.get(0).getAnswer());
        }

        nextCardButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                if (allFlashcards.size() == 0)
                    return;
                currentCardDisplayIndex ++;

                if (currentCardDisplayIndex >= allFlashcards.size()) {
                    Snackbar.make(nextCardButton,
                            "You've reached the end of the cards, going back to start.",
                            Snackbar.LENGTH_SHORT)
                            .show();
                currentCardDisplayIndex = 0;}
                allFlashcards = flashcardDatabase.getAllCards();
                Flashcard flashcard = allFlashcards.get(currentCardDisplayIndex);

                ((TextView) findViewById(R.id.flashcard_answer)).setText(flashcard.getAnswer());
                ((TextView) findViewById(R.id.flashcard_question)).setText(flashcard.getQuestion());
            }
        });

        flashcardQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flashcardQuestion.setVisibility(View.INVISIBLE);
                flashcardAnswer.setVisibility(View.VISIBLE);
            }
        });
        flashcardAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flashcardQuestion.setVisibility(View.VISIBLE);
                flashcardAnswer.setVisibility(View.INVISIBLE);
            }
        });
        addCardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddCardActivity.class);
                intent.putExtra("question", flashcardQuestion.getText());
                intent.putExtra("answer", flashcardAnswer.getText());
                MainActivity.this.startActivityForResult(intent, 100);
            }
        });

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100 && resultCode == RESULT_OK && data != null) {

            // assigns the string from AddCardActivity to string1 and passes to flashcardQuestion
            String string1 = data.getExtras().getString("question");
            TextView flashcardQuestion = findViewById(R.id.flashcard_question);
            flashcardQuestion.setText(string1);

            // assigns string1 from AddCardActivity to string2 and passes to flashcardAnswer
            String string2 = data.getExtras().getString("answer");
            TextView flashcardAnswer = findViewById(R.id.flashcard_answer);
            flashcardAnswer.setText(string2);

            //saves the new question and answer from AddCardActivity to the Room database
            flashcardDatabase.insertCard(new Flashcard(string1, string2));
            allFlashcards = flashcardDatabase.getAllCards();

            // creates a short message (aka 'snack') after successfully creating a new card
            Snackbar.make(findViewById(R.id.flashcard_question),
                    "Card Successfully Created",
                    Snackbar.LENGTH_SHORT)
                    .show();

        }


    }
}