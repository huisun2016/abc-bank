package com.abc;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.abs;

public class Customer {

    private String name;
    private List<Account> accounts;

    public Customer(String name) {
        this.name = name;
        this.accounts = new ArrayList<Account>();
    }

    public String getName() {
        return name;
    }

    /**
     * @fromAcct: the account the money is from
     * @toAcct: the destination account
     * @transfer amount 
     * This function implements an additional feature of
     * transfer between customer accounts for the same customer.
     * This function is synchronized in order to guarantee thread safety 
     * Assumptions: amount must to positive and the destination account balance must not be negative
     */
    public synchronized void profileTransfer(Account fromAcct, Account toAcct, double amount) {
      double fromAcctBlance = fromAcct.sumTransactions() ;
        if (amount < 0 ) {
            throw new IllegalArgumentException("transfer amount must be greater than zero");
        } else if( fromAcctBlance < amount || fromAcctBlance < 0){
            throw new IllegalArgumentException("balance cant not be negative");
        } else {
         // the withdrawal and deposit should be executed neither or both
         // commit and rollback to be implemented so the withdrawal and the deposit become one atomic transaction 
            fromAcct.withdraw(amount);
            toAcct.deposit(amount);
        }

    }

    public Customer openAccount(Account account) {
        accounts.add(account);
        return this;
    }

    public int getNumberOfAccounts() {
        return accounts.size();
    }

    public double totalInterestEarned() {
        double total = 0;
        for (Account a : accounts) {
            total += a.interestEarned();
        }
        return total;
    }

    public String getStatement() {
        String statement = null;
        statement = "Statement for " + name + "\n";
        double total = 0.0;
        for (Account a : accounts) {
            statement += "\n" + statementForAccount(a) + "\n";
            total += a.sumTransactions();
        }
        statement += "\nTotal In All Accounts " + toDollars(total);
        return statement;
    }

    private String statementForAccount(Account a) {
        String s = "";

        //Translate to pretty account type
        switch (a.getAccountType()) {
            case Account.CHECKING:
                s += "Checking Account\n";
                break;
            case Account.SAVINGS:
                s += "Savings Account\n";
                break;
            case Account.MAXI_SAVINGS:
                s += "Maxi Savings Account\n";
                break;
        }

        //Now total up all the transactions
        double total = 0.0;
        for (Transaction t : a.transactions) {
            s += "  " + (t.amount < 0 ? "withdrawal" : "deposit") + " " + toDollars(t.amount) + "\n";
            total += t.amount;
        }
        s += "Total " + toDollars(total);
        return s;
    }

    private String toDollars(double d) {
        return String.format("$%,.2f", abs(d));
    }
}
