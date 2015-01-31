package com.example.assignment_1_alpha;

import java.util.ArrayList;

public class ClaimList {
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
