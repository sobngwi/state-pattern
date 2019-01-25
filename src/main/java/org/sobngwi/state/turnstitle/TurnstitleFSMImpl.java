package org.sobngwi.state.turnstitle;

public class TurnstitleFSMImpl extends TurnstitleFSM {
private String actions ="";

    public TurnstitleFSMImpl() {
        this.setState(OneCoinTurnstileState.LOCKED); // Initialize the state, establish the logic
    }

    @Override
    public void alarm() {
        actions +="A";
    }

    @Override
    public void lock() {
        actions +="L";
    }

    @Override
    public void unlock() {
        actions +="U";
    }

    @Override
    public void thankyou() {
        actions +="T";
    }

    public String getActions() {
        return actions;
    }
}
