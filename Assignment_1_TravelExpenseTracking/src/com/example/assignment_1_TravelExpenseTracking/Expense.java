package com.example.assignment_1_TravelExpenseTracking;

public class Expense { // only about setters and getters
/*
 * By the way, I already set spinner for category, description of expense item can be also seen, so the name
 * of expense item is kind of useless then I remove it
 */
	private String expenseDate;
	private String category;
	private int amount;
	private String currency;
	private String epxenseDescription;
	
	public String getExpenseDate() {
		return expenseDate;
	}
	public void setExpenseDate(String expenseDate) {
		this.expenseDate = expenseDate;
	}	
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public int getAmount() {
		return amount;
	}
	public void setAmount(int amount) {
		this.amount = amount;
	}

	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public String getEpxenseDescription() {
		return epxenseDescription;
	}
	public void setEpxenseDescription(String epxenseDescription) {
		this.epxenseDescription = epxenseDescription;
	}
}