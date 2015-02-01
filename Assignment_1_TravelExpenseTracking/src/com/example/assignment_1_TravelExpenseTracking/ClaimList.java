package com.example.assignment_1_TravelExpenseTracking;

import java.util.ArrayList;

public class ClaimList {

/*
 * ClaimList is the type I choose to save in file. So the outline of data saving is
 * ClaimList<Claim1<ExpenseItem1,ExpenseItem2>,Claim2<ExpenseItem1,ExpenseItem2,ExpenseItem3>,...>;
 * In this way we can easily change data through index of ArrayList
 */
	
	private ArrayList<Claim> claimList;
	
	public ClaimList(){
		this.claimList=new ArrayList<Claim>();
	}

	public ArrayList<Claim> getClaimList() {
		return claimList;
	}

	public void setClaimList(ArrayList<Claim> claimList) {
		this.claimList = claimList;
	}
	
	public void addClaim(Claim newClaim){
		this.claimList.add(newClaim);
	}
	
	public void removeClaimByIndex(int index){
		this.claimList.remove(index);
	}
}
