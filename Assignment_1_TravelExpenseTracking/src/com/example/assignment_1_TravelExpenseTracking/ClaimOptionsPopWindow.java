package com.example.assignment_1_TravelExpenseTracking;

import com.example.assignment_1_alpha.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.Toast;

public class ClaimOptionsPopWindow extends PopupWindow {
/*
 * I removed the title bar so there's no menu button on right top corner, then I used object from this Class 
 * as new menu options. 
 */
	
	private View mMenuView;
	private Button add_claim_button;
	private Button info_button;

	public ClaimOptionsPopWindow(final Activity context, OnClickListener itemsOnClick) {
		super(context);
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mMenuView = inflater.inflate(R.layout.claim_options_window, null);
		add_claim_button = (Button) mMenuView.findViewById(R.id.add_claim_button);
		info_button = (Button) mMenuView.findViewById(R.id.info_button);
		add_claim_button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(context, ClaimActivity.class);
				context.startActivity(intent);
				dismiss();
			}
		});
		info_button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Toast.makeText(context, "Mingtuo Li\nGitHub: https://github.com/FadedWill", Toast.LENGTH_LONG).show();
				dismiss();
			}
		});
		int w = context.getWindowManager().getDefaultDisplay().getWidth();
		this.setContentView(mMenuView);
		this.setFocusable(true);
		this.setHeight(LayoutParams.WRAP_CONTENT);
		this.setWidth(w / 2 + 50);
		this.setAnimationStyle(R.style.mystyle); // animation files are in the anim folder under res
		ColorDrawable cd = new ColorDrawable(0);
		this.setBackgroundDrawable(cd);
		mMenuView.setOnTouchListener(new OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				int height = mMenuView.findViewById(R.id.pop_layout).getTop();
				int y = (int) event.getY();
				if (event.getAction() == MotionEvent.ACTION_UP) {
					if (y < height) {
						dismiss();
					}
				}
				return true;
			}
		});
	}
}