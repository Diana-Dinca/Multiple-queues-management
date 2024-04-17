package model;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class Server implements Runnable {
    private BlockingQueue<Client> clients;
    private AtomicInteger waitingPeriod;
    private boolean loop;
    public Server() {
        //initialize queue and waitingPeriod
        clients= new LinkedBlockingQueue<>();
        waitingPeriod= new AtomicInteger(0);
        waitingPeriod.set(0);
        loop= true;
    }
    public void addClient (Client newClient) {
        //add client to queue
        clients.add(newClient);
        //increment the waitingPeriod
        waitingPeriod.addAndGet(newClient.getServiceTime());
    }

    public void run() {
        while (loop) {
            try{
                synchronized (clients) {
                    if (clients.isEmpty())
                        continue;

                    Client client = clients.peek();
                    if (client.getServiceTime()> 0) {
                        Thread.sleep(1000);
                        client.setServiceTime(client.getServiceTime()- 1);
                        waitingPeriod.getAndAdd(-1);
                    }
                    if (client.getServiceTime()== 0)
                        clients.poll();
                }
            }
            catch (InterruptedException e){
                throw new RuntimeException(e);
            }
        }
    }
    public synchronized AtomicInteger calculateWaitingPeriod() {
        //waitingPeriod= new AtomicInteger(0);
        waitingPeriod.set(0);
        for(Client c: clients)
            if(c!=null){
                waitingPeriod.getAndAdd(c.getServiceTime());
            }
        return waitingPeriod;
    }

    public AtomicInteger getWaitingPeriod() {return waitingPeriod;}
    public BlockingQueue<Client> getClients() {return clients;}
    public void stopLoop(){ loop= false;}

}