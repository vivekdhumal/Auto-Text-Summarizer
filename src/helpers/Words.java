/**
 * Words class performs text processing on words which includes extracting
 * words from text, eliminating stopwords, find unique words from string
 * also calculate normalised frequency of each unique words and find max
 * centroid value from a sentence
 * 
 * @author Team ATS
 * @version 1.0
 */
package helpers;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Words {

	private SummaryHelper summaryHelper;

	/**
	 * This method is used to extract stop words from file stopwords.txt
	 * which contains English stop words 
	 * 
	 * @return String  this returns hash set of string
	 */
	public HashSet<String> getStopwords() {
		HashSet<String> stopwords = new HashSet<String>();
		try {			
			InputStream is = getClass().getResourceAsStream("/resources/data/stopwords.txt");
		    InputStreamReader fr = new InputStreamReader(is);
		    BufferedReader br = new BufferedReader(fr);		    
		    String strWords = "";			
			while ((strWords = br.readLine()) != null) {
				stopwords.add(strWords);
			}			
		} catch (Exception e) {
			System.out.println(e);
		}
		return stopwords;
	}

	/**
	 * This method is used to eliminate stop words from list of all words
	 * that appears in document
	 * 
	 * @param set<String> Words set of all words
	 * @return ArrayList  this returns hash set of string
	 */
	public ArrayList removeStopwords(Set<String> words) {
		String[] strArr = words.toArray(new String[words.size()]);
		ArrayList result = new ArrayList();

		HashSet<String> stopwords = getStopwords();

		if (stopwords.size() > 0) {
			for (int i = 0; i < strArr.length; i++) {

				if (strArr[i] != null && !strArr[i].equals("")) {
					String word = strArr[i].toLowerCase().replaceAll("[.\r\']",
							null);

					if (!stopwords.contains(word) && word.length() > 0
							&& !word.equals("") && !word.isEmpty()) {
						result.add(word);
					}
				}
			}
		}
		return result;
	}

	/**
	 * This method is used to extract words from string
	 * 
	 * @param content object of String
	 * @return List<String>  this will returns list of words
	 */
	public List<String> wordSeparator(String content) {
		List<String> wordList = Arrays.asList(content.split(" "));
		return wordList;
	}

	/**
	 * This method is used to extract key words or unique words from string
	 * 
	 * @param str object of String
	 * @return Set<String>  this will returns set of unique words
	 */
	public Set<String> getKeywords(String str) {
		//remove extra spaces and special characters from sentence
		summaryHelper = new SummaryHelper();
		str = summaryHelper.formatSentence(str);

		List<String> wordList = this.wordSeparator(str);
		wordList.removeAll(Collections.singleton(""));
		Set<String> words = new HashSet<String>(wordList);
		ArrayList aList = removeStopwords(words); // remove stopwords from all words
		Set<String> uniqueWords = new HashSet<String>(aList);
		uniqueWords.removeAll(Collections.singleton(""));
		return uniqueWords;
	}

	/**
	 * This method is used to normalize frequency of unique words from string
	 * 
	 * @param str object of String
	 * @param keywords set of unique words
	 * @return Map<String, Double>  this will returns set of unique words with its respected normalize frequency 
	 */
	public Map<String, Double> findFrequencyWords(String str,
			Set<String> keywords) {
		summaryHelper = new SummaryHelper();
		str = summaryHelper.formatSentence(str);
		List<String> wordList = this.wordSeparator(str);
		Map<String, Double> frequencies = new LinkedHashMap<String, Double>();
		for (String words1 : keywords) {
			double freq = Collections.frequency(wordList, words1);
			// frequency of each word by total no of words
			double termFreq = (freq / wordList.size());
			frequencies.put(words1, termFreq);
		}

		return frequencies;

	}

	/**
	 * This method is used to centroid or occurrence of unique words in string
	 * means it will return count of unique words found in a string
	 * 
	 * @param str object of String
	 * @param keywords set of unique words
	 * @return int  this will returns number of all unique words that appear in string 
	 */
	public int findMaxCentroidValue(String str, Set<String> keywords) {
		int maxCentroidValue = 0;
		summaryHelper = new SummaryHelper();
		str = summaryHelper.formatSentence(str);
		List<String> wordList = this.wordSeparator(str);
		for (String words1 : keywords) {
			int freq = Collections.frequency(wordList, words1);
			if (freq > 0)
				maxCentroidValue++;
		}
		return maxCentroidValue;
	}

}
