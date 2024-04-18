package logic;

import gui.SimulationFrame;
import model.Client;
import model.Server;

import javax.swing.*;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SimulationManager implements Runnable {
    //data read from UI
    public int simulationEnd; //maximum processing time - read from UI
    public int maxServiceTime;
    public int minServiceTime;
    public int numberOfServers;
    public int numberOfClients;
    public int minArrivalTime;
    public int maxArrivalTime;
    public SelectionPolicy selectionPolicy = SelectionPolicy.SHORTEST_QUEUE;

    //entity responsible with queue management and client distribution private Scheduler scheduler:
    private Scheduler scheduler;
    //frame for displaying simulation
    private SimulationFrame frame;
    //pool of tasks (client shopping in the store)
    private Generator generator;
    private List<Client> generatedClients;

    public SimulationManager(int numberOfClients, int numberOfServers, int minArrivalTime, int maxArrivalTime, int minServiceTime, int maxServiceTime,  int simulationEnd, SelectionPolicy selectionPolicy) {
        this.simulationEnd = simulationEnd;
        this.maxServiceTime = maxServiceTime;
        this.minServiceTime = minServiceTime;
        this.numberOfServers = numberOfServers;
        this.numberOfClients = numberOfClients;
        this.minArrivalTime = minArrivalTime;
        this.maxArrivalTime = maxArrivalTime;
        this.selectionPolicy = selectionPolicy;

        scheduler = new Scheduler(numberOfServers, numberOfClients);
        scheduler.changeStrategy(selectionPolicy);

        generator = new Generator(minServiceTime, maxServiceTime, minArrivalTime, maxArrivalTime);
        generatedClients= new ArrayList<Client>();
        generatedClients= generator.generateNRandomClients(numberOfClients);

        frame = new SimulationFrame(generatedClients,scheduler.getServers());
    }

    @Override
    public void run() {
        int currentTime= 0, verify= 0, peekHour= 0, period= 0, maxTime= 0;
        double avg= 0;
        FileWriter fileWriter= null;
        try {
            fileWriter= new FileWriter("logOfEvents.txt", false);
            fileWriter= new FileWriter("logOfEvents.txt", true);
            while (currentTime <= simulationEnd && (!generatedClients.isEmpty() || verify!=numberOfServers)) {
                verify= 0;
                Iterator<Client> clientIterator= generatedClients.iterator();// iterate generatedTasks list and pick tasks that have the
                while (clientIterator.hasNext()){
                    Client client= clientIterator.next(); //arrivalTime equal with the currentTime
                    if (client.getArrivalTime() == currentTime){
                        scheduler.dispatchClient(client); // - send task to queue by calling the dispatchTask method from Scheduler

                        for(Server s: scheduler.getServers())
                            synchronized (s){
                            for (Client c : s.getClients())
                                if (c == client) {
                                    avg += s.getWaitingPeriod().get() - c.getServiceTime();
                                }
                        }
                        clientIterator.remove();// - delete client from list
                    }
                }
                period= 0;
                for(Server i: scheduler.getServers()) {
                    if (i.getClients().isEmpty())
                        verify++;
                    period+= i.getWaitingPeriod().get();
                }
                if(period> maxTime)
                {
                    maxTime= period;
                    peekHour= currentTime;
                }
                frame.repaint();// update UI frame

                fileWriter.write("\n\nTime: "+ currentTime);
                fileWriter.write("\nWaiting clients: ");
                for(Client i: generatedClients)
                    if(i!=null){
                        fileWriter.write(i+"; ");
                    }
                int cnt= 1;
                for(Server i: scheduler.getServers())
                    if(i!=null){
                        fileWriter.write("\nQueue "+ (cnt++) + ": ");
                        if(!(i.getClients().isEmpty())){
                            for(Client j: i.getClients())
                                if(j!=null){
                                    fileWriter.write(j+"; ");
                                }
                        }
                        else fileWriter.write("Closed");
                    }
                currentTime++;
                Thread.sleep(1000);  // wait an interval of 1 second

            }
            fileWriter.write("\n\nAverage Waiting Time: "+ avg/(double)numberOfClients +"\nAverage Service Time: " + Generator.servTime +"\nPeek Hour: "+ peekHour);
            fileWriter.close();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        for(Server i: scheduler.getServers()) {
            i.stopLoop();
        }
        JOptionPane.showMessageDialog(new JFrame(), "Average Waiting Time: "+ avg/(double)numberOfClients +"\nAverage Service Time: " + Generator.servTime +"\nPeek Hour: "+ peekHour, "Simulation Details", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void main(String[] args) {
        SimulationManager gen = new SimulationManager(10,2,3,9,3,9,200,SelectionPolicy.SHORTEST_TIME);
        Thread t = new Thread(gen);
        t.start();
    }
}