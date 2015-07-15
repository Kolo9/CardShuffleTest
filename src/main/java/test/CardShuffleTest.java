package test;

import gnu.trove.set.hash.THashSet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CardShuffleTest {

	private static enum Suit {
		CLUBS, HEARTS, SPADES, DIAMONDS;
	};
	
	private static enum Rank {
		TWO("2"), THREE("3"), FOUR("4"), FIVE("5"), SIX("6"), SEVEN("7"), EIGHT("8"), NINE("9"), TEN("10"), JACK, QUEEN, KING, ACE;
		
		String s = null;
		
		Rank() {}
		
		Rank(String s) {
			this.s = s;
		}
		
		@Override
		public String toString() {
			if (s != null) {
				return s;
			} else {
				return this.name().charAt(0) + "";
			}
		}
	};
	
	private static class Card {
		Suit s;
		Rank r;
		
		Card(Suit s, Rank r) {
			this.s = s;
			this.r = r;
		}
		
		Card(Card c) {
			s = c.s;
			r = c.r;
		}
		
		@Override
		public int hashCode() {
			return 5*r.ordinal() + s.ordinal();
		}
		
		@Override
		public boolean equals(Object obj) {
			if (!(obj instanceof Card)) return false;
			Card c2 = (Card) obj;
			return s == c2.s && r == c2.r;
		}
		
		@Override
		public String toString() {
			return r + "" + s.toString().charAt(0);
		}		
	}
	
	private static class Deck {
		List<Card> cards = new ArrayList<Card>(52);
		
		public Deck() {}
		
		public Deck(Deck deck) {
			for (Card c: deck.cards) {
				cards.add(new Card(c));
			}
		}
		
		public void add(Card c) {
			cards.add(c);
		}
		
		@Override
		public boolean equals(Object obj) {
			if (!(obj instanceof Deck)) return false;
			Deck d2 = (Deck) obj;
			if (cards.size() != d2.cards.size()) return false;
			for (int i = 0; i < cards.size(); i++) {
				if (!cards.get(i).equals(d2.cards.get(i))) return false;
			}
			return true;
		}
		
		@Override //Can't use this to test
		public int hashCode() {
			return cards.hashCode();
		}
		
		@Override
		public String toString() {
			return cards.toString();
		}
	}
	
	
	public static void main(String[] args) {
		THashSet<Deck> shuffleSet = new THashSet<Deck>(5000000);
		//Set<Deck> shuffleSet = new HashSet<Deck>();
		//THashMap<Integer, Deck> shuffleMap = new THashMap<Integer, Deck>(500000);
		Deck deck = new Deck();
		for (Suit s: Suit.values()) {
			for (Rank r: Rank.values()) {
				deck.add(new Card(s, r));
			}
		}
		
		/*
		for (Card c1: deck.cards) {
			for (Card c2: deck.cards) {
				if (c1.equals(c2)) System.out.println(c1 + " EQUALS " + c2);
			}
		}
		*/
		
		
		int timesShuffled = 0;
		Suit firstFiveSuit;
		boolean isRoyalFlush;
		while (true) {
			Collections.shuffle(deck.cards);
			isRoyalFlush = true;
		
			firstFiveSuit = deck.cards.get(0).s;
			for (int j = 0; j < 5; j++) {
				if (deck.cards.get(j).s != firstFiveSuit
					|| deck.cards.get(j).r.ordinal() < 8)
						isRoyalFlush = false;
			}
			
			if (isRoyalFlush) {
				System.out.println("ROYAL FLUSH!");
				System.out.println(deck);
			}
			
			timesShuffled++;
			if (timesShuffled % 100000 == 0) System.out.println("Shuffled " + timesShuffled + " times");

			
			
			if (!shuffleSet.add(new Deck(deck))) {
				System.out.println("WOW GOT A MATCH!");
				System.out.println(deck);
				System.out.println("matches");
				List<Deck> shuffleList = new ArrayList<Deck>(shuffleSet.size());
				
				shuffleList.addAll(shuffleSet);
				for (int j = 0; j < shuffleList.size(); j++) {
					if (shuffleList.get(j).equals(deck)) {
						System.out.println(j + ": " + shuffleList.get(j));
					}
				}
				
				System.exit(0);
			}
			
		}
	}
}
