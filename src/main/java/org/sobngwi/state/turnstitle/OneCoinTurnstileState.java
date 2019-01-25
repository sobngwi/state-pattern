package org.sobngwi.state.turnstitle;

public enum OneCoinTurnstileState implements TurnstileState {

    LOCKED {
        @Override
        public void pass(TurnstitleFSM fsm) {
            fsm.alarm();
        }

        @Override
        public void coin(TurnstitleFSM fsm) {
            fsm.setState(UNLOCKED);
            fsm.unlock();
        }
    },

    UNLOCKED {
        @Override
        public void pass(TurnstitleFSM fsm) {
            fsm.setState(LOCKED);
            fsm.lock();
        }

        @Override
        public void coin(TurnstitleFSM fsm) {
            fsm.thankyou();
        }
    }
}
