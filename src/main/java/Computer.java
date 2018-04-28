import java.util.Random;

public class Computer extends Thread
{
    public int location;
    public int identifier;
    public Random generator;
    public int failedTransmissions;

    public Computer(int coordinate, int ID)
    {
        location = coordinate;
        identifier = ID;
        generator = new Random();
        failedTransmissions = 0;
    }

    public void run()
    {
        try
        {
            int delay = generator.nextInt(10);
            sleep(delay * 100);
        }
        catch(InterruptedException e)
        {
            e.printStackTrace();
        }

        while(true)
        {
            if(failedTransmissions >= 16)
            {
                break;
            }

            transmit();
        }
    }

    public void transmit()
    {

    }
}
