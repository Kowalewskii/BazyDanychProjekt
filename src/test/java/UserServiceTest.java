import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;

import dao.UserDAO;
import objs.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import service.UserService;

public class UserServiceTest {

    private UserDAO dao;
    private UserService service;

    @BeforeEach
    void setUp() {
        dao = mock(UserDAO.class);
        service = new UserService();
        service.dao = dao;
    }

    @Test
    void addUser_validData_callsSave() {
        service.addUser("John", "Doe", "+48123456789", "john.doe@example.com", LocalDate.of(1990,1,1));
        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(dao).save(captor.capture());
        User savedUser = captor.getValue();
        assertEquals("John", savedUser.getName());
        assertEquals("Doe", savedUser.getSurname());
        assertEquals("+48123456789", savedUser.getPhoneNumber());
        assertEquals("john.doe@example.com", savedUser.getEmail());
        assertEquals(LocalDate.of(1990,1,1), savedUser.getBirthDate());
        assertNotNull(savedUser.getJoinedDate());
    }

    @Test
    void addUser_invalidEmail_throwsException() {
        Exception ex = assertThrows(IllegalArgumentException.class, () -> {
            service.addUser("John", "Doe", "+48123456789", "invalid-email",
                    LocalDate.of(1990,1,1));
        });
        assertTrue(ex.getMessage().contains("Invalid email format"));
    }

    @Test
    void addUser_tooYoung_throwsException() {
        LocalDate tooYoung = LocalDate.now().minusYears(10);
        Exception ex = assertThrows(IllegalArgumentException.class, () -> {
            service.addUser("John", "Doe", "+48123456789", "john.doe@example.com", tooYoung);
        });
        assertTrue(ex.getMessage().contains("at least 11 years old"));
    }

    @Test
    void getUserById_existingUser_returnsUser() {
        User user = new User();
        user.setName("Jane");
        when(dao.getById(1L)).thenReturn(user);
        User result = service.getUserById(1L);
        assertEquals("Jane", result.getName());
    }

    @Test
    void updateUserEmail_validEmail_updatesEmail() {
        User user = new User();
        user.setEmail("old@example.com");
        when(dao.getById(1L)).thenReturn(user);
        service.updateUserEmail(1L, "new@example.com");
        assertEquals("new@example.com", user.getEmail());
        verify(dao).update(user);
    }

    @Test
    void updateUserEmail_invalidEmail_throwsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            service.updateUserEmail(1L, "bad-email");
        });
        verify(dao, never()).update(any());
    }

    @Test
    void deleteUser_existingUser_callsDelete() {
        User user = new User();
        when(dao.getById(1L)).thenReturn(user);
        service.deleteUser(1L);
        verify(dao).delete(user);
    }

    @Test
    void deleteUser_nonExistingUser_noDeleteCall() {
        when(dao.getById(1L)).thenReturn(null);
        service.deleteUser(1L);
        verify(dao, never()).delete(any());
    }

    @Test
    void getAllUsers_returnsListFromDao() {
        List<User> users = new ArrayList<>();
        users.add(new User());
        when(dao.getAll()).thenReturn(users);
        List<User> result = service.getAllUsers();
        assertEquals(1, result.size());
    }

    @Test
    void updateUserNameAndSurname_existingUser_updatesFields() {
        User user = new User();
        user.setName("OldName");
        user.setSurname("OldSurname");
        when(dao.getById(1L)).thenReturn(user);
        service.updateUserNameAndSurname(1L, "NewName", "NewSurname");
        assertEquals("NewName", user.getName());
        assertEquals("NewSurname", user.getSurname());
        verify(dao).update(user);
    }

    @Test
    void testDeleteUser_whenUserExists() {
        User user = new User();
        user.setEmail("test@example.com");

        when(dao.getUserByEmail("test@example.com")).thenReturn(user);

        service.deleteUser("test@example.com");

        verify(dao, times(1)).delete(user);
    }

    @Test
    void testDeleteUser_whenUserDoesNotExist() {
        when(dao.getUserByEmail("missing@example.com")).thenReturn(null);
        assertDoesNotThrow(() -> service.deleteUser("missing@example.com"));
        verify(dao, never()).delete(any());
    }

    @Test
    void testUpdatePhoneNumber_validNumber() {
        User user = new User();
        user.setPhoneNumber("+48123456789");

        when(dao.getById(1L)).thenReturn(user);

        service.updateUserPhoneNumber(1L, "+48123456789");

        assertEquals("+48123456789", user.getPhoneNumber());
        verify(dao).update(user);
    }

    @Test
    void testUpdatePhoneNumber_invalidNumber_throwsException() {
        assertThrows(IllegalArgumentException.class, () ->
                service.updateUserPhoneNumber(1L, "1234"));
    }
}

