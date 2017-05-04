package com.example.jackson.datadam;

/**
 * Created by jackson on 5/2/17.
 */
class Application{
    private String name;
    private int uid;
    private long previousbytes;
    private long bytes=0;
    public Application next;

    public Application(String name, int uid, long previousbytes, long bytes){
        this.name=name;
        this.uid=uid;
        this.previousbytes=previousbytes;
        this.bytes= bytes;
    }
    public Application(String name, int uid, long previousbytes){
        this.name=name;
        this.uid=uid;
        this.previousbytes=previousbytes;
        bytes= previousbytes;
    }
    //Adds bytes from current run cycle to the total number of bytes used.
    //Updates previousbytes with the parameter to create a new comparison flag.
    public void addBytes(long newbytes){
        long bytesused= newbytes-previousbytes;
        bytes= bytesused + bytes;
        previousbytes= newbytes;
    }
    //Updates previousbytes with new flag.
    //Only used when application is online
    public void updatePrevious(long bytes){
        previousbytes=bytes;
    }
    public int getUid(){
        return uid;
    }
    public long getBytes(){
        return bytes;
    }
    public String getName(){return name;}

    @Override
    public String toString(){
        return name + " " + bytes / 1000 + "kB";
    }
}
