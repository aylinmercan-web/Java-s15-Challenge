package com.workintech.person;

public class Faculty extends Reader {

    public Faculty(String name, Long memberId) {
        super(name, memberId);
    }

    @Override
    public int getLoanDays() {
        return 30;
    }

    @Override
    public void whoYouAre() {
        System.out.println("Ben akademisyenim, adım " + getName());
    }
}

