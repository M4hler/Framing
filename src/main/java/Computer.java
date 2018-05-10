import java.util.Random;

public class Computer extends Thread
{
    public int location;
    public int identifier;
    public Random generator;
    public int messageWidth;
    boolean collision;

    public Computer(int coordinate, int ID, int width)
    {
        location = coordinate;
        identifier = ID;
        generator = new Random();
        messageWidth = width;
        collision = false;
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

        System.out.println("Computer " + identifier + " here, starting work");

        transmit();
    }

    private void transmit()
    {
        String[] array = Ethernet.getTransmissionMedium();
        int numberOfComputers = Ethernet.getNumberOfComputers();

        int targetComputer = generator.nextInt(numberOfComputers);
        while(targetComputer == identifier)
        {
            targetComputer = generator.nextInt(numberOfComputers);
        }

        int messageStartPointer = location;
        int messageEndPointer = location;

        int counter = 0;
        while(counter < 10)
        {
            if (targetComputer < identifier)
            {
                synchronized (array) //synchronized (array[messageStartPointer])
                {
                    if(Math.abs(messageStartPointer - messageEndPointer) == 0)
                    {
                        messageStartPointer--;
                        if(array[messageStartPointer].equals("Z"))
                            array[messageStartPointer] = String.valueOf(targetComputer);
                        else if(array[messageStartPointer].equals("Computer" + targetComputer))
                        {
                            //TODO change state of target computer to listening
                        }
                        else if(array[messageStartPointer].startsWith("Computer"))
                        {
                            //TODO nothing or change state to listening but do not process message
                        }
                        else
                        {
                            array[messageStartPointer] = "X";
                            //TODO send message to other thread about collision
                            collision = true;
                        }
                    }
                    else if(Math.abs(messageStartPointer - messageEndPointer) == 1)
                    {
                        messageStartPointer--;
                        if(array[messageStartPointer].equals("Z"))
                        {
                            array[messageStartPointer] = String.valueOf(targetComputer);
                            array[messageStartPointer + 1] = String.valueOf(identifier);
                        }
                        else if(array[messageStartPointer].equals("Computer" + targetComputer))
                        {
                            //TODO change state of target computer to listening
                            array[messageStartPointer + 1] = String.valueOf(identifier);
                        }
                        else if(array[messageStartPointer].startsWith("Computer"))
                        {
                            //TODO nothing or change state to listening but do not process message
                            array[messageStartPointer + 1] = String.valueOf(identifier);
                        }
                        else
                        {
                            array[messageStartPointer + 1] = String.valueOf(identifier);
                            array[messageStartPointer] = "X";
                            //TODO send message to other thread about collision
                            collision = true;
                        }
                    }
                    else if(Math.abs(messageStartPointer - messageEndPointer) < messageWidth)
                    {
                        messageStartPointer--;
                        if(array[messageStartPointer].equals("Z"))
                        {
                            int i;
                            for(i = 0; i < Math.abs(messageStartPointer - messageEndPointer) - 1; i++)
                            {
                                array[messageStartPointer + i] = array[messageStartPointer + i + 1];
                            }

                            char c = (char)(identifier + 65);
                            array[messageStartPointer + i] = String.valueOf(c);
                        }
                        else if(array[messageStartPointer].equals("Computer" + targetComputer))
                        {
                            //TODO change state of target computer to listening
                            int i;
                            for(i = 1; i < Math.abs(messageStartPointer - messageEndPointer) - 1; i++)
                            {
                                array[messageStartPointer + i] = array[messageStartPointer + i + 1];
                            }

                            char c = (char)(identifier + 65);
                            array[messageStartPointer + i] = String.valueOf(c);
                        }
                        else if(array[messageStartPointer].startsWith("Computer"))
                        {
                            //TODO nothing or change state to listening but do not process message
                            int i;
                            for(i = 1; i < Math.abs(messageStartPointer - messageEndPointer) - 1; i++)
                            {
                                array[messageStartPointer + i] = array[messageStartPointer + i + 1];
                            }

                            char c = (char)(identifier + 65);
                            array[messageStartPointer + i] = String.valueOf(c);
                        }
                        else
                        {
                            int i;
                            for(i = 1; i < Math.abs(messageStartPointer - messageEndPointer) - 1; i++)
                            {
                                array[messageStartPointer + i] = array[messageStartPointer + i + 1];
                            }

                            char c = (char)(identifier + 65);
                            array[messageStartPointer + i] = String.valueOf(c);

                            //TODO send message to other thread about collision
                            collision = true;
                        }
                    }
                    else
                    {
                        messageStartPointer--;
                        if(array[messageStartPointer].equals("Z"))
                        {
                            int i;
                            for(i = 0; i < Math.abs(messageStartPointer - messageEndPointer) - 1; i++)
                            {
                                array[messageStartPointer + i] = array[messageStartPointer + i + 1];
                            }

                            char c = (char)(identifier + 65);
                            array[messageStartPointer + i] = String.valueOf(c);
                        }
                        else if(array[messageStartPointer].equals("Computer" + targetComputer))
                        {
                            //TODO change state of target computer to listening
                            int i;
                            for(i = 1; i < Math.abs(messageStartPointer - messageEndPointer) - 1; i++)
                            {
                                array[messageStartPointer + i] = array[messageStartPointer + i + 1];
                            }

                            char c = (char)(identifier + 65);
                            array[messageStartPointer + i] = String.valueOf(c);
                        }
                        else if(array[messageStartPointer].startsWith("Computer"))
                        {
                            //TODO nothing or change state to listening but do not process message
                            int i;
                            for(i = 1; i < Math.abs(messageStartPointer - messageEndPointer) - 1; i++)
                            {
                                array[messageStartPointer + i] = array[messageStartPointer + i + 1];
                            }

                            char c = (char)(identifier + 65);
                            array[messageStartPointer + i] = String.valueOf(c);
                        }
                        else
                        {
                            int i;
                            for(i = 1; i < Math.abs(messageStartPointer - messageEndPointer) - 1; i++)
                            {
                                array[messageStartPointer + i] = array[messageStartPointer + i + 1];
                            }

                            char c = (char)(identifier + 65);
                            array[messageStartPointer + i] = String.valueOf(c);

                            array[messageStartPointer] = "X";
                            //TODO send message to other thread about collision
                            collision = true;
                        }

                        messageEndPointer--;
                        array[messageEndPointer] = "Z";
                    }

                    if(collision == true)
                    {
                        for(int i = messageStartPointer; i < messageEndPointer; i++)
                            array[i] = "X";
                    }

                    for(int i = 0; i < array.length; i++)
                    {
                        System.out.print(array[i]);
                        if(array[i].startsWith("Computer"))
                            System.out.print(" ");
                    }
                }
            }
            else // targetComputer > identifier
            {
                synchronized (array)
                {
                    if(Math.abs(messageStartPointer - messageEndPointer) == 0)
                    {
                        messageStartPointer++;
                        if(array[messageStartPointer].equals("Z"))
                            array[messageStartPointer] = String.valueOf(targetComputer);
                        else if(array[messageStartPointer].equals("Computer" + targetComputer))
                        {
                            //TODO change state of target computer to listening
                        }
                        else if(array[messageStartPointer].startsWith("Computer"))
                        {
                            //TODO nothing or change state to listening but do not process message
                        }
                        else
                        {
                            array[messageStartPointer] = "X";
                            //TODO send message to other thread about collision
                            collision = true;
                        }
                    }
                    else if(Math.abs(messageStartPointer - messageEndPointer) == 1)
                    {
                        messageStartPointer++;
                        if(array[messageStartPointer].equals("Z"))
                        {
                            array[messageStartPointer] = String.valueOf(targetComputer);
                            array[messageStartPointer - 1] = String.valueOf(identifier);
                        }
                        else if(array[messageStartPointer].equals("Computer" + targetComputer))
                        {
                            //TODO change state of target computer to listening
                            array[messageStartPointer - 1] = String.valueOf(identifier);
                        }
                        else if(array[messageStartPointer].startsWith("Computer"))
                        {
                            //TODO nothing or change state to listening but do not process message
                            array[messageStartPointer - 1] = String.valueOf(identifier);
                        }
                        else
                        {
                            array[messageStartPointer - 1] = String.valueOf(identifier);
                            array[messageStartPointer] = "X";
                            //TODO send message to other thread about collision
                            collision = true;
                        }
                    }
                    else if(Math.abs(messageStartPointer - messageEndPointer) < messageWidth)
                    {
                        messageStartPointer++;
                        if(array[messageStartPointer].equals("Z"))
                        {
                            int i;
                            for(i = 0; i < Math.abs(messageStartPointer - messageEndPointer) - 1; i++)
                            {
                                array[messageStartPointer - i] = array[messageStartPointer - i - 1];
                            }

                            char c = (char)(identifier + 65);
                            array[messageStartPointer - i] = String.valueOf(c);
                        }
                        else if(array[messageStartPointer].equals("Computer" + targetComputer))
                        {
                            //TODO change state of target computer to listening
                            int i;
                            for(i = 1; i < Math.abs(messageStartPointer - messageEndPointer) - 1; i++)
                            {
                                array[messageStartPointer - i] = array[messageStartPointer - i - 1];
                            }

                            char c = (char)(identifier + 65);
                            array[messageStartPointer - i] = String.valueOf(c);
                        }
                        else if(array[messageStartPointer].startsWith("Computer"))
                        {
                            //TODO nothing or change state to listening but do not process message
                            int i;
                            for(i = 1; i < Math.abs(messageStartPointer - messageEndPointer) - 1; i++)
                            {
                                array[messageStartPointer - i] = array[messageStartPointer - i - 1];
                            }

                            char c = (char)(identifier + 65);
                            array[messageStartPointer - i] = String.valueOf(c);
                        }
                        else
                        {
                            int i;
                            for(i = 1; i < Math.abs(messageStartPointer - messageEndPointer) - 1; i++)
                            {
                                array[messageStartPointer - i] = array[messageStartPointer - i - 1];
                            }

                            char c = (char)(identifier + 65);
                            array[messageStartPointer - i] = String.valueOf(c);

                            //TODO send message to other thread about collision
                            collision = true;
                        }
                    }
                    else
                    {
                        messageStartPointer++;
                        if(array[messageStartPointer].equals("Z"))
                        {
                            int i;
                            for(i = 0; i < Math.abs(messageStartPointer - messageEndPointer) - 1; i++)
                            {
                                array[messageStartPointer - i] = array[messageStartPointer - i - 1];
                            }

                            char c = (char)(identifier + 65);
                            array[messageStartPointer - i] = String.valueOf(c);
                        }
                        else if(array[messageStartPointer].equals("Computer" + targetComputer))
                        {
                            //TODO change state of target computer to listening
                            int i;
                            for(i = 1; i < Math.abs(messageStartPointer - messageEndPointer) - 1; i++)
                            {
                                array[messageStartPointer - i] = array[messageStartPointer - i - 1];
                            }

                            char c = (char)(identifier + 65);
                            array[messageStartPointer - i] = String.valueOf(c);
                        }
                        else if(array[messageStartPointer].startsWith("Computer"))
                        {
                            //TODO nothing or change state to listening but do not process message
                            int i;
                            for(i = 1; i < Math.abs(messageStartPointer - messageEndPointer) - 1; i++)
                            {
                                array[messageStartPointer - i] = array[messageStartPointer - i - 1];
                            }

                            char c = (char)(identifier + 65);
                            array[messageStartPointer - i] = String.valueOf(c);
                        }
                        else
                        {
                            int i;
                            for(i = 1; i < Math.abs(messageStartPointer - messageEndPointer) - 1; i++)
                            {
                                array[messageStartPointer - i] = array[messageStartPointer - i - 1];
                            }

                            char c = (char)(identifier + 65);
                            array[messageStartPointer - i] = String.valueOf(c);

                            array[messageStartPointer] = "X";
                            //TODO send message to other thread about collision
                            collision = true;
                        }

                        messageEndPointer++;
                        array[messageEndPointer] = "Z";
                    }

                    for(int i = 0; i < array.length; i++)
                    {
                        System.out.print(array[i]);
                        if(array[i].startsWith("Computer"))
                            System.out.print(" ");
                    }
                }
            }
            counter++;

            try
            {
                System.out.println("");
                Thread.sleep(200);
            }
            catch(InterruptedException e)
            {

            }
        }
    }
}
