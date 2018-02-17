package info.ostaszewski.domain;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Asus on 29.08.2017.
 */

@Entity
public class Month  {

    @Id
    @GeneratedValue
    private long id;
    private String name;
    private int year;
    private double budget;

    @OneToMany (cascade= CascadeType.ALL,fetch = FetchType.EAGER)
    @JoinColumn (name = "month_id")
    private List<Expense>expenses=new ArrayList<Expense>();

    @Transient
    private ObservableList<Expense>expenseObservableList= FXCollections.observableArrayList();
    @Transient
    private StringProperty budgetFormated;

    public Month(){

    }

    public Month(String name, int year, double budget) {
        this.name = name;
        this.year = year;
        this.budget = budget;
        this.budgetFormated=setfirstBudgetFormated(budget);
    }

    public String getName() {
        return name;
    }

    public int getYear() {
        return year;
    }

    public double getBudget() {
        return budget;
    }

    public List<Expense> getExpenses() {
        return expenses;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public void setBudget(double budget) {
        this.budget = budget;
    }

    public void setExpenses(List<Expense> expenses) {
        this.expenses = expenses;
    }

    public long getId() {
        return id;
    }

    public void addExpense(Expense expense){
        expenses.add(expense);
    }

    public void deleteExpense (Expense expense){
        expenses.remove(expense);
    }



    public StringProperty getNameProperty(){
        StringProperty s=new SimpleStringProperty(name);
        return s;
    }

    public IntegerProperty getYearProperty(){
        IntegerProperty s=new SimpleIntegerProperty(year);
        return s;
    }

    public DoubleProperty getBudgetProperty(){
        DoubleProperty s = new SimpleDoubleProperty(budget);
        return s;
    }


    public ObservableList<Expense> getExpenseObservableList() {
        return expenseObservableList;
    }

    public void setExpenseObservableList(ObservableList<Expense> expenseObservableList) {
        this.expenseObservableList = expenseObservableList;
    }

    public void addExpenseToObservableList(Expense e){
        expenseObservableList.add(e);
    }

    public StringProperty setfirstBudgetFormated(double budget){
        String q=String.format("%.2f",budget);
        q=q+" zł";
        StringProperty x=new SimpleStringProperty(q);

        return (x);
    }
    public void setBudgetFormated(double budget){
        String q =String.format("%.2f",budget);
        q=q+" zł";
        this.budgetFormated.set(q);
    }
    public StringProperty getBudgetFormated(){
        budgetFormated=setfirstBudgetFormated(budget);
        return budgetFormated;}

        public String getNameAndYear(){
        return name + " " + year;
        }

}
