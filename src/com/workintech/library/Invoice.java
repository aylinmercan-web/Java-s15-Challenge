package com.workintech.library;

import com.workintech.book.Book;
import com.workintech.person.Reader;

import java.time.LocalDate;

public class Invoice {

    private final Reader reader;
    private final Book book;
    private final double amount;
    private final LocalDate issueDate;
    private final LocalDate dueDate;
    private boolean refunded;

    public Invoice(Reader reader, Book book, double amount) {
        this.reader = reader;
        this.book = book;
        this.amount = amount;
        this.issueDate = LocalDate.now();
        this.dueDate = issueDate.plusDays(reader.getLoanDays());
        this.refunded = false;
    }

    public Reader getReader() {
        return reader;
    }

    public Book getBook() {
        return book;
    }

    public double getAmount() {
        return amount;
    }

    public LocalDate getIssueDate() {
        return issueDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public boolean isRefunded() {
        return refunded;
    }

    public void refund() {
        this.refunded = true;
    }

    public void display() {
        System.out.println("FATURA | Okuyucu: " + reader.getName()
                + " | Kitap: " + book.getName()
                + " | Tutar: " + amount + " TL"
                + " | Tarih: " + issueDate
                + " | Son iade: " + dueDate
                + (refunded ? " | İade edildi" : ""));
    }
}
