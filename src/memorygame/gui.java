package memorygame;
/*
@authors: Alex Demers, Nikunj Vala
@Description: gui for the game creates the j frame the buttons and the text fields
@date: Friday november 28th 2014
*/
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.*;

public final class gui extends JFrame{
        
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();//object to get screensize to set jframe in the middle of the users screen
	JPanel panel = new JPanel();//panel containing the card buttons
        JPanel buttonPanel = new JPanel();//panel containing the exit and newgame buttons
        JPanel textPanel = new JPanel();//panel containing the text fields
        JButton newGame;//creates a new game when pressed
        JButton exit;//exits the program when pressed
        JButton[] buttons = new JButton[16];//array of buttons that contains the card images
        JTextField score;//text field containing the users score
        JTextField time;//text field containing the time left 
        JTextField message;//text field containing the message for the user
        ImageIcon[] cardIcons = new ImageIcon[16];//array of the card images that gets copied to the Jbuttons array
        ImageIcon cardBack;//cardback image that the array of buttons have as default icon
        ImageIcon firstClick, secondClick;//holds the first  and second imageicons the user clicks on for camparison 
        int button1 = 0, button2 = 0; //holds the index of the jbuttons array that the user clicks
        int count = 0;//holds the number of cards the user clicks on as an int and resets if it reaches two
        int timeLeft = 60;//time left 
        int scoreInt = 0;//users score
        boolean wasCalled = false; //boolean for storing wether the final message box at end of game has appeared or not
        Timer gameTime = new Timer(1000, new TListener());//60 second game timer
        
        
        /*
        @creates the jframe using the super constuctor 
        sets size location and calls the initilise method
        */
        public gui(){
            super("MemoryGame");
            this.setSize(450,450);
            this.setResizable(false);
            this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);
            this.setDefaultCloseOperation(EXIT_ON_CLOSE);
            init();
        }
        
        
        /*
        creates the gui
        creates an array of buttons (buttons[]) to hold the card buttons
        adds icons to the card buttons and adds event listneres to them
        creates newgame buttons and exit buttons (newGame) (exit) and adds event listeners to them
        creates text fields and adds text to them
        creates 3 panels
        adds the panels to the jframe
        sets up the exit and newgame buttons and adds event listneres for them
        first panel (panel) adds the card array of buttons to the panel and specifies location
        second panel (buttonPanel) adds the newgame and exit buttons to the panel and specifies location
        third panel (textPanel) adds the textfields to the panel and specifies location
        */
        public void init(){
            panel.setLayout(new GridLayout(4,4));
            buttonPanel.setLayout(new GridLayout(1,3));
            textPanel.setLayout(new GridLayout(1,3));
            try {
                cardBack = new ImageIcon(ImageIO.read(new File("images/cards/cardback.png")));
            } catch (IOException ex) {
                Logger.getLogger(gui.class.getName()).log(Level.SEVERE, null, ex);
            }
            for(int i = 0; i < 16; i++){
                buttons[i] = new JButton();
                buttons[i].setIcon(cardBack);
                buttons[i].addActionListener(new gui.ImageButtonListener());
                panel.add(buttons[i]);
            }
            newGame = new JButton("New Game");
            exit = new JButton("Exit");
            score = new JTextField("Score: " + scoreInt);
            time = new JTextField("Time remaining: " + timeLeft);
            message = new JTextField("Pick a card, any card!");
            score.setEditable(false);
            time.setEditable(false);
            message.setEditable(false);
            newGame.addActionListener(new gui.ImageButtonListener());
            exit.addActionListener(new gui.ImageButtonListener());
            textPanel.add(time);
            textPanel.add(message);
            textPanel.add(score);
            buttonPanel.add(newGame);
            buttonPanel.add(exit);  
            newGame.setEnabled(false);
            add(textPanel, BorderLayout.NORTH);
            add(panel, BorderLayout.CENTER);  
            add(buttonPanel, BorderLayout.SOUTH);
            setVisible(true);    
        }
        
        
        /*
        copys the contents of the array to another array
        @param: cards is the array of image icons that gets copied to the array cardIcons
        */
        public void getIcons(ImageIcon[] cards){            
            System.arraycopy(cards, 0, cardIcons, 0, cardIcons.length);
	}
        
        
        /*
        @gameover method gets called when the users time runs out or all cards have been matched
        calls a method tat gives the user a message that displays wehter he wins or loses 
        stops the timer from counting and disables all buttons
        */      
        public void gameOver(){
            for (JButton button : buttons) {
                button.setEnabled(false);
                gameTime.stop();
                message.setText("Congratulations!");
                if(scoreInt < 8){
                    infoBox("You lost! Your final score is " + scoreInt, "Better luck next time!");
                }
                if(scoreInt == 8){                   
                    infoBox("You won! Your final score is " + scoreInt, "Congratulations!");
                }
                
            }
        }
        
        
        /*
        method that handles the creating of a new game 
        resets all button images re-enables them and sets them to visible
        resets all variables buttons and textfields to the original state
        creates an instance of the cards class sets up the deck shuffles them and gets the icons for the new game
        */
        public void newGame(){
            for (JButton button : buttons) {
                button.setIcon(cardBack);
                button.setVisible(true);
                button.setEnabled(true);
            }
            newGame.setEnabled(false);
            wasCalled = false;//
            gameTime.stop();
            timeLeft = 60;
            scoreInt = 0;
            time.setText("Time remaining: " + timeLeft);
            score.setText("Score: " + scoreInt);
            cards cards = new cards();
            cards.setCards();
            getIcons(cards.getCards());
        }
        
        
        /*
        info box that displays when the game ends displays either a winning or losing message depending on out come of the game 
        sets the boolean to true after to prevent an infinite loop
        */
        public void infoBox(String infoMessage, String titleBar){
            if(wasCalled == false){
                JOptionPane.showMessageDialog(this, infoMessage, titleBar, JOptionPane.INFORMATION_MESSAGE);
                wasCalled = true;
            }
        }
        
        
        /*
        listerner for the matching portion of the game
        gives the user 120 milliseconds to see what the second card is and then flips 
        them back over or removes them and adds a point to the players score 
        depending on if they matched
        */     
        private class TimerListener implements ActionListener{
            @Override
            public void actionPerformed(ActionEvent e){
                if(firstClick.getImage() == secondClick.getImage()){//handles the event of the two cards matching
                    scoreInt++;
                    message.setText("Right, pick again!");
                    buttons[button1].setVisible(false);
                    buttons[button2].setVisible(false);
                    score.setText("Score: " + scoreInt);                      
            }
                if(firstClick.getImage() != secondClick.getImage()){//handles the event of the two cards not matching
                    message.setText("Wrong, pick again!");
                    buttons[button1].setIcon(cardBack);
                    buttons[button2].setIcon(cardBack);
                }
            }                      
        }
        
        
        //lister for the gameTimer counts down from 60 seconds and calls the gameOver method if it reaches 0
        private class TListener implements ActionListener{
            @Override
            public void actionPerformed(ActionEvent e){
                if(timeLeft != 0){
                    timeLeft--;
                    time.setText("Time Remaining: " + timeLeft);
                    if(scoreInt == 8 && wasCalled == false){
                        gameOver();
                  
                    
                    }
                }
                if(timeLeft == 0){
                    gameOver();
                }
            }
        }
        
        
        /*
        event listener for the buttons calls the newgame method or exits the game if those buttons are clicked respectively
        
        */
        private class ImageButtonListener implements ActionListener {
            @Override
            public void actionPerformed(ActionEvent e){  
                newGame.setEnabled(true);//enalbes the new game button when the first card is clicked
                gameTime.start();//starts the timer for the game when the first card is clicked 
                Timer myTimer = new Timer(120,new TimerListener());//creates the 120 millisecond timer for the second card to give the user a chance to see it
                //60 milliseconds is to long
                myTimer.setRepeats(false);//makes sure the timer doesnt restart
                count++;//counts the number of clicks the user 
                if(exit == e.getSource()){
                    System.exit(0);//closes the program when exit button is pressed
                }
                if(newGame == e.getSource()){
                    count = 0; 
                    newGame(); //calls the new game method                   
                }
                //handles the events of the card button clicks 
                for (int i = 0; i < buttons.length;i++){
                    if (e.getSource().equals(buttons[i])) {
                        buttons[i].setIcon(cardIcons[i]);
                        if(count == 1){
                        firstClick = cardIcons[i];
                        firstClick.getImage();
                        button1 = i;
                        message.setText("Now pick another card!");
                        }
                        if(count == 2 && button1 != i){
                        secondClick = cardIcons[i]; 
                        button2 = i;
                        count = 0;
                        myTimer.start();
                        }                       
                        else if(count == 2 && button1 == i){
                        count = 0;
                        buttons[i].setIcon(cardBack);
                        message.setText("Pick a card, any card!");
                        }
                    }
                }
               
            }
        }   
}
	
