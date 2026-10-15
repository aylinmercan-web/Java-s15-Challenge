package com.workintech.library;

import java.time.LocalDate;

public class MemberRecord {

    public static final int MAX_BOOK_LIMIT = 5;

    private final Long memberId;
    private final LocalDate dateOfMembership;

    public MemberRecord(Long memberId, LocalDate dateOfMembership) {
        this.memberId = memberId;
        this.dateOfMembership = dateOfMembership;
    }

    public Long getMemberId() {
        return memberId;
    }

    public LocalDate getDateOfMembership() {
        return dateOfMembership;
    }
}


