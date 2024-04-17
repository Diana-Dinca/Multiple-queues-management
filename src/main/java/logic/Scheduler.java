package logic;
import model.Client;
import model.Server;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Scheduler {
    private List<Server> servers;
    private int maxNoQueue;
    private int maxClientsPerQueue;
    private Strategy strategy;
    public Scheduler (int maxNoQueue, int maxClientsPerQueue) {
        this.strategy= new StrategyTime();
        this.servers = new ArrayList<Server>();
        this.maxNoQueue= maxNoQueue;
        this.maxClientsPerQueue=maxClientsPerQueue;
        //for maxNoQueue
        ExecutorService executorService= Executors.newFixedThreadPool(maxNoQueue);
        for (int i=0; i<maxNoQueue; i++){
            // - create queue object
            Server queue= new Server();
            servers.add(queue);
            // - create thread with the object
           executorService.execute(queue);
        }
        executorService.shutdown();
    }

    public void changeStrategy (SelectionPolicy policy) {
        //apply strategy pattern to instantiate the strategy with the concrete
        // strategy corresponding to policy
        if (policy == SelectionPolicy.SHORTEST_QUEUE) {
            strategy = new StrategyQueue();
        }
        if (policy == SelectionPolicy.SHORTEST_TIME){
            strategy = new StrategyTime();
        }
    }
    public void dispatchClient (Client client){
        //call the strategy addClient method
        strategy.addClient(servers,client);
    }

    public List<Server> getServers() {return servers;}

    public int getMaxNoQueue() {return maxNoQueue;}

    public int getMaxClientsPerQueue() {return maxClientsPerQueue;}

    public Strategy getStrategy() {return strategy;}

    public List<Server> getQueues() {
        return servers;
    }
}