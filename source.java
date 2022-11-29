import java.util.Hashtable;
import java.util.Vector;
import java.util.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.awt.*;
import java.awt.event.*;

import javax.swing.JComponent;
import javax.lang.model.util.ElementScanner14;
import javax.swing.*;
import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;


public class source {
    public static void main(String[] args) {
        mainGUI ui = new mainGUI();
        ui.runGUI();
    }
}


class mySlangWordDictionary
{
  private Hashtable<String, String> Dictionary;
  private String SPLIT_SLANGWORD_CHAR = "`";

  mySlangWordDictionary()
  {
    Dictionary = new Hashtable<String, String>();
  }

  public boolean import_Database_fromTXT(String filename)
  {
    try
    {
        BufferedReader file = new BufferedReader(new FileReader(filename));
        String aRow = file.readLine();//ignore the first line of instruction;
        while((aRow = file.readLine()) != null)
        {
            String[] splitedData = aRow.split(this.SPLIT_SLANGWORD_CHAR);
            if(splitedData.length == 2)
            {
                this.Dictionary.put(splitedData[0], splitedData[1]);
            }
            
        }
        return true;
    }
    catch(FileNotFoundException f)
    {
        return false;
    }
    catch(IOException e)
    {
        return false;
    }
  }

  public String[] searchByKey(String inputKey)
  {
    String[] result = new String[2];// result[0]: SlangWord, result[1]: meaning
    result[1] = this.Dictionary.get(inputKey);
    if(result[1] == null)
    {
        result[0] = null;
    }
    else
    {
        result[0] = inputKey;
    }
    return result;
  }

  public Hashtable<String, String> searchRelativeKey(String inputKey)
  {
    if(inputKey == null || inputKey == "")
    {
        return null;
    }
    Hashtable<String, String> result = new Hashtable<String, String>();
    Enumeration<String> keyCollection = this.Dictionary.keys();
    while(keyCollection.hasMoreElements())
    {
        String aKey = keyCollection.nextElement();
        if(aKey.contains(inputKey))
        {
            result.put(aKey, this.Dictionary.get(aKey));
        }
    }
    if(result.size() == 0)
    {
        return null;
    }
    return result;
  }

  public int getSize()
  {
    return this.Dictionary.size();
  }

  public String[] getElementByIndex(int index)
  {
    if(index < 0 || index > this.Dictionary.size()-1)
    {
        return null;
    }
    String[] res = new String[2];
    Enumeration<String> keyCollection = this.Dictionary.keys();
    String selectedKey = new String("");
    while(keyCollection.hasMoreElements() && index > 1)
    {
        keyCollection.nextElement();
        index-=1;
    }
    res[0] = keyCollection.nextElement();
    res[1] = this.Dictionary.get(res[0]);
    return res;
  }

  public Hashtable<String, String> searchRelativeDefinition(String inputKey)
  {
    if(inputKey.equals(null) == true || inputKey.equals("") == true)
    {
        return null;
    }
    System.out.println("Meaning search: "+inputKey);
    Hashtable<String, String> res = new Hashtable<String, String>();
    Enumeration<String> keyCollection = this.Dictionary.keys();
    while(keyCollection.hasMoreElements())
    {
        String key = keyCollection.nextElement();
        String meaning = this.Dictionary.get(key);
        if(meaning.contains(inputKey) == true);
        {
            res.put(key, meaning);
        }
    }
    if(res.size() == 0)
    {
        return null;
    }
    else
    {
        return res;
    }
  }

  public boolean addNewSlangWord(String inputSlag, String inputDefinition, boolean isOverwrited)
  {
    if(inputSlag == null || inputSlag == "" || inputDefinition == null || inputDefinition == "")
    { 
        return false; 
    }
    if(this.Dictionary.containsKey(inputSlag))
    {
        if(isOverwrited == true)
        {
            this.Dictionary.replace(inputSlag, inputDefinition);
            return true;
        }
        else
        {
            return false;
        }
    }
    else
    {
        this.Dictionary.put(inputSlag, inputDefinition);
        return true;
    }
  }
  
