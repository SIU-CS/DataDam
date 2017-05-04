package com.example.jackson.datadam;

/**
 * Created by Seth on 5/3/2017.
 */

public class TimePeriod {

    private String name;
    private int timeperiod;
    private int timepast;
    private long bytesused=0;
    private String notification;
    private boolean complete=false;
    public TimePeriod(String name, int timeperiod, String notification){
        this.name=name;
        this.timeperiod =timeperiod;
        this.notification=notification;
    }
    public TimePeriod(String name, int timeperiod, int timepast, long bytesused,String notification){
        this.name=name;
        this.timeperiod =timeperiod;
        this.timepast = timepast;
        this.bytesused=bytesused;
        this.notification= notification;
    }
    //Adds bytes and time from current run cycle to the respective totals
    //No flag need for previousbytes due to that being handled by homeactivity
    public void addTime(long newtime, long newbytes){
        bytesused+=newbytes;
        timepast+=newtime;
        if(timepast>timeperiod){
            complete=true;
            //Run toast function..... for Brandt to do
        }

    }
    public long getBytesUsed(){
        return bytesused;
    }
    public int getTimepast(){return timepast;}
    public int getTimeperiod(){
        return timeperiod;
    }
    public String getName(){return name;}
    public boolean isComplete(){return complete;}
    public  String getNotification(){return notification;};

    @Override
    public String toString(){
        return name+" "+timeperiod+ " " + timepast+" "+" "+bytesused + " " + notification;
    }
}
