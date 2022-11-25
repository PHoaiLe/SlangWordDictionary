import java.util.Hashtable;
import java.util.ArrayList;
import java.awt.*;
import javax.swing.*;
import java.io.*;


public class source {
    public static void main(String[] args) {
        
    }
}


class mySlangWordDictionary
{
    private String[] keyChar = {"Q", "W", "E", "R", "T", "Y", "U", "I", "O", "P", "A",
                                "S", "D", "F", "G", "H", "J", "K", "L", "Z", "X", "C",
                                "V", "B", "N", "M", "~", "!", "@", "#", "$", "%", "^",
                                "&", "*", "(", ")", "-", "_", "=", "+", "'", ";", ":",
                                "[", "]", "\\", "|", "<", ",", ">", ".", "?", "/"};
    private Long[] mapPrime = {2L, 3L, 5L, 7L, 11L, 13L, 17L, 19L, 23L, 29L,
                                31L, 37L, 41L, 43L, 47L, 53L, 59L, 61L, 67L, 71L,
                                73L, 79L, 83L, 89L, 97L, 101L, 103L, 107L, 109L, 113L,
                                127L, 131L, 137L, 139L, 149L, 151L, 157L, 163L, 167L, 173L,
                                179L, 181L, 191L, 193L, 199L, 211L, 223L, 227L, 229L, 233L, 
                                239L, 241L, 251L, 257L};
    private String SLAG_MEANING_SPLIT_CHAR = "`";
    private String DEFINITION_SPLIT_CHAR = "|";    
    private Hashtable<String,Long> PrimeMapping;
    private Hashtable<Long, ArrayList<String>> SlangWordDic;
    private int numOfSearchResult = 10;

    mySlangWordDictionary()
    {
        this.PrimeMapping = new Hashtable<String,Long>();
        this.SlangWordDic = new Hashtable<Long, ArrayList<String>>();
        for(int i=0; i< this.keyChar.length; i++)
        {
            PrimeMapping.put(this.keyChar[i], mapPrime[i]);
        }
    }

    public boolean importDatabase_fromTXT(String filename)
    {
        try
        {
            BufferedReader file = new BufferedReader(new FileReader(filename));
            String aRow = file.readLine(); //doc va luot bo Slag`Meaning
            while((aRow = file.readLine()) != null)
            {
                //splitedStrings[0]: slangWord
                //splitedStrings[1...len-1]: cac definition
                String[] splitedString = aRow.split(this.SLAG_MEANING_SPLIT_CHAR);
                String[] splitedSameDefinition = splitedString[1].split(this.DEFINITION_SPLIT_CHAR);
                Long key = 1L;
                for(int i = 0; i< splitedString[0].length(); i++)
                {
                    String charKey = String.valueOf(splitedString[0].charAt(i));
                    key = key*this.PrimeMapping.get(charKey);
                }
                ArrayList<String> values = new ArrayList<String>();
                //mac dinh gia tri values[0] chinh la noi luu SlangWord, values[1...len-1]: luu definition cua SlangWord tuong ung
                values.add(splitedString[0]);
                for(int i=1; i<splitedSameDefinition.length; i++)
                {
                    values.add(splitedSameDefinition[i]);
                }

                this.SlangWordDic.put(key, values);
            }
            file.close();
            return true;
        }
        catch(FileNotFoundException ef)
        {
            return false;
        }
        catch(IOException e)
        {
            return false;
        }
    }
    
    public Hashtable<String, ArrayList<String>> searchSlangWord(String input)
    {
        if(input == null || input == "")
        {
            return null;
        }
        Hashtable<String,ArrayList<String>> result = new Hashtable<String,ArrayList<String>>();
        Long inputKey = 1L;
        //convert inputString to Key number
        for(int i=0; i< input.length(); i++)
        {
            String charKey = String.valueOf(input.charAt(i));
            inputKey = inputKey*this.PrimeMapping.get(charKey);
        }

        return result;
    }

}
