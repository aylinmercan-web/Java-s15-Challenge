package com.workintech.person;

import com.workintech.book.Book;
import com.workintech.library.Invoice;
import com.workintech.library.MemberRecord;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class Reader extends Person {

    private final MemberRecord memberRecord;
    private final Set<Book> borrowedBooks = new HashSet<>();
    private final List<Invoice> invoices = new ArrayList<>();

    public Reader(String name, Long memberId) {
        super(name);
        this.memberRecord = new MemberRecord(memberId, LocalDate.now());
    }

    public abstract int getLoanDays();

    public Long getId() {
        return memberRecord.getMemberId();
    }

    public MemberRecord getMemberRecord() {
        return memberRecord;
    }

    public boolean canBorrow() {
        return borrowedBooks.size() < MemberRecord.MAX_BOOK_LIMIT;
    }

    public boolean hasBook(Book book) {
        return borrowedBooks.contains(book);
    }

    public void addBook(Book book) {
        borrowedBooks.add(book);
    }

    public void removeBook(Book book) {
        borrowedBooks.remove(book);
    }

    public Set<Book> getBorrowedBooks() {
        return Collections.unmodifiableSet(borrowedBooks);
    }

    public void addInvoice(Invoice invoice) {
        invoices.add(invoice);
    }

    public List<Invoice> getInvoices() {
        return Collections.unmodifiableList(invoices);
    }
}

