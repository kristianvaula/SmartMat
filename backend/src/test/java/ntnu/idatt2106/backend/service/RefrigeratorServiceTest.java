package ntnu.idatt2106.backend.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import ntnu.idatt2106.backend.exceptions.LastSuperuserException;
import ntnu.idatt2106.backend.exceptions.RefrigeratorNotFoundException;
import ntnu.idatt2106.backend.exceptions.UnauthorizedException;
import ntnu.idatt2106.backend.exceptions.UserNotFoundException;
import ntnu.idatt2106.backend.model.Refrigerator;
import ntnu.idatt2106.backend.model.RefrigeratorUser;
import ntnu.idatt2106.backend.model.User;
import ntnu.idatt2106.backend.model.dto.MemberDTO;
import ntnu.idatt2106.backend.model.dto.RefrigeratorDTO;
import ntnu.idatt2106.backend.model.enums.FridgeRole;
import ntnu.idatt2106.backend.model.refrigerator.NewRefrigeratorDTO;
import ntnu.idatt2106.backend.model.requests.MemberRequest;
import ntnu.idatt2106.backend.model.requests.RemoveMemberRequest;
import ntnu.idatt2106.backend.repository.*;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.nio.file.AccessDeniedException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;

public class RefrigeratorServiceTest {

    @Mock
    private RefrigeratorRepository refrigeratorRepository;

    @Mock
    private RefrigeratorGroceryRepository refrigeratorGroceryRepository;

    @Mock
    private ShoppingCartRepository shoppingCartRepository;

    @Mock
    private ShoppingListRepository shoppingListRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserService userService;

    @Mock
    private CookieService cookieService;

    @Mock
    private JwtService jwtService;

    @Mock
    private RefrigeratorUserRepository refrigeratorUserRepository;

    @InjectMocks
    private RefrigeratorService refrigeratorService;

    private Refrigerator refrigerator;
    private User user;
    private User superuser;
    private HttpServletRequest request;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        refrigerator = new Refrigerator();
        refrigerator.setId(1L);
        refrigerator.setName("Test Refrigerator");

        user = new User();
        user.setId("testUserId");
        user.setEmail("testuser@test.com");

