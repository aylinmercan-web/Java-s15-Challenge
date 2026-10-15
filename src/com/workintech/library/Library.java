package com.workintech.library;

import com.workintech.book.Book;
import com.workintech.book.BookStatus;
import com.workintech.person.Reader;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Library {

    public static final double DEPOSIT = 100.0;

    private final Map<Long, Book> books = new HashMap<>();
    private final Map<Long, Reader> readers = new HashMap<>();
    private final Map<Book, Reader> borrowedBooks = new HashMap<>();
    private long nextBookId = 1;

    // ---------- KİTAP İŞLEMLERİ ----------

    public Long generateBookId() {
        return nextBookId++;
    }

    public void addBook(Book book) {
        books.put(book.getId(), book);
    }

    public Book findBookById(Long id) {
        return books.get(id);
    }

    public List<Book> findBooksByName(String name) {
        List<Book> result = new ArrayList<>();
        for (Book book : books.values()) {
            if (book.getName().toLowerCase().contains(name.toLowerCase())) {
                result.add(book);
            }
        }
        return result;
    }

    public List<Book> findBooksByAuthor(String authorName) {
        List<Book> result = new ArrayList<>();
        for (Book book : books.values()) {
            if (book.getAuthor().getName().toLowerCase().contains(authorName.toLowerCase())) {
                result.add(book);
            }
        }
        return result;
    }

    public List<Book> findBooksByCategory(String category) {
        List<Book> result = new ArrayList<>();
        for (Book book : books.values()) {
            if (book.getClass().getSimpleName().equalsIgnoreCase(category)) {
                result.add(book);
            }
        }
        return result;
    }

    public Collection<Book> getAllBooks() {
        return books.values();
    }

    public boolean updateBook(Long id, String newName, double newPrice, String newEdition) {
        Book book = books.get(id);
        if (book == null) {
            System.out.println("Bu id ile kitap bulunamadı.");
            return false;
        }
        book.setName(newName);
        book.setPrice(newPrice);
        book.setEdition(newEdition);
        System.out.println("Kitap güncellendi.");
        return true;
    }

    public boolean removeBook(Long id) {
        Book book = books.get(id);
        if (book == null) {
            System.out.println("Bu id ile kitap bulunamadı.");
            return false;
        }
        if (book.getStatus() == BookStatus.BORROWED) {
            System.out.println("Ödünçte olan kitap silinemez.");
            return false;
        }
        books.remove(id);
        System.out.println("Kitap silindi.");
        return true;
    }

    // ---------- OKUYUCU İŞLEMLERİ ----------

    public void addReader(Reader reader) {
        readers.put(reader.getId(), reader);
    }

    public Reader findReaderById(Long id) {
        return readers.get(id);
    }

    // ---------- ÖDÜNÇ VE İADE ----------

    public boolean lendBook(Long bookId, Long readerId) {
        Book book = books.get(bookId);
        Reader reader = readers.get(readerId);

        if (book == null) {
            System.out.println("Bu id ile kitap bulunamadı.");
            return false;
        }
        if (reader == null) {
            System.out.println("Bu id ile üye bulunamadı.");
            return false;
        }
        if (book.getStatus() == BookStatus.BORROWED) {
            System.out.println("Bu kitap şu an başka bir üyede.");
            return false;
        }
        if (!reader.canBorrow()) {
            System.out.println("Üye " + MemberRecord.MAX_BOOK_LIMIT + " kitap limitine ulaştı.");
            return false;
        }

        book.setStatus(BookStatus.BORROWED);
        borrowedBooks.put(book, reader);
        reader.addBook(book);

        Invoice invoice = new Invoice(reader, book, DEPOSIT);
        reader.addInvoice(invoice);
        invoice.display();
        return true;
    }

    public boolean takeBackBook(Long bookId, Long readerId) {
        Book book = books.get(bookId);
        Reader reader = readers.get(readerId);

        if (book == null || reader == null) {
            System.out.println("Kitap ya da üye bulunamadı.");
            return false;
        }
        if (!reader.hasBook(book)) {
            System.out.println("Bu kitap bu üyede değil.");
            return false;
        }

        book.setStatus(BookStatus.AVAILABLE);
        borrowedBooks.remove(book);
        reader.removeBook(book);

        for (Invoice invoice : reader.getInvoices()) {
            if (invoice.getBook().equals(book) && !invoice.isRefunded()) {
                invoice.refund();
                System.out.println(invoice.getAmount() + " TL depozito iade edildi.");
                break;
            }
        }
        return true;
    }

    public Reader getReaderOfBook(Book book) {
        return borrowedBooks.get(book);
    }
    public Collection<Reader> getAllReaders() {
        return readers.values();
    }
}


