package org.chisty.uniconverter;

import java.math.BigDecimal;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

/**
 * The Class MainActivity.
 */
public class MainActivity extends Activity {

	/** The current measure. */
	private String currentMeasure = "";

	/** The left unit type. */
	private String leftUnitType = "";

	/** The right unit type. */
	private String rightUnitType = "";

	/** The right unit spinner. */
	private Spinner spinner, leftUnitSpinner, rightUnitSpinner;

	/** The to unit txt. */
	private EditText fromUnitTxt, toUnitTxt;

	/** The obj right. */
	private Object objLeft, objRight;

	/** The adapter. */
	private LabelValueAdapter adapter;

	/** The unit array str. */
	String[] unitArrayStr = { "WEIGHT", "LENGTH", "AREA", "ENERGY", "TEMPERATURE", "SPEED", "PRESSURE", "POWER",
			"VOLUME", "TIME" };

	private String oldFrom, oldTo;

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		spinner = (Spinner) findViewById(R.id.spinner);
		leftUnitSpinner = (Spinner) findViewById(R.id.spinnerLeft);
		rightUnitSpinner = (Spinner) findViewById(R.id.spinnerRight);
		fromUnitTxt = (EditText) findViewById(R.id.fromUnitTxt);
		// fromUnitTxt.setText("");
		toUnitTxt = (EditText) findViewById(R.id.toUnitTxt);
		// toUnitTxt.setText("");
		// ==========================================
		// General adapter
		// ==========================================
		ArrayAdapter<String> strAdapter = new ArrayAdapter<>(this, R.layout.spinner_txtview, unitArrayStr);
		spinner.setAdapter(strAdapter);
		spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				currentMeasure = (String) parent.getItemAtPosition(position);
				populateLeftRightSpinners();
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
			}
		});
		// ---------------------------------------------------------
		leftUnitSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				objLeft = parent.getItemAtPosition(position); // TODO
				// calculate();
				// toUnitTxt.setText(calculateResult(fromUnitTxt.getText()));
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
			}
		});
		rightUnitSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				// TODO: Uncomment later
				objRight = parent.getItemAtPosition(position);
				// calculate();
				// fromUnitTxt.setText(calculateResult(toUnitTxt.getText()));

			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
				System.out.print("Nothing");
			}
		});
		// ---------------------------------------------------------
		toUnitTxt.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				toUnitTxt.setText("");
				// System.out.println("old From field value: " + oldFrom);
				if (null != fromUnitTxt.getText() && fromUnitTxt.getText().length() != 0) {
					if (null == toUnitTxt.getText() || toUnitTxt.getText().length() == 0) {
						calculateTo();
						return true;
					}
				}
				return false;
			}
		});

	}

	/**
	 * Calculate.
	 */

	private void calculateTo() {
		CharSequence cs = fromUnitTxt.getText();
		if (cs.length() > 0) {
			StringBuffer sb = new StringBuffer(cs);
			String result = getConvertedUnitValueAsText(sb.toString());
			toUnitTxt.setText("");
			toUnitTxt.append(result);
			toUnitTxt.setTextIsSelectable(true);
			oldTo = toUnitTxt.getText().toString();
		}
	}

	/**
	 * Populate left right spinners.
	 */
	// TODO
	private void populateLeftRightSpinners() {
		if (currentMeasure.equals("WEIGHT")) {
			ArrayAdapter<WeightTypeEnum> strAdapter = new ArrayAdapter<>(this, R.layout.spinner_txtview,
					WeightTypeEnum.values());
			leftUnitSpinner.setAdapter(strAdapter);
			rightUnitSpinner.setAdapter(strAdapter);
		} else if (currentMeasure.equals("LENGTH")) {
			ArrayAdapter<LengthTypeEnum> strAdapter = new ArrayAdapter<>(this, R.layout.spinner_txtview,
					LengthTypeEnum.values());
			leftUnitSpinner.setAdapter(strAdapter);
			rightUnitSpinner.setAdapter(strAdapter);
			// ---------------------------------------------------------------------
			// adapter = new LabelValueAdapter(this, R.layout.custom_spinner);
			// for (LengthTypeEnum s : LengthTypeEnum.values()) {
			// LabelValue lv = new LabelValue(s.getLabel(), s.name());
			// adapter.add(lv);
			// }
			// leftUnitSpinner.setAdapter(adapter);
			// rightUnitSpinner.setAdapter(adapter);
			// ---------------------------------------------------------------------
		} else if (currentMeasure.equals("SPEED")) {
			adapter = new LabelValueAdapter(this, R.layout.spinner_txtview);
			// adapter.clear();
			for (SpeedTypeEnum s : SpeedTypeEnum.values()) {
				LabelValue lv = new LabelValue(s.getLabel(), s.getValue());
				adapter.add(lv);
			}
			leftUnitSpinner.setAdapter(adapter);
			rightUnitSpinner.setAdapter(adapter);
		} else {
			leftUnitSpinner.setAdapter(null);
			rightUnitSpinner.setAdapter(null);
		}
	}

	/**
	 * Gets the converted unit value as text.
	 *
	 * @param sourceUnitAsText
	 *            the source unit as text
	 * @return the converted unit value as text
	 */

	private String getConvertedUnitValueAsText(String sourceUnitAsText) {
		if (null == sourceUnitAsText) {
			Toast.makeText(getApplicationContext(), "Value is empty", Toast.LENGTH_SHORT).show();
			return "";
		}

		sourceUnitAsText = sourceUnitAsText.trim();

		BigDecimal sourceAmount = new BigDecimal(sourceUnitAsText);
		BigDecimal targetAmount = BigDecimal.ZERO;

		String output = "";
		if (currentMeasure.equals("WEIGHT")) {
			WeightTypeEnum sourceEnum = (WeightTypeEnum) objLeft;
			WeightTypeEnum targetEnum = (WeightTypeEnum) objRight;
			targetAmount = WeightManager.getConvertedAmount(sourceEnum, targetEnum, sourceAmount);
			output = sourceAmount + " " + sourceEnum.name() + " = " + targetAmount + " " + targetEnum;
		} else if (currentMeasure.equals("LENGTH")) {
			LengthTypeEnum sourceEnum = (LengthTypeEnum) objLeft;
			LengthTypeEnum targetEnum = (LengthTypeEnum) objRight;
			targetAmount = LengthManager.getConvertedAmount(sourceEnum, targetEnum, sourceAmount);
			output = sourceAmount + " " + sourceEnum.name() + " = " + targetAmount + " " + targetEnum;
		} else if (currentMeasure.equals("SPEED")) {
			LabelValue left = (LabelValue) objLeft;
			LabelValue right = (LabelValue) objRight;
			// output = "From " + left.getLabel() + " To " + right.getLabel();
			// targetAmount = BigDecimal.ZERO;
			SpeedTypeEnum sourceEnum = SpeedTypeEnum.getName(left.getValue());
			SpeedTypeEnum targetEnum = SpeedTypeEnum.getName(right.getValue());
			targetAmount = SpeedManager.getConvertedAmount(sourceEnum, targetEnum, sourceAmount);
			output = sourceAmount + " " + sourceEnum.name() + " = " + targetAmount + " " + targetEnum;
		}
		// Toast.makeText(getApplicationContext(), output,
		// Toast.LENGTH_SHORT).show();
		return targetAmount.toPlainString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onOptionsItemSelected(android.view.MenuItem)
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
