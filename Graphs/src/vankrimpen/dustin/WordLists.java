package vankrimpen.dustin;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Scanner;

/* Not really a part of the assignment
 * 
 * A collection of methods which deal with lists of words (used to create initial list of words used in WordLadder) 
 * 
 * Includes functions for the following:
 * 		Given a list of words, create a new list of words of a specified character length
 * 		Given a list of words, reduce the lists size
 * 		Generate random word ladders from a list of words  
 * 
 */
public class WordLists {
	static LinkedList<String> ladder = new LinkedList<String>();
	static ArrayList<String> wordList = new ArrayList<String>();
	
	// Main method 
	public static void main(String[] args) {
		readWords("wordlist.txt", 4);
		printRandLadders(); // generates random word ladders given list of words	
	}
	
	
	// Helper method for generateLAdders()
	// takes two strings and returns true if there is a one char difference between them
	public static boolean isRung(String word, String potential) {
		boolean isRung = false;
		int count = 0;
		for (int i = word.length()-1; i >= 0; i--) {
			if (word.charAt(i) == potential.charAt(i)) {
				count ++;
			}
		}
		if (count == word.length()-1) {
			isRung = true;
		}
		return isRung;
	}
	
	// A function that prints randomly generated word ladders given a list of words and a starting index and 
	@SuppressWarnings("unchecked")
	public static void generateLadders(int i) {
		LinkedList<String> tempLadder = new LinkedList<String>();
		tempLadder.clear();

		String word = wordList.get(i);
		tempLadder.add(word);
		
		int ind[] = makeArray(wordList.size());

		for (int j=0; j<wordList.size();j++) {
			if(wordList.get(ind[j]).length() == word.length() && ind[j]!= i) {
				if (isRung(word, wordList.get(ind[j]))) {
					tempLadder.add(wordList.get(ind[j]));
					word = wordList.get(ind[j]);
					i = j;
				}
			}
		}
		if(tempLadder.size() > ladder.size()) {
			ladder.clear();
			ladder = (LinkedList<String>) tempLadder.clone();
			System.out.println(ladder);
		}
	}
	
	// Prints randomly generated ladders from wordlist 
	private static void printRandLadders() {
		
		int indecies[] = makeArray(wordList.size());
		
		for (int i = 0; i< indecies.length;i++) {
			generateLadders(indecies[i]);
		}
		
	}
	
	// creates array of non-repeating shuffled ints from 0 to size-1
	 private static int[] makeArray(int size) {
		  // Integer type used first to make use of Collections.shuffle
		  Integer unshuffled[] = new Integer[size];
		  for (int i = 0; i < unshuffled.length; i++) {
			  unshuffled[i] = i;
		  }
		  Collections.shuffle(Arrays.asList(unshuffled));
		  // convert back to primitive and return 
		  int arr[] = new int[size];
		  for (int i = 0; i < unshuffled.length; i++) {
			  arr[i] =unshuffled[i];
			  }

			return arr;
		}
	
	 // reads a txt file of words and adds words of the given length to the word list 
	public static void readWords(String path, int length) {
		  String pattern= "^[a-zA-Z]*$";
			String line;
			File file = new File(path); 
			Scanner sc;
			try {
				sc = new Scanner(file);
				while (sc.hasNextLine()) { 
					line = sc.nextLine().toString();
					
					if (line.matches(pattern) && line.length() == length) {
						wordList.add(line.toUpperCase());
					}
							    	
				}
				sc.close();
				System.out.printf("Word list successfully read.\n");
			} catch (FileNotFoundException e) {
				System.out.println("Unable to read from " + path);
				e.printStackTrace();
			}
	}
	
	// divides a word list by the given divisor 
	@SuppressWarnings("unchecked")
	public static void modList(int divisor) {
		ArrayList<String> temp = new ArrayList<String>();
		for (int i = 0;i<wordList.size();i++) {
			if(i % divisor == 0) {
				temp.add(wordList.get(i));
			}
		}
		wordList = (ArrayList<String>) (temp.clone());
	}
	
	// writes a word list to a txt file 
	public static void writeList(String path) {
		 try {
		      FileWriter myWriter = new FileWriter(path);
		      for (String s : wordList) {
		    	  myWriter.write(s+"\n");
		      }
		     
		      myWriter.close();
		      System.out.println("Successfully wrote to the file.");
		    } catch (IOException e) {
		      System.out.println("An error occurred.");
		      e.printStackTrace();
		    }
	}

}
