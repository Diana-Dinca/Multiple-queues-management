package logic;

import model.Client;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Generator {
    Integer minProcessingTime;
    Integer maxProcessingTime;
    Integer minArrivalTime;
    Integer maxArrivalTime;
    public static double servTime= 0;

    public Generator(Integer minProcessingTime, Integer maxProcessingTime, Integer minArrivalTime, Integer maxArrivalTime) {
        this.minProcessingTime = minProcessingTime;
        this.maxProcessingTime = maxProcessingTime;
        this.minArrivalTime = minArrivalTime;
        this.maxArrivalTime = maxArrivalTime;
    }
    List<Client> generateNRandomClients(Integer N) {
        List<Client> clients = new ArrayList<>();
        Random random = new Random();
        // generate N random tasks:
        for (int i = 0; i < N; i++) {
            int arrivalTime = random.nextInt(minArrivalTime, maxArrivalTime);
            int serviceTime = random.nextInt(minProcessingTime,maxProcessingTime);
            Client client = new Client(i+1, arrivalTime, serviceTime);
            servTime += client.getServiceTime();
            clients.add(client);
        }
        servTime/=(double) N;
        //sort list with respect to arrivalTime
        Collections.sort(clients);
        return clients;
    }

}
