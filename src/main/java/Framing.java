import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.zip.CRC32;

public class Framing
{
    private CRC32 crcCode;

    public Framing()
    {
        crcCode = new CRC32();
    }

    public String putIntoFrame(String buffer)
    {
        String startToken = "01111110";
        String endToken = "01111110";
        String outputStream = "";
        int frameSize = 64;
        int subStringIndex = 0;

        while(subStringIndex < buffer.length())
        {
            String bufferSubstring = "";
            if(subStringIndex + frameSize <= buffer.length())
            {
                bufferSubstring += buffer.substring(subStringIndex, subStringIndex + frameSize);
            }
            else
            {
                bufferSubstring += buffer.substring(subStringIndex, buffer.length());
            }

            crcCode.update(bufferSubstring.getBytes());

            bufferSubstring += " " + Long.toBinaryString(crcCode.getValue());
            bufferSubstring = bufferSubstring.replaceAll("11111", "111110");

            outputStream += startToken + " " + bufferSubstring + " " + endToken + " ";
            subStringIndex += frameSize;
        }

        return outputStream;
    }

    public String putOutOfFrame(String buffer)
    {
        String[] content = buffer.split(" ");
        String outputStream = "";

        for(int i = 1; i < content.length; i += 4)
        {
            String control_code = content[i + 1];

            if(content[i + 1].equals(control_code))
            {
                outputStream += content[i];
            }
        }

        outputStream = outputStream.replaceAll("111110", "11111");

        return outputStream;
    }

    public String readFromFile(String sourceFile)
    {
        String fileContent = "";
        FileReader fileReader = null;
        BufferedReader bufferedReader;

        try
        {
            fileReader = new FileReader(sourceFile);
        }
        catch(FileNotFoundException e)
        {
            System.out.println("File wasn't found");
            System.exit(0);
        }

        bufferedReader = new BufferedReader(fileReader);

        try
        {
            String singleLine = "";
            singleLine = bufferedReader.readLine();

            if(singleLine == null)
            {
                System.out.println("File is empty!");
                System.exit(0);
            }

            while(singleLine != null)
            {
                fileContent = fileContent.concat(singleLine);
                singleLine = bufferedReader.readLine();
            }
        }
        catch(IOException e)
        {
            System.out.println("An error occurred during reading from file");
            System.exit(0);
        }

        return fileContent;
    }

    public static void main(String[] args)
    {
        Framing framing = new Framing();
        String fileBuffer = framing.readFromFile("Z.txt");
        System.out.println("File buffer: " + fileBuffer);
        String framedBuffer = framing.putIntoFrame(fileBuffer);
        System.out.println("Fr: " + framedBuffer); //framed

        try
        {
            FileWriter fileWriter = new FileWriter("W.txt");
            fileWriter.write(framedBuffer);
            fileWriter.flush();
            fileWriter.close();
        }
        catch(IOException e)
        {
            System.out.println("Something went wrong during saving to file");
            System.exit(0);
        }

        fileBuffer = framing.readFromFile("W.txt");

        String bufferWithoutFrame = framing.putOutOfFrame(fileBuffer);
        System.out.println("File buffer: " + bufferWithoutFrame);
    }
}
