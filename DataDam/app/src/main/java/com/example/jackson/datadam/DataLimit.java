package com.example.jackson.datadam;


public class DataLimit {

    private String name;
    private long datalimit;
    private long bytesused=0;
    private String notification;
    private boolean complete=false;
    public DataLimit(String name, long datalimit, String notification){
        this.name=name;
        this.datalimit =datalimit;
        this.notification=notification;
    }
    public DataLimit(String name, long datalimit, long bytesused,String notification){
        this.name=name;
        this.datalimit =datalimit;
        this.bytesused=bytesused;
        this.notification= notification;
    }
    //Adds bytes from current run cycle to the total number of bytes used.
    //No flag need for previousbytes due to that being handled by homeactivity
    public void addBytes(long newbytes){
        bytesused+=newbytes;
        if(bytesused>datalimit){
            complete=true;
        }

    }
    public long getBytesUsed(){
        return bytesused;
    }
    public long getDataLimit(){
        return datalimit;
    }
    public String getName(){return name;}
    public boolean isComplete(){return complete;}
    public  String getNotification(){return notification;};

    @Override
    public String toString(){
        return name+" "+datalimit+ " "+bytesused + " " + notification;
    }


}