  public boolean updateSlangWord(String oldSlag, String newSlag, String inputDefinition)
  {
    if(oldSlag == null || oldSlag == "" || newSlag == null || newSlag == "")
    { 
        return false; 
    }
    if(this.Dictionary.containsKey(oldSlag))
    {
        String aDef;
        if(inputDefinition.equals(null)) //change key, keep value
        {
            aDef = this.Dictionary.get(oldSlag);
        }
        else //change both key and value
        {
            aDef = inputDefinition;
        }
        this.Dictionary.remove(oldSlag);
        this.Dictionary.put(newSlag, aDef);
        return true;
    }
    else
    {
        return false;
    }
  }

  public boolean deleteSlangWord(String inputKey)
  {
    if((inputKey.equals(null) || inputKey.equals("")) || this.Dictionary.containsKey(inputKey) == false)
    {
        return false;
    }
    this.Dictionary.remove(inputKey);
    return true;
  }

  public void clearCurrentData()
  {
    this.Dictionary.clear();
  }

}

class mainGUI
{
    private JFrame frame;
    private JPanel mainPanel;
    private SearchMenu searchMenu;
    private RandomMenu randomMenu;
    private HistoryMenu historyMenu;
    private QuizMenu quizMenu;
    private ChangeMenu changeMenu;
    private CardLayout controlLayout;
    
    //switch-case for changing screens 
    private Integer curOption = 1;
    private String sSEARCH = "search";
    private String sRANDOM = "random";
    private String sHISTORY = "history";
    private String sQUIZ = "quiz";
    private String sCHANGE = "change";

    private mySlangWordDictionary data;
    private String OriginalDataFile = "./slang.txt";
    private String ChangedDataFile = "./custom.txt";
    private String setUpPath = "./setup.txt";
    private int isChanged = 0;

    private JRadioButton state;

