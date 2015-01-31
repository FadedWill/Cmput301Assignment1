package com.example.assignment_1_alpha;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ClaimEmailActivity extends Activity {
	
	final Context context=this;
	
	private ClaimList Data;
	private static final String FILENAME = "data.sav";
	private Claim choosingClaim;
	private int claim_position;
	private String claimIntentTrans;
	
	private ArrayList<Expense> expenseList;
	//--------------------------
	private TextView emailPreview;
	private EditText emailAddressView;
	private Button confirmButton;
	private Button cancelButton;
	
	public void init(){
		
		Data=loadFromFile();
		
		Intent intentTrans = getIntent();
		claimIntentTrans = intentTrans.getStringExtra("claim_position");
		claim_position = Integer.parseInt(claimIntentTrans);
		
		choosingClaim=Data.getClaimList().get(claim_position);
		
		emailAddressView=(EditText)findViewById(R.id.claim_email_addressView);
		confirmButton=(Button)findViewById(R.id.claim_email_done);
		cancelButton=(Button)findViewById(R.id.claim_email_cancel);
		
		confirmButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent sendIntent=new Intent(Intent.ACTION_SEND);
				sendIntent.setType("plain/text");
				String[] content={emailAddressView.getText().toString()};
				sendIntent.putExtra(Intent.EXTRA_EMAIL, content);
				sendIntent.putExtra(Intent.EXTRA_SUBJECT, "Claim of trip in "+choosingClaim.getDestination());
				sendIntent.putExtra(Intent.EXTRA_TEXT, generateMail());
				try{
					startActivity(Intent.createChooser(sendIntent, "Choose a way to send E-mail"));
				}catch (android.content.ActivityNotFoundException e){
					Toast.makeText(ClaimEmailActivity.this, "Error happened: Cannot access any mail App", Toast.LENGTH_SHORT).show();
				}
				saveInFile();
			}
		});
		
		cancelButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onBackPressed();
			}
		});
	}
	
	protected void onStart(){
		super.onStart();
		Data=loadFromFile();
		choosingClaim=Data.getClaimList().get(claim_position);
		emailPreview=(TextView)findViewById(R.id.claim_email_Preview);
		emailPreview.setText(generateMail());
		emailPreview.setTextColor(getResources().getColor(R.color.black));
	}
	
	public String generateMail(){
		StringBuffer mailSB=new StringBuffer();
		mailSB.append("Travel Destination: "+choosingClaim.getDestination()+"\n");
		mailSB.append("Start Date: "+choosingClaim.getStartDate()+"\n");
		mailSB.append("End Date: "+choosingClaim.getEndDate()+"\n");
		mailSB.append("Total Expense: "+choosingClaim.getClaimMoneySpent().toString()+"\n");
		mailSB.append("Description:\n"+"<"+choosingClaim.getDescription()+">\n\n");
		
		expenseList=choosingClaim.getExpenseList();
		for(int i=0;i<expenseList.size();i++){
			mailSB.append("¡ñ  Expense Item "+Integer.toString(i+1)+": "+expenseList.get(i).getCategory()+"\n");
			mailSB.append("   -Date: "+expenseList.get(i).getExpenseDate()+"\n");
			mailSB.append("   -Amount Spend: "+Integer.toString(expenseList.get(i).getAmount())+expenseList.get(i).getCurrency()+"\n");
			mailSB.append("   -Description:\n"+"    <"+expenseList.get(i).getEpxenseDescription()+">\n\n");
		}		
		return mailSB.toString();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_claim_email);
		init();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.claim_email, menu);
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

	// save claim list from file
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
	
	public void onBackPressed(){
		new AlertDialog.Builder(this).setTitle("Cancel Email").setMessage("Are you sure to back now?").
		setPositiveButton("Yes",new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				Intent backIntent=new Intent();
				backIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				backIntent.setClass(ClaimEmailActivity.this,MainActivity.class);
				startActivity(backIntent);
			}
		}).setNegativeButton("No", null).create().show();
	}	
}
