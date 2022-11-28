import java.util.Hashtable;
import java.util.Vector;
import java.util.jar.Manifest;
import java.util.ArrayList;
import java.awt.*;
import java.awt.event.*;
import java.awt.GridBagConstraints;


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

    private curOption = 1;
    private String sSEARCH = "search";
    private String sRANDOM = "random";
    private String sHISTORY = "history";
    private String sQUIZ = "quiz";



    mainGUI()
    {
        this.frame = new JFrame();
        controlLayout = new CardLayout(10,10);
        this.mainPanel = new JPanel(controlLayout);
        this.searchMenu = new SearchMenu();
        this.randomMenu = new RandomMenu();
        this.historyMenu = new HistoryMenu();
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
                this.curOption = 1;
            }
        });

        historyButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent ae)
            {
                switchPanel(2);
                this.curOption = 2
            }
        });

        randomButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent ae)
            {
                switchPanel(3);
                this.curOption = 3;
            }
        });

        quizButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent ae)
            {
                switchPanel(4);
                this.curOption = 4;
            }
        });

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
    private String curSlagWord;
    private String curDefinition;

    SearchMenu()
    {
        RelativeWords = null;
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
            }
        });
        
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        this.add(searchPanel, BorderLayout.CENTER);

        JPanel bottom = new JPanel();
        bottom.setSize(new Dimension(500, 300));
        
        bottom.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,5,5,5);

        JLabel preSlagLabel = new JLabel("Slang word:");
        JTextArea preSlagText = new JTextArea(1, 20);
        JLabel preDefinitionLabel = new JLabel("Definition:");
        JTextArea preDefinitionText = new JTextArea(10, 20);

        JLabel preRecomLabel = new JLabel("Recommend:");
        JList preRecomList = new JList<String>(RelativeWords);
        preRecomList.setLayoutOrientation(JList.VERTICAL);
        JScrollBar scrollBar = new JScrollBar(JScrollBar.VERTICAL);
        scrollBar.add(preRecomList);
        scrollBar.setPreferredSize(new Dimension(100, 100));

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
        
        this.add(bottom, BorderLayout.PAGE_END);
    }

    public String[] getInputText()
    {
        String[] result = new String[2];
        result[0] = text;
        result[1] = selectedMode;
        return result;
    }

    public void setRepresentResult(String foundSlag, String foundDef, Vector<String> recommend)
    {
        if(foundSlag == " " || foundSlag == null)
        {
            curSlagWord = "No result";
            curDefinition = " ";
            RelativeWords.clear();
            return;
        }
        curSlagWord = foundSlag;
        curDefinition = foundDef;
        RelativeWords.clear();
        RelativeWords.addAll(recommend);
    }
}

class HistoryMenu extends JPanel
{

}

class RandomMenu extends JPanel
{

}

class QuizMenu extends JPanel
{

}
