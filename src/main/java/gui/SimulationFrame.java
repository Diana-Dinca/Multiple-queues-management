package gui;

import logic.Generator;
import logic.SelectionPolicy;
import logic.SimulationManager;
import model.Client;
import model.Server;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;

import static java.lang.Integer.parseInt;

public class SimulationFrame extends JFrame implements ActionListener {
    private JButton backButton;
    private List<Client> waitingClientsQueue;
    private List<Server> servers;
    private int t=0;

    public SimulationFrame(List<Client> clients, List<Server> servers) {
        this.waitingClientsQueue= clients;
        this.servers= servers;

        setTitle("Simulation");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 800);
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
        UIDefaults defaults = UIManager.getLookAndFeelDefaults();
        defaults.put("Button.background", new Color(93, 143, 236));
        defaults.put("Button.foreground", Color.BLACK);

        backButton= new JButton("New Simulation");
        backButton.addActionListener(this);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setOpaque(false);
        buttonPanel.add(backButton);
        backButton.setVisible(true);

        JPanel containerPanel = new JPanel(new BorderLayout()); /*{
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(new Color(204, 229, 255));
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        };*/

        Draw drawPanel = new Draw();
        containerPanel.setOpaque(false);
        containerPanel.add(buttonPanel, BorderLayout.SOUTH);
        containerPanel.add(drawPanel);
        add(containerPanel);
        setVisible(true);
    }
    private class Draw extends JPanel{
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            paintC(g);
        }
    }
    public void paintC(Graphics g){
        Font originalFont = g.getFont();
        g.setFont(originalFont.deriveFont(Font.BOLD));
        g.setColor(Color.BLUE);

        g.drawString("Time: " + t, 850, 30);

        int y = 155;
        int x = 50;
        int diameter = 30;
        int queueNumber = 1;

        for (Server queue : servers) {
            g.setColor(Color.BLUE);
            g.drawString("Queue " + queueNumber + ": ", x, y);
            y += 50;
            queueNumber++;
        }

        y = 140;

        for (Server queue : servers) {
            x = 100;
            for (Client client : queue.getClients()) {
                g.setColor(Color.BLUE);
                x += 60;
                g.fillOval(x, y, diameter, diameter);
                g.drawString(client.toString(),x, y+diameter+15);
            }
            y+=50;
        }

        y = 30;
        x = 50;
        g.drawString("Waiting Clients: ", x, y);
        y += 20;
        for (Client client : waitingClientsQueue) {
            g.drawString(client.toString(),x, y+diameter+15);
            g.setColor(Color.BLUE);
            g.fillOval(x, y, diameter, diameter);
            x += 60;
        }
        t++;
    }

    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == backButton) {
            setVisible(false);
            SimulationInput simulationInput= new SimulationInput();
            simulationInput.setVisible(true);
        }
    }

    public static void main(String[] args) {
        //SwingUtilities.invokeLater(() -> new SimulationFrame().setVisible(true));
    }

}
