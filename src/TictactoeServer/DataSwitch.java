package TictactoeServer;

import java.io.*;

public class DataSwitch extends Thread
{
    private final InputStream in;
    private final OutputStream out;

    public DataSwitch(InputStream in, OutputStream out)
    {
        this.in = in;
        this.out = out;
        setDaemon(true);
    }

    public void run()
    {
        try
        {
            for (int b = in.read(); b != -1; b = in.read())
            {
                out.write(b);
            }
        }
        catch (IOException e)
        {
            System.out.println(e);
        }
        finally
        {
            try
            {
                out.close();
            }
            catch (IOException e)
            {
                System.out.println(e);
            }
        }
    }

    private boolean checkWin(String data, char mark)
    {
        if (data.charAt(0) == mark && data.charAt(1) == mark && data.charAt(2) == mark) return true;
        if (data.charAt(3) == mark && data.charAt(4) == mark && data.charAt(5) == mark) return true;
        if (data.charAt(6) == mark && data.charAt(7) == mark && data.charAt(8) == mark) return true;

        if (data.charAt(0) == mark && data.charAt(3) == mark && data.charAt(6) == mark) return true;
        if (data.charAt(1) == mark && data.charAt(4) == mark && data.charAt(7) == mark) return true;
        if (data.charAt(2) == mark && data.charAt(5) == mark && data.charAt(8) == mark) return true;

        if (data.charAt(0) == mark && data.charAt(4) == mark && data.charAt(8) == mark) return true;
        if (data.charAt(2) == mark && data.charAt(4) == mark && data.charAt(6) == mark) return true;

        return false;
    }
}
//Part of Tictactoe Game Server
//Author: Drekurion