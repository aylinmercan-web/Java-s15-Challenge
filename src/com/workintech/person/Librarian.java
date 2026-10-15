package com.workintech.person;

import com.workintech.library.Library;

public class Librarian extends Person {

    public Librarian(String name) {
        super(name);
    }

    @Override
    public void whoYouAre() {
        System.out.println("Ben kütüphaneciyim, adım " + getName());
    }

    public boolean issueBook(Library library, Long bookId, Long readerId) {
        System.out.println(getName() + " kitabı ödünç veriyor...");
        return library.lendBook(bookId, readerId);
    }

    public boolean takeBackBook(Library library, Long bookId, Long readerId) {
        System.out.println(getName() + " iade alıyor...");
        return library.takeBackBook(bookId, readerId);
    }
}

