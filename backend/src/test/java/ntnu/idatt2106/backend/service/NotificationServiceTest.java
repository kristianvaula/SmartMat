package ntnu.idatt2106.backend.service;


import jnr.constants.platform.Local;
import ntnu.idatt2106.backend.exceptions.NotificationException;
import ntnu.idatt2106.backend.model.*;
import ntnu.idatt2106.backend.model.dto.GroceryNotificationDTO;
import ntnu.idatt2106.backend.model.enums.UserRole;
import ntnu.idatt2106.backend.model.grocery.Grocery;
import ntnu.idatt2106.backend.model.grocery.GroceryNotification;
import ntnu.idatt2106.backend.model.grocery.RefrigeratorGrocery;
import ntnu.idatt2106.backend.repository.GroceryNotificationRepository;
import ntnu.idatt2106.backend.repository.RefrigeratorGroceryRepository;
import ntnu.idatt2106.backend.repository.RefrigeratorUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class NotificationServiceTest {

    @Mock
    private GroceryNotificationRepository groceryNotificationRepository;
    @Mock
    private RefrigeratorUserRepository refrigeratorUserRepository;
    @Mock
    private RefrigeratorGroceryRepository refrigeratorGroceryRepository;
    @InjectMocks
    private NotificationService notificationService;


    private Grocery grocery;
    private User user;
    private Refrigerator refrigerator;
    private RefrigeratorGrocery refrigeratorGrocery;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        grocery = Grocery.builder()
                .name("Milk")
                .groceryExpiryDays(14)
                .description("Milk from cow")
                .subCategory(SubCategory
                        .builder().build())
                .build();

        user = new User();
        user.setId("testUserId");
        user.setEmail("testuser@test.com");

        refrigerator = new Refrigerator();
        refrigerator.setId(1L);
        refrigerator.setName("Test Refrigerator");

        refrigeratorGrocery = RefrigeratorGrocery.builder()
                .grocery(grocery)
                .refrigerator(refrigerator)
                .build();


    }

    @Test
    public void testGenerateNotificationsNoPrevious(){
        List<RefrigeratorGrocery> refrigeratorGroceries = new ArrayList<>();
        refrigeratorGrocery.setPhysicalExpireDate(LocalDate.now());
        refrigeratorGroceries.add(refrigeratorGrocery);

        when(groceryNotificationRepository.findAllByGroceryEntity(refrigeratorGroceries.get(0))).thenReturn(new ArrayList<>());
        notificationService.generateNotifications(refrigeratorGroceries, user);

        verify(groceryNotificationRepository, times(1)).save(Mockito.any(GroceryNotification.class));
    }

    @Test
    public void testGenerateNotificationsOnePrevious(){
        List<RefrigeratorGrocery> refrigeratorGroceries = new ArrayList<>();
        refrigeratorGrocery.setPhysicalExpireDate(LocalDate.now());
        refrigeratorGroceries.add(refrigeratorGrocery);

        ArrayList<GroceryNotification> notifications = new ArrayList<>();
        notifications.add(GroceryNotification.builder().build());

        when(groceryNotificationRepository.findAllByGroceryEntity(refrigeratorGroceries.get(0))).thenReturn(notifications);
        notificationService.generateNotifications(refrigeratorGroceries, user);

        verify(groceryNotificationRepository, times(1)).save(Mockito.any(GroceryNotification.class));
    }

    @Test
    public void testGenerateNotificationsTwoPrevious(){
        List<RefrigeratorGrocery> refrigeratorGroceries = new ArrayList<>();
        refrigeratorGrocery.setPhysicalExpireDate(LocalDate.now());
        refrigeratorGroceries.add(refrigeratorGrocery);

        ArrayList<GroceryNotification> notifications = new ArrayList<>();
        notifications.add(GroceryNotification.builder().build());
        notifications.add(GroceryNotification.builder().build());

        when(groceryNotificationRepository.findAllByGroceryEntity(refrigeratorGroceries.get(0))).thenReturn(notifications);
        notificationService.generateNotifications(refrigeratorGroceries, user);

        verify(groceryNotificationRepository, times(0)).save(Mockito.any(GroceryNotification.class));
    }

    @Test
    public void testGenerateNotificationsItemDoesNotNeed() throws ParseException {
        List<RefrigeratorGrocery> refrigeratorGroceries = new ArrayList<>();

        String date_string = "26-04-2300";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate date = LocalDate.parse(date_string, formatter);
        refrigeratorGrocery.setPhysicalExpireDate(date);
        refrigeratorGroceries.add(refrigeratorGrocery);
        ArrayList<GroceryNotification> notifications = new ArrayList<>();

        when(groceryNotificationRepository.findAllByGroceryEntity(refrigeratorGroceries.get(0))).thenReturn(notifications);
        notificationService.generateNotifications(refrigeratorGroceries, user);

        verify(groceryNotificationRepository, times(0)).save(Mockito.any(GroceryNotification.class));
    }

    @Test
    public void testGenerateNotificationsItemDoesNotNeedAlreadyExists() throws ParseException {
        List<RefrigeratorGrocery> refrigeratorGroceries = new ArrayList<>();
        // Create a new Date object
        LocalDate currentDate = LocalDate.now().plusDays(3);

        refrigeratorGrocery.setPhysicalExpireDate(currentDate);
        refrigeratorGroceries.add(refrigeratorGrocery);

        ArrayList<GroceryNotification> notifications = new ArrayList<>();
        notifications.add(GroceryNotification.builder().build());

        when(groceryNotificationRepository.findAllByGroceryEntity(refrigeratorGroceries.get(0))).thenReturn(notifications);
        notificationService.generateNotifications(refrigeratorGroceries, user);

        verify(groceryNotificationRepository, times(0)).save(Mockito.any(GroceryNotification.class));
    }

    @Test
    public void testGenerateNotificationsThreeDayWarning() throws ParseException {
        List<RefrigeratorGrocery> refrigeratorGroceries = new ArrayList<>();
        // Create a new Date object
        LocalDate currentDate = LocalDate.now().plusDays(3);
        refrigeratorGrocery.setPhysicalExpireDate(currentDate);
        refrigeratorGroceries.add(refrigeratorGrocery);

        ArrayList<GroceryNotification> notifications = new ArrayList<>();
        when(groceryNotificationRepository.findAllByGroceryEntity(refrigeratorGroceries.get(0))).thenReturn(notifications);
        notificationService.generateNotifications(refrigeratorGroceries, user);

        verify(groceryNotificationRepository, times(1)).save(Mockito.any(GroceryNotification.class));
    }


    @Test
    void deleteNotification_Successful() throws NotificationException {
        long notifId = 1L;
        GroceryNotification groceryNotification = new GroceryNotification(1, user, refrigeratorGrocery, (long)3, false);
        Mockito.when(groceryNotificationRepository.findById(notifId))
                .thenReturn(Optional.of(groceryNotification));
        GroceryNotificationDTO result = notificationService.deleteNotification(user, notifId);
        assertEquals(new GroceryNotificationDTO(groceryNotification), result);
        Mockito.verify(groceryNotificationRepository, Mockito.times(1)).save(groceryNotification);
    }

    @Test
    void deleteNotification_NotFound() {
        long notifId = 1L;
        Mockito.when(groceryNotificationRepository.findById(notifId)).thenReturn(Optional.empty());
        assertThrows(NotificationException.class, () ->
                notificationService.deleteNotification(user, notifId));
        Mockito.verify(groceryNotificationRepository, Mockito.never()).save(Mockito.any());
    }

    @Test
    void deleteNotification_Unauthorized() {
        long notifId = 1L;
        User otherUser = User.builder()
                .id("123")
                .name("otherUser")
                .email("123@123.no")
                .password("123")
                .userRole(UserRole.USER)
                .build();
        GroceryNotification groceryNotification = new GroceryNotification(1, otherUser, refrigeratorGrocery, (long)3, false);
        Mockito.when(groceryNotificationRepository.findById(notifId))
                .thenReturn(Optional.of(groceryNotification));
        assertThrows(NotificationException.class, () ->
                notificationService.deleteNotification(user, notifId));
        Mockito.verify(groceryNotificationRepository, Mockito.never()).save(Mockito.any());
    }

    @Test
    void deleteNotification_FailedToDelete() {
        long notifId = 1L;
        GroceryNotification groceryNotification = new GroceryNotification(1, user, refrigeratorGrocery, (long)3, false);
        Mockito.when(groceryNotificationRepository.findById(notifId))
                .thenReturn(Optional.of(groceryNotification));
        Mockito.doThrow(new RuntimeException()).when(groceryNotificationRepository).save(groceryNotification);

        assertThrows(NotificationException.class, () ->
                notificationService.deleteNotification(user, notifId));
        Mockito.verify(groceryNotificationRepository, Mockito.times(1)).save(groceryNotification);
    }
}
