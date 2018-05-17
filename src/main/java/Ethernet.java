import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Ethernet
{
    private static String[] transmissionMedium;
    private static int numberOfComputers;
    private static Computer[] computers;

    public Ethernet(String arraySize, String positions)
    {
        transmissionMedium = new String[Integer.valueOf(arraySize)];
        String[] coordinates = positions.split(" ");
        numberOfComputers = coordinates.length;
        computers = new Computer[numberOfComputers];

        int index = 0;

        for(int i = 0; i < transmissionMedium.length; i++)
        {
            transmissionMedium[i] = "Z";
        }

        for(String s: coordinates)
        {
            transmissionMedium[Integer.valueOf(s)] = "Computer" + index;
            computers[index] = new Computer(Integer.valueOf(s), index);
            index++;
        }

        for(int i = 0; i < transmissionMedium.length; i++)
        {
            System.out.print(transmissionMedium[i]);
        }
        System.out.println("");

//        computers[1].start();
        for(Computer c : computers)
        {
            c.start();
        }
    }

    public static String[] getTransmissionMedium()
    {
        return transmissionMedium;
    }

    public static int getNumberOfComputers()
    {
        return numberOfComputers;
    }

    public static Computer getComputer(int a)
    {
        return computers[a];
    }

    public static void main(String[] args)
    {
        FileReader fileReader = null;

        try
        {
            fileReader = new FileReader("ethernet.txt");
        }
        catch(FileNotFoundException e)
        {
            System.out.println("File wasn't found");
            System.exit(0);
        }

        BufferedReader bufferedReader = new BufferedReader(fileReader);
        String firstLine = "";
        String secondLine = "";

        try
        {
            firstLine = bufferedReader.readLine();
            secondLine = bufferedReader.readLine();
        }
        catch(IOException e)
        {
            System.out.println("Error during reading from file");
        }

        Ethernet ethernet = new Ethernet(firstLine, secondLine);
    }
}
