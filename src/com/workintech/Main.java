package com.workintech;

import com.workintech.book.Book;
import com.workintech.book.Journal;
import com.workintech.book.Magazine;
import com.workintech.book.Novel;
import com.workintech.book.StudyBook;
import com.workintech.library.Library;
import com.workintech.library.MemberRecord;
import com.workintech.person.Author;
import com.workintech.person.Faculty;
import com.workintech.person.Librarian;
import com.workintech.person.Reader;
import com.workintech.person.Student;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Scanner;

public class Main {

    private static final Scanner scanner = new Scanner(System.in);
    private static final Library library = new Library();
    private static final Librarian librarian = new Librarian("Aylin");

    public static void main(String[] args) {
        loadSampleData();
        System.out.println("Kütüphane sistemine hoş geldiniz.");
        librarian.whoYouAre();

        boolean running = true;
        while (running) {
            printMenu();
            int choice = readInt("Seçiminiz: ");
            switch (choice) {
                case 1 -> printBooks(library.getAllBooks());
                case 2 -> addBook();
                case 3 -> searchBook();
                case 4 -> updateBook();
                case 5 -> removeBook();
                case 6 -> listByCategory();
                case 7 -> printBooks(library.findBooksByAuthor(readText("Yazar adı: ")));
                case 8 -> lendBook();
                case 9 -> takeBackBook();
                case 10 -> showMember();
                case 0 -> running = false;
                default -> System.out.println("Geçersiz seçim.");
            }
        }
        System.out.println("Görüşmek üzere.");
    }

    private static void printMenu() {
        System.out.println("\n========== MENÜ ==========");
        System.out.println("1  - Tüm kitapları listele");
        System.out.println("2  - Kitap ekle");
        System.out.println("3  - Kitap ara (ID / isim / yazar)");
        System.out.println("4  - Kitap güncelle");
        System.out.println("5  - Kitap sil");
        System.out.println("6  - Kategoriye göre listele");
        System.out.println("7  - Yazara göre listele");
        System.out.println("8  - Ödünç ver");
        System.out.println("9  - İade al");
        System.out.println("10 - Üye bilgilerini göster");
        System.out.println("0  - Çıkış");
    }

    // ---------- MENÜ İŞLEMLERİ ----------

    private static void addBook() {
        String name = readText("Kitap adı: ");
        String authorName = readText("Yazar adı: ");
        double price = readDouble("Fiyat: ");
        String edition = readText("Baskı (örn. 3.): ");
        System.out.println("Kategori: 1-StudyBook  2-Journal  3-Magazine  4-Novel");
        int category = readInt("Kategori seçiminiz: ");

        if (category < 1 || category > 4) {
            System.out.println("Geçersiz kategori, kitap eklenmedi.");
            return;
        }

        Author author = new Author(authorName);
        Long id = library.generateBookId();
        LocalDate today = LocalDate.now();

        Book book = switch (category) {
            case 1 -> new StudyBook(id, name, author, price, edition, today);
            case 2 -> new Journal(id, name, author, price, edition, today);
            case 3 -> new Magazine(id, name, author, price, edition, today);
            default -> new Novel(id, name, author, price, edition, today);
        };

        library.addBook(book);
        System.out.println("Kitap eklendi:");
        book.display();
    }

    private static void searchBook() {
        int type = readInt("Neye göre aramak istersiniz? (1-ID, 2-İsim, 3-Yazar): ");
        switch (type) {
            case 1 -> {
                Book book = library.findBookById(readLong("Kitap ID: "));
                if (book == null) {
                    System.out.println("Kitap bulunamadı.");
                } else {
                    book.display();
                    Reader reader = library.getReaderOfBook(book);
                    if (reader != null) {
                        System.out.println("Şu an bu üyede: " + reader.getName());
                    }
                }
            }
            case 2 -> printBooks(library.findBooksByName(readText("Kitap adı: ")));
            case 3 -> printBooks(library.findBooksByAuthor(readText("Yazar adı: ")));
            default -> System.out.println("Geçersiz seçim.");
        }
    }

    private static void updateBook() {
        Long id = readLong("Güncellenecek kitabın ID'si: ");
        Book book = library.findBookById(id);
        if (book == null) {
            System.out.println("Kitap bulunamadı.");
            return;
        }
        book.display();
        System.out.println("Değiştirmek istemediğiniz alanı boş bırakıp Enter'a basın.");

        String newName = readOptional("Yeni ad: ");
        String priceText = readOptional("Yeni fiyat: ");
        String newEdition = readOptional("Yeni baskı: ");

        double newPrice = book.getPrice();
        if (!priceText.isEmpty()) {
            try {
                newPrice = Double.parseDouble(priceText.replace(',', '.'));
            } catch (NumberFormatException e) {
                System.out.println("Geçersiz fiyat, eski fiyat korundu.");
            }
        }

        library.updateBook(id,
                newName.isEmpty() ? book.getName() : newName,
                newPrice,
                newEdition.isEmpty() ? book.getEdition() : newEdition);
        book.display();
    }

