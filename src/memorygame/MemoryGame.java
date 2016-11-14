package memorygame;
/*
@authors: Alex Demers, Nikunj Vala 
@description: creates a matching game that lets the user pick one of 16 cards and then clicks again to find a match if no match is found
it flips them back over and you try again gives user 60 seconds to match all 16 cards and then it ends.
@version number: 1.0.0
@date:Friday November 28th 2014
*/


public class MemoryGame {

    public static void main(String[] args) throws Exception {
        gui game = new gui();// creates the game 
        cards playCards = new cards();//creates the cards
        playCards.setCards();//sets up the deck and shuffles them
        game.getIcons(playCards.getCards());//sets up the game cards 
    }
    
}
