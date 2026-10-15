package com.workintech.book;

import com.workintech.person.Author;

import java.time.LocalDate;

public class StudyBook extends Book {

    public StudyBook(Long id, String name, Author author, double price, String edition, LocalDate dateOfPurchase) {
        super(id, name, author, price, edition, dateOfPurchase);
    }

    @Override
    public void display() {
        System.out.println("ID: " + getId()
                + " | " + getName()
                + " | " + getAuthor().getName()
                + " | " + getEdition() + " baskı"
                + " | Ders kitabı (StudyBook)"
                + " | " + getStatus());
    }
}
