import java.util.Random;

public class Computer extends Thread
{
    public int location;
    public int identifier;
    public Random generator;
    public int targetComputer;

    public Computer(int coordinate, int ID)
    {
        location = coordinate;
        identifier = ID;
        generator = new Random();
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

        int counter = 0;
        while(counter < 1)  //main loop
        {
            transmit();

            try
            {
                int a = generator.nextInt(10);
                sleep(a * 100);
            }
            catch(InterruptedException e)
            {

            }

            counter++;
        }
    }

    private void transmit()
    {
        String[] array = Ethernet.getTransmissionMedium();
        int numberOfComputers = Ethernet.getNumberOfComputers();

        synchronized (array)
        {
            System.out.println("Computer " + identifier + " here, starting work");
        }

        targetComputer = generator.nextInt(numberOfComputers);
        while(targetComputer == identifier)
        {
            targetComputer = generator.nextInt(numberOfComputers);
        }

        int messageStartPointerLeft = location;
        int messageEndPointerLeft = location;
        int messageStartPointerRight = location;
        int messageEndPointerRight = location;
        boolean messageLeftGoingLeft = true;
        boolean messageRightGoingRight = true;
        boolean leftActive = true;
        boolean rightActive = true;
        boolean collision = false;
        int collisionCounter = 0;

        while(leftActive == true || rightActive == true || collision == true)
        {
            synchronized (array)
            {
                if(messageLeftGoingLeft == true && leftActive == true) //left message going left, until it reaches end of cable
                {
                    messageStartPointerLeft--;

                    if(array[messageStartPointerLeft].equals("Z")) //OK, change array field
                    {
                        if(array[messageStartPointerLeft + 1].startsWith("Computer") && array[messageEndPointerLeft].equals("X")) //collision update
                        {
                            array[messageStartPointerLeft] = "X";
                            messageEndPointerLeft--;
                            array[messageEndPointerLeft + 1] = "Z";
                        }
                        else if(array[messageStartPointerLeft + 1].startsWith("Computer")) //normal update
                        {
                            array[messageStartPointerLeft] = String.valueOf(targetComputer);
                            if(messageEndPointerLeft != location)
                            {
                                array[messageEndPointerLeft] = "Z";
                                messageEndPointerLeft--;
                            }
                        }
                        else //normal update
                        {
                            array[messageStartPointerLeft] = array[messageStartPointerLeft + 1];
                            messageEndPointerLeft--;

                            if(array[messageEndPointerLeft + 1].startsWith("Computer") && array[messageStartPointerLeft + 1].equals("X"))
                                array[messageEndPointerLeft] = "X";
                            else if(array[messageEndPointerLeft + 1].startsWith("Computer"))
                                array[messageEndPointerLeft] = String.valueOf(identifier);
                            else
                            {
                                array[messageEndPointerLeft] = array[messageEndPointerLeft + 1];
                                array[messageEndPointerLeft + 1] = "Z";
                            }
                        }
                    }
                    else if(array[messageStartPointerLeft].startsWith("Computer")) //send info to Computer
                    {
                        int computer = Integer.valueOf(array[messageStartPointerLeft].substring(8));

                        if(computer == identifier)
                        {
                            if(!array[messageStartPointerLeft + 1].equals("X"))
                            {
                                System.out.println("Computer" + identifier + " left message delivered successfully");
                            }
                            else
                            {
                                System.out.println("Collision occurred");
                                collision = true;
                            }

                            leftActive = false;
                            array[messageEndPointerLeft] = "Z";
                        }
                        else
                        {
                            Ethernet.getComputer(computer).message(array[messageStartPointerLeft - 1]);
                            try
                            {
                                array.wait(array.length * 10 * 2);
                            }
                            catch(InterruptedException e)
                            {

                            }
                        }
                        array[messageStartPointerLeft + 1] = array[messageEndPointerLeft];
                        messageEndPointerLeft--;
                        array[messageEndPointerLeft + 1] = "Z";
                    }
                    else //collision
                    {
                        if(messageStartPointerLeft + 1 == location)
                        {
                            messageStartPointerLeft++;
                            continue;
                        }

                        array[messageStartPointerLeft] = "X";

                        if(!array[messageEndPointerLeft].startsWith("Computer"))
                            array[messageEndPointerLeft] = "Z";

                        messageEndPointerLeft--;

                        if(!array[messageEndPointerLeft].startsWith("Computer"))
                            array[messageEndPointerLeft] = "X";
                    }
                }
                else if(leftActive == true)//end of cable, message needs to return to right side
                {
                    messageStartPointerLeft++;

                    if(messageStartPointerLeft >= array.length) //UPDATE
                        messageStartPointerLeft--;

                    if(array[messageStartPointerLeft].equals("Z")) //OK, change array field
                    {
                        if(array[messageStartPointerLeft - 1].startsWith("Computer") && array[messageEndPointerLeft].equals("X")) //collision update
                        {
                            array[messageStartPointerLeft] = "X";
                            messageEndPointerLeft++;
                            array[messageEndPointerLeft - 1] = "Z";
                        }
                        else if(array[messageStartPointerLeft - 1].startsWith("Computer")) //normal update
                        {
                            array[messageStartPointerLeft] = String.valueOf(targetComputer);
                            if(messageEndPointerLeft != location)
                            {
                                array[messageEndPointerLeft] = "Z";
                                messageEndPointerLeft++;
                            }
                        }
                        else //normal update
                        {
                            array[messageStartPointerLeft] = array[messageStartPointerLeft - 1];
                            messageEndPointerLeft++;

                            if(array[messageEndPointerLeft - 1].startsWith("Computer") && array[messageStartPointerLeft - 1].equals("X"))
                                array[messageEndPointerLeft] = "X";
                            else if(array[messageEndPointerLeft - 1].startsWith("Computer"))
                                array[messageEndPointerLeft] = String.valueOf(identifier);
                            else
                            {
                                array[messageEndPointerLeft] = array[messageEndPointerLeft - 1];
                                array[messageEndPointerLeft - 1] = "Z";
                            }
                        }
                    }
                    else if(array[messageStartPointerLeft].startsWith("Computer")) //send info to Computer
                    {
                        int computer = Integer.valueOf(array[messageStartPointerLeft].substring(8));

                        if(computer == identifier)
                        {
                            if(!array[messageStartPointerLeft - 1].equals("X"))
                            {
                                System.out.println("Computer" + identifier + " left message delivered successfully");
                            }
                            else
                            {
                                System.out.println("Collision occurred");
                                collision = true;
                            }

                            leftActive = false;
                            array[messageEndPointerLeft] = "Z";
                        }
                        else
                        {
                            Ethernet.getComputer(computer).message(array[messageStartPointerLeft - 1]);
                            try
                            {
                                array.wait(array.length * 10 * 2);
                            }
                            catch(InterruptedException e)
                            {

                            }
                        }
                        array[messageStartPointerLeft - 1] = array[messageEndPointerLeft];
                        messageEndPointerLeft++;
                        array[messageEndPointerLeft - 1] = "Z";
                    }
                    else //collision
                    {
                        if(messageStartPointerLeft - 1 == location)
                        {
                            messageStartPointerLeft--;
                            continue;
                        }

                        array[messageStartPointerLeft] = "X";

                        if(!array[messageEndPointerLeft].startsWith("Computer"))
                            array[messageEndPointerLeft] = "Z";

                        messageEndPointerLeft++;

                        if(!array[messageEndPointerLeft].startsWith("Computer"))
                            array[messageEndPointerLeft] = "X";
                    }
                }

                if(messageRightGoingRight == true && rightActive == true) //same for initially right going message
                {
                    messageStartPointerRight++;
                    if(array[messageStartPointerRight].equals("Z")) //OK, change array field
                    {
                        if(array[messageStartPointerRight - 1].startsWith("Computer") && array[messageEndPointerRight].equals("X"))
                        {
                            array[messageStartPointerRight] = "X";
                            messageEndPointerRight++;
                            array[messageEndPointerRight - 1] = "Z";
                        }
                        else if(array[messageStartPointerRight - 1].startsWith("Computer"))
                        {
                            array[messageStartPointerRight] = String.valueOf(targetComputer);
                            if(messageEndPointerRight != location)
                            {
                                array[messageEndPointerRight] = "Z";
                                messageEndPointerRight++;
                            }
                        }
                        else
                        {
                            array[messageStartPointerRight] = array[messageStartPointerRight - 1];
                            messageEndPointerRight++;

                            if(array[messageEndPointerRight - 1].startsWith("Computer") && array[messageStartPointerRight - 1].equals("X"))
                                array[messageEndPointerRight] = "X";
                            else if(array[messageEndPointerRight - 1].startsWith("Computer"))
                                array[messageEndPointerRight] = String.valueOf(identifier);
                            else
                            {
                                array[messageEndPointerRight] = array[messageEndPointerRight - 1];
                                array[messageEndPointerRight - 1] = "Z";
                            }
                        }
                    }
                    else if(array[messageStartPointerRight].startsWith("Computer")) //send info to Computer
                    {
                        int computer = Integer.valueOf(array[messageStartPointerRight].substring(8));

                        Ethernet.getComputer(computer).message(array[messageStartPointerRight - 1]);
                        try
                        {
                            array.wait(array.length * 10 * 2);
                        }
                        catch(InterruptedException e)
                        {

                        }
                        array[messageStartPointerRight - 1] = array[messageEndPointerRight];
                        messageEndPointerRight++;
                        array[messageEndPointerRight - 1] = "Z";
                    }
                    else //collision
                    {
                        if(messageStartPointerRight - 1 == location)
                        {
                            messageStartPointerRight--;
                            continue;
                        }

                        array[messageStartPointerRight] = "X";

                        if(!array[messageEndPointerRight].startsWith("Computer"))
                            array[messageEndPointerRight] = "Z";

                        messageEndPointerRight++;

                        if(!array[messageEndPointerRight].startsWith("Computer"))
                            array[messageEndPointerRight] = "X";
                    }
                }
                else if(rightActive == true) //right message reached end of cable, going back to left side
                {
                    messageStartPointerRight--;

                    if(messageStartPointerRight <= 0)
                        messageStartPointerRight++;

                    if(array[messageStartPointerRight].equals("Z")) //OK, change array field
                    {
                        if(array[messageStartPointerRight + 1].startsWith("Computer") && array[messageEndPointerRight].equals("X"))
                        {
                            array[messageStartPointerRight] = "X";
                            messageEndPointerRight--;
                            array[messageEndPointerRight + 1] = "Z";
                        }
                        else if(array[messageStartPointerRight + 1].startsWith("Computer"))
                        {
                            array[messageStartPointerRight] = String.valueOf(targetComputer);
                            if(messageEndPointerRight != location)
                            {
                                array[messageEndPointerRight] = "Z";
                                messageEndPointerRight--;
                            }
                        }
                        else
                        {
                            array[messageStartPointerRight] = array[messageStartPointerRight + 1];
                            messageEndPointerRight--;

                            if(array[messageEndPointerRight + 1].startsWith("Computer") && array[messageStartPointerRight + 1].equals("X"))
                                array[messageEndPointerRight] = "X";
                            else if(array[messageEndPointerRight + 1].startsWith("Computer"))
                                array[messageEndPointerRight] = String.valueOf(identifier);
                            else
                            {
                                array[messageEndPointerRight] = array[messageEndPointerRight + 1];
                                array[messageEndPointerRight + 1] = "Z";
                            }
                        }
                    }
                    else if(array[messageStartPointerRight].startsWith("Computer")) //send info to Computer
                    {
                        int computer = Integer.valueOf(array[messageStartPointerRight].substring(8));

                        if(computer == identifier)
                        {
                            if(!array[messageStartPointerLeft - 1].equals("X"))
                            {
                                System.out.println("Computer" + identifier + " right message delivered successfully");
                            }
                            else
                            {
                                System.out.println("Collision occurred");
                                collision = true;
                            }

                            rightActive = false;
                            array[messageEndPointerRight] = "Z";
                        }
                        else
                        {
                            Ethernet.getComputer(computer).message(array[messageStartPointerRight + 1]);
                            try
                            {
                                array.wait(array.length * 10 * 2);
                            }
                            catch(InterruptedException e)
                            {

                            }
                        }
                        array[messageStartPointerRight + 1] = array[messageEndPointerRight];
                        messageEndPointerRight--;
                        array[messageEndPointerRight + 1] = "Z";
                    }
                    else //collision
                    {
                        if(messageStartPointerRight + 1 == location)
                        {
                            messageStartPointerRight++;
                            continue;
                        }

                        array[messageStartPointerRight] = "X";

                        if(!array[messageEndPointerRight].startsWith("Computer"))
                            array[messageEndPointerRight] = "Z";

                        messageEndPointerRight--;

                        if(!array[messageEndPointerRight].startsWith("Computer"))
                            array[messageEndPointerRight] = "X";
                    }
                }

                if(array[messageStartPointerLeft].equals("X") || array[messageEndPointerLeft].equals("X"))
                {
                    if(!array[messageStartPointerLeft].startsWith("Computer"))
                    {
                        array[messageStartPointerLeft] = "X";
                    }
                    if(!array[messageEndPointerLeft].startsWith("Computer"))
                    {
                        array[messageEndPointerLeft] = "X";
                    }
                }

                if(array[messageStartPointerRight].equals("X") || array[messageEndPointerRight].equals("X"))
                {
                    if(!array[messageStartPointerRight].startsWith("Computer"))
                    {
                        array[messageStartPointerRight] = "X";
                    }
                    if(!array[messageEndPointerRight].startsWith("Computer"))
                    {
                        array[messageEndPointerRight] = "X";
                    }
                }

                if(messageStartPointerLeft == 0)
                {
                    messageLeftGoingLeft = false;

                    if(!array[messageStartPointerLeft].startsWith("Computer"))
                    {
                        String temp = array[messageStartPointerLeft];
                        array[messageStartPointerLeft] = array[messageEndPointerLeft];
                        array[messageEndPointerLeft] = temp;
                        messageEndPointerLeft = 0;
                        messageStartPointerLeft = 1;
                    }
                }
                if(messageStartPointerRight == array.length - 1)
                {
                    messageRightGoingRight = false;

                    if(!array[messageStartPointerRight].startsWith("Computer"))
                    {
                        String temp = array[messageStartPointerRight];
                        array[messageStartPointerRight] = array[messageEndPointerRight];
                        array[messageEndPointerRight] = temp;
                        messageEndPointerRight = array.length - 1;
                        messageStartPointerRight = array.length - 2;
                    }
                }

                if(collision == true && leftActive == false && rightActive == false)
                {
                    collisionCounter++;

                    if(collisionCounter == 16)
                        break;

                    collision = false;
                    leftActive = true;
                    rightActive = true;
                    try
                    {
                        sleep(array.length * 10);
                    }
                    catch(InterruptedException e)
                    {

                    }

                    int gap = generator.nextInt((int)Math.pow(2, collisionCounter));
                    try
                    {
                        sleep(100 * gap);
                    }
                    catch(InterruptedException e)
                    {

                    }
                }

                System.out.print("Computer" + identifier + ": ");
                for(int i = 0; i < array.length; i++)
                {
                    if(array[i].startsWith("Computer"))
                        System.out.print(" " + array[i] + " ");
                    else
                        System.out.print(array[i]);
                }
            }

            System.out.println("");

            try
            {
                sleep(10);
            }
            catch(InterruptedException e)
            {

            }
        } //while

        try
        {
            int a = generator.nextInt(5) + 1;
            sleep(a * 100);
        }
        catch(InterruptedException e)
        {

        }
    }

    void message(String message)
    {
        if(message.equals(identifier))
            System.out.println("Computer" + identifier + " received message");
    }
}
