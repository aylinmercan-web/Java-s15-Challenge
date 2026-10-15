package com.workintech.person;

public class Student extends Reader {

    public Student(String name, Long memberId) {
        super(name, memberId);
    }

    @Override
    public int getLoanDays() {
        return 15;
    }

    @Override
    public void whoYouAre() {
        System.out.println("Ben öğrenciyim, adım " + getName());
    }
}
