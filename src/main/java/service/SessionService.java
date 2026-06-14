package service;

import java.util.UUID;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SessionService {
    private static final Map<String, String> sessions = new ConcurrentHashMap<>();

    public static String createSession(String email) {
        String sessionId = UUID.randomUUID().toString();
        sessions.put(sessionId, email);
        return sessionId;
    }

    public static String getEmailBySession(String sessionId) {
        if (sessionId == null) return null;
        return sessions.get(sessionId);
    }

    public static void invalidateSession(String sessionId) {
        if (sessionId != null) {
            sessions.remove(sessionId);
        }
    }
}