    private static void removeBook() {
        library.removeBook(readLong("Silinecek kitabın ID'si: "));
    }

    private static void listByCategory() {
        System.out.println("1-StudyBook  2-Journal  3-Magazine  4-Novel");
        int choice = readInt("Kategori: ");
        String category = switch (choice) {
            case 1 -> "StudyBook";
            case 2 -> "Journal";
            case 3 -> "Magazine";
            case 4 -> "Novel";
            default -> null;
        };
        if (category == null) {
            System.out.println("Geçersiz kategori.");
            return;
        }
        printBooks(library.findBooksByCategory(category));
    }

    private static void lendBook() {
        listMembers();
        Long readerId = readLong("Üye ID: ");
        Long bookId = readLong("Kitap ID: ");
        librarian.issueBook(library, bookId, readerId);
    }

    private static void takeBackBook() {
        listMembers();
        Long readerId = readLong("Üye ID: ");
        Long bookId = readLong("Kitap ID: ");
        librarian.takeBackBook(library, bookId, readerId);
    }

    private static void showMember() {
        listMembers();
        Reader reader = library.findReaderById(readLong("Üye ID: "));
        if (reader == null) {
            System.out.println("Üye bulunamadı.");
            return;
        }
        reader.whoYouAre();
        System.out.println("Elindeki kitaplar (" + reader.getBorrowedBooks().size()
                + "/" + MemberRecord.MAX_BOOK_LIMIT + "):");
        printBooks(reader.getBorrowedBooks());

        System.out.println("Faturalar:");
        if (reader.getInvoices().isEmpty()) {
            System.out.println("Fatura yok.");
        } else {
            reader.getInvoices().forEach(invoice -> invoice.display());
        }
    }

    // ---------- YARDIMCI METOTLAR ----------

    private static void printBooks(Collection<Book> books) {
        if (books.isEmpty()) {
            System.out.println("Kitap bulunamadı.");
            return;
        }
        for (Book book : books) {
            book.display();
        }
    }

    private static void listMembers() {
        System.out.println("Üyeler:");
        for (Reader reader : library.getAllReaders()) {
            System.out.print("ID: " + reader.getId() + " | ");
            reader.whoYouAre();
        }
    }

    private static int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Lütfen bir sayı girin.");
            }
        }
    }

    private static Long readLong(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            try {
                return Long.parseLong(input);
            } catch (NumberFormatException e) {
                System.out.println("Lütfen bir sayı girin.");
            }
        }
    }

    private static double readDouble(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim().replace(',', '.');
            try {
                return Double.parseDouble(input);
            } catch (NumberFormatException e) {
                System.out.println("Lütfen geçerli bir sayı girin.");
            }
        }
    }

    private static String readText(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            if (!input.isEmpty()) {
                return input;
            }
            System.out.println("Bu alan boş bırakılamaz.");
        }
    }

    private static String readOptional(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    // ---------- ÖRNEK VERİ ----------

    private static void loadSampleData() {
        Author adorno = new Author("Theodor Adorno");
        Author sabahattin = new Author("Sabahattin Ali");
        Author dostoyevski = new Author("Fyodor Dostoyevski");

        library.addBook(new StudyBook(library.generateBookId(), "Aydınlanmanın Diyalektiği", adorno, 250, "3.", LocalDate.of(2015, 3, 10)));
        library.addBook(new Novel(library.generateBookId(), "İçimizdeki Şeytan", sabahattin, 120, "45.", LocalDate.of(2020, 9, 15)));
        library.addBook(new Novel(library.generateBookId(), "Kürk Mantolu Madonna", sabahattin, 130, "60.", LocalDate.of(2021, 5, 2)));
        library.addBook(new Novel(library.generateBookId(), "Kuyucaklı Yusuf", sabahattin, 110, "20.", LocalDate.of(2018, 11, 8)));
        library.addBook(new Novel(library.generateBookId(), "Suç ve Ceza", dostoyevski, 150, "12.", LocalDate.of(2019, 4, 20)));
        library.addBook(new Novel(library.generateBookId(), "İnsancıklar", dostoyevski, 90, "8.", LocalDate.of(2021, 1, 12)));
        library.addBook(new Novel(library.generateBookId(), "Yeraltından Notlar", dostoyevski, 85, "10.", LocalDate.of(2022, 6, 30)));

        library.addReader(new Student("Emel Koç", 1L));
        library.addReader(new Faculty("Emre Şahiner", 2L));
    }
}

