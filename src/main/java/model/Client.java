package model;

import java.util.concurrent.atomic.AtomicInteger;

public class Client implements Comparable<Client>{
    private int id;
    private int arrivalTime;
    private int serviceTime;
    private int waitingTime;

    public Client() {}
    public Client(int id, int arrivalTime, int serviceTime) {
        this.id= id;
        this.arrivalTime = arrivalTime;
        this.serviceTime = serviceTime;
        this.waitingTime = 0;
    }

    public int getWaitingTime() {return waitingTime;}
    public int getId() {return id;}
    public int getArrivalTime() {return arrivalTime;}
    public int getServiceTime() {return serviceTime;}
    public void setWaitingTime(int waitingTime) {this.waitingTime = waitingTime;}
    public void setId(int id) {this.id = id;}
    public void setArrivalTime(int arrivalTime) {this.arrivalTime = arrivalTime;}
    public void setServiceTime(int serviceTime) {this.serviceTime=serviceTime;}

    @Override
    public int compareTo(Client otherClient) {
        // Compare based on arrivalTime
        return Integer.compare(this.getArrivalTime(), otherClient.getArrivalTime());
    }
    @Override
    public String toString(){return "("+ id+ ", "+ arrivalTime+ ", "+ serviceTime+ ")"; }

}
