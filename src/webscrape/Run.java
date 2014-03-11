package webscrape;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;


//https://siftscience.com/jobs#engineer

//email to: jobs+frontend+b64@siftscience.com

public class Run {
	//Assuming that we can have an infinite set of valid letters.
	static Map<String, Integer> counter = new TreeMap<String, Integer>();
	public static void main(String[] args) {
		//Create a wordlist from the given site and tag.
		ArrayList<String> wordlist = scrape("http://www.giwersworld.org/computers/linux/common-words.phtml", "pre");
		//ArrayList<String> wordlist = scrape("http://phrontistery.info/a.html", "td");
		
		
		
		
		for(int i = 0; i < wordlist.size(); ++i){
			dissectWord(wordlist.get(i));
		}
		
		//Get a value ordered SortedSet from the TreeMap.
		SortedSet orderedList = valSort(counter);
		counter = null;
		
		//Iterate through the orderedList and print the 10 most frequent letters.
		int i = 0;
		Iterator it = orderedList.iterator();
		while(i < 10 && it.hasNext()){
			Object element = it.next();
			System.out.println(element.toString());
			++i;
		}
		
	}
	
	static <K,V extends Comparable<? super V>> SortedSet<Map.Entry<K,V>> valSort(Map<K,V> map) {
	    SortedSet<Map.Entry<K,V>> sortedEntries = new TreeSet<Map.Entry<K,V>>(
	        new Comparator<Map.Entry<K,V>>() {
	            @Override public int compare(Map.Entry<K,V> e1, Map.Entry<K,V> e2) {
	                return e2.getValue().compareTo(e1.getValue());
	            }
	        }
	    );
	    sortedEntries.addAll(map.entrySet());
	    return sortedEntries;
	}
	
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
		
		//Debug to show what I'm parsing.
		//System.out.println(list);
		
		//Split the string by newlines and put it into the returned ArrayList.
		String temp[] = list.split("\\r?\\n");
		for(int i = 0; i < temp.length; ++i){
			wordlist.add(temp[i]);
		}
		return wordlist;
	}
	
	
	
	/**
	 * Method dissectWord
	 * Adds 
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
