package objs;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.Date;

@Entity
@Table(name="users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_seq")
    @SequenceGenerator(name="user_seq", sequenceName = "user_seq", allocationSize = 1)
    @Column(name="user_id")
    private Long id;
    @Column(name="name",nullable=false)
    private String name;

    @Column(name="surname", nullable=false)
    private String surname;

    @Column(name="phone_number",nullable=false)
    private String phoneNumber;

    @Column(name="email", nullable=false)
    private String email;

    @Column(name="birth_date",nullable=false)
    private LocalDate birthDate;

    @Column(name="joined_date",nullable=false)
    private LocalDate joinedDate;

    // SETTERS

    public void setID(Long id) {
        this.id = id;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setSurname(String surname) {
        this.surname = surname;
    }
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }
    public void setJoinedDate(LocalDate joinedDate) {
        this.joinedDate = joinedDate;
    }

    // GETTERS

    public Long getID() {
        return id;
    }
    public String getName() {
        return name;
    }
    public String getSurname() {
        return surname;
    }
    public String getPhoneNumber() {
        return phoneNumber;
    }
    public String getEmail() {
        return email;
    }
    public LocalDate getBirthDate() {
        return birthDate;
    }
    public LocalDate getJoinedDate() {
        return joinedDate;
    }

}
