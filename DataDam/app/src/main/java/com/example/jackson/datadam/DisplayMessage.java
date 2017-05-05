package com.example.jackson.datadam;

/**
 * Created by utsav on 5/5/17.
 */

public class DisplayMessage {

    private String msg1,msg2;
    private int number;

    public DisplayMessage(String Message1, String Message2, int num){
        msg1=Message1;
        msg2=Message2;
        number=num;

    }

    public String getMsg1(){
        return msg1;
    }

    public String getMsg2(){
        return msg2;
    }


    public int square (){
        int x = number;
        return x*x;
    }


    public int countA(){
        int count = 0;
        int word_length = msg1.length();
        for(int i=0;i<word_length;i++){
            if(msg1.charAt(i)=='a' || msg1.charAt(i)=='A'){
                count++;
            }
        }
        return count;
    }
}
