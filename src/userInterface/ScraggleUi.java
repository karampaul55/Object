
package userInterface;

import game.Game;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import java.util.ArrayList;
import java.net.URL;

/**
 *
 * @author Alexis Styron
 */
public class ScraggleUi 
{    
    private ResetGameListener resetGameListener;
    
    private JFrame frame;
    private JMenuBar menuBar;
    private JMenu gameMenu;
    private JMenuItem exit;
    private JMenuItem newGame;
    
    // Scraggle board
    private JPanel scragglePanel;
    private JButton[][] diceButtons;

    // Enter found words
    private JPanel wordsPanel;
    private JScrollPane scrollPane;
    private JTextPane wordsArea;
    
    // Set up Shake Dice Button
    private JButton shakeDice;
    
    // Setup Timer
    private JLabel timeLabel;
    private Timer timer;
    private int minutes = 3;
    private int seconds = 0;

    // Enter current word
    private JPanel currentPanel;
    private JLabel currentLabel;
    private JButton currentSubmit;
        
    // Set up Score
    private JLabel scoreLabel;
    private int score;
    
    private Game game;
    
    private final ArrayList<String> foundWords = new ArrayList<String>();

    private final int GRID = 4;
    
//    private ResetGameListener resetGameListener;
    
    public ScraggleUi(Game inGame)
    {
        game = inGame;
        initObjects();
        initComponents();
    }
    
    // Common Programming Technique to Instantiate Objects before Proceeding 
    private void initObjects() 
    {
        resetGameListener = new ResetGameListener();
    }
    
    private void initComponents()
    {
        // Initialize the JFrame
        frame = new JFrame("Scraggle");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(660, 500);
        
        // Initialize the JMenuBar and add to the JFrame
        createMenu();   

        // Initialize the JPane for the current word
        setupCurrentPanel();
        
        // Initialize the JPanel for the word entry
        setupWordPanel();
        
        // Initialize the JPanel for the Scraggle dice
        setupScragglePanel();        
        
        // Prepare Timer
        setupTimer();
                
        // Add everything to the JFrame
        frame.setJMenuBar(menuBar);
        frame.add(scragglePanel, BorderLayout.WEST);
        frame.add(wordsPanel, BorderLayout.CENTER);
        frame.add(currentPanel, BorderLayout.SOUTH);
        frame.setVisible(true);
    }
    
    // Created Method for Modularity
    private void setupTimer()
    {
        timer = new Timer(1000,new TimerListener());
        timer.start();
    }
    
    private void createMenu()
    {
        menuBar = new JMenuBar();
        gameMenu = new JMenu("Scraggle");
        gameMenu.setMnemonic('S');
        
        newGame = new JMenuItem("New Game");
        newGame.setMnemonic('N');
        newGame.addActionListener(new ResetGameListener());

        exit = new JMenuItem("Exit");
        exit.setMnemonic('E');
        exit.addActionListener(new ExitListener());
        
        gameMenu.add(newGame);    
        gameMenu.add(exit);    
        
        menuBar.add(gameMenu);
    }

    private void setupCurrentPanel()
    {
        currentPanel = new JPanel();
        currentPanel.setBorder(BorderFactory.createTitledBorder("Current Word"));

        currentLabel = new JLabel();
        currentLabel.setBorder(BorderFactory.createTitledBorder("Current Word"));
        currentLabel.setMinimumSize(new Dimension(300, 50));
        currentLabel.setPreferredSize(new Dimension(300,50));
        currentLabel.setHorizontalAlignment(SwingConstants.LEFT);
        
        currentSubmit = new JButton("Submit Word");
        currentSubmit.setMinimumSize(new Dimension(200, 100));
        currentSubmit.setPreferredSize(new Dimension(200, 50));
        currentSubmit.addActionListener(new SubmitListener()); // Register ActionListener
        
        scoreLabel = new JLabel();
        scoreLabel.setBorder(BorderFactory.createTitledBorder("Score"));
        scoreLabel.setMinimumSize(new Dimension(100, 50));
        scoreLabel.setPreferredSize(new Dimension(100,50));
        
        currentPanel.add(currentLabel);
        currentPanel.add(currentSubmit);
        currentPanel.add(scoreLabel);
    }

    private void setupWordPanel()
    {
        wordsPanel = new JPanel();
        wordsPanel.setLayout(new BoxLayout(wordsPanel, BoxLayout.Y_AXIS));
        wordsPanel.setBorder(BorderFactory.createTitledBorder("Enter Words Found"));
        
        wordsArea = new JTextPane();
        scrollPane = new JScrollPane(wordsArea);
        scrollPane.setPreferredSize(new Dimension(180, 330));
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        timeLabel = new JLabel("3:00");
        timeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        timeLabel.setFont(new Font("Serif", Font.PLAIN, 48));
        timeLabel.setPreferredSize(new Dimension(240, 100));
        timeLabel.setMinimumSize(new Dimension(240, 100));
        timeLabel.setMaximumSize(new Dimension(240, 100));
        timeLabel.setBorder(BorderFactory.createTitledBorder("Time Left"));
        
        shakeDice = new JButton("Shake Dice");
        shakeDice.setPreferredSize(new Dimension(240, 100));
        shakeDice.setMinimumSize(new Dimension(240, 100));
        shakeDice.setMaximumSize(new Dimension(240, 100));
        shakeDice.addActionListener(new ResetGameListener());
        
        wordsPanel.add(scrollPane);
        wordsPanel.add(timeLabel);
        wordsPanel.add(shakeDice);
    }

