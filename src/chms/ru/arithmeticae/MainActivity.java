package chms.ru.arithmeticae;

import java.util.Random;

import android.os.Bundle;
import android.app.Activity;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

public class MainActivity extends Activity {

	protected final int START_LIVES = 3;
	protected final int START_HEIGHT = 0;
	
	protected final int NUM_UPPER_BOUND = 10;
	
	protected TextView livesTextView;
	protected TextView heightCountTextView;
	protected LinearLayout equationsContainer;
	protected int currentHeight = 0;
	
	// First & second multipliers
	protected int currentFirst;
	protected int currentSecond;
	
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
		
		TextView label = new TextView(this);
		label.setLayoutParams(layoutParams);
		label.setTextSize(24);
		label.setText(currentFirst + " " + timesSign + " " + currentSecond + " = ");
		
		EditText result = new EditText(this);
		result.setLayoutParams(layoutParams);
		result.setTextSize(24);
		result.setInputType(InputType.TYPE_CLASS_NUMBER);
		
		LinearLayout ec = new LinearLayout(this);
		ec.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		ec.addView(label); ec.addView(result);
		
		equationsContainer.addView(ec);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
