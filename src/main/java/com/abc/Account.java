package com.abc;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Account {

    public static final int CHECKING = 0;
    public static final int SAVINGS = 1;
    public static final int MAXI_SAVINGS = 2;
    private final int accountType;
    public List<Transaction> transactions;

    public Account(int accountType) {
        this.accountType = accountType;
        this.transactions = new ArrayList<Transaction>();
    }

    public void deposit(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("amount must be greater than zero");
        } else {
            transactions.add(new Transaction(amount));
        }
    }

    public void withdraw(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("amount must be greater than zero");
        } else {
            transactions.add(new Transaction(-amount));
        }
    }
//Additional Feature for Maxi account
    
    private boolean withDrawalInPastTenDaysExists() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -10);
        for (Transaction t : transactions) {
            //if any transaction associated with the account is within ten days
            // and it is a withdrawal
            if (t.getTransactionDate().after(cal.getTime())&&t.amount < 0) {
                return true;
            }
        }
        return false;
    }

    public double interestEarned() {
        double amount = sumTransactions();
        switch (accountType) {
            case SAVINGS:
                if (amount <= 1000) {
                    return amount * 0.001;
                } else {
                    return 1 + (amount - 1000) * 0.002;
                }
//            case SUPER_SAVINGS:
//                if (amount <= 4000)
//                    return 20;
            case MAXI_SAVINGS:
                if (withDrawalInPastTenDaysExists()) {
                    return amount * 0.001;
                }
                else{
                    return amount * 0.05;
                }
               
            default:
                return amount * 0.001;
        }
    }

    public double sumTransactions() {
        return checkIfTransactionsExist(true);
    }

    private double checkIfTransactionsExist(boolean checkAll) {
        double amount = 0.0;
        for (Transaction t : transactions) {
            amount += t.amount;
        }
        return amount;
    }

    public int getAccountType() {
        return accountType;
    }
}
