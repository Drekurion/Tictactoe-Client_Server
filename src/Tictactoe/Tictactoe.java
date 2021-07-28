package Tictactoe;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Tictactoe
{
    private final JFrame frame = new JFrame("Tictactoe");
    private final List<JLabel> fields = new ArrayList<JLabel>();
    private final String waitingForConnectionPrompt = "Connecting...";
    private final String opponentFoundPrompt = "Opponent found. ";
    private final String yourSidePrompt = "Your side: ";
    private final String yourTurnPrompt = "Your turn.";
    private final String waitingForOpponentPrompt = "Waiting for opponents move";
    private final String victoryPrompt = "You win!";
    private final String lossPrompt = "You lost";
    private JPanel Window;
    private JTextField TF_Upper;
    private JTextField TF_Lower;
    private JPanel Board;
    private JPanel Field0;
    private JPanel Field1;
    private JPanel Field2;
    private JPanel Field3;
    private JPanel Field4;
    private JPanel Field5;
    private JPanel Field6;
    private JPanel Field7;
    private JPanel Field8;
    private JLabel label0;
    private JLabel label1;
    private JLabel label2;
    private JLabel label3;
    private JLabel label4;
    private JLabel label5;
    private JLabel label6;
    private JLabel label7;
    private JLabel label8;
    private BufferedReader in;
    private PrintWriter out;
    private ImageIcon blueCross;
    private ImageIcon redCross;
    private ImageIcon blueCircle;
    private ImageIcon redCircle;
    private char[] data = new char[9];
    private char mark = 'X';
    private boolean yourTurn = false;
    private boolean end = false;
    private Socket socket;

    public Tictactoe()
    {
        prepareGroup();
        loadImages();
        frame.setContentPane(this.Window);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        try
        {
            run();
        }
        catch (Exception e)
        {

            System.out.println(e);
        }
    }

    public static void main(String[] args) throws Exception
    {
        Tictactoe t = new Tictactoe();
    }

    private boolean checkVictory()
    {
        if (data[0] == mark && data[1] == mark && data[2] == mark) return true;
        if (data[3] == mark && data[4] == mark && data[5] == mark) return true;
        if (data[6] == mark && data[7] == mark && data[8] == mark) return true;

        if (data[0] == mark && data[3] == mark && data[6] == mark) return true;
        if (data[1] == mark && data[4] == mark && data[7] == mark) return true;
        if (data[2] == mark && data[5] == mark && data[8] == mark) return true;

        if (data[0] == mark && data[4] == mark && data[8] == mark) return true;
        if (data[2] == mark && data[4] == mark && data[6] == mark) return true;

        return false;
    }

    private void loadImages()
    {
        redCross = new ImageIcon(getClass().getResource("/res/redCross.png"));
        redCircle = new ImageIcon(getClass().getResource("/res/redCircle.png"));
        blueCross = new ImageIcon(getClass().getResource("/res/blueCross.png"));
        blueCircle = new ImageIcon(getClass().getResource("/res/blueCircle.png"));
    }

    private void run() throws Exception
    {
        String serverAddress = getServerAddress();
        TF_Lower.setText(waitingForConnectionPrompt);
        socket = new Socket(serverAddress, 9001);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);
        try
        {
            while (true)
            {
                String line = in.readLine();
                if (line.startsWith("Move:"))
                {
                    for (int i = 0; i < 9; ++i)
                    {
                        data[i] = line.charAt(i + 5);
                    }
                    update();
                    yourTurn = true;
                    TF_Upper.setText(yourSidePrompt + mark);
                    TF_Lower.setText(yourTurnPrompt);
                }
                else if (line.startsWith("Side:"))
                {
                    mark = line.charAt(5);
                    if (line.charAt(5) == 'X')
                    {
                        yourTurn = true;
                        TF_Lower.setText(yourTurnPrompt);
                        System.out.println("Side X");
                    }
                    else if (line.charAt(5) == 'O')
                    {
                        yourTurn = false;
                        TF_Lower.setText(waitingForOpponentPrompt);
                        System.out.println("Side O");
                    }
                    TF_Upper.setText(opponentFoundPrompt + yourSidePrompt + mark);
                }
                else if (line.startsWith("You lost"))
                {
                    for (JLabel field : fields)
                    {
                        field.setEnabled(false);
                    }
                    end = true;
                    TF_Lower.setText(lossPrompt);
                }
            }
        }
        catch (NullPointerException ex)
        {
        }
    }

    private void update()
    {
        for (int i = 0; i < fields.size(); ++i)
        {
            if (fields.get(i).getIcon() == null)
            {
                if (mark == 'X')
                {
                    if (data[i] == 'O')
                    {
                        fields.get(i).setIcon(redCircle);
                        fields.get(i).setDisabledIcon(redCircle);
                    }
                }
                if (mark == 'O')
                {
                    if (data[i] == 'X')
                    {
                        fields.get(i).setIcon(redCross);
                        fields.get(i).setDisabledIcon(redCross);
                    }
                }
                fields.get(i).setEnabled(false);
            }
        }
    }

    private void prepareGroup()
    {
        fields.add(label0);
        fields.add(label1);
        fields.add(label2);
        fields.add(label3);
        fields.add(label4);
        fields.add(label5);
        fields.add(label6);
        fields.add(label7);
        fields.add(label8);

        label0.addMouseListener(new Listener(label0));
        label1.addMouseListener(new Listener(label1));
        label2.addMouseListener(new Listener(label2));
        label3.addMouseListener(new Listener(label3));
        label4.addMouseListener(new Listener(label4));
        label5.addMouseListener(new Listener(label5));
        label6.addMouseListener(new Listener(label6));
        label7.addMouseListener(new Listener(label7));
        label8.addMouseListener(new Listener(label8));
    }

    private String getServerAddress()
    {
        return JOptionPane.showInputDialog(
                frame,
                "Enter IP Address of the Server:",
                "Welcome to Tictactoe!",
                JOptionPane.QUESTION_MESSAGE);
    }

    private String getData()
    {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 9; ++i)
        {
            sb.append(data[i]);
        }
        return sb.toString();
    }

    private class Listener extends MouseAdapter
    {
        private JLabel source;

        public Listener(JLabel label)
        {
            this.source = label;
        }

        @Override
        public void mouseClicked(MouseEvent e)
        {
            if (yourTurn)
            {
                if (source.getIcon() == null)
                {
                    if (mark == 'X')
                    {
                        source.setIcon(blueCross);
                        source.setDisabledIcon(blueCross);
                    }
                    else if (mark == 'O')
                    {
                        source.setIcon(blueCircle);
                        source.setDisabledIcon(blueCircle);
                    }
                    int position = Integer.parseInt(source.getName());
                    data[position] = mark;
                    if (checkVictory())
                    {
                        out.println("You lost");
                        for (JLabel field : fields)
                        {
                            field.setEnabled(false);
                        }
                        yourTurn = false;
                        end = true;
                        TF_Lower.setText(victoryPrompt);
                    }
                    else
                    {
                        source.setEnabled(false);
                        out.println("Move:" + getData());
                        yourTurn = false;
                        TF_Lower.setText(waitingForOpponentPrompt);
                    }
                }
            }
        }
    }
}
