package com.example.assignment_1_alpha;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;


public class SingleDatePickerDialog extends AlertDialog implements OnClickListener, OnDateChangedListener {

	private static final String START_YEAR = "start_year";

	private static final String START_MONTH = "start_month";

	private static final String START_DAY = "start_day";


	private final DatePicker mDatePicker_start;

	private final OnDateSetListener mCallBack;


	public interface OnDateSetListener {

		
		void onDateSet(DatePicker startDatePicker, int startYear, int startMonthOfYear, int startDayOfMonth);
	}

	
	public SingleDatePickerDialog(Context context, OnDateSetListener callBack, int year, int monthOfYear, int dayOfMonth) {
		this(context, 0, callBack, year, monthOfYear, dayOfMonth);
	}

	public SingleDatePickerDialog(Context context, int theme, OnDateSetListener callBack, int year, int monthOfYear,
			int dayOfMonth) {
		this(context, 0, callBack, year, monthOfYear, dayOfMonth, true);
	}

	
	public SingleDatePickerDialog(Context context, int theme, OnDateSetListener callBack, int year, int monthOfYear,
			int dayOfMonth, boolean isDayVisible) {
		super(context, theme);

		mCallBack = callBack;

		Context themeContext = getContext();
		setButton(BUTTON_POSITIVE, "Confirm", this);
		setButton(BUTTON_NEGATIVE, "Cancel", this);

		setIcon(0);

		LayoutInflater inflater = (LayoutInflater) themeContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.single_date_dialog, null);
		setView(view);
		mDatePicker_start = (DatePicker) view.findViewById(R.id.expense_datePickerStart);
		
		mDatePicker_start.init(year, monthOfYear, dayOfMonth, this);
		

	}

	public void onClick(DialogInterface dialog, int which) {

		if (which == BUTTON_POSITIVE)
			tryNotifyDateSet();
	}

	@Override
	public void onDateChanged(DatePicker view, int year, int month, int day) {
		if (view.getId() == R.id.expense_datePickerStart)
			mDatePicker_start.init(year, month, day, this);
	

	}

	public DatePicker getDatePickerStart() {
		return mDatePicker_start;
	}



	public void updateStartDate(int year, int monthOfYear, int dayOfMonth) {
		mDatePicker_start.updateDate(year, monthOfYear, dayOfMonth);
	}


	
	private void tryNotifyDateSet() {
		if (mCallBack != null) {
			mDatePicker_start.clearFocus();
			
			mCallBack.onDateSet(mDatePicker_start, mDatePicker_start.getYear(), mDatePicker_start.getMonth(),
					mDatePicker_start.getDayOfMonth());
		}
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	@Override
	public Bundle onSaveInstanceState() {
		Bundle state = super.onSaveInstanceState();
		state.putInt(START_YEAR, mDatePicker_start.getYear());
		state.putInt(START_MONTH, mDatePicker_start.getMonth());
		state.putInt(START_DAY, mDatePicker_start.getDayOfMonth());		
		return state;
	}

	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		int start_year = savedInstanceState.getInt(START_YEAR);
		int start_month = savedInstanceState.getInt(START_MONTH);
		int start_day = savedInstanceState.getInt(START_DAY);
		mDatePicker_start.init(start_year, start_month, start_day, this);

	}
}
