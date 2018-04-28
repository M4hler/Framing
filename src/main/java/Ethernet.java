import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Ethernet
{
    private String[] transmissionMedium;
    private int numberOfComputers;
    private Computer[] computers;

    public Ethernet(String arraySize, String positions)
    {
        transmissionMedium = new String[Integer.valueOf(arraySize)];
        String[] coordinates = positions.split(" ");
        numberOfComputers = coordinates.length;
        computers = new Computer[numberOfComputers];

        int index = 0;
        for(String s: coordinates)
        {
            transmissionMedium[Integer.valueOf(s)] = "Computer";
            computers[index] = new Computer(index, Integer.valueOf(s));
        }

        for(Computer c : computers)
        {
            c.run();
        }
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