    mainGUI()
    {

        this.state = new JRadioButton();
        state.setVisible(true);
        this.data = new mySlangWordDictionary();
        isChanged = import_setUp_data(setUpPath);
        if(isChanged == 0)
        {
            data.import_Database_fromTXT(this.OriginalDataFile);
        }
        else
        {
            data.import_Database_fromTXT(this.ChangedDataFile);
        }
        

        this.frame = new JFrame();
        controlLayout = new CardLayout(10,10);
        this.mainPanel = new JPanel(controlLayout);
        this.searchMenu = new SearchMenu(state);
        this.randomMenu = new RandomMenu(state);
        this.historyMenu = new HistoryMenu(state);
        this.quizMenu = new QuizMenu(state);
        this.changeMenu = new ChangeMenu(state);

        
        JPanel controlPanel = new JPanel(new FlowLayout());
        JButton searchButton = new JButton("Search");
        JButton historyButton = new JButton("History");
        JButton randomButton = new JButton("Random");
        JButton quizButton = new JButton("Quiz");
        JButton changeButton = new JButton("Change");

        searchButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent ae)
            {
                switchPanel(1);
                curOption = 1;
            }
        });

        historyButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent ae)
            {
                switchPanel(2);
                curOption = 2;
            }
        });

        randomButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent ae)
            {
                switchPanel(3);
                curOption = 3;
            }
        });

        quizButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent ae)
            {
                switchPanel(4);
                curOption = 4;
            }
        });

        changeButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent ae)
            {
                switchPanel(5);
                curOption = 5;
            }
        });

        this.frame.add(state, BorderLayout.BEFORE_FIRST_LINE);
        controlPanel.add(searchButton);
        controlPanel.add(historyButton);
        controlPanel.add(randomButton);
        controlPanel.add(quizButton);
        controlPanel.add(changeButton);
        
        this.frame.add(controlPanel, BorderLayout.PAGE_START);
        
        mainPanel.add(searchMenu, this.sSEARCH); 
        mainPanel.add(randomMenu, this.sRANDOM);
        mainPanel.add(historyMenu, this.sHISTORY);
        mainPanel.add(quizMenu, this.sQUIZ);
        mainPanel.add(changeMenu, this.sCHANGE);
        this.frame.add(mainPanel, BorderLayout.CENTER);

        state.addItemListener(new ItemListener()
        {
            public void itemStateChanged(ItemEvent ie)
            {
                if(ie.getStateChange() == 1)
                {
                    switch(curOption)
                    {
                        case 1:
                        {
                            String[] request = searchMenu.getInputText(); //request[0]:inputKey; request[1]:mode
                            System.out.println("Slang: " + request[0] + " Mode: " + request[1]);
                            if((request[1].equals("Slang") == true) && (request[0].equals("") == false))
                            {
                                String[] res = data.searchByKey(request[0]);
                                if(res[0] == null)
                                {
                                    Hashtable<String, String> relativeWords = data.searchRelativeKey(request[0]);
                                    searchMenu.setRepresentResult(res[0], res[1], relativeWords);
                                }
                                else
                                {
                                    searchMenu.setRepresentResult(res[0], res[1], null);
                                    historyMenu.addNewWord(res);
                                    changeMenu.addNewWord(res);
                                }
                            }
                            else if((request[1].equals("Meaning")) == true && (request[0].equals("") == false))
                            {
                                Hashtable<String, String> res = data.searchRelativeDefinition(request[0]);
                                if(res == null)
                                {
                                    searchMenu.setRepresentResult("Nome", "", res);
                                }
                                else
                                {
                                    searchMenu.setRepresentResult("^_^", "Please check the Recommend area", res);
                                }
                            }
                        }break;

                        case 2:
                        {

                        }break;

                        case 3:
                        {
                            int request = randomMenu.getRandomSelection(data.getSize());
                            String[] res = data.getElementByIndex(request);
                            randomMenu.setRandomInput(res);
                        }break;

                        case 4:
                        {

                        }break;

                        case 5:
                        {
                            String[] request = changeMenu.getRequest();
                            if(request.length == 4) //request[0]: ComboBox, request[1]: inputSlag, request[2]: inputDefinition, request[3]: mode
                            {
                                if(((request[0] == null || request[0].equals("")) || (request[1].equals("") || request[1] == null)) && 
                                (request[3].equals(changeMenu.NO_OPTION)))
                                {
                                    break;
                                }
                                else if(request[3].equals(changeMenu.OPTION_ADD)) //add
                                {
                                    boolean check = data.addNewSlangWord(request[1], request[2], false);
                                    if(check == false)
                                    {
                                        changeMenu.setStatus("Existed the same Slang word '"+ request[1]+ "' in the dictionary!");
                                    }
                                    else
                                    {
                                        changeMenu.setStatus("Add new Slang word successfully...");
                                        isChanged = 1;
                                    }
                                }
                                else if(request[3].equals(changeMenu.OPTION_UPDATE)) //update
                                {
                                    boolean check = data.updateSlangWord(request[0], request[1], request[2]);
                                    if(check == false)
                                    {
                                        changeMenu.setStatus("Failed to update. Please check again...");
                                    }
                                    else
                                    {
                                        changeMenu.setStatus("Update successfully...");
                                        isChanged = 1;
                                    }
                                    
                                }
                                else if(request[3].equals(changeMenu.OPTION_DELETE)) //delete
                                {
                                    boolean check = data.deleteSlangWord(request[0]);
                                    if(check == false)
                                    {
                                        changeMenu.setStatus("Failed to delete Slang word. Please check again");
                                    }
                                    else
                                    {
                                        changeMenu.setStatus("Delete Slang word successfully...");
                                        isChanged = 1;
                                    }
                                }

                            }
                            else if(request.length == 3) // resend adding and overwritting request
                            {
                                String[] aRespone = changeMenu.getRequest();
                                boolean check = data.addNewSlangWord(aRespone[1], request[2], true);
                                if(check == false)
                                {
                                    changeMenu.setStatus("Cannot add and overwrite the Slang word. Please check again...");
                                }
                                else 
                                {
                                    changeMenu.setStatus("Add and overwrite the Slang word successully...");
                                    isChanged = 1;
                                }
                            
                            }
                            else
                            {
                                //request.length = 1;
                                data.clearCurrentData();
                                data.import_Database_fromTXT(OriginalDataFile);
                                isChanged = 0;
                                changeMenu.setStatus("Reset successfully...");
                            } 

                        }break;
                    }
                    state.setSelected(false);
                }
            }
        });
    }

    public void runGUI()
    {
        javax.swing.SwingUtilities.invokeLater(new Runnable(){
            public void run()
            {
                createAndShowGUI();
            }
        });
    }

    public void createAndShowGUI()
    {
        this.frame.setDefaultLookAndFeelDecorated(true);
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.controlLayout.show(this.mainPanel, this.sSEARCH);
       
        this.frame.setSize(new Dimension(500, 500));
        // this.frame.pack();
        this.frame.setLocationRelativeTo(null);
        this.frame.setVisible(true);
    }

    public void switchPanel(int option)
    {
        switch(option)
        {
            case 1: //search
            {
                this.controlLayout.show(this.mainPanel, this.sSEARCH);
            }break;
            
            case 2: //History
            {
                this.controlLayout.show(this.mainPanel, this.sHISTORY);
            }break;

            case 3: //Random
            {
                this.controlLayout.show(this.mainPanel, this.sRANDOM);
            }break;

            case 4: //Quiz
            {
                this.controlLayout.show(this.mainPanel, this.sQUIZ);
            }break;

            case 5:
            {
                this.controlLayout.show(this.mainPanel, this.sCHANGE);
            }
           
        }
        
    }

    public int import_setUp_data(String filename)
    {
        try
        {
            FileReader file = new FileReader(filename);
            int res = file.read();
            return res;
        }
        catch(FileNotFoundException f)
        {
            return 0;
        }
        catch(IOException e)
        {
            return 0;
        }
    }

}

