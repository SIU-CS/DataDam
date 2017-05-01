package com.example.jackson.datadam;


public class DataLimit {

    private String name;
    private long datalimit;
    private long bytesused=0;
    public DataLimit(String name, long datalimit){
        this.name=name;
        this.datalimit =datalimit;
    }
    //Adds bytes from current run cycle to the total number of bytes used.
    //No flag need for previousbytes due to that being handled by homeactivity
    public void addBytes(long newbytes){
       bytesused+=newbytes;
    }
    public long getBytesUsed(){
        return bytesused;
    }
    public long getDataLimit(){
        return datalimit;
    }
    public String getName(){return name;}

    @Override
    public String toString(){
        return name+" "+datalimit+ " "+bytesused;
    }


}