    private void setupScragglePanel()
    {   
        scragglePanel = new JPanel();
        scragglePanel.setLayout(new GridLayout(4, 4));
        scragglePanel.setMinimumSize(new Dimension(400, 400));
        scragglePanel.setPreferredSize(new Dimension(400, 400));
        scragglePanel.setBorder(BorderFactory.createTitledBorder("Scraggle Board"));
        
        diceButtons = new JButton[GRID][GRID];
        
        // Creates icon and button and places them in the correct grid index
        for(int row = 0; row < GRID; row++)
            for(int col = 0; col < GRID; col++)
        {
            URL imgPath = getClass().getResource(game.getGrid()[row][col].getImgPath());
            ImageIcon icon = new ImageIcon(imgPath);
            diceButtons[row][col] = new JButton(icon);
            diceButtons[row][col].putClientProperty("row", row);
            diceButtons[row][col].putClientProperty("col", col);
            diceButtons[row][col].putClientProperty("letter", game.getGrid()[row][col].getLetter());
            diceButtons[row][col].addActionListener(new LetterListener());
            scragglePanel.add(diceButtons[row][col]);
        }
    }
    
    // Method to take an acceptable current word and add it properly to the text area
    private void updateTextArea(String data)
    {
        wordsArea.setText(wordsArea.getText() + "\n" + data);
        wordsArea.setCaretPosition(wordsArea.getDocument().getLength());
    }
    
//    private class ScraggleListener implements ActionListener
//    {
//        @Override
//        public void actionPerformed(ActionEvent ae) 
//        {
//           
//        }
//    }
    
    private class ExitListener implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e) 
        {
            int response = JOptionPane.showConfirmDialog(frame,"Confirm to exit Scraggle?",
                    "Exit?", JOptionPane.YES_NO_OPTION);
            
            if (response == JOptionPane.YES_OPTION)
                System.exit(0);
        }
    }
    
    private class ResetGameListener implements ActionListener
    {

        @Override
        public void actionPerformed(ActionEvent e) 
        {
            // reset scraggle board
            score = 0;
            game.populateGrid();
            
            // rest main scraggle panel and add it back
            frame.remove(scragglePanel);
            scragglePanel.removeAll();
            setupScragglePanel();
            scragglePanel.revalidate();
            scragglePanel.repaint();
            frame.add(scragglePanel,BorderLayout.WEST);
            
            // Empty out scraggle panel of all contents
            wordsArea.setText("");
            scoreLabel.setText("0");
            currentLabel.setText("");
            timeLabel.setText("3:00");
            foundWords.removeAll(foundWords);
            
            timer.stop();
            minutes = 3;
            seconds = 0;
            timer.start();
        }
    }
    
    private class SubmitListener implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e) 
        {
            // Compares submited word to dictionary results
            int wordScore = game.getDictionary().search(currentLabel.getText().toLowerCase());
            System.out.println(wordScore);        
            // Check if word has already been used
            if (foundWords.contains(currentLabel.getText().toLowerCase())) 
            {
                JOptionPane.showMessageDialog(frame, "Word found already");
            }
            else 
            {
                if (wordScore > 0) 
                {
                    updateTextArea(currentLabel.getText());
                    foundWords.add(currentLabel.getText().toLowerCase());
                    score+= wordScore;
                    scoreLabel.setText(String.valueOf(score));
                }
            }
            currentLabel.setText("");
        }
    }
    
    private class LetterListener implements ActionListener
    {

        @Override
        public void actionPerformed(ActionEvent e) 
        {
            if (e.getSource() instanceof JButton)
            {
                JButton button = (JButton)e.getSource();
                String letter = (String)button.getClientProperty("letter");
                currentLabel.setText(currentLabel.getText() + letter);
            }
        }
    }

//    private class TileListener implements ActionListener
//    {
//        @Override
//        public void actionPerformed(ActionEvent e) 
//        {
//        }
//        
//    }
    
    private class TimerListener implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e) 
        {
            if(seconds == 0 && minutes == 0)
            {              
                timer.stop();
                JOptionPane.showMessageDialog(frame,"Timer has run out. Game has Stopped");
            }
            else
            {
                if(seconds == 0)
                {
                    seconds = 59;
                    minutes--;
                }
                else
                {
                    seconds--;
                }
            }

            if(seconds < 10)
            {
                String strSeconds = "0" + String.valueOf(seconds);
                timeLabel.setText(String.valueOf(minutes) + ":" + strSeconds);
            }
            else
            {
                timeLabel.setText(String.valueOf(minutes) + ":" + String.valueOf(seconds));
            }
        }
        
    }


    
}

          