class SearchMenu extends JPanel
{
    private String selectedMode;
    private JTextField searchField;
    private String text;
    private Vector<String> RelativeWords;
    private Vector<String> RelativeMeaning;
    private String curSlagWord;
    private String curDefinition;
    private JComboBox preRecomList;
    private JTextArea preSlagText;
    private JTextArea preDefinitionText;

    SearchMenu(JRadioButton state)
    {
        RelativeWords = new Vector<String>();
        RelativeWords.addElement("Select");
        RelativeMeaning = new Vector<String>();
        RelativeMeaning.addElement("Not a meaning of any Slang word");
        curDefinition = "";
        curSlagWord = "";

        this.setLayout(new BorderLayout());

        selectedMode = new String("Slang");
        text = new String("");
        JPanel top = new JPanel();
        JLabel Search = new JLabel("SEARCH");
        JRadioButton rb1 = new JRadioButton();
        JRadioButton rb2 = new JRadioButton();
        rb1.setText("Slang");
        rb2.setText("Meaning");

        rb1.addItemListener(new ItemListener()
        {
            public void itemStateChanged(ItemEvent ie)
            {
                if(ie.getStateChange() == 1)//khi radio duoc chon
                {
                    selectedMode = new String("Slang");
                }
            }
        });

        rb2.addItemListener(new ItemListener()
        {
            public void itemStateChanged(ItemEvent ie)
            {
                if(ie.getStateChange() == 1)
                {
                    selectedMode = new String("Meaning");
                }
            }
        });

        ButtonGroup bgr = new ButtonGroup();
        rb1.setSelected(true);
        top.add(Search);
        bgr.add(rb1);
        bgr.add(rb2);
        top.add(rb1);
        top.add(rb2);
        this.add(top, BorderLayout.PAGE_START);

        JPanel searchPanel = new JPanel();
        searchField = new JTextField(20);
        JButton searchButton = new JButton("Search");

        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        this.add(searchPanel, BorderLayout.CENTER);

        JPanel bottom = new JPanel();
        bottom.setSize(new Dimension(500, 300));
        
        bottom.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,5,5,5);

