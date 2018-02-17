package info.ostaszewski.domain;

import info.ostaszewski.Main;
import javafx.beans.property.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

/**
 * Created by Asus on 29.08.2017.
 */

@Entity
public class Expense {

    @Id
    @GeneratedValue
    private long id;
    private String name;
    private String category;
    private double price;
    private Date date;
    @Transient
    private LocalDate localDate;
    @Transient
    private StringProperty priceFormated;
    @Transient
    private Main main;

    public Expense() {

    }

    public Expense(String name, String category, double price, LocalDate localDate) {
        this.name = name;
        this.category = category;
        this.price = price;
        this.localDate = localDate;
        this.date = Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
        this.priceFormated=setfirstPriceFormated(price);
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public double getPrice() {
        return price;
    }

    public LocalDate getDate() {
        return localDate;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setPrice(double pirce) {
        this.price = pirce;

    }

    public void setLocalDate(LocalDate localDate) {
        date = Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
        this.localDate = localDate;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public LocalDate getLocalDate() {
        return localDate;
    }


    public StringProperty getNameExpenseProperty() {
        StringProperty s = new SimpleStringProperty(name);
        return s;
    }

    public StringProperty getCategoryExpenseProperty() {
        StringProperty s = new SimpleStringProperty(category);

        return s;
    }

    public DoubleProperty getPriceExpenseProperty() {
        DoubleProperty s = new SimpleDoubleProperty(price);
        return s;
    }

    public ObjectProperty<LocalDate> getLocalDateExpenseProperty() {

        localDate = Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
        ObjectProperty<LocalDate> s = new SimpleObjectProperty<>(localDate);
        return s;
    }

    public LocalDate asLocalDate(Date date) {
        return Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
    }


    private StringProperty setfirstPriceFormated(double price) {
        String q = String.format("%.2f", price);
        q = q + " " + "zł";
        StringProperty x = new SimpleStringProperty(q);

        return (x);
    }

    public void setPriceFormated(double price) {
        String q = String.format("%.2f", price);
        q = q + " " + "zł";
        this.priceFormated.set(q);
    }

    public StringProperty getPriceFormated() {
        priceFormated = setfirstPriceFormated(price);
        return priceFormated;
    }


}
