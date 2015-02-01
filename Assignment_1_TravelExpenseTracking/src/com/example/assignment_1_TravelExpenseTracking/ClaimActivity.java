package com.example.assignment_1_TravelExpenseTracking;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;

import com.example.assignment_1_alpha.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ClaimActivity extends Activity {
/*
 * This activity controls the add and edit functions of claim
 */
	static private boolean editFlag = false; // if this flag equals to true, value will be set on proper views
	
	private boolean dateChanged=false; // In case of null pointer exception happens due to no calendar variable exists

	private String startDate; // values will be saved into Claim object
	private String endDate;
	private Calendar startDateP;
	private Calendar endDateP;
	private String destination;
	private String description;

	private int claim_position; // get value from intent
	private String claimIntentTrans;
	// --------------------------------------------------------------
	private EditText claim_destination; // corresponding views of each attribute
	private EditText claim_description;
	private TextView start_view;
	private TextView end_view;
	private Button new_claim_done;
	private Button new_claim_cancel;
	
	private ClaimList Data; // same as in MainActivity
	private Claim existClaim; // if claim is being edited, we need an object to store it temporarily
	private static final String FILENAME = "data.sav";

	private void init() {
		//find view for each variable
		claim_destination = (EditText) findViewById(R.id.claim_destination);
		claim_description = (EditText) findViewById(R.id.claim_description);
		start_view = (TextView) findViewById(R.id.startDate_view);
		end_view = (TextView) findViewById(R.id.endDate_view);
		new_claim_done = (Button) findViewById(R.id.new_claim_submit);
		new_claim_cancel = (Button) findViewById(R.id.new_claim_cancel);

		Data = this.loadFromFile();

		if (editFlag == true) { // check editflag then set values to corresponding views
			Intent intentTrans = getIntent();
			claimIntentTrans = intentTrans.getStringExtra("claim_position");
			claim_position = Integer.parseInt(claimIntentTrans);

			existClaim = Data.getClaimList().get(claim_position);
			start_view.setText(existClaim.getStartDate());
			start_view.setTextColor(getResources().getColor(R.color.black));
			end_view.setText(existClaim.getEndDate());
			end_view.setTextColor(getResources().getColor(R.color.black));
			claim_destination.setText(existClaim.getDestination());
			claim_description.setText(existClaim.getDescription());
		}

		new_claim_done.setOnClickListener(new View.OnClickListener() {
			// try to save file if edit or add done
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				destination = claim_destination.getText().toString();
				description = claim_description.getText().toString();
				startDate = start_view.getText().toString();
				endDate = end_view.getText().toString();
				
				//check no empty input
				if(destination.equals("")||startDate.equals("")||endDate.equals("")){
					Toast.makeText(ClaimActivity.this, "Only description can be empty\n"+"               Submit Denied", Toast.LENGTH_SHORT).show();
					return;
				}
				
				//then check is end date later than start date
				if(dateChanged){
					if(startDateP.after(endDateP)){
						Toast.makeText(ClaimActivity.this, "Invalid date input\n"+"  Submit Denied", Toast.LENGTH_SHORT).show();
						dateChanged=false;
						return;
					}
				}
							
				if (editFlag == true) { // if claim is being edit we just change its value
					existClaim.setStartDate(startDate);
					existClaim.setEndDate(endDate);
					existClaim.setDestination(destination);
					existClaim.setDescription(description);
					existClaim.setStartDateCalendar(startDateP);
					existClaim.setEndDateCalendar(endDateP);
					editFlag=false;
				} else { // else we create a new claim and add into ClaimList object
					Claim newClaim = new Claim(destination);
					newClaim.setStartDate(startDate);
					newClaim.setEndDate(endDate);
					newClaim.setDescription(description);
					newClaim.setStatus("In Progress");
					newClaim.setStartDateCalendar(startDateP);
					newClaim.setEndDateCalendar(endDateP);
					Data.addClaim(newClaim);
				}
				// get start date then compare and sort before save in file
				Collections.sort(Data.getClaimList(),new Comparator<Claim>(){
					public int compare(Claim claim_1,Claim claim_2){
						return claim_1.getStartDate().compareTo(claim_2.getStartDate());
					}
				});
				saveInFile();
				Intent Done = new Intent(); // back to MainActivity
				Done.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // clear extra activities when call finish()
				Done.setClass(ClaimActivity.this, MainActivity.class);
				ClaimActivity.this.startActivity(Done);

			}
		});

		new_claim_cancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// make this button same as back button
				onBackPressed();
			}
		});

		start_view.setOnClickListener(new View.OnClickListener() {
			Calendar c = Calendar.getInstance();
			// bind datepicker on textview, there's no difference between clicking start_view and end_view
			// after user confirm time the date will be set on textview
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				new DoubleDatePickerDialog(ClaimActivity.this, 0, new DoubleDatePickerDialog.OnDateSetListener() {

					@Override
					public void onDateSet(DatePicker startDatePicker, int startYear, int startMonthOfYear,
							int startDayOfMonth, DatePicker endDatePicker, int endYear, int endMonthOfYear,
							int endDayOfMonth) {
						String textString_start = String.format("%d-%d-%d", startYear, startMonthOfYear + 1,
								startDayOfMonth);
						String textString_end = String.format("%d-%d-%d", endYear, endMonthOfYear + 1, endDayOfMonth);
						start_view.setText(textString_start);
						start_view.setTextColor(getResources().getColor(R.color.black));
						end_view.setText(textString_end);
						end_view.setTextColor(getResources().getColor(R.color.black));
						startDateP = Calendar.getInstance();
						startDateP.set(startYear, startMonthOfYear + 1, startDayOfMonth);
						endDateP = Calendar.getInstance();
						endDateP.set(endYear, endMonthOfYear + 1, endDayOfMonth);
						dateChanged=true;
					}
				}, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DATE), true).show();
			}
		});

		end_view.setOnClickListener(new View.OnClickListener() {
			Calendar c = Calendar.getInstance();

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				new DoubleDatePickerDialog(ClaimActivity.this, 0, new DoubleDatePickerDialog.OnDateSetListener() {

					@Override
					public void onDateSet(DatePicker startDatePicker, int startYear, int startMonthOfYear,
							int startDayOfMonth, DatePicker endDatePicker, int endYear, int endMonthOfYear,
							int endDayOfMonth) {
						String textString_start = String.format("%d-%d-%d", startYear, startMonthOfYear + 1,
								startDayOfMonth);
						String textString_end = String.format("%d-%d-%d", endYear, endMonthOfYear + 1, endDayOfMonth);
						start_view.setText(textString_start);
						start_view.setTextColor(getResources().getColor(R.color.black));
						end_view.setText(textString_end);
						end_view.setTextColor(getResources().getColor(R.color.black));
						startDateP = Calendar.getInstance();
						startDateP.set(startYear, startMonthOfYear + 1, startDayOfMonth);
						endDateP = Calendar.getInstance();
						endDateP.set(endYear, endMonthOfYear + 1, endDayOfMonth);
						dateChanged=true;
					}
				}, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DATE), true).show();
			}
		});
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_claim);
		init();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.claim, menu);
		return true;
	}

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

	// load and save are exactly same in every activity
	private ClaimList loadFromFile() {
		Gson gson = new Gson();
		Data = new ClaimList();
		try {
			FileInputStream fis = openFileInput(FILENAME);
			InputStreamReader in = new InputStreamReader(fis);
			Type typeOfT = new TypeToken<ClaimList>() {
			}.getType();
			Data = gson.fromJson(in, typeOfT);
			fis.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return Data;
	}

	private void saveInFile() {
		Gson gson = new Gson();
		try {
			FileOutputStream fos = openFileOutput(FILENAME, 0);
			OutputStreamWriter osw = new OutputStreamWriter(fos);
			gson.toJson(Data, osw);
			osw.flush();
			fos.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static boolean getEditFlag() {
		return editFlag;
	}

	public static void setEditFlag(boolean editFlag) {
		ClaimActivity.editFlag = editFlag;
	}


	public void onBackPressed(){
		new AlertDialog.Builder(this).setTitle("Cancel Edit").setMessage("Are you sure to back without saving?").
		setPositiveButton("Yes",new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				editFlag=false; // every time make sure editFlag is false before back
				Intent backIntent=new Intent();
				backIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // also call MainActivity clear extra activities
				backIntent.setClass(ClaimActivity.this,MainActivity.class);
				startActivity(backIntent);
			}
		}).setNegativeButton("No", null).create().show();
	}
	
}