        superuser = new User();
        superuser.setId("testSuperuserId");
        superuser.setEmail("testSuperuser@test.com");
    }

    @Test
    @DisplayName("Test saving a refrigerator with a valid user")
    public void testSaveRefrigeratorWithValidUser() throws Exception {
        // Arrange
        NewRefrigeratorDTO refrigeratorDTO = new NewRefrigeratorDTO();
        refrigeratorDTO.setName("name");
        refrigeratorDTO.setAddress("Test Address");
        String userEmail = "user@example.com";
        refrigerator = refrigeratorService.convertToEntity(refrigeratorDTO);

        when(refrigeratorService.extractEmail(request)).thenReturn(userEmail);
        when(userRepository.findByEmail(userEmail)).thenReturn(Optional.of(new User()));
        when(refrigeratorRepository.save(refrigerator)).thenReturn(refrigerator);

        // Act
        Refrigerator result = refrigeratorService.save(refrigeratorDTO, request);

        // Assert
        Assertions.assertNotNull(result);
    }



    @Test
    @DisplayName("Test getUser returns existing user ")
    public void testGetUserReturnsExistingUser() throws UserNotFoundException {
        // Arrange
        String existingUserEmail = "john.doe@example.com";
        User existingUser = new User();
        existingUser.setEmail(existingUserEmail);
        Mockito.when(userRepository.findByEmail(existingUserEmail)).thenReturn(Optional.of(existingUser));

        // Act
        User result = refrigeratorService.getUser(existingUserEmail);

        // Assert
        Assertions.assertNotNull(result);
        Assertions.assertEquals(existingUserEmail, result.getEmail());
        Mockito.verify(userRepository, Mockito.times(1)).findByEmail(existingUserEmail);
    }

    @Test
    @DisplayName("Test getUser returns existing user ")
    public void testGetUserThrowsNonExistingUser() {
        // Arrange
        String nonExistingUserEmail = "john.doe@example.com";

        // Act
        Assert.assertThrows(UserNotFoundException.class,() -> refrigeratorService.getUser(nonExistingUserEmail));
    }

    @Test
    @DisplayName("Test saving a refrigerator with an invalid user")
    public void testSaveRefrigeratorWithInvalidUser() {
        // Arrange
        NewRefrigeratorDTO refrigeratorDTO = new NewRefrigeratorDTO();
        refrigeratorDTO.setName(null);
        refrigeratorDTO.setAddress("Test Address");
        String userEmail = "invalid@example.com";

        when(refrigeratorService.extractEmail(request)).thenReturn(userEmail);
        when(userService.findByEmail(userEmail)).thenThrow(new UsernameNotFoundException("User not found with email: invalid@example.com"));

        // Act and assert
        Assertions.assertThrows(Exception.class, () -> refrigeratorService.save(refrigeratorDTO, request));
    }

    @Test
    @DisplayName("getAllRefrigerators should return list of refrigerators")
    public void getAllRefrigerators_shouldReturnListOfRefrigerators() throws UserNotFoundException {
        List<RefrigeratorUser> refrigeratorList = new ArrayList<>();
        RefrigeratorUser ru = new RefrigeratorUser();
        ru.setRefrigerator(refrigerator);
        ru.setUser(user);
        refrigeratorList.add(ru);
        List<Refrigerator> refrigerators = new ArrayList<>();
        refrigerators.add(refrigerator);

        when(refrigeratorService.extractEmail(request)).thenReturn(user.getUsername());
        Mockito.when(refrigeratorUserRepository.findByUser(user)).thenReturn(refrigeratorList);
        Mockito.when(userRepository.findByEmail(user.getUsername())).thenReturn(Optional.ofNullable(user));

        List<Refrigerator> result = refrigeratorService.getAllByUser(request);

        Assertions.assertEquals(refrigerators, result);
    }

    @Test
    @DisplayName("Test adding a member to a refrigerator")
    public void testAddMemberToRefrigerator() {
        // Arrange
        User newUser = new User();
        newUser.setId("id");
        newUser.setEmail("user1@example.com");

        MemberRequest request1 = new MemberRequest();
        request1.setRefrigeratorId(refrigerator.getId());
        request1.setUserName(newUser.getUsername());
        request1.setFridgeRole(FridgeRole.USER);

        RefrigeratorUser ru = new RefrigeratorUser();
        ru.setUser(user);
        ru.setFridgeRole(FridgeRole.SUPERUSER);
        ru.setRefrigerator(refrigerator);

        when(refrigeratorService.extractEmail(request)).thenReturn(user.getUsername());
        when(refrigeratorRepository.findById(request1.getRefrigeratorId())).thenReturn(Optional.of(refrigerator));
        when(userRepository.findByEmail(user.getUsername())).thenReturn(Optional.of(user));
        when(userRepository.findByEmail(newUser.getUsername())).thenReturn(Optional.of(newUser));
        when(refrigeratorUserRepository.findByUserAndRefrigerator(user, refrigerator)).thenReturn(Optional.of(ru));

        //Act
        Assertions.assertDoesNotThrow(() -> refrigeratorService.addMember(request1, request));
    }

    @Test
    @DisplayName("Test setFridgeRole with valid input")
    public void testSetRoleWithValidInput() throws UserNotFoundException, UnauthorizedException, RefrigeratorNotFoundException, LastSuperuserException {
        User user = new User();
        user.setId("test_user_id");
        user.setEmail("test_user@test.com");
        user.setPassword("test_password");
        User superUser = new User();
        superUser.setId("super_user_id");
        superUser.setEmail("super_user@test.com");
        superUser.setPassword("super_password");

        Refrigerator refrigerator = new Refrigerator();
        refrigerator.setId(1L);
        refrigerator.setName("test_refrigerator");

        RefrigeratorUser refrigeratorUser = new RefrigeratorUser();
        refrigeratorUser.setId(1L);
        refrigeratorUser.setUser(user);
        refrigeratorUser.setRefrigerator(refrigerator);

        RefrigeratorUser refrigeratorSuper = new RefrigeratorUser();
        refrigeratorSuper.setId(1L);
        refrigeratorSuper.setUser(superUser);
        refrigeratorSuper.setRefrigerator(refrigerator);
        refrigeratorSuper.setFridgeRole(FridgeRole.SUPERUSER);

        when(refrigeratorService.extractEmail(request)).thenReturn(superUser.getUsername());
        when(userRepository.findByEmail(user.getUsername())).thenReturn(Optional.of(user));
        when(userRepository.findByEmail(superUser.getUsername())).thenReturn(Optional.of(superUser));
        when(refrigeratorUserRepository.findByUser_IdAndRefrigerator_Id("test_user_id", 1L)).thenReturn(Optional.of(refrigeratorUser));
        when(refrigeratorUserRepository.save(any(RefrigeratorUser.class))).thenReturn(refrigeratorUser);
        when(refrigeratorRepository.findById(refrigerator.getId())).thenReturn(Optional.of(refrigerator));
        when(refrigeratorUserRepository.findByUserAndRefrigerator(superUser, refrigerator)).thenReturn(Optional.of(refrigeratorSuper));
        when(refrigeratorUserRepository.findByUserAndRefrigerator(user,refrigerator)).thenReturn(Optional.of(refrigeratorUser));

        FridgeRole newFridgeRole = FridgeRole.SUPERUSER;

        MemberRequest memberRequest = new MemberRequest();
        memberRequest.setFridgeRole(newFridgeRole);
        memberRequest.setRefrigeratorId(1L);
        memberRequest.setUserName(user.getUsername());
        MemberDTO result = refrigeratorService.setFridgeRole(memberRequest, request);

        Assertions.assertEquals(result.getFridgeRole(), newFridgeRole);
    }

    @Test
    @DisplayName("Test setFridgeRole with invalid privilege")
    public void testSetRoleWithInvalidPrivilege() throws UserNotFoundException, UnauthorizedException, RefrigeratorNotFoundException {
        User user = new User();
        user.setId("test_user_id");
        user.setEmail("test_user@test.com");
        user.setPassword("test_password");
        User superUser = new User();
        superUser.setId("super_user_id");
        superUser.setEmail("super_user@test.com");
        superUser.setPassword("super_password");

        Refrigerator refrigerator = new Refrigerator();
        refrigerator.setId(1L);
        refrigerator.setName("test_refrigerator");

        RefrigeratorUser refrigeratorUser = new RefrigeratorUser();
        refrigeratorUser.setId(1L);
        refrigeratorUser.setUser(user);
        refrigeratorUser.setRefrigerator(refrigerator);

        RefrigeratorUser refrigeratorSuper = new RefrigeratorUser();
        refrigeratorSuper.setId(1L);
        refrigeratorSuper.setUser(superUser);
        refrigeratorSuper.setRefrigerator(refrigerator);
        refrigeratorSuper.setFridgeRole(FridgeRole.USER);

        when(refrigeratorService.extractEmail(request)).thenReturn(user.getUsername());
        when(userRepository.findByEmail(user.getUsername())).thenReturn(Optional.of(user));
        when(userRepository.findByEmail(superUser.getUsername())).thenReturn(Optional.of(superUser));
        when(refrigeratorUserRepository.findByUser_IdAndRefrigerator_Id("test_user_id", 1L)).thenReturn(Optional.of(refrigeratorUser));
        when(refrigeratorUserRepository.save(any(RefrigeratorUser.class))).thenReturn(refrigeratorUser);
        when(userRepository.findByEmail(user.getUsername())).thenReturn(Optional.of(user));
        when(refrigeratorRepository.findById(refrigerator.getId())).thenReturn(Optional.of(refrigerator));
        when(refrigeratorUserRepository.findByUser_IdAndRefrigerator_Id(superUser.getId(), refrigerator.getId())).thenReturn(Optional.of(refrigeratorSuper));
        when(refrigeratorUserRepository.findByUserAndRefrigerator(user,refrigerator)).thenReturn(Optional.of(refrigeratorUser));

        FridgeRole newFridgeRole = FridgeRole.SUPERUSER;

        MemberRequest memberRequest = new MemberRequest();
        memberRequest.setFridgeRole(newFridgeRole);
        memberRequest.setRefrigeratorId(1L);
        memberRequest.setUserName(user.getUsername());

        Assertions.assertThrows(UnauthorizedException.class, () -> refrigeratorService.setFridgeRole(memberRequest, request));
    }

    @Test
    @DisplayName("Test setFridgeRole with invalid user ")
    public void testSetRoleWithInvalidUser() {
        Mockito.when(userRepository.findByEmail("nonexistent_user@test.com")).thenReturn(Optional.empty());

        FridgeRole newFridgeRole = FridgeRole.SUPERUSER;
        MemberRequest memberRequest = new MemberRequest();
        memberRequest.setFridgeRole(newFridgeRole);
        memberRequest.setRefrigeratorId(1L);
        memberRequest.setUserName("nonexistent_user@test.com");

        when(refrigeratorService.extractEmail(request)).thenReturn(user.getUsername());

        Assertions.assertThrows(UserNotFoundException.class, () -> refrigeratorService.setFridgeRole(memberRequest, request));
    }

    @Test
    @DisplayName("Test setFridgeRole with invalid refrigerator user")
    public void testSetRoleWithInvalidRefrigeratorUser() throws UserNotFoundException, UnauthorizedException, RefrigeratorNotFoundException {
        User user = new User();
        user.setId("test_user_id");
        user.setEmail("test_user@test.com");
        user.setPassword("test_password");
        User superUser = new User();
        superUser.setId("super_user_id");
        superUser.setEmail("super_user@test.com");
        superUser.setPassword("super_password");

        Refrigerator refrigerator = new Refrigerator();
        refrigerator.setId(1L);
        refrigerator.setName("test_refrigerator");

        RefrigeratorUser refrigeratorUser = new RefrigeratorUser();
        refrigeratorUser.setId(1L);
        refrigeratorUser.setUser(user);
        refrigeratorUser.setRefrigerator(refrigerator);

        RefrigeratorUser refrigeratorSuper = new RefrigeratorUser();
        refrigeratorSuper.setId(1L);
        refrigeratorSuper.setUser(superUser);
        refrigeratorSuper.setRefrigerator(refrigerator);
        refrigeratorSuper.setFridgeRole(FridgeRole.SUPERUSER);

        when(userRepository.findByEmail(user.getUsername())).thenReturn(Optional.of(user));
        when(userRepository.findByEmail(superUser.getUsername())).thenReturn(Optional.of(superUser));
        when(userRepository.findByEmail(user.getUsername())).thenReturn(Optional.of(user));
        when(refrigeratorRepository.findById(refrigerator.getId())).thenReturn(Optional.of(refrigerator));
        when(refrigeratorUserRepository.findByUserAndRefrigerator(user,refrigerator)).thenReturn(Optional.empty());
        when(refrigeratorService.extractEmail(request)).thenReturn(user.getUsername());

        FridgeRole newFridgeRole = FridgeRole.SUPERUSER;

        MemberRequest memberRequest = new MemberRequest();
        memberRequest.setFridgeRole(newFridgeRole);
        memberRequest.setRefrigeratorId(1L);
        memberRequest.setUserName(user.getUsername());

        Assertions.assertThrows(UnauthorizedException.class, () -> refrigeratorService.setFridgeRole(memberRequest, request));
    }

    @Test
    @DisplayName("Test removing a user from a refrigerator")
    public void testRemoveUserFromRefrigerator() throws Exception {
        RemoveMemberRequest request1 = new RemoveMemberRequest();
        request1.setRefrigeratorId(refrigerator.getId());
        request1.setUserName(user.getEmail());
        request1.setForceDelete(false);

        RefrigeratorUser userRole = new RefrigeratorUser();
        userRole.setId(1L);
        userRole.setRefrigerator(refrigerator);
        userRole.setUser(user);
        userRole.setFridgeRole(FridgeRole.USER);

        RefrigeratorUser superuserRole = new RefrigeratorUser();
        superuserRole.setId(2L);
        superuserRole.setRefrigerator(refrigerator);
        superuserRole.setUser(superuser);
        superuserRole.setFridgeRole(FridgeRole.SUPERUSER);

        List<RefrigeratorUser> superUsers = new ArrayList<>();
        superUsers.add(superuserRole);

        when(refrigeratorService.extractEmail(request)).thenReturn(user.getUsername());
        when(refrigeratorRepository.findById(refrigerator.getId())).thenReturn(Optional.of(refrigerator));
        when(userRepository.findByEmail(user.getUsername())).thenReturn(Optional.of(user));
        when(userRepository.findByEmail(superuser.getUsername())).thenReturn(Optional.of(superuser));
        when(refrigeratorUserRepository.findByUser_IdAndRefrigerator_Id(user.getId(), refrigerator.getId())).thenReturn(Optional.of(userRole));
        when(refrigeratorUserRepository.findByUser_IdAndRefrigerator_Id(superuser.getId(), refrigerator.getId())).thenReturn(Optional.of(superuserRole));
        when(refrigeratorUserRepository.findByRefrigeratorIdAndFridgeRole(refrigerator.getId(), FridgeRole.SUPERUSER)).thenReturn(superUsers);

        refrigeratorService.removeUserFromRefrigerator(request1, request);

        Mockito.verify(refrigeratorUserRepository, Mockito.times(1)).delete(userRole);
        Mockito.verify(refrigeratorRepository, Mockito.times(1)).save(refrigerator);
    }

    @Test
    @DisplayName("Test user tries to remove other user")
    public void testUserRemovesUserShouldFail() {
        RemoveMemberRequest request1 = new RemoveMemberRequest();
        request1.setRefrigeratorId(refrigerator.getId());
        request1.setUserName(user.getEmail());
        request1.setForceDelete(false);

        RefrigeratorUser userRole = new RefrigeratorUser();
        userRole.setId(1L);
        userRole.setRefrigerator(refrigerator);
        userRole.setUser(user);
        userRole.setFridgeRole(FridgeRole.USER);

        superuser.setId("Something else");
        RefrigeratorUser userRole1 = new RefrigeratorUser();
        userRole1.setId(2L);
        userRole1.setRefrigerator(refrigerator);
        userRole1.setUser(superuser);
        userRole1.setFridgeRole(FridgeRole.USER); //Pretend is a normal user

        when(refrigeratorService.extractEmail(request)).thenReturn(superuser.getUsername());
        when(refrigeratorRepository.findById(refrigerator.getId())).thenReturn(Optional.of(refrigerator));
        when(userRepository.findByEmail(user.getUsername())).thenReturn(Optional.of(user));
        when(userRepository.findByEmail(superuser.getUsername())).thenReturn(Optional.of(superuser));
        when(refrigeratorUserRepository.findByUser_IdAndRefrigerator_Id(user.getId(), refrigerator.getId())).thenReturn(Optional.of(userRole));
        when(refrigeratorUserRepository.findByUser_IdAndRefrigerator_Id(superuser.getId(), refrigerator.getId())).thenReturn(Optional.of(userRole1));

        Assertions.assertThrows(AccessDeniedException.class, () -> refrigeratorService.removeUserFromRefrigerator(request1, request));
    }

    @Test
    @DisplayName("Test removing the last superuser without forceDelete")
    public void testRemoveLastSuperuserWithoutForceDeleteShouldFail() throws EntityNotFoundException {
        RemoveMemberRequest request1 = new RemoveMemberRequest();
        request1.setRefrigeratorId(refrigerator.getId());
        request1.setUserName(superuser.getEmail());
        request1.setForceDelete(false);

        RefrigeratorUser superuserRole = new RefrigeratorUser();
        superuserRole.setId(2L);
        superuserRole.setRefrigerator(refrigerator);
        superuserRole.setUser(superuser);
        superuserRole.setFridgeRole(FridgeRole.SUPERUSER);

        List<RefrigeratorUser> superUsers = new ArrayList<>();
        superUsers.add(superuserRole);

        when(refrigeratorService.extractEmail(request)).thenReturn(superuser.getUsername());
        when(refrigeratorRepository.findById(refrigerator.getId())).thenReturn(Optional.of(refrigerator));
        when(userRepository.findByEmail(superuser.getUsername())).thenReturn(Optional.of(superuser));
        when(refrigeratorUserRepository.findByUser_IdAndRefrigerator_Id(superuser.getId(), refrigerator.getId())).thenReturn(Optional.of(superuserRole));
        when(refrigeratorUserRepository.findByRefrigeratorIdAndFridgeRole(refrigerator.getId(), FridgeRole.SUPERUSER)).thenReturn(superUsers);

        Assertions.assertThrows(LastSuperuserException.class, () -> refrigeratorService.removeUserFromRefrigerator(request1, request));

        Mockito.verify(refrigeratorUserRepository, Mockito.times(0)).delete(superuserRole);
        Mockito.verify(refrigeratorRepository, Mockito.times(0)).save(refrigerator);
    }

    @Test
    @DisplayName("Test removing a user from a refrigerator when user is not a member")
    public void testRemoveUserFromRefrigeratorUserNotMember() throws EntityNotFoundException {
        RemoveMemberRequest request1 = new RemoveMemberRequest();
        request1.setRefrigeratorId(refrigerator.getId());
        request1.setUserName("not_a_member@test.com");
        request1.setForceDelete(false);

        when(refrigeratorService.extractEmail(request)).thenReturn(user.getUsername());
        when(refrigeratorRepository.findById(refrigerator.getId())).thenReturn(Optional.of(refrigerator));
        when(userRepository.findByEmail(request1.getUserName())).thenReturn(Optional.empty());

        Assertions.assertThrows(UserNotFoundException.class, () -> refrigeratorService.removeUserFromRefrigerator(request1, request));
    }

    @Test
    @DisplayName("Test removing a user from a refrigerator when superuser is not a member")
    public void testRemoveUserFromRefrigeratorSuperuserNotMember() throws EntityNotFoundException {
        RemoveMemberRequest request1 = new RemoveMemberRequest();
        request1.setRefrigeratorId(refrigerator.getId());
        request1.setUserName(user.getEmail());
        request1.setForceDelete(false);

        RefrigeratorUser userRole = new RefrigeratorUser();
        userRole.setId(1L);
        userRole.setRefrigerator(refrigerator);
        userRole.setUser(user);
        userRole.setFridgeRole(FridgeRole.USER);

        when(refrigeratorService.extractEmail(request)).thenReturn(superuser.getUsername());
        when(refrigeratorRepository.findById(refrigerator.getId())).thenReturn(Optional.of(refrigerator));
        when(userRepository.findByEmail(user.getUsername())).thenReturn(Optional.of(user));
        when(refrigeratorService.extractEmail(request)).thenReturn(user.getUsername());
        when(refrigeratorUserRepository.findByUser_IdAndRefrigerator_Id(user.getId(), refrigerator.getId())).thenReturn(Optional.empty());
        when(refrigeratorUserRepository.findByUser_IdAndRefrigerator_Id(superuser.getId(), refrigerator.getId())).thenReturn(Optional.empty());

        Assertions.assertThrows(EntityNotFoundException.class, () -> refrigeratorService.removeUserFromRefrigerator(request1, request));
    }

    @Test
    @DisplayName("Test removing a user from a refrigerator when user tries to remove other user")
    public void testRemoveUserFromRefrigeratorUserTriesToRemoveOtherUser() throws EntityNotFoundException {
        RemoveMemberRequest request1 = new RemoveMemberRequest();
        request1.setRefrigeratorId(refrigerator.getId());
        request1.setUserName(user.getEmail());
        request1.setForceDelete(false);

        RefrigeratorUser userRole = new RefrigeratorUser();
        userRole.setId(1L);
        userRole.setRefrigerator(refrigerator);
        userRole.setUser(user);
        userRole.setFridgeRole(FridgeRole.USER);

        RefrigeratorUser userRole1 = new RefrigeratorUser();
        userRole1.setId(2L);
        userRole1.setRefrigerator(refrigerator);
        userRole1.setUser(superuser);
        userRole1.setFridgeRole(FridgeRole.USER);

        when(refrigeratorService.extractEmail(request)).thenReturn(superuser.getUsername());
        when(refrigeratorRepository.findById(refrigerator.getId())).thenReturn(Optional.of(refrigerator));
        when(userRepository.findByEmail(user.getUsername())).thenReturn(Optional.of(user));
        when(userRepository.findByEmail(superuser.getUsername())).thenReturn(Optional.of(superuser));
        when(refrigeratorUserRepository.findByUser_IdAndRefrigerator_Id(user.getId(), refrigerator.getId())).thenReturn(Optional.of(userRole));
        when(refrigeratorUserRepository.findByUser_IdAndRefrigerator_Id(superuser.getId(), refrigerator.getId())).thenReturn(Optional.of(userRole1));

        Assertions.assertThrows(AccessDeniedException.class, () -> refrigeratorService.removeUserFromRefrigerator(request1, request));
    }

    @Test
    @DisplayName("Test force deleting a refrigerator when user is not a superuser")
    public void testForceDeleteRefrigeratorUserNotSuperuser() throws EntityNotFoundException {
        // Arrange
        RefrigeratorUser userRole = new RefrigeratorUser();
        userRole.setId(1L);
        userRole.setRefrigerator(refrigerator);
        userRole.setUser(user);
        userRole.setFridgeRole(FridgeRole.USER);

        when(refrigeratorService.extractEmail(request)).thenReturn(superuser.getUsername());
        when(userRepository.findByEmail(superuser.getEmail())).thenReturn(Optional.of(superuser));
        when(refrigeratorUserRepository.findByUser_IdAndRefrigerator_Id(superuser.getId(), refrigerator.getId())).thenReturn(Optional.of(userRole));

        // Act and Assert
        Assertions.assertThrows(AccessDeniedException.class, () -> refrigeratorService.forceDeleteRefrigerator(refrigerator.getId(), request));
    }

    @Test
    @DisplayName("Test force deleting a refrigerator when user is a superuser and refrigerator exists with no members")
    public void testForceDeleteRefrigeratorSuperuserNoMembers() throws Exception {
        // Arrange
        RefrigeratorUser superUserRole = new RefrigeratorUser();
        superUserRole.setId(1L);
        superUserRole.setRefrigerator(refrigerator);
        superUserRole.setUser(superuser);
        superUserRole.setFridgeRole(FridgeRole.SUPERUSER);

        when(refrigeratorService.extractEmail(request)).thenReturn(user.getUsername());
        when(userRepository.findByEmail(any())).thenReturn(Optional.of(superuser));
        when(refrigeratorUserRepository.findByUser_IdAndRefrigerator_Id(superuser.getId(), refrigerator.getId())).thenReturn(Optional.of(superUserRole));
        when(refrigeratorRepository.existsById(refrigerator.getId())).thenReturn(true);

        // Act
        refrigeratorService.forceDeleteRefrigerator(refrigerator.getId(), request);

        // Assert
        Mockito.verify(refrigeratorRepository, times(1)).deleteById(refrigerator.getId());
    }

    /**
    @Test
    @DisplayName("Test force deleting a refrigerator when user is a superuser and refrigerator exists with members")
    public void testForceDeleteRefrigeratorSuperuserWithMembers() throws Exception {
        // Arrange
        RefrigeratorUser superUserRole = new RefrigeratorUser();
        superUserRole.setId(1L);
        superUserRole.setRefrigerator(refrigerator);
        superUserRole.setUser(superuser);
        superUserRole.setFridgeRole(FridgeRole.SUPERUSER);

        when(refrigeratorService.extractEmail(request)).thenReturn(superuser.getUsername());
        when(userRepository.findByEmail(superuser.getEmail())).thenReturn(Optional.of(superuser));
        when(refrigeratorUserRepository.findByUser_IdAndRefrigerator_Id(superuser.getId(), refrigerator.getId())).thenReturn(Optional.of(superUserRole));
        when(refrigeratorRepository.existsById(refrigerator.getId())).thenReturn(true);
        when(refrigeratorUserRepository.count()).thenReturn(2L);
        // Act
        refrigeratorService.forceDeleteRefrigerator(refrigerator.getId(), request);

        // Assert
        Mockito.verify(refrigeratorUserRepository, times(1)).removeByRefrigeratorId(refrigerator.getId());
        Mockito.verify (refrigeratorRepository, times(1)).deleteById(refrigerator.getId());
    }*/

    @Test
    @DisplayName("Test force deleting a non-existent refrigerator")
    public void testForceDeleteRefrigeratorNonExistent() {
        // Arrange
        String email = superuser.getEmail();

        when(refrigeratorService.extractEmail(request)).thenReturn(email);
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(superuser));
        when(refrigeratorUserRepository.findByUser_IdAndRefrigerator_Id(superuser.getId(), refrigerator.getId())).thenThrow(EntityNotFoundException.class);
        when(refrigeratorRepository.existsById(refrigerator.getId())).thenReturn(false);

        // Act and Assert
        Assertions.assertThrows(EntityNotFoundException.class, () -> refrigeratorService.forceDeleteRefrigerator(refrigerator.getId(), request));
    }

    /**
    @Test
    @DisplayName("Test getting an existing refrigerator by id")
    public void testGetExistingRefrigeratorById() throws EntityNotFoundException, RefrigeratorNotFoundException, UserNotFoundException {
        // Arrange
        long id = 1L;
        Refrigerator refrigerator = new Refrigerator();
        refrigerator.setId(id);
        refrigerator.setName("Test Refrigerator");

        when(refrigeratorService.extractEmail(request)).thenReturn(superuser.getUsername());
        when(refrigeratorRepository.existsById(id)).thenReturn(true);
        when(refrigeratorRepository.findById(id)).thenReturn(Optional.of(refrigerator));
        when(refrigeratorUserRepository.findByRefrigeratorId(id)).thenReturn(new ArrayList<>());

        // Act
        RefrigeratorDTO result = refrigeratorService.getRefrigeratorDTOById(id, request);

        // Assert
        Assertions.assertNotNull(result);
        Assertions.assertEquals(id, result.getId());
        Assertions.assertEquals(refrigerator.getName(), result.getName());
        Assertions.assertEquals(new ArrayList<MemberDTO>(), result.getMembers());
    }*/

    @Test
    @DisplayName("Test getting a non-existing refrigerator by id")
    public void testGetNonExistingRefrigeratorById() {
        // Arrange
        long id = 1L;
        when(refrigeratorRepository.existsById(id)).thenReturn(false);

        // Act & Assert
        Assertions.assertThrows(RefrigeratorNotFoundException.class, () -> refrigeratorService.getRefrigerator(id));
    }
}