        JLabel preSlagLabel = new JLabel("Slang word:");
        preSlagText = new JTextArea(1, 20);
        preSlagText.setLineWrap(true);
        preSlagText.setWrapStyleWord(true);

        JLabel preDefinitionLabel = new JLabel("Definition:");
        preDefinitionText = new JTextArea(10, 20);
        preDefinitionText.setLineWrap(true);
        preDefinitionText.setWrapStyleWord(true);

        JLabel preRecomLabel = new JLabel("Recommend:");
        preRecomList = new JComboBox<String>(RelativeWords);
        preRecomList.setPreferredSize(new Dimension(100,30));
        // preRecomList.setLayoutOrientation(JList.VERTICAL);
        JScrollBar scrollBar = new JScrollBar(JScrollBar.VERTICAL);
        scrollBar.add(preRecomList);
        scrollBar.setPreferredSize(new Dimension(100, 100));
        
        preRecomList.addItemListener(new ItemListener()
        {
            public void itemStateChanged(ItemEvent ie)
            {
                if(ie.getStateChange() == 1)
                {
                    int selectedIndex = preRecomList.getSelectedIndex();
                    System.out.println("Index: " + selectedIndex);
                    System.out.println("Re-w: " + RelativeWords.elementAt(selectedIndex));
                    System.out.println("Re-m: " + RelativeMeaning.elementAt(selectedIndex));
                    preSlagText.setText(RelativeWords.elementAt(selectedIndex));
                    preDefinitionText.setText(RelativeMeaning.elementAt(selectedIndex));
                }
            }
        });

        gbc.gridx = 0;
        gbc.gridy = 0;
        bottom.add(preSlagLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        bottom.add(preSlagText, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        bottom.add(preDefinitionLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        bottom.add(preDefinitionText, gbc);

        gbc.gridx = 2;
        gbc.gridy = 0;
        bottom.add(preRecomLabel, gbc);

        gbc.gridx = 2;
        gbc.gridy = 1;
        bottom.add(preRecomList, gbc);

        searchButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent ae)
            {
                String inputedText = searchField.getText();
                if(inputedText != "" && inputedText != null)
                {
                    text = inputedText;
                }
                System.out.println(text);
                System.out.println(selectedMode);
                preSlagText.setText(curSlagWord);
                preDefinitionText.setText(curDefinition);
                state.setSelected(true);
            }
        });
        
        this.add(bottom, BorderLayout.PAGE_END);
    }

    public String[] getInputText()
    {
        String[] result = new String[2];
        result[0] = text;
        result[1] = selectedMode;
        return result;
    }

    public void setRepresentResult(String foundSlag, String foundDef, Hashtable<String, String> recommend)
    {
        if(foundSlag == " " || foundSlag == null)
        {
            curSlagWord = "No result";
            curDefinition = " ";
        }
        else
        {
            curSlagWord = foundSlag;
            curDefinition = foundDef;
        }
        if(recommend != null)
        {
            RelativeWords.clear();
            RelativeMeaning.clear();
            RelativeWords.addElement("Select");
            RelativeMeaning.addElement("not a meaning of any Slang word");
            Enumeration<String> keys = recommend.keys();
            while(keys.hasMoreElements())
            {
                String aKey = keys.nextElement();
                RelativeWords.addElement(aKey);
                RelativeMeaning.addElement(recommend.get(aKey));
            }
        }
        else
        {
            RelativeWords.clear();
            RelativeMeaning.clear();
        }
        preSlagText.setText(curSlagWord);
        preDefinitionText.setText(curDefinition);
        // preRecomList = new JComboBox<String>(RelativeWords);
    }
}

class HistoryMenu extends JPanel
{
    private JComboBox history;
    private Vector<String> searchedWords;
    private Vector<String> searchedWordsMeaning;
    private String SlagText;
    private String DefText;

