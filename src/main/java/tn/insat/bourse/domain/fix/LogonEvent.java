package tn.insat.bourse.domain.fix;

/**
 * Created by WiKi on 09/12/2016.
 */
import quickfix.SessionID;

public class LogonEvent {
    private SessionID sessionID;
    private boolean loggedOn;

    public LogonEvent(SessionID sessionID, boolean loggedOn) {
        this.sessionID = sessionID;
        this.loggedOn = loggedOn;
    }

    public SessionID getSessionID() {
        return sessionID;
    }
    public boolean isLoggedOn() {
        return loggedOn;
    }
}

