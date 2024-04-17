package logic;

import model.Client;
import model.Server;

import java.util.List;

public class StrategyQueue implements Strategy {
    @Override
    public void addClient(List<Server> servers, Client client) {
        int numberOfClients= 0;
        int minim= Integer.MAX_VALUE;
        Server selectedServer = null;

        for(Server i: servers)
            if(i!=null) {
                numberOfClients= 0;

                for (Client j : i.getClients())
                    if (j != null)
                        numberOfClients++;

                if(numberOfClients< minim) {
                    minim = numberOfClients;
                    selectedServer = i;
                }
            }

        if(selectedServer != null)
            selectedServer.addClient(client);
    }
}
