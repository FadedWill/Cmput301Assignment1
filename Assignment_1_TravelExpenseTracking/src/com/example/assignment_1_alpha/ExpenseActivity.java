package com.example.assignment_1_alpha;

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
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class ExpenseActivity extends Activity {

	int[] categoryIds = { R.string.category_air_fare, R.string.category_ground_transport,
			R.string.category_vehicle_rental, R.string.category_fuel, R.string.category_parking,
			R.string.category_registration, R.string.category_accommodation, R.string.category_meal };
	int[] currencyIds = { R.string.currency_CAD, R.string.currency_USD, R.string.currency_EUR, R.string.currency_GBP,
			R.string.currency_CNY, R.string.currency_JPY };

	private int claim_position;
	private String claimIntentTrans;
	private int expense_position;
	private String expenseIntentTrans;

	static private boolean expenseEditFlag = false;

	private ClaimList Data;
	private static final String FILENAME = "data.sav";
	private Expense existExpense;

	private TextView tvn;
	private TextView tvn1;
	private boolean tvn_flag;
	private boolean tvn1_flag;

	private Spinner category_sp;
	private Spinner currency_sp;
	private EditText amount;
	private EditText description;
	private TextView expense_date;
	private Button expense_cancel;
	private Button expense_submit;
	// --------------------------------
	private String expenseCateogry;
	private String expenseDate;
	private Calendar expenseDateP;
	private int expenseAmount;
	private String expenseCurrency;
	private String expenseDescription;

	private void init() {

		Data = this.loadFromFile();

		tvn_flag = false;
		tvn1_flag = false;

		amount = (EditText) findViewById(R.id.amount_edit);
		amount.setText("0");
		amount.setFocusable(true);
		amount.setFocusableInTouchMode(true);
		description = (EditText) findViewById(R.id.expense_description);
		expense_date = (TextView) findViewById(R.id.expense_date);
		expense_cancel = (Button) findViewById(R.id.new_expense_cancel);
		expense_submit = (Button) findViewById(R.id.new_expense_submit);
		category_sp = (Spinner) findViewById(R.id.expense_category_spinner);
		currency_sp = (Spinner) findViewById(R.id.expense_currency_spinner);
		
		Intent intentTrans = getIntent();
		claimIntentTrans = intentTrans.getStringExtra("claim_position");
		claim_position = Integer.parseInt(claimIntentTrans);
		if (expenseEditFlag == true) {
			
			expenseIntentTrans = intentTrans.getStringExtra("expense_position");
			expense_position = Integer.parseInt(expenseIntentTrans);

			tvn_flag = true;
			tvn1_flag = true;

			existExpense = Data.getClaimList().get(claim_position).getExpenseList().get(expense_position);
			expense_date.setText(existExpense.getExpenseDate());
			expense_date.setTextColor(getResources().getColor(R.color.black));
			amount.setText(Integer.toString(existExpense.getAmount()));
			description.setText(existExpense.getEpxenseDescription());
		}

		BaseAdapter category_ba = new BaseAdapter() {

			@Override
			public int getCount() {
				// TODO Auto-generated method stub
				return 8;
			}

			@Override
			public Object getItem(int position) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public long getItemId(int position) {
				// TODO Auto-generated method stub
				return 0;
			}

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				// TODO Auto-generated method stub
				LinearLayout category_ll = new LinearLayout(ExpenseActivity.this);
				category_ll.setOrientation(LinearLayout.HORIZONTAL);
				TextView tv = new TextView(ExpenseActivity.this);
				tv.setText(" " + getResources().getText(categoryIds[position]));
				category_ll.addView(tv);
				return category_ll;
			}
		};

		category_sp.setAdapter(category_ba);
		category_sp.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				LinearLayout category_ll = (LinearLayout) view;
				tvn = (TextView) category_ll.getChildAt(0);
				tvn.setTextSize(15);
				tvn.setTextColor(getResources().getColor(R.color.black));
				if (tvn_flag) {
					tvn.setText(existExpense.getCategory());
					tvn_flag = false;
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
			}
		});

		BaseAdapter currency_ba = new BaseAdapter() {

			@Override
			public int getCount() {
				// TODO Auto-generated method stub
				return 6;
			}

			@Override
			public Object getItem(int position) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public long getItemId(int position) {
				// TODO Auto-generated method stub
				return 0;
			}

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				// TODO Auto-generated method stub
				LinearLayout currency_ll = new LinearLayout(ExpenseActivity.this);
				currency_ll.setOrientation(LinearLayout.HORIZONTAL);
				TextView tv1 = new TextView(ExpenseActivity.this);
				tv1.setText(" " + getResources().getText(currencyIds[position]));
				currency_ll.addView(tv1);
				return currency_ll;
			}
		};
		currency_sp.setAdapter(currency_ba);
		currency_sp.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub

				LinearLayout currency_ll = (LinearLayout) view;
				tvn1 = (TextView) currency_ll.getChildAt(0);
				tvn1.setTextSize(20);
				tvn1.setTextColor(getResources().getColor(R.color.black));
				if (tvn1_flag) {
					tvn1.setText(existExpense.getCurrency());
					tvn1_flag = false;
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub

			}
		});

		expense_date.setOnClickListener(new View.OnClickListener() {
			Calendar c = Calendar.getInstance();

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				new SingleDatePickerDialog(ExpenseActivity.this, 0, new SingleDatePickerDialog.OnDateSetListener() {

					@Override
					public void onDateSet(DatePicker startDatePicker, int startYear, int startMonthOfYear,
							int startDayOfMonth) {
						String textString_start = String.format("%d-%d-%d", startYear, startMonthOfYear + 1,
								startDayOfMonth);
						expense_date.setText(textString_start);
						expense_date.setTextColor(getResources().getColor(R.color.black));
						expenseDateP = Calendar.getInstance();
						expenseDateP.set(startYear, startMonthOfYear + 1, startDayOfMonth);
					}
				}, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DATE), true).show();
			}
		});

		expense_cancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				
				onBackPressed();
			}
		});

		expense_submit.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				expenseDate = expense_date.getText().toString();
				expenseCateogry = tvn.getText().toString();
				expenseAmount = Integer.parseInt(amount.getText().toString());
				expenseCurrency = tvn1.getText().toString();
				expenseDescription = String.valueOf(description.getText().toString());

				if(expenseDate.equals("")||expenseAmount<=0){
					Toast.makeText(ExpenseActivity.this, "Information is not completed\n"+"             Submit Denied", Toast.LENGTH_SHORT).show();
					return;
				}
				
				if (expenseEditFlag) {
					existExpense.setCategory(expenseCateogry);
					existExpense.setExpenseDate(expenseDate);
					existExpense.setAmount(expenseAmount);
					existExpense.setCurrency(expenseCurrency);
					existExpense.setEpxenseDescription(expenseDescription);
					existExpense.setExpenseDateCalendar(expenseDateP);
					//Data.getClaimList().get(claim_position).generateMoneyMap();
					expenseEditFlag=false;
				} else {
					Expense newExpense = new Expense();
					newExpense.setCategory(expenseCateogry);
					newExpense.setExpenseDate(expenseDate);
					newExpense.setAmount(expenseAmount);
					newExpense.setCurrency(expenseCurrency);
					newExpense.setEpxenseDescription(expenseDescription);
					newExpense.setExpenseDateCalendar(expenseDateP);
					Claim parentClaim = Data.getClaimList().get(claim_position);
					parentClaim.addExpense(newExpense);
					//parentClaim.generateMoneyMap();
				}
				Collections.sort(Data.getClaimList().get(claim_position).getExpenseList(),new Comparator<Expense>(){
					public int compare(Expense expense_1,Expense expense_2){
						return expense_1.getExpenseDate().compareTo(expense_2.getExpenseDate());
					}
				});
				saveInFile();
				Intent addExpenseDone = new Intent();
				addExpenseDone.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				addExpenseDone.setClass(ExpenseActivity.this, MainActivity.class);
				startActivity(addExpenseDone);
			}
		});
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_expense);
		init();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.expense, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar wicategory_ll
		// automaticacategory_lly handle clicks on the Home/Up button, so long
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

	public static boolean getExpenseEditFlag() {
		return expenseEditFlag;
	}

	public static void setExpenseEditFlag(boolean expenseEditFlag) {
		ExpenseActivity.expenseEditFlag = expenseEditFlag;
	}
	
	public void onBackPressed(){
		new AlertDialog.Builder(this).setTitle("Cancel Edit").setMessage("Are you sure to back without saving?").
		setPositiveButton("Yes",new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				tvn_flag = false;
				tvn1_flag = false;
				expenseEditFlag = false;
				Intent backIntent=new Intent();
				backIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				backIntent.setClass(ExpenseActivity.this,MainActivity.class);
				startActivity(backIntent);
			}
		}).setNegativeButton("No", null).create().show();
	}
}