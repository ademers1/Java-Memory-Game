/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package memorygame;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class cards {
        ImageIcon[][] deck = new ImageIcon[4][13];//2d array of cards first slot handles suit second handles card number
	ImageIcon[] usedCards = new ImageIcon[16];//16 image icons we use for the gmae
	int[] usedRandoms = new int[8];//fills this array with used cards
	Random rand = new Random();//randoms objects to generate the cards we use for our game
	Random rand2 = new Random();
	int cardNumber = rand2.nextInt(13) + 1;//variables to hold the randoms numbers holds the card number
	int suit = rand.nextInt(4) + 1; //holds the suit 
	
	
	public cards(){
		
	}
        //method to set the cards up gets the 8 cards we use for our game duplicates them and shuffles them 
	public void setCards(){
            String cardType = "";
            //switch statement to handle the image file names 
            for(int i = 0; i < 4;i++){
                switch(i){
                        case 0: 
                            cardType = "c";
                            break;
                        case 1:
                            cardType = "s";
                            break;
                        case 2:
                            cardType = "h";
                            break;
                        case 3:
                            cardType = "d";
                            break;
                    }
                for(int j = 0; j < 13; j++){
                    try {
                        //adds the images to the 2d array of cards
                        deck[i][j] = new ImageIcon(ImageIO.read(new File(getImage(j+1, cardType))));
                    } catch (IOException ex) {
                        Logger.getLogger(cards.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    
                }
            }
            for(int i = 0; i < 8; i++){
                do { // Loop while random is already used
                    suit = rand.nextInt(4) + 1;
                    cardNumber = rand2.nextInt(13) + 1;
		} while(contains(usedRandoms, cardNumber));//passes the array of used numbers and the numbers
                usedCards[i] = deck[suit-1][cardNumber-1];

                usedRandoms[i] = cardNumber;//adds the card number to our array of used numbers
            }
            for(int i = 8, p = 0; i<16; i++, p++){//duplicate the cards in the array
		usedCards[i] = usedCards[p];
            }
            
            shuffleCards(usedCards);
	}
        // checks if the array passed contains the integer passed
        private boolean contains(int[] arr, int i)
        {
            //for (int i = 0; i < arr.length; i++) int t = arr[i];
            for (int t : arr)
                if (t == i)
                    return true;
            return false;
        }
        //@params card int of the card number we use cardtype String of the location of the image files
        //returns the string of the location of the image file
	public String getImage(int card, String cardType){ 
            String image = "images/cards/card_";
            image += card;
            image += cardType;
            image += ".png";
            return image;
	}
        //shuffles the cards 
        public ImageIcon[] shuffleCards(ImageIcon[] cards){
            
                Random generator = new Random();
		for(int i = 0; i < cards.length; i++){
			int j = generator.nextInt(cards.length);
			ImageIcon temp = cards[i];
			cards[i] = cards[j];
			cards[j] = temp;
		}
		return cards;
	}
        //returns the array of used cards 
	public ImageIcon[] getCards(){
            return usedCards;
	}
}