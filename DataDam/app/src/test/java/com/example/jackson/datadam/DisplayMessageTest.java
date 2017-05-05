package com.example.jackson.datadam;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by utsav on 5/5/17.
 */
public class DisplayMessageTest {
    @Test
    public void getMsg1() throws Exception {

        DisplayMessage msg1 = new DisplayMessage("This is msg1","This is msg 2",5);
        assertEquals("This is msg1","This is msg1");

    }

    @Test
    public void getMsg2() throws Exception {

        DisplayMessage msg1 = new DisplayMessage("This is msg1","This is msg 2",5);
        assertEquals("This is msg2","This is msg2");

    }

    @Test
    public void square() throws Exception {
        DisplayMessage msg1 = new DisplayMessage("This is msg1","This is msg 2",5);
        assertEquals(25,25);
    }

    @Test
    public void countA() throws Exception {
        DisplayMessage msg1 = new DisplayMessage("This is msg1","This is msg 2",5);
        assertEquals(0,0);
    }

}