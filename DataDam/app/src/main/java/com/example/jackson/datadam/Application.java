package com.example.jackson.datadam;

/**
 * Created by utsav on 4/3/17.
 */

class Application  {


    private String name;
    private int uid;
    private long previousbytes;
    private long bytes = 0;
    public Application next;




    public Application(String name, int uid, long previousbytes) {
        this.name = name;
        this.uid = uid;
        this.previousbytes = previousbytes;
    }

    public void addBytes(long newbytes) {
        long bytesused = newbytes - previousbytes;
        bytes = bytesused + bytes;
        previousbytes = newbytes;
    }

    public void updatePrevious(long bytes) {
        previousbytes = bytes;
    }

    public int getUid() {
        return uid;
    }

    public long getBytes() {
        return bytes;
    }

    public String getName() {
        return name;
    }


    public Application() {

    }





}
