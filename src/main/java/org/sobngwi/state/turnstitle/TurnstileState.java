package org.sobngwi.state.turnstitle;

public interface TurnstileState {

    void pass(TurnstitleFSM fsm);
    void coin(TurnstitleFSM fsm);

}
