package webscrape;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;


//https://siftscience.com/jobs#engineer


public class Run {
	//Assuming that we can have an infinite set of valid letters.
	static Map<String, Integer> counter = new TreeMap<String, Integer>();
	public static void main(String[] args) {
		//Create a wordlist from the given site and tag.
		ArrayList<String> wordlist = scrape("http://www.giwersworld.org/computers/linux/common-words.phtml", "pre");
		
		
		for(int i = 0; i < wordlist.size(); ++i){
			dissectWord(wordlist.get(i));
		}
		
		//Get a value ordered SortedSet from the TreeMap.
		List<Entry<String, Integer>> orderedList = valSort(counter);
		counter = null;
		
		//Iterate through the orderedList and print the 10 most frequent letters.
		Iterator<Entry<String, Integer>> it = orderedList.iterator();
		for(int i = 0; i < 10 && it.hasNext(); ++i){
			Object element = it.next();
			System.out.println(element.toString());
		}
		
	}
	
	/**
	 * Method valSort
	 * Extends generic TreeSet functionality to sort by the value, as opposed
	 * to the key. Then return a SortedSet of generic Map Entries.
	 * 
	 * @param map 
	 * @return SortedSet sorted by value
	 */
	static <K,V extends Comparable<? super V>> List<Entry<K, V>> valSort(Map<K,V> map) {

		List<Entry<K,V>> sortedEntries = new ArrayList<Entry<K,V>>(map.entrySet());
	
		Collections.sort(sortedEntries, new Comparator<Entry<K,V>>() {
	        @Override
	        public int compare(Entry<K,V> e1, Entry<K,V> e2) {
	            return e2.getValue().compareTo(e1.getValue());
	        }
		}
		);

		return sortedEntries;
	}
	
	/**
	 * Method scrape
	 * Goes to a given site, looks for the given tag, and puts newline-
	 * separated text into an ArrayList for further processing. Also removes
	 * empty lines and any tags.
	 * 
	 * @param url (String) URL of the target site.
	 * @param tag (String) Dictates which HTML tags to look for when parsing
	 * 	text.
	 * @return ArrayList<String> filled with newline-delimited words/phrases.
	 */
	private static ArrayList<String> scrape(String url, String tag){
		ArrayList<String> wordlist = new ArrayList<String>();
		Document doc;
		Elements content;
		String list = "";
		try {
			doc = Jsoup.connect(url).timeout(500).userAgent("Webscrape").get();
			content = doc.getElementsByTag(tag);
			//temp1 = doc.toString();
			list = content.toString();
		} catch (IOException e) {
			System.out.println("Error while scraping site.");
			e.printStackTrace();
		}
		
		//Remove the tags.
		list = list.replaceAll("\\<.*?\\>", "");
		
		//Remove whitespace lines.
		list = list.replaceAll("(?m)^[ \t]*\r?\n", "");
		
		//System.out.println(list); //DEBUG
		
		//Split the string by newlines and put it into the returned ArrayList.
		String temp[] = list.split("\\r?\\n");
		for(int i = 0; i < temp.length; ++i){
			wordlist.add(temp[i]);
		}
		return wordlist;
	}
	
	
	
	/**
	 * Method dissectWord
	 * Adds the letters of a given word to the counter map.
	 * 
	 * @param word
	 */
	private static void dissectWord(String word){
		for(int i = 0; i < word.length(); ++i){
			String letter = word.substring(i, i + 1);
			if(!counter.containsKey(letter)){
				counter.put(letter, 1);
			}else{
				int updatedValue = counter.get(letter) + 1;
				counter.put(letter, updatedValue);
			}
		}
	}
}
