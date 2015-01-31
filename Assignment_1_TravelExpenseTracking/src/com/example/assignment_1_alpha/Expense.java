package com.example.assignment_1_alpha;

import java.util.Calendar;

public class Expense {

	private String expenseDate;
	private Calendar expenseDateCalendar;
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
	public Calendar getExpenseDateCalendar() {
		return expenseDateCalendar;
	}
	public void setExpenseDateCalendar(Calendar expenseDateCalendar) {
		this.expenseDateCalendar = expenseDateCalendar;
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