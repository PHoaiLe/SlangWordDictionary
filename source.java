import java.util.Hashtable;
import java.util.Vector;

import java.util.ArrayList;
import java.util.Enumeration;
import java.awt.*;
import java.awt.event.*;

import javax.swing.JComponent;

import javax.swing.*;
import java.io.*;


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
  
}

class mainGUI
{
    private JFrame frame;
    private JPanel mainPanel;
    private SearchMenu searchMenu;
    private RandomMenu randomMenu;
    private HistoryMenu historyMenu;
    private QuizMenu quizMenu;
    private CardLayout controlLayout;

    private Integer curOption = 1;
    private String sSEARCH = "search";
    private String sRANDOM = "random";
    private String sHISTORY = "history";
    private String sQUIZ = "quiz";

    private mySlangWordDictionary data;
    private String OriginalDataFile = "./slang.txt";

    private JRadioButton state;

    mainGUI()
    {

        this.state = new JRadioButton();
        state.setVisible(true);

        this.data = new mySlangWordDictionary();
        data.import_Database_fromTXT(this.OriginalDataFile);
        this.frame = new JFrame();
        controlLayout = new CardLayout(10,10);
        this.mainPanel = new JPanel(controlLayout);
        this.searchMenu = new SearchMenu(state);
        this.randomMenu = new RandomMenu();
        this.historyMenu = new HistoryMenu(state);
        this.quizMenu = new QuizMenu();

        
        JPanel controlPanel = new JPanel(new FlowLayout());
        JButton searchButton = new JButton("Search");
        JButton historyButton = new JButton("History");
        JButton randomButton = new JButton("Random");
        JButton quizButton = new JButton("Quiz");

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

        this.frame.add(state, BorderLayout.BEFORE_FIRST_LINE);
        controlPanel.add(searchButton);
        controlPanel.add(historyButton);
        controlPanel.add(randomButton);
        controlPanel.add(quizButton);
        
        this.frame.add(controlPanel, BorderLayout.PAGE_START);
        
        mainPanel.add(searchMenu, this.sSEARCH);
        mainPanel.add(randomMenu, this.sRANDOM);
        mainPanel.add(historyMenu, this.sHISTORY);
        mainPanel.add(quizMenu, this.sQUIZ);
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
                            if(request[1].equals("Slang") && (request[0] != ""))
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
                                }
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
        JLabel preDefinitionLabel = new JLabel("Definition:");
        preDefinitionText = new JTextArea(10, 20);

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
                    preDefinitionText.setText(RelativeMeaning.elementAt(selectedIndex))
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
                if(inputedText != "" || inputedText != null)
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
            System.out.println(RelativeWords);
            System.out.println(RelativeMeaning);
        }
        else
        {
            RelativeWords.clear();
            RelativeMeaning.clear();
        }
        preSlagText.setText(curSlagWord);
        preDefinitionText.setText(curDefinition);
        preRecomList = new JComboBox<String>(RelativeWords);
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
        
        JLabel preDefinitionLabel = new JLabel("Meaning:");
        JTextArea preDefinitionText = new JTextArea(20, 20);

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
    
}

class QuizMenu extends JPanel
{

}
