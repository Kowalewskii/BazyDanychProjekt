package service;

import dao.UserDAO;
import objs.User;

import java.time.LocalDate;
import java.util.List;

public class UserService {
    public UserDAO dao = new UserDAO();
    public void addUser(String name, String surname, String phoneNumber, String email, LocalDate birthDate) {
        if(dao.getUserByEmail(email) != null) {
            throw new IllegalArgumentException("User already exists");
        }
        validateData(name, surname, phoneNumber, email, birthDate);
        User user=new User();
        user.setName(name);
        user.setSurname(surname);
        user.setPhoneNumber(phoneNumber);
        user.setEmail(email);
        user.setBirthDate(birthDate);
        user.setJoinedDate(LocalDate.now());
        dao.save(user);
    }
    public User getUserById(Long id){
        return dao.getById(id);
    }

    public void updateUserEmail(Long id, String email) {
        validateEmail(email);
        User user=dao.getById(id);
        if (user!=null){
            user.setEmail(email);
            dao.update(user);
        }
    }
    public void updateUserPhoneNumber(Long id, String phoneNumber) {
        validatePhoneNumber(phoneNumber);
        User user=dao.getById(id);
        if (user!=null){
            user.setPhoneNumber(phoneNumber);
            dao.update(user);
        }
    }
    public void deleteUser(Long id){
        User user=dao.getById(id);
        if (user!=null){
            dao.delete(user);
        }
    }
    public void deleteUser(String email){
        User user=dao.getUserByEmail(email);
        if (user!=null){
            dao.delete(user);
        } else{
            System.out.println("User not found");
        }
    }
    public List<User> getAllUsers() {
        return dao.getAll();
    }
    public void updateUserNameAndSurname(Long id, String name, String surname) {
        User user=dao.getById(id);
        if (user!=null){
            user.setName(name);
            user.setSurname(surname);
            dao.update(user);
        }
    }

    //walidacja danych
    private void validateData(String name, String surname, String phoneNumber, String email, LocalDate birthDate) {
        if(name==null || name.isBlank()) throw new IllegalArgumentException("Name cannot be blank");
        if(surname == null || surname.isBlank()) throw new IllegalArgumentException("Surname cannot be blank");
        validatePhoneNumber(phoneNumber);
        validateEmail(email);
        Long minage=Long.parseLong("11");
        LocalDate mini=LocalDate.now().minusYears(minage);
        if (birthDate == null) {
            throw new IllegalArgumentException("Birth date cannot be blank.");
        }
        if(birthDate.isAfter(mini)) throw new IllegalArgumentException("User must be at least 11 years old.");
    }
    private void validatePhoneNumber(String phoneNumber) {
        if(phoneNumber==null  || phoneNumber.isBlank()) throw new IllegalArgumentException("PhoneNumber cannot be blank");
        String e164Pattern = "^\\+[1-9]\\d{9,14}$";
        if(!phoneNumber.matches(e164Pattern)){
            throw new IllegalArgumentException("Invalid phone number. Expected format like +48123456789");
        }
    }
    private void validateEmail(String email) {
        if(email==null  ||  email.isBlank()) throw new IllegalArgumentException("Email cannot be blank");
        String emailPattern = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,}$";
        if(!email.matches(emailPattern)){
            throw new IllegalArgumentException("Invalid email format.");
        }
    }

}
