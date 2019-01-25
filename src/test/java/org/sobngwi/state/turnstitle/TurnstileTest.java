package org.sobngwi.state.turnstitle;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

public class TurnstileTest {

    @Spy
    TurnstitleFSMImpl fsm;
    InOrder inOrder;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    private void assertActions(String expected){
        assertEquals(expected, fsm.getActions());
        assertThat(expected, is(equalTo(fsm.getActions())));
    }

    @Test
    public void coinEventUnlockTheFSM() {


        Mockito.doNothing().when(fsm).setState(any());
        Mockito.doNothing().when(fsm).unlock();

        fsm.coin();

        inOrder = inOrder(fsm, fsm);
        inOrder.verify(fsm).setState(any());
        inOrder.verify(fsm).unlock();

    }

    @Test
    public void normalOperation(){
        fsm.coin();
        assertActions("U");
        fsm.pass();
        assertActions("UL");

        verify(fsm, times(1)).unlock();
        verify(fsm, times(1)).lock();
        verify(fsm, never()).alarm();
        verify(fsm, never()).thankyou();

        inOrder = inOrder(fsm, fsm);
        inOrder.verify(fsm).unlock();
        inOrder.verify(fsm).lock();
    }

    @Test
    public void doublePayment() {
        fsm.coin();
        fsm.coin();
        assertActions("UT");

        verify(fsm, times(1)).unlock();
        verify(fsm, times(1)).thankyou();
        verify(fsm, never()).lock();
        verify(fsm, never()).alarm();
    }

    @Test
    public void forceEntry(){
        fsm.pass();
        assertActions("A");

        verify(fsm, times(1)).alarm();
        verify(fsm, never()).unlock();
        verify(fsm, never()).lock();
        verify(fsm, never()).thankyou();
    }

    @Test
    public void manyCoins() {
        for (int i = 0; i < 5; i++)
            fsm.coin();
        assertActions("UTTTT");

        verify(fsm, times(1)).unlock();
        verify(fsm, times(4)).thankyou();
        verify(fsm, never()).lock();
        verify(fsm, never()).alarm();
        verifyThankyouCallSuccessively(4);
    }

    @Test
    public void manyCoinsThenPass() {
        for (int i = 0; i < 5; i++)
            fsm.coin();
            fsm.pass();
        assertActions("UTTTTL");

        verify(fsm, times(1)).unlock();
        verify(fsm, times(1)).lock();
        verify(fsm, times(4)).thankyou();
        verify(fsm, never()).alarm();

        verifyThankyouCallSuccessively(4);
    }

    private void verifyThankyouCallSuccessively(int howMany) {
        inOrder = inOrder(fsm, fsm);
        inOrder.verify(fsm).unlock();
        for ( int i = 0 ; i < howMany ; i++)
            inOrder.verify(fsm).thankyou();
    }

}
