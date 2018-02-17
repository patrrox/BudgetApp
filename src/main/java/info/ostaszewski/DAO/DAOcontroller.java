package info.ostaszewski.DAO;

import info.ostaszewski.Main;
import info.ostaszewski.domain.Expense;
import info.ostaszewski.domain.Month;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * Created by Asus on 29.08.2017.
 */
public class DAOcontroller{

    EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("myDatabase");
    EntityManager entityManager = entityManagerFactory.createEntityManager();

    public DAOcontroller() {

    }

    public void addMonth(Month entity) {


        entityManager.getTransaction().begin();
        entityManager.persist(entity);
        entityManager.getTransaction().commit();


    }

    public void addExpense(Month entity, Expense expense) {

        entityManager.getTransaction().begin();
        entity.addExpense(expense);
        entityManager.getTransaction().commit();

    }

    public Month findOne(long id) {
        entityManager.getTransaction().begin();
        Month m = entityManager.find(Month.class, id);

        entityManager.getTransaction().commit();
        return m;
    }



    public boolean findMonthDuplicate(String name, int year, double budget){
        boolean q=false;

        entityManager.getTransaction().begin();

        TypedQuery query = entityManager.createQuery(
                "Select e from Month e where e.name = :fName and e.year = :fYear"
                , Month.class);

        query.setParameter("fName",name);
        query.setParameter("fYear",year);
       // query.setParameter("fBudget",budget);

       List <Month>months=query.getResultList();
       if (months.isEmpty()==false){
           q=true;
       }

        entityManager.getTransaction().commit();

        return q;
    }

    public void addExpenseToMonth(String name, int year, double budget,Expense expense){
        entityManager.getTransaction().begin();
        TypedQuery query = entityManager.createQuery(
                "Select e from Month e where e.name = :fName and e.year = :fYear"
                , Month.class);

        query.setParameter("fName",name);
        query.setParameter("fYear",year);

        Month month = (Month)query.getSingleResult();

        month.addExpense(expense);
        entityManager.getTransaction().commit();
    }

    public List<Month> findAll() {
        entityManager.getTransaction().begin();
        TypedQuery<Month> query = entityManager.createQuery("from Month", Month.class);
        List<Month> months = query.getResultList();

        entityManager.getTransaction().commit();
        return months;
    }


    public ObservableList<Month> findAllObservable() {
        List<Month> months = findAll();
        ObservableList<Month> monthObservableList = FXCollections.observableArrayList();

        for (Month m : months) {
            monthObservableList.add(m);
            for (Expense e : m.getExpenses()) {
                m.addExpenseToObservableList(e);
            }
        }

        return monthObservableList;
    }

    public Month updateMonth(Month entity) {
        entityManager.getTransaction().begin();
        Month m = entityManager.merge(entity);
        entityManager.getTransaction().commit();
        return m;
    }

    public Expense updateExpense(Expense expense) {
        entityManager.getTransaction().begin();
        Expense e = entityManager.merge(expense);
        entityManager.getTransaction().commit();

        return e;
    }

    public void deleteMonth(Month entity) {
        entityManager.getTransaction().begin();
        entityManager.remove(entity);
        entityManager.getTransaction().commit();
    }

    public void deleteExpense(Month month, Expense entity) {
        entityManager.getTransaction().begin();
        month.deleteExpense(entity);
        entityManager.remove(entity);
        entityManager.getTransaction().commit();
    }


    public void exitFromDataBase() {
        entityManager.close();
        entityManagerFactory.close();
    }


}
