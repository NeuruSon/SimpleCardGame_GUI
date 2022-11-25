/*
 * Matt Levan
 * CSC 331, Dr. Amlan Chatterjee
 * Data Structures
 *
 * Project 3 -- Simple Card Game
 *
 * Hand.java
 *
 * CITATION: 
 * Java Programming: From the Ground Up by Bravaco, Simonson
 * Page 481
 *
 * Original code modified to fit the needs of the project.
 *
 * Due in full by 11/16/2015 @ 11:59 PM.
 *
 */

public class Hand {
     // Attributes

     private SingleLinkedList cards; // Cards in hand

     // Default constructor

     public Hand() {
          cards = new SingleLinkedList(); // A singly linked list of cards
     }

     // Methods

     public void addCard(Card card) {
          cards.add(card);
     }

     public Card playCard() {
          Card cardToPlay = (Card) cards.removeFirst();

          return cardToPlay;
     }

     public int getSize() {
          return cards.size;
     }
}