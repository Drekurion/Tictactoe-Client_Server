package TictactoeServer;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class TictactoeServer extends Thread
{
    private static final int PORT = 9001;
    private final List<Socket> waiting = new ArrayList<Socket>();

    public static void main(String[] args) throws Exception
    {
        TictactoeServer t = new TictactoeServer();
        t.start();
        t.join();
    }

    public void run()
    {
        try
        {
            ServerSocket server = new ServerSocket(PORT);
            while (true)
            {
                Socket socket = server.accept();
                Socket other = null;
                if (socket != null)
                {
                    synchronized (this)
                    {
                        if (waiting.isEmpty())
                        {
                            waiting.add(socket);
                        }
                        else
                        {
                            other = waiting.get(waiting.size() - 1);
                            waiting.remove(waiting.size() - 1);
                        }
                    }
                    if (other != null)
                    {
                        System.out.println("Establishing connection");
                        establishConnection(socket, other);
                    }
                    else
                    {
                        System.out.println("Waiting for second player");
                    }
                }
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    private void establishConnection(Socket socket, Socket other) throws IOException
    {
        PrintWriter outX = new PrintWriter(socket.getOutputStream(), true);
        outX.println("Side:X");
        Thread thread = new DataSwitch(socket.getInputStream(), other.getOutputStream());
        thread.start();
        PrintWriter outO = new PrintWriter(other.getOutputStream(), true);
        outO.println("Side:O");
        thread = new DataSwitch(other.getInputStream(), socket.getOutputStream());
        thread.start();
    }
}
//Tictactoe Game Server
//Author: Drekurion
