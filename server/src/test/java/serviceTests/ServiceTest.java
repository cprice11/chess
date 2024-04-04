package serviceTests;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import service.AuthService;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ServiceTest {
    //    In addition to the HTTP server pass off tests provided in the starter code, you need to write tests that execute
//    directly against your service classes. These tests skip the HTTP server network communication and will help you in
//    the development of your service code for this phase.
//
//    Good tests extensively show that we get the expected behavior. This could be asserting that data put into the
//    database is really there, or that a function throws an error when it should. Write a positive and a negative JUNIT
//    test case for each public method on your Service classes, except for Clear which only needs a positive test case.
//    A positive test case is one for which the action happens successfully (e.g., successfully claiming a spot in a
//    game). A negative test case is one for which the operation fails (e.g., trying to claim an already claimed spot).
//
//    The service unit tests must directly call the methods on your service classes. They should not use the HTTP server
//    pass off test code that is provided with the starter code.
//
//    ⚠ You must place your service test cases in a folder named server/src/test/java/serviceTests.
    private final static String username = "exampleUsername37";
    private final static String password = "$^R6DPVDtdSNSj&W83WJ";

    @Test
    @Order(1)
    void getAuthByUsername() {
    }

    @Test
    @Order(2)
    void getAuthByAuthToken() {
    }

    @Test
    @Order(3)
    void testCreateAuth() {
        AuthService service = new AuthService();
        service.createAuth(username);
    }

    @Test
    void testLogin() {
    }

    @Test
    void testLogout() {
    }

    @Test
    void testVerify() {
    }


}