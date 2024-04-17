package gui;
import logic.SelectionPolicy;
import logic.SimulationManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static java.lang.Integer.parseInt;

public class SimulationInput extends JFrame implements ActionListener {
    private JTextField numberOfClients;
    private JTextField numberOfQueues;
    private JTextField simulationEnd;
    private JTextField minArrivalTime;
    private JTextField maxArrivalTime;
    private JTextField minServiceTime;
    private JTextField maxServiceTime;
    private JButton startSimulation;
    private JButton validateInput;
    private JComboBox<String> strategyDropdown;
    private int verify= 0;
    private String selectedStrategy;
    private int nrClients= 0, nrQueues= 0;
    private int minArrival= 0, maxArrival= 0, ends= 0;
    private int minService= 0, maxService= 0;
    private SelectionPolicy selectionPolicy;
    public SimulationInput() {
        setTitle("Simulation");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
        UIDefaults defaults = UIManager.getLookAndFeelDefaults();
        defaults.put("Button.background", new Color(93, 143, 236));
        defaults.put("Button.foreground", Color.BLACK);

        numberOfClients = new JTextField(10);
        numberOfQueues = new JTextField(10);
        simulationEnd = new JTextField(10);
        minArrivalTime = new JTextField(10);
        maxArrivalTime = new JTextField(10);
        minServiceTime = new JTextField(10);
        maxServiceTime = new JTextField(10);

        validateInput = new JButton("Validate input");
        validateInput.addActionListener(this);
        startSimulation = new JButton("Start Simulation");
        startSimulation.addActionListener(this);
        Font boldFont = UIManager.getFont("Label.font").deriveFont(Font.BOLD);

        JPanel leftPanel = new JPanel(new GridLayout(8, 2));
        leftPanel.setOpaque(false);
        leftPanel.add(createBoldLabel("Number of Clients:", boldFont));
        leftPanel.add(numberOfClients);
        leftPanel.add(createBoldLabel("Minimum arrival time:", boldFont));
        leftPanel.add(minArrivalTime);
        leftPanel.add(createBoldLabel("Minimum service time:", boldFont));
        leftPanel.add(minServiceTime);
        strategyDropdown = new JComboBox<>(new String[]{"TimeStrategy", "QueueStrategy"});
        leftPanel.add(createBoldLabel("Select Strategy:", boldFont));
        leftPanel.add(strategyDropdown);

        JPanel rightPanel = new JPanel(new GridLayout(8, 2));
        rightPanel.setOpaque(false);
        rightPanel.add(createBoldLabel("Number of Queues:", boldFont));
        rightPanel.add(numberOfQueues);
        rightPanel.add(createBoldLabel("Maximum arrival time:", boldFont));
        rightPanel.add(maxArrivalTime);
        rightPanel.add(createBoldLabel("Maximum service time:", boldFont));
        rightPanel.add(maxServiceTime);
        rightPanel.add(createBoldLabel("Simulation ends at:", boldFont));
        rightPanel.add(simulationEnd);

        JPanel mainPanel = new JPanel(new GridLayout(1, 2));
        mainPanel.setOpaque(false);
        mainPanel.add(leftPanel);
        mainPanel.add(rightPanel);

        JPanel containerPanel = new JPanel(new BorderLayout()){
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(new Color(204, 229, 255));
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        containerPanel.setOpaque(false);
        containerPanel.add(mainPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setOpaque(false);
        buttonPanel.add(validateInput);
        startSimulation.setVisible(false);
        buttonPanel.add(startSimulation);

        JPanel bottomPanel = new JPanel();
        bottomPanel.setOpaque(false);
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.Y_AXIS));
        bottomPanel.add(Box.createVerticalGlue());
        bottomPanel.add(buttonPanel);
        containerPanel.add(bottomPanel, BorderLayout.SOUTH);

        add(containerPanel);
    }
    private JLabel createBoldLabel(String text, Font font) {
        JLabel label = new JLabel(text);
        label.setFont(font);
        label.setForeground(Color.BLUE);
        return label;
    }
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == validateInput) {
            verify= 0;
            try{nrClients= parseInt(numberOfClients.getText());
                if(nrClients <= 0)
                    throw new NumberFormatException();
                verify++;
            } catch(NumberFormatException exception) {
                JOptionPane.showMessageDialog(this, "Number of Clients is not valid!", "Error", JOptionPane.ERROR_MESSAGE);
            }

            try{nrQueues= parseInt(numberOfQueues.getText());
                if(nrQueues <= 0)
                    throw new NumberFormatException();
                verify++;
            } catch(NumberFormatException exception) {
                JOptionPane.showMessageDialog(this, "Number of Queues is not valid!", "Error", JOptionPane.ERROR_MESSAGE);
            }

            try{minArrival= parseInt(minArrivalTime.getText());
                if(minArrival <= 0)
                    throw new NumberFormatException();
                verify++;
            } catch(NumberFormatException exception){
                JOptionPane.showMessageDialog(this, "Minimum Arrival time is not valid!", "Error", JOptionPane.ERROR_MESSAGE);
            }

            try{maxArrival= parseInt(maxArrivalTime.getText());
                if(maxArrival <= 0)
                    throw new NumberFormatException();
                verify++;
            } catch(NumberFormatException exception){
                JOptionPane.showMessageDialog(this, "Maximum Arrival time is not valid!", "Error", JOptionPane.ERROR_MESSAGE);
            }

            if(minArrival >= maxArrival)
                JOptionPane.showMessageDialog(this, "Minimum Arrival Time >= Maximum Arrival Time", "Error", JOptionPane.ERROR_MESSAGE);


            try{ends= parseInt(simulationEnd.getText());
                if(ends <= 0)
                    throw new NumberFormatException();
                verify++;
            } catch(NumberFormatException exception){
                JOptionPane.showMessageDialog(this, "Simulation End Time is not valid!", "Error", JOptionPane.ERROR_MESSAGE);
            }

            try{minService= parseInt(minServiceTime.getText());
                if(minService <= 0)
                    throw new NumberFormatException();
                verify++;
            } catch(NumberFormatException exception){
                JOptionPane.showMessageDialog(this, "Minimum Service Time is not valid!", "Error", JOptionPane.ERROR_MESSAGE);
            }

            try{maxService= parseInt(maxServiceTime.getText());
                if(maxService <= 0)
                    throw new NumberFormatException();
                verify++;
            } catch(NumberFormatException exception){
                JOptionPane.showMessageDialog(this, "Maximum Service Time is not valid!", "Error", JOptionPane.ERROR_MESSAGE);
            }
            if(minService >= maxService)
                JOptionPane.showMessageDialog(this, "Minimum Service Time >= Maximum Service Time", "Error", JOptionPane.ERROR_MESSAGE);

            selectedStrategy = (String) strategyDropdown.getSelectedItem();
        }
        //System.out.println(nrQueues);
        if(verify == 7){
            startSimulation.setVisible(true);
            if (e.getSource() == startSimulation){
                if(selectedStrategy.equals("TimeStrategy"))
                    selectionPolicy= SelectionPolicy.SHORTEST_TIME;
                else
                    selectionPolicy= SelectionPolicy.SHORTEST_QUEUE;
                SimulationManager simulationManager= new SimulationManager(nrClients,nrQueues,minArrival,maxArrival,minService,maxService,ends,selectionPolicy);
                Thread t = new Thread(simulationManager);
                t.start();

                setVisible(false);
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SimulationInput().setVisible(true));
    }
}
