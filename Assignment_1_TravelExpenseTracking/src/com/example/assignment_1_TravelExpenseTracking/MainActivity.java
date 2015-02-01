package com.example.assignment_1_TravelExpenseTracking;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.assignment_1_TravelExpenseTracking.MainScreenAdapter;
import com.example.assignment_1_alpha.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

public class MainActivity extends Activity implements OnItemClickListener, OnItemLongClickListener {

	private ImageView claim_options; // pop window which is bind to main activity
	static ClaimOptionsPopWindow menuWindow; // menu that contains options of adding new claim, or checking my info

	private ListView mListView; // listview on main screen that can show all claims and expense items
	private MainScreenAdapter mAdapter; // adapter for listview
	private Dialog dialog; // this dialog only shows when item is being long clicked, also it's associated with status

	private int claim_position; // I store data with ArrayList, but show them with List<Map>, so the index of claim -
								//or expense item in save file is different from index on screen, then I need to -
								// get correct index every time and save it with this variable
	private String claimPositionName = "claim_position"; // intent mark
	private int expense_position; // same as claim_position
	private String expensePositionName = "expense_position"; // intent mark

	private ClaimList Data; // used for storing data from sav file during activity

	private static final String FILENAME = "data.sav"; // sav file name

	private List<Map<String, Object>> dataList; // the list shows out all claims and expense items

	
	// init() is used for initializing during onCreate() function
	private void init() {
		Data = this.loadFromFile(); //load data every time when back to main activity, because most times items-
									//on screen will be changed so I load data and put them on listview again

		dialog = new Dialog(MainActivity.this); // dialog controls access of different status 

		claim_options = (ImageView) findViewById(R.id.claim_options); //pop up menu window

		mListView = (ListView) findViewById(R.id.listView_main); // main listview
		mListView.setOnItemClickListener(this);
		mListView.setOnItemLongClickListener(this);
		mListView.setAdapter(mAdapter = getAdapter()); //getAdapter() loads every item into listview

		claim_options.setAlpha(130); // make sure that menu button does not overlap background
		claim_options.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				uploadImage(MainActivity.this); // bind menu window to this activity
			}
		});
	}

	private MainScreenAdapter getAdapter() {
		// form claim is "title", "status", "amount", "date"; but for expense item it's actually "category", -
		// "date","description" and "amount"
		String[] from = { "title", "status", "amount", "date" };
		int[] to = { R.id.item_text, R.id.item_text1, R.id.item_text2, R.id.item_text3 };
		return new MainScreenAdapter(this, getListData(), R.layout.activity_main_sublist, from, to);
	}

	// first we only add claims into dataList, then get their own expense items when they are being clicked
	private List<Map<String, Object>> getListData() {
		dataList = new ArrayList<Map<String, Object>>();
		int claimAmount = Data.getClaimList().size();
		for (int i = 0; i < claimAmount; i++) {
			Claim tempClaim = Data.getClaimList().get(i);
			Map<String, Object> claim_item = new HashMap<String, Object>();
			claim_item.put(MainScreenAdapter.KEY_LEVEL, 1); // all claims are key_level 1
			claim_item.put(MainScreenAdapter.KEY_EXPANDED, false); // only claims can be expanded
			claim_item.put("title", tempClaim.getDestination()); // set values for list item
			claim_item.put("status", tempClaim.getStatus());
			if (!tempClaim.getExpenseList().isEmpty()) {
				StringBuffer tempSB = tempClaim.getClaimMoneySpent();
				String finalSB = tempSB.toString();
				claim_item.put("amount", finalSB);
			}
			claim_item.put("date", "From: " + tempClaim.getStartDate() + "\n" + "To: " + tempClaim.getEndDate());
			dataList.add(claim_item);
		}
		return dataList; // all claims have been put into list
	}

	// the only function this method has is that shows all expense items when claim is being clicked
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		Map<String, Object> item = dataList.get(position);
		Integer levelCheck = (Integer) item.get(MainScreenAdapter.KEY_LEVEL);
		if (levelCheck == 1) { // as I mentioned, we need to get correct index through this way every time
			int claimCounter = -1;
			for (int i = 0; i <= position; i++) {
				Map<String, Object> parent_item = dataList.get(i);
				if ((Integer) parent_item.get(MainScreenAdapter.KEY_LEVEL) == 1) {
					claimCounter += 1;
				}
				claim_position = claimCounter;
			}
		}
		if (levelCheck == 1) { // if expense item is being clicked, nothing will happen
			boolean expanded = (Boolean) item.get(MainScreenAdapter.KEY_EXPANDED);
			if (!expanded) {
				int expenseItemsAmount = Data.getClaimList().get(claim_position).getExpenseList().size();
				if (expenseItemsAmount > 0) {
					// child here actually equals to claim's expenseList
					ArrayList<Map<String, Object>> child = new ArrayList<Map<String, Object>>();
					for (int k = 0; k < expenseItemsAmount; k++) {
						Expense tempExpense = Data.getClaimList().get(claim_position).getExpenseList().get(k);
						Map<String, Object> expense_item = new HashMap<String, Object>();
						expense_item.put(MainScreenAdapter.KEY_LEVEL, 2); // all expense items are level 2
						expense_item.put(MainScreenAdapter.KEY_EXPANDED, false); // they cannot be expanded
						expense_item.put("title", tempExpense.getCategory());
						expense_item.put("status", tempExpense.getExpenseDate());
						expense_item.put("amount", "  "+tempExpense.getEpxenseDescription());
						expense_item.put("date", tempExpense.getAmount() + tempExpense.getCurrency());
						child.add(expense_item);
					}
					mAdapter.addChildListData(child, position); // adds sublist of expense items after claim
				}
			} else {
				mAdapter.removeChildListData(position); // if claim is already expanded, expense items should-
														// be removed when click
			}
		}
	}

	/*
	 In onItemLongClick(), dialog will be set as different ContentView due to different status, and only returned-
	 and submitted status can provide access to edit. And every time when data changes, reset a new adapter to-
	 listview.
	 */
	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
		// TODO Auto-generated method stub

		Map<String, Object> item = dataList.get(position);
		Integer levelCheck = (Integer) item.get(MainScreenAdapter.KEY_LEVEL);
		// get correct index of claim again
		if (levelCheck == 1) {
			int claimCounter = -1;
			for (int i = 0; i <= position; i++) {
				Map<String, Object> parent_item = dataList.get(i);
				if ((Integer) parent_item.get(MainScreenAdapter.KEY_LEVEL) == 1) {
					claimCounter += 1;
				}
				claim_position = claimCounter;
			}
		} else if (levelCheck == 2) { // now we also need to get correct idnex for expense item
			int claimCounter = -1;
			for (int i = 0; i <= position; i++) {
				Map<String, Object> parent_item = dataList.get(i);
				if ((Integer) parent_item.get(MainScreenAdapter.KEY_LEVEL) == 1) {
					claimCounter += 1;
				}
				claim_position = claimCounter;
				int layout_pos = 0;
				for (int j = 0; j < position; j++) {
					Map<String, Object> layout_posItem = dataList.get(j);
					if ((String) layout_posItem.get("title") == Data.getClaimList().get(claim_position)
							.getDestination()) {
						layout_pos = j;
					}
				}
				expense_position = position - layout_pos - 1;
			}
		}

		if (levelCheck == 1) { // pop dialog for claim
			Data = loadFromFile();

			if (Data.getClaimList().get(claim_position).getStatus().equals("In Progress")
					|| Data.getClaimList().get(claim_position).getStatus().equals("Returned")) {
				// in this type of dialog user can access "edit" option
				dialog.setContentView(R.layout.claim_longclick_dialog);
				Button dialogAddnew = (Button) dialog.findViewById(R.id.claim_dialog_addNew);
				Button dialogSubmit = (Button) dialog.findViewById(R.id.claim_dialog_submit);
				Button dialogEdit = (Button) dialog.findViewById(R.id.claim_dialog_edit);
				Button dialogDelete = (Button) dialog.findViewById(R.id.claim_dialog_delete);
				dialog.show();

				dialogAddnew.setOnClickListener(new View.OnClickListener() {
					// button links to add new expense item
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						dialog.dismiss();
						Intent intentAddE = new Intent();
						intentAddE.putExtra(claimPositionName, Integer.toString(claim_position));
						intentAddE.setClass(MainActivity.this, ExpenseActivity.class);
						startActivity(intentAddE);
					}
				});

				dialogEdit.setOnClickListener(new View.OnClickListener() {
					// button links to edit pre-existed claim
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						ClaimActivity.setEditFlag(true);
						dialog.dismiss();
						Intent intentEdit = new Intent();
						intentEdit.putExtra(claimPositionName, Integer.toString(claim_position));
						intentEdit.setClass(MainActivity.this, ClaimActivity.class);
						startActivity(intentEdit);
					}
				});

				dialogSubmit.setOnClickListener(new View.OnClickListener() {
					// button links to change status to submitted
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						dialog.dismiss();
						if (!Data.getClaimList().get(claim_position).getExpenseList().isEmpty()) {
							Data.getClaimList().get(claim_position).setStatus("Submitted");
							saveInFile();
							Data = loadFromFile();
							mListView.setAdapter(mAdapter = getAdapter());
							mAdapter.notifyDataSetChanged();
						} else { // but claim without any expense item should not be able to submit
							Toast.makeText(MainActivity.this,
									"Cannot submit claim without expense\n" + "                 Action Denied.",
									Toast.LENGTH_SHORT).show();
						}

					}
				});

				dialogDelete.setOnClickListener(new View.OnClickListener() {
					// button links to delete pre-existed claim
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						dialog.dismiss();
						Data.getClaimList().remove(claim_position);
						saveInFile();
						Data = loadFromFile();
						mListView.setAdapter(mAdapter = getAdapter());
						mAdapter.notifyDataSetChanged();
					}
				});
			} else if (Data.getClaimList().get(claim_position).getStatus().equals("Submitted")) {
				// claim has been submitted is not editable now, but can be returned or approved, also can -
				// be sent as email
				dialog.setContentView(R.layout.claim_return_dialog);
				Button dialogEmail = (Button) dialog.findViewById(R.id.claim_returnEmail);
				Button dialogReturn = (Button) dialog.findViewById(R.id.claim_returnToReturn);
				Button dialogApprove = (Button) dialog.findViewById(R.id.claim_returnApprove);
				Button dialogDelete = (Button) dialog.findViewById(R.id.claim_returnDelete);
				dialog.show();

				dialogEmail.setOnClickListener(new View.OnClickListener() {
					// button links to email
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						dialog.dismiss();
						Intent intentEmail = new Intent();
						intentEmail.putExtra(claimPositionName, Integer.toString(claim_position));
						intentEmail.setClass(MainActivity.this, ClaimEmailActivity.class);
						startActivity(intentEmail);

					}
				});

				dialogReturn.setOnClickListener(new View.OnClickListener() {
					// button links to return
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						dialog.dismiss();
						Data.getClaimList().get(claim_position).setStatus("Returned");
						saveInFile();
						Data = loadFromFile();
						mListView.setAdapter(mAdapter = getAdapter());
						mAdapter.notifyDataSetChanged();
					}
				});
				dialogApprove.setOnClickListener(new View.OnClickListener() {
					// button links to approve
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						dialog.dismiss();
						Data.getClaimList().get(claim_position).setStatus("Approved");
						saveInFile();
						Data = loadFromFile();
						mListView.setAdapter(mAdapter = getAdapter());
						mAdapter.notifyDataSetChanged();
					}
				});
				dialogDelete.setOnClickListener(new View.OnClickListener() {
					// button links to delete
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						dialog.dismiss();
						Data.getClaimList().remove(claim_position);
						saveInFile();
						Data = loadFromFile();
						mListView.setAdapter(mAdapter = getAdapter());
						mAdapter.notifyDataSetChanged();
					}
				});

			} else if (Data.getClaimList().get(claim_position).getStatus().equals("Approved")) {
				// claim has been approved is not editable anymore, but user can still delete or send it out,
				// to make sure that user can still check description of it, so I add one more button here to
				// review comments with Toast
				dialog.setContentView(R.layout.claim_approve_dialog);
				Button dialogEmail = (Button) dialog.findViewById(R.id.claim_approve_email);
				Button dialogCheck = (Button) dialog.findViewById(R.id.claim_approve_checkDescription);
				Button dialogDelete = (Button) dialog.findViewById(R.id.claim_approve_delete);
				dialog.show();

				dialogEmail.setOnClickListener(new View.OnClickListener() {
					// button links to email
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						dialog.dismiss();
						Intent intentEmail = new Intent();
						intentEmail.putExtra(claimPositionName, Integer.toString(claim_position));
						intentEmail.setClass(MainActivity.this, ClaimEmailActivity.class);
						startActivity(intentEmail);
					}
				});
				dialogCheck.setOnClickListener(new View.OnClickListener() {
					// button links to check description
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						dialog.dismiss();
						Toast.makeText(MainActivity.this, Data.getClaimList().get(claim_position).getDescription(),
								Toast.LENGTH_LONG).show();
					}
				});
				dialogDelete.setOnClickListener(new View.OnClickListener() {
					// button links to delete
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						dialog.dismiss();
						Data.getClaimList().remove(claim_position);
						saveInFile();
						Data = loadFromFile();
						mListView.setAdapter(mAdapter = getAdapter());
						mAdapter.notifyDataSetChanged();
					}
				});
			}
		} else if (levelCheck == 2 && (!Data.getClaimList().get(claim_position).getStatus().equals("Approved"))
				&& (!Data.getClaimList().get(claim_position).getStatus().equals("Submitted"))) {
			// pop dialog for expense item
			dialog.setContentView(R.layout.expense_longclick_dialog);
			Button eDialogEdit = (Button) dialog.findViewById(R.id.expense_dialog_edit);
			Button eDialogDelete = (Button) dialog.findViewById(R.id.expense_dialog_delete);
			dialog.show();

			eDialogEdit.setOnClickListener(new View.OnClickListener() {
				// button links to edit
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					ExpenseActivity.setExpenseEditFlag(true);
					dialog.dismiss();
					Intent intentEditExpense = new Intent();
					intentEditExpense.putExtra(claimPositionName, Integer.toString(claim_position));
					intentEditExpense.putExtra(expensePositionName, Integer.toString(expense_position));
					intentEditExpense.setClass(MainActivity.this, ExpenseActivity.class);
					startActivity(intentEditExpense);
				}
			});

			eDialogDelete.setOnClickListener(new View.OnClickListener() {
				// button links to delete
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					dialog.dismiss();
					Data.getClaimList().get(claim_position).getExpenseList().remove(expense_position);
					saveInFile();
					Data = loadFromFile();
					mListView.setAdapter(mAdapter = getAdapter());
					mAdapter.notifyDataSetChanged();
				}
			});
		}
		return true;
	}

	public void uploadImage(final Activity context) { // bind menu window to MainActivity
		menuWindow = new ClaimOptionsPopWindow(MainActivity.this, itemsOnClick);
		menuWindow
				.showAtLocation(MainActivity.this.findViewById(R.id.claim_options), Gravity.TOP | Gravity.END, 0, 285);
	}

	private OnClickListener itemsOnClick = new OnClickListener() { // remove menu window when nothing is clicked

		public void onClick(View v) {
			menuWindow.dismiss();
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		init(); // initialized things here
	}

	protected void OnStart() {
		super.onStart();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
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

	// loadFromFile() and saveInFile() are nearly the same as code in Lab, so nothing special
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

	// when user back to MainActivity, all other activities have been removed, so finish() can work
	public void onBackPressed() {
		new AlertDialog.Builder(this).setTitle("Exit Program").setMessage("Are you sure to quit program?")
				.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						MainActivity.this.finish();
					}
				}).setNegativeButton("No", null).create().show();
	}
}