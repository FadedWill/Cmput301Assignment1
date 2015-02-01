package com.example.assignment_1_TravelExpenseTracking;

import java.util.ArrayList;
import java.util.Calendar;

public class Claim { // most about setters and getters, except the getClaimMoneySpent() function
	private String startDate; // string format of date is used to show on screen
	private String endDate;
	private Calendar startDateCalendar; // calendar format of date is used to sort and check logical rationality
	private Calendar endDateCalendar;
	private String destination;
	private String description;
	private String status;
	private ArrayList<Expense> expenseList;

	public Calendar getStartDateCalendar() {
		return startDateCalendar;
	}

	public void setStartDateCalendar(Calendar startDateCalendar) {
		this.startDateCalendar = startDateCalendar;
	}

	public Calendar getEndDateCalendar() {
		return endDateCalendar;
	}

	public void setEndDateCalendar(Calendar endDateCalendar) {
		this.endDateCalendar = endDateCalendar;
	}

	public Claim(String destination) {
		this.destination = destination;
		expenseList = new ArrayList<Expense>();

	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public ArrayList<Expense> getExpenseList() {
		return expenseList;
	}

	public void setExpenseList(ArrayList<Expense> expenseList) {
		this.expenseList = expenseList;
	}

	public void addExpense(Expense newExpense) {
		this.expenseList.add(newExpense);
	}

	public void removeExpenseByIndex(int index) {
		this.expenseList.remove(index);
	}

	public StringBuffer getClaimMoneySpent() {
		// so search through expenseList, if currency are the same then sum up and put into stringbuffer
		StringBuffer newSB = new StringBuffer();
		newSB.append("Total Expense: ");
		String[] currencyList = { " CAD", " USD", " EUR", " GBP", " CNY", " JPY" };
		for (int m = 0; m < 6; m++) {
			int moneyCount = 0;
			for (int n = 0; n < this.expenseList.size(); n++) {
				if (this.expenseList.get(n).getCurrency().equals(currencyList[m])) {
					moneyCount += (Integer) this.expenseList.get(n).getAmount();
				}
			}
			if (moneyCount > 0) {
				newSB.append(Integer.toString(moneyCount));
				newSB.append(currencyList[m]);
				newSB.append(";");
			}
		}

		return newSB;
	}

}