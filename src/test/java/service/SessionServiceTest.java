package service;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SessionServiceTest {

    @Test
    public void testSessionLifecycle() {
        String email = "test@user.com";

        String sessionId = SessionService.createSession(email);
        assertNotNull(sessionId, "Session ID should not be null");

        String retrievedEmail = SessionService.getEmailBySession(sessionId);
        assertEquals(email, retrievedEmail, "Retrieved email should match the session creator");

        SessionService.invalidateSession(sessionId);

        String expiredEmail = SessionService.getEmailBySession(sessionId);
        assertNull(expiredEmail, "Session should be destroyed after invalidation");
    }

    @Test
    public void testNonExistentSession() {
        String email = SessionService.getEmailBySession("fake-session-id");
        assertNull(email, "Non-existent session should return null");
    }
}