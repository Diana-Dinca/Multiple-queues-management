package logic;
import model.Client;
import model.Server;

import java.util.List;

public class StrategyTime implements Strategy {
    @Override
    public void addClient(List<Server> servers, Client client) {
        int minim= Integer.MAX_VALUE;
        Server selectedServer = null;

        for(Server i: servers)
            if(i!=null)
                if(i.getWaitingPeriod().get()< minim){
                    minim= i.getWaitingPeriod().get();
                    selectedServer = i;
                }

        if(selectedServer != null)
            selectedServer.addClient(client);
    }
}