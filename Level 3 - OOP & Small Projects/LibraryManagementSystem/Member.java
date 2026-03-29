package LibraryManagementSystem;

public class Member {
    private String memberId;
    private String name;
    private String email;
    private String phone;
    private String membershipDate;
    private int borrowedCount;
    private static final int MAX_BORROW = 5;

    public Member(String memberId, String name, String email, String phone, String membershipDate) {
        this.memberId = memberId;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.membershipDate = membershipDate;
        this.borrowedCount = 0;
    }

    public String getMemberId() {
        return memberId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getMembershipDate() {
        return membershipDate;
    }

    public int getBorrowedCount() {
        return borrowedCount;
    }

    public void setBorrowedCount(int borrowedCount) {
        this.borrowedCount = borrowedCount;
    }

    public boolean canBorrow() {
        return borrowedCount < MAX_BORROW;
    }

    public void borrowBook() {
        if(borrowedCount < MAX_BORROW) {
            borrowedCount++;
        }
    }

    public void returnBook() {
        if(borrowedCount > 0) {
            borrowedCount--;
        }
    }

    public String toString() {
        return "Member ID: " + memberId + "\nName: " + name + "\nEmail: " + email +
                "\nPhone: " + phone + "\nMembership Date: " + membershipDate +
                "\nBooks Borrowed: " + borrowedCount + "/" + MAX_BORROW;
    }
}