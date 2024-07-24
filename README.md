#### Efficient management of queues is crucial to balancing service quality and cost-effectiveness. This can be achieved by efficiently assigning clients to queues based on their arrival times and service durations. To model the system, we can identify the main entities: Clients and Queues. Clients arrive at the system, enter queues, wait, are served, and then leave. In this system, the main objective is to minimize the waiting time for clients before they are served.
#### The application's implementation is organized into three distinct packages:
###### • *gui package*: this package houses the SimulationInput class, which manages the graphical user interface for inputting the data, and SimulationFrame class, which displays the dynamic simulation;
###### • *model package*: within this package, you'll find the Client and Server classes, essential for representing and manipulating the clients and queues;
###### • *logic package*: this package includes the Strategy interface, StrategyQueue and StrategyTime classes, which implements Strategy, SelectionPolicy enumeration, Generator class, Scheduler class and SimulationManager class, providing the necessary methods for generating clients and dispatching them into the right queues, based on the strategy selected.
 For an improved functionality, I used a BlockingQueue<Client> for storing the list of clients on each queue. The clients contain an id, their arrival time at the queue, their service time and the waiting time.
 The challenge I encountered during the implementation of this project was safely scheduling the threads, and also creating a visually pleasing interface for the manager to see the placement of each costumer and their time in queue:
###### The method below dispatches the clients to the queues, based on the chosen strategy, it updates the simulation frame and it writes in the log file, the progress made while simulating;
```java
    public void run() {
        int currentTime= 0, verify= 0, peekHour= 0, period= 0, maxTime= 0;
        double avg= 0;
        FileWriter fileWriter= null;
        try {
            fileWriter= new FileWriter("logOfEvents.txt", false);
            fileWriter= new FileWriter("logOfEvents.txt", true);
            while (currentTime <= simulationEnd && (!generatedClients.isEmpty() || verify!=numberOfServers)) {
                verify= 0;
                Iterator<Client> clientIterator= generatedClients.iterator(); // iterate generatedTasks list and pick tasks that have the
                while (clientIterator.hasNext()){
                    Client client= clientIterator.next(); // arrivalTime equal with the currentTime
                    if (client.getArrivalTime() == currentTime){
                        scheduler.dispatchClient(client); // send task to queue by calling the dispatchTask method from Scheduler

                        for(Server s: scheduler.getServers())
                            synchronized (s){
                            for (Client c : s.getClients())
                                if (c == client) { avg += s.getWaitingPeriod().get() - c.getServiceTime();}
                        }
                        clientIterator.remove(); // delete client from list
                    }
                }
                period= 0;
                for(Server i: scheduler.getServers()) {
                    if (i.getClients().isEmpty()) verify++;
                    period+= i.getWaitingPeriod().get();
                }
                if(period> maxTime) {
                    maxTime= period;
                    peekHour= currentTime;
                }
                frame.repaint();// update UI frame

                fileWriter.write("\n\nTime: "+ currentTime);
                fileWriter.write("\nWaiting clients: ");
                for(Client i: generatedClients)
                    if(i!=null){ fileWriter.write(i+"; "); }
                int cnt= 1;
                for(Server i: scheduler.getServers())
                    if(i!=null){
                        fileWriter.write("\nQueue "+ (cnt++) + ": ");
                        if(!(i.getClients().isEmpty())){
                            for(Client j: i.getClients())
                                if(j!=null){ fileWriter.write(j+"; "); }
                        } else fileWriter.write("Closed");
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
```
###### For the display of the on-going process of the clients I considered a dynamic simulation, where blue circles represent the Clients waiting in line, and the numbers below represents the id, arrival time and service time (which decreases with every rising of time unit) of each person.
![image](https://github.com/user-attachments/assets/11bfe9ee-733f-453b-b037-c05ecbce8c41)
#### After the simulation has ended, an additional page will appear and show the user the Average Waiting Time, the Average Service Time and the Peek hour.
#### I have conducted many tests: first, I tested the validation functionality and performance by introducing invalid data. Secondly, I tested various simulation cases, calculating the expected output manually and comparing it with the output generated by the application.
