package chms.ru.arithmeticae;

import java.util.Random;

import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnKeyListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

@SuppressLint("ResourceAsColor")
public class MainActivity extends Activity {

	protected final int START_LIVES = 3;
	protected final int START_HEIGHT = 0;
	// How much height will be increased then solving one identity
	protected final int HEIGHT_MODIFIER = 5; 
	
	protected final int NUM_UPPER_BOUND = 10;
	
	protected TextView livesTextView;
	protected TextView heightCountTextView;
	protected LinearLayout equationsContainer;
	
	protected int currentHeight = 0;
	protected int currentLives;
	
	// First & second multipliers
	protected int currentFirst;
	protected int currentSecond;
	
	// Current identity view
	protected LinearLayout currentEquationContainer;
	protected TextView     currentEquationLabel;
	protected EditText     currentEquationEdit;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		livesTextView = (TextView)findViewById(R.id.lives_count);
		heightCountTextView = (TextView)findViewById(R.id.tower_height_count);
		equationsContainer = (LinearLayout)findViewById(R.id.tower_equations_container);
		
		initialize();
	}
	
	/**
	 * Initialization
	 */
	private void initialize(){
		Log.i("Initialize", "start");
		
		equationsContainer.removeAllViews();
		 
		Log.i("Initialize", "set lives to" + START_LIVES);
		setCurrentLives(START_LIVES);
		
		Log.i("Initialize", "set height unit to " + START_HEIGHT);
		setCurrentHeight(START_HEIGHT);
		
		Log.i("Initialize", "add identity to tower");
		addNewIdentity();
	}
	
	/**
	 * Sets current height of tower
	 * @param {int} value Height
	 */
	private void setCurrentHeight(int value){
		currentHeight = value;
		String heightUnit = getString(R.string.height_unit);
		heightCountTextView.setText(value + " " + heightUnit);
	}
	
	/**
	 * Sets current live count
	 * @param {int} value Lives count
	 */
	private void setCurrentLives(int value){
		currentLives = value;
		
		String liveIcon = getString(R.string.live_label);
		
		String lives = "";
		int i = 0;
		while (i<value){
			lives+=" " + liveIcon;
			i++;
		}
		livesTextView.setText(lives);
	}
	
	/**
	 * Adds new identity to tower
	 */
	private void addNewIdentity(){
		Log.i("addNewIdentity", "");
		
		Random random = new Random();
		currentFirst  = random.nextInt(NUM_UPPER_BOUND);
		currentSecond = random.nextInt(NUM_UPPER_BOUND);
		
		Log.i("addNewIdentity", "first: " + currentFirst + " second: " + currentSecond);
		
		LayoutParams layoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		
		String timesSign = getString(R.string.times_sign);
		
		currentEquationLabel = new TextView(this);
		currentEquationLabel.setLayoutParams(layoutParams);
		currentEquationLabel.setTextSize(24);
		currentEquationLabel.setText(currentFirst + " " + timesSign + " " + currentSecond + " = ");
		
		currentEquationEdit = new EditText(this);
		currentEquationEdit.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		currentEquationEdit.setTextSize(24);
		currentEquationEdit.setInputType(InputType.TYPE_CLASS_NUMBER);
		
		currentEquationContainer = new LinearLayout(this);
		currentEquationContainer.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		currentEquationContainer.addView(currentEquationLabel);
		currentEquationContainer.addView(currentEquationEdit);
		
		equationsContainer.addView(currentEquationContainer, 0);
		
		currentEquationEdit.requestFocus();
		
		currentEquationEdit.setOnKeyListener(new OnKeyListener(){
			public boolean onKey(View v, int keyCode, KeyEvent event){
				if( event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER ){
					Log.i("result.setOnKeyListener.onKey", "done is pressed!");
					String answer = ((EditText)v).getText().toString();
					checkUserAnswer(answer);
					return true;
				}
				return false;
			}
		});
	}
	
	/**
	 * Checks user answer, if it's correct add another identity
	 * Otherwise take one live from user
	 * If lives count is zero game is over
	 * @param {String} answer User answer
	 */
	protected void checkUserAnswer(String answer){
		Log.i("checkUserAnswer", "user answer is " + answer + ", right answer is " + currentFirst*currentSecond);
		
		int iAnswer; // answer converted to integer
		try{
			iAnswer = Integer.parseInt(answer);
		}catch(NumberFormatException e){
			return; // if answer can't be converted to integer then return
		}
		
		if(iAnswer==currentFirst*currentSecond){
			correctAnswer(iAnswer);
		}else{
			incorrectAnswer(iAnswer);
		}
	}
	
	/**
	 * Correct answer handler
	 * @param {int} answer User answer
	 */
	protected void correctAnswer(int answer){
		Log.i("correctAnswer", "given answer " + answer + " is correct");
		
		removeEditFromCurrentEquation();
		currentEquationContainer.setBackgroundColor(Color.TRANSPARENT);
		
		setCurrentHeight(currentHeight+HEIGHT_MODIFIER);
		
		// This must be below because it changes currentEquation***
		addNewIdentity();
	}
	
	/**
	 * Incorrect answer handler
	 * @param {int} answer User answer
	 */
	protected void incorrectAnswer(int answer){
		Log.i("checkUserAnswer", "answer is incorrect");
		
		currentEquationContainer.setBackgroundColor(getResources().getColor(R.color.incorrect_answer));
		currentEquationEdit.requestFocus();
		
		setCurrentLives(currentLives-1);
		
		checkGameOver();
	}
	
	/**
	 * Removes edit from currentEquation and put it content to label
	 */
	protected void removeEditFromCurrentEquation(){
		// Add equation answer to equation text
		String equationText = currentEquationLabel.getText().toString();
		String answerText = currentEquationEdit.getText().toString();
		currentEquationLabel.setText(equationText + answerText);

		// Remove equation user answer from currentEquation
		currentEquationContainer.removeView(currentEquationEdit);	
	}
	
	/**
	 * Checks for game over state
	 */
	protected void checkGameOver(){
		if( currentLives<=0 ){
			// Game is over
			Log.i("checkGameOver", "game is over");
			
			removeEditFromCurrentEquation();
			
			LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT); 
			
			TextView gameOverLabel = new TextView(this);
			gameOverLabel.setLayoutParams(lp);
			gameOverLabel.setTextSize(22);
			gameOverLabel.setText(R.string.game_over_label);
			
			Button restart = new Button(this);
			restart.setLayoutParams(lp);
			restart.setTextSize(22);
			restart.setText(R.string.restart_button);
			
			equationsContainer.addView(restart, 0);
			equationsContainer.addView(gameOverLabel, 0);
			
			restart.setOnClickListener(new View.OnClickListener(){
				public void onClick(View v) { initialize(); }
			});
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