    HistoryMenu(JRadioButton component)
    {
        SlagText = new String();
        DefText = new String();

        searchedWords = new Vector<String>();
        searchedWords.addElement("Select");
        searchedWordsMeaning = new Vector<String>();
        searchedWordsMeaning.addElement("not a meaning of any Slang word");

        history = new JComboBox(searchedWords);
        history.setPreferredSize(new Dimension(200, 30));
        JScrollBar scrollBar = new JScrollBar(Scrollbar.VERTICAL);
        scrollBar.add(history);
        scrollBar.setPreferredSize(new Dimension(100,100));
        
        JLabel title = new JLabel("HISTORY");
        JLabel preSlagLabel = new JLabel("Slang word:");
        JTextArea preSlagText = new JTextArea(1, 20);
        preSlagText.setLineWrap(true);
        preSlagText.setWrapStyleWord(true);
        
        JLabel preDefinitionLabel = new JLabel("Meaning:");
        JTextArea preDefinitionText = new JTextArea(20, 20);
        preDefinitionText.setLineWrap(true);
        preDefinitionText.setWrapStyleWord(true);

        history.addItemListener(new ItemListener()
        {
            public void itemStateChanged(ItemEvent ie)
            {
                if(ie.getStateChange() == 1)
                {
                    int selectedIndex = history.getSelectedIndex();
                    preSlagText.setText(searchedWords.elementAt(selectedIndex));
                    preDefinitionText.setText(searchedWordsMeaning.elementAt(selectedIndex));
                }
            }
        });

        this.setLayout(new BorderLayout());
        JPanel top = new JPanel();
        top.add(title);
        top.add(history);
        this.add(top, BorderLayout.PAGE_START);

        JPanel body1 = new JPanel();
        body1.add(preSlagLabel);
        body1.add(preSlagText);

        JPanel body2 = new JPanel();
        body2.add(preDefinitionLabel);
        body2.add(preDefinitionText);

        JPanel body = new JPanel(new BorderLayout());
        body.setSize(new Dimension(500,500));
        body.add(body1, BorderLayout.CENTER);
        body.add(body2, BorderLayout.PAGE_END);
        // GridBagConstraints gbc = new GridBagConstraints();
        // gbc.insets = new Insets(1,5,5,5);

        // gbc.gridx = 0;
        // gbc.gridy = 0;
        // gbc.gridwidth = 3;
        // body.add(body1, gbc);

        // gbc.gridx = 0;
        // gbc.gridy = 3;
        // gbc.gridwidth = 3;
        // gbc.fill = GridBagConstraints.HORIZONTAL;
        // body.add(body2, gbc);

        this.add(body, BorderLayout.CENTER);
    }

    public void addNewWord(String[] newWord)
    {
        if(searchedWords.contains(newWord[0]) == false)
        {
            searchedWords.addElement(newWord[0]);
            searchedWordsMeaning.addElement(newWord[1]);
        }
    }
}

class RandomMenu extends JPanel
{
    private String randomSlag;
    private String randomMeaning;
    private JTextArea preSlagText;
    private JTextArea preDefinitionText;
    private int step;

    RandomMenu(JRadioButton component)
    {
        randomSlag = new String("");
        randomMeaning = new String("");
        step = 11;

        this.setLayout(new BorderLayout());

        JButton random = new JButton("GO!");
        // random.setPreferredSize(new Dimension(50,80));

        JPanel panel1 = new JPanel();
        JLabel preSlagLabel = new JLabel("Slang word:");
        preSlagText = new JTextArea(1, 20);
        preSlagText.setLineWrap(true);
        preSlagText.setWrapStyleWord(true);
        panel1.add(preSlagLabel);
        panel1.add(preSlagText);

        JPanel panel2 = new JPanel();
        JLabel preDefinitionLabel = new JLabel("Meaning:");
        preDefinitionText = new JTextArea(10,20);
        preDefinitionText.setLineWrap(true);
        preDefinitionText.setWrapStyleWord(true);
        panel2.add(preDefinitionLabel);
        panel2.add(preDefinitionText);

        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        // panel.add(random);
        panel.add(panel1);
        panel.add(panel2);
       
        this.add(random, BorderLayout.PAGE_START);
        this.add(panel, BorderLayout.CENTER);

        random.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent ae)
            {
                preSlagText.setText(randomSlag);
                preDefinitionText.setText(randomMeaning);
                component.setSelected(true);
            }
        });
    }

    public void setRandomInput(String[] input)
    {
        if(input == null)
        {
            return;
        }
        randomSlag = input[0];
        randomMeaning = input[1];
        preSlagText.setText(randomSlag);
        preDefinitionText.setText(randomMeaning);
    }

    public int getRandomSelection(int sizeOfDictionary)
    {
        LocalDate date = LocalDate.now();
        LocalTime time = LocalTime.now();
        int day = date.getDayOfMonth();
        int month = date.getMonthValue();
        int year = date.getYear();
        int res = (day*month*year + step)% sizeOfDictionary;
        step = (step + time.getSecond()) % sizeOfDictionary;
        return res;
    }
}

