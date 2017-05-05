package com.example.jackson.datadam;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Seth on 5/4/2017.
 */

public class DataLimitTest {
    @Test
    public void DataLimitAddbytes(){
        DataLimit subject1= new DataLimit("Seth",5000,"Hey I'm done");
        subject1.addBytes(500);
        assertEquals(500,subject1.getBytesUsed());
        DataLimit subject2= new DataLimit("Phantose", 3145, 400, "Completed");
        subject2.addBytes(300);
        assertEquals(700,subject2.getBytesUsed());
    }
    @Test
    public void DataLimitCompleted(){
        DataLimit subject1= new DataLimit("Seth",5000,"Hey I'm done");
        assertFalse(subject1.isComplete());
        subject1.addBytes(4999);
        assertFalse(subject1.isComplete());
        subject1.addBytes(1);
        assertTrue(subject1.isComplete());
        DataLimit subject2= new DataLimit("Phantose", 3145, 400, "Completed");
        assertFalse(subject2.isComplete());
        subject2.addBytes(2744);
        assertFalse(subject2.isComplete());
        subject2.addBytes(1);
        assertTrue(subject2.isComplete());

    }
    @Test
    public void DataLimittoStringOveride(){
        DataLimit subject1= new DataLimit("Seth",5000,"Hey I'm done");
        String teststring1= subject1.toString();
        String expected= "Seth 5000 0 Hey I'm done";
        assertEquals(expected,teststring1);
        DataLimit subject2= new DataLimit("Phantose", 3145, 400, "Completed");
        String teststring2= subject2.toString();
        expected= "Phantose 3145 400 Completed";
        assertEquals(expected,teststring2);
    }
}