class QuizMenu extends JPanel
{
    QuizMenu(JRadioButton component)
    {

    }
}


class ChangeMenu extends JPanel
{
    private Vector<String> searchedWords;
    private String inputSlag;
    private String inputDefinition;
    private JTextField getSlagText;
    private JTextField getDefinitionText;
    private JLabel status;
    private String INPUT_WARNING = "Please input both 'input field'!";
    private String NO_SEARCHED_WORD = "Please select a existed Slang word!";
    public final String NO_OPTION = "0";
    public final String OPTION_ADD = "1";
    public final String OPTION_UPDATE = "2";
    public final String OPTION_DELETE = "3";
    public final String OPTION_RESET = "4";
    public final String OPTION_ADD_OVERWRITE = "5";
    private String option;

    private JComboBox searchedList;
    private String comboBoxSlangWord;

    ChangeMenu(JRadioButton component)
    {
        comboBoxSlangWord = new String("None");
        searchedWords = new Vector<String>();
        searchedWords.addElement("Select");

        inputSlag = new String("");
        inputDefinition = new String("");

        option = "0";
        JPanel body = new JPanel();
        body.setLayout(new BorderLayout());

        JPanel body1 = new JPanel();
        JLabel getSlagLabel = new JLabel("Input Slang word:");
        getSlagText = new JTextField(18);
        body1.add(getSlagLabel);
        body1.add(getSlagText);

        JPanel body_23 = new JPanel();
        body_23.setLayout(new BoxLayout(body_23, BoxLayout.Y_AXIS));

        
        searchedList = new JComboBox(searchedWords);
        searchedList.setSelectedIndex(0);
        searchedList.setPreferredSize(new Dimension(100, 30));
        JScrollBar scrollBar = new JScrollBar(JScrollBar.VERTICAL);
        scrollBar.add(searchedList);
        scrollBar.setPreferredSize(new Dimension(200, 200));

        JPanel body2 = new JPanel();
        JLabel getDefinitionLabel = new JLabel("Input meaning:");
        getDefinitionText = new JTextField(20);
        body2.add(getDefinitionLabel);
        body2.add(getDefinitionText);

        JPanel body3 = new JPanel();
        JButton addButton = new JButton("Add");
        JButton updateButton = new JButton("Update");
        JButton deleteButton = new JButton("Delete");
        JButton resetButton = new JButton("Reset");

        

        status = new JLabel("Status:");

        body3.add(addButton);
        body3.add(updateButton);
        body3.add(deleteButton);
        body3.add(resetButton);

        body_23.add(searchedList);
        body_23.add(body2);
        body_23.add(body3);


        body.add(body1, BorderLayout.PAGE_START);
        body.add(body_23, BorderLayout.CENTER);
        body.add(status, BorderLayout.PAGE_END);
        
        this.add(body, BorderLayout.CENTER);

        addButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent ae)
            {
                inputSlag = getSlagText.getText();
                inputDefinition = getDefinitionText.getText();
                comboBoxSlangWord = String.valueOf(searchedList.getSelectedItem());
                if(inputSlag.equals("") || inputSlag.equals(null) || inputDefinition.equals("") || inputDefinition.equals(null))
                {
                    status.setText("Status: " + INPUT_WARNING);
                }
                else if(option.equals(OPTION_ADD_OVERWRITE))
                {
                    component.setSelected(true);
                }
                // else if(comboBoxSlangWord.equals("Select"))
                // {
                //     status.setText("Status: " + NO_SEARCHED_WORD);
                // }
                else
                {
                    option = OPTION_ADD;
                    component.setSelected(true);
                }
            }
        });

        updateButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent ae)
            {
                inputSlag = getSlagText.getText();
                inputDefinition = getDefinitionText.getText();
                comboBoxSlangWord = String.valueOf(searchedList.getSelectedItem());
                if(inputSlag.equals("") || inputSlag.equals(null) || inputDefinition.equals("") || inputDefinition.equals(null))
                {
                    status.setText("Status: " + INPUT_WARNING);
                }
                else if(comboBoxSlangWord.equals("Select"))
                {
                    status.setText("Status: " + NO_SEARCHED_WORD);
                }
                else
                {
                    option = OPTION_UPDATE;
                    component.setSelected(true);
                }
            }
        });

        deleteButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent ae)
            {
                inputSlag = getSlagText.getText();
                inputDefinition = getDefinitionText.getText();
                comboBoxSlangWord = String.valueOf(searchedList.getSelectedItem());
                if(inputSlag.equals("") || inputSlag.equals(null) || inputDefinition.equals("") || inputDefinition.equals(null))
                {
                    status.setText("Status: " + INPUT_WARNING);
                }
                else if(comboBoxSlangWord.equals("Select"))
                {
                    status.setText("Status: " + NO_SEARCHED_WORD);
                }
                else
                {
                    int check = JOptionPane.showConfirmDialog(null, "Do you want to delete '" + inputSlag + "'?", "Delete confirm", JOptionPane.YES_NO_OPTION);
                    if(check == 0)// yes
                    {
                        option = OPTION_DELETE;
                        component.setSelected(true);
                    }
                    else
                    {
                        option = NO_OPTION;
                    }
                }
            }
        });

        resetButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent ae)
            {
                int check = JOptionPane.showConfirmDialog(null, "Do you want to reset all data?", "Reset confirm", JOptionPane.YES_NO_OPTION);
                if(check == 0) // yes
                {
                    option = OPTION_RESET;
                    component.setSelected(true);
                }
                else
                {
                    option = NO_OPTION;
                }
            }
        });
    }

    public String[] getRequest()
    {
        String[] request = null;
        if(option.equals(OPTION_ADD))
        {
            request = new String[4];
            request[0] = null;
            request[1] = inputSlag;
            request[2] = inputDefinition;
            request[3] = OPTION_ADD;
        }
        else if(option.equals(OPTION_UPDATE))
        {
            request = new String[4];
            request[0] = comboBoxSlangWord;
            request[1] = inputSlag;
            request[2] = inputDefinition;
            request[3] = OPTION_UPDATE;
        }
        else if(option.equals(OPTION_DELETE))
        {
            request = new String[4];
            request[0] = comboBoxSlangWord;
            request[1] = inputSlag;
            request[2] = inputDefinition;
            request[3] = OPTION_DELETE;
        }
        else if(option.equals(OPTION_ADD_OVERWRITE))
        {
            request = new String[3];
            request[0] = inputSlag;
            request[1] = inputDefinition;
            request[2] = OPTION_ADD_OVERWRITE;
        }
        else
        {
            request = new String[1];
            request[0] = OPTION_RESET;
        }
        return request;
    }

    public void setStatus(String _status)
    {
        if(_status.contains("Existed"))
        {
           int check = JOptionPane.showConfirmDialog(this, "Do you want to overwrite the Slang word?", "Confirm overwrite", JOptionPane.YES_NO_OPTION);
           if(check == 0)
           {
                option = OPTION_ADD_OVERWRITE; 
           }      
        }
        status.setText(_status);
    }

    public void addNewWord(String[] newWord)
    {
        if(searchedWords.contains(newWord[0]) ==  false)
        {
            searchedWords.addElement(newWord[0]);
        }
    }
}