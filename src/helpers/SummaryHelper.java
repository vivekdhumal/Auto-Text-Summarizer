/**
 * SummaryHelper class
 * Performs extractive summarization of a document
 * by algorithmically selecting a list of importance sentences 
 * and arrange that sentences in a chronological order
 * this class also includes method of text preprocessing, sentence extraction, finding sentence score
 * and also apply compression rate before producing summary
 * 
 * @author Team ATS
 * @version 1.0
 */
package helpers;

import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class SummaryHelper {

	private int COMPRESS_RATE = 10; // default compress rate or summary length
									// is 10

	/**
	 * This method is used to set compression rate
	 * 
	 * @param compression
	 */
	public void setCompressionRate(int compression) {
		this.COMPRESS_RATE = compression;
	}

	/**
	 * This method is used to get compression rate
	 * 
	 * @return int compression rate value
	 */
	public int getCompressionRate() {
		return this.COMPRESS_RATE;
	}

	/**
	 * This method is used to extract sentence from string
	 * 
	 * @param str
	 * @return ArrayList<String> list of sentences
	 */
	public ArrayList<String> getSentences(String str) {
		ArrayList<String> strSentences = new ArrayList<String>();
		BreakIterator iteratorSentence = BreakIterator
				.getSentenceInstance(Locale.US);
		iteratorSentence.setText(str);
		int start = iteratorSentence.first();
		for (int end = iteratorSentence.next(); end != BreakIterator.DONE; start = end, end = iteratorSentence
				.next()) {
			strSentences.add(str.substring(start, end));
		}
		return strSentences;
	}

	/**
	 * This method is used to calculate total sentence frequency of each
	 * sentence
	 * 
	 * @param str
	 * @return ArrayList<String> list of sentences
	 */
	public double calculateTotalSentenceFrequency(
			Map<String, Double> sentenceFreq) {
		double totalFrequency = 0;
		for (Double wordOccur : sentenceFreq.values()) {
			totalFrequency += wordOccur;
		}
		return totalFrequency;
	}

	/**
	 * This method is used to format sentence by eliminating special characters,
	 * extra spaces
	 * 
	 * @param sentence
	 * @return String formated sentence
	 */
	public String formatSentence(String sentence) {
		String content = new String(sentence);
		content.replaceAll("[.\r\']", "");
		content = content.replaceAll("[^\\w\\s]", "");
		content = content.replaceAll("\\s+", " ");
		content = content.trim().toLowerCase();
		return content;
	}

	/**
	 * This method is used to prepare sentence score for each sentence
	 * 
	 * @param content
	 * @return Map<Integer, Double> matrix of sentence index and its score
	 */
	public Map<Integer, Double> prepareSentenceScore(String content) {
		Words words = new Words();
		Map<Integer, Double> finalScore = new LinkedHashMap<Integer, Double>();
		Set<String> uniqwords = words.getKeywords(content); // get all keyword

		ArrayList<String> sentences = this.getSentences(content); // get sentences

		int index = 0;
		double indexpos = 0;
		for (String sentence : sentences) {
			double sentenceTotalFreq = 0;
			double sentencePos, sentenceScore;
			// find frequency of words
			Map<String, Double> sentenceFreq = words.findFrequencyWords(
					sentence, uniqwords);
			// find max centroid
			int maxCentroidValue = words.findMaxCentroidValue(sentence,
					uniqwords);
			// calculate sentence frequency
			sentenceTotalFreq = this
					.calculateTotalSentenceFrequency(sentenceFreq);
			// calculate sentence position weight
			sentencePos = ((sentences.size() - indexpos) / (double) sentences
					.size()) * maxCentroidValue;
			// calculate sentence score
			sentenceScore = sentenceTotalFreq + sentencePos;

			finalScore.put(index, sentenceScore);

			indexpos++;
			index++;
		}
		return finalScore;
	}

	/**
	 * This method is used to sort sentences by its sentence score from higher to lower
	 * 
	 * @param strScores Map<Integer, Double>
	 * @return ArrayList<Integer> this will returns array list of indices of sentences from higher to lower order by sentence score
	 */
	public ArrayList<Integer> sortSentences(Map<Integer, Double> strScores) {
		ArrayList<Integer> strSentences = new ArrayList<Integer>();
		Set<Entry<Integer, Double>> set = strScores.entrySet();
		List<Entry<Integer, Double>> list = new ArrayList<Entry<Integer, Double>>(
				set);
		Collections.sort(list, new Comparator<Map.Entry<Integer, Double>>() {
			public int compare(Map.Entry<Integer, Double> o1,
					Map.Entry<Integer, Double> o2) {
				return (o2.getValue()).compareTo(o1.getValue());
			}
		});
		for (Map.Entry<Integer, Double> entry : list) {
			strSentences.add(entry.getKey());
		}
		return strSentences;
	}

	/**
	 * This method is used to sort sentence indices as per chronological order in the original document
	 * and it will take only that much sentences which will come under the max summary length
	 * means here we are applying compression rate
	 * 
	 * @param sentences ArrayList<String>
	 * @param sortedIndices ArrayList<Integer>
	 * @return ArrayList<Integer> this will returns array list of indices of sentences by chronological order
	 */
	public ArrayList<Integer> sortByDocumentIndices(
			ArrayList<String> sentences, ArrayList<Integer> sortedIndices) {
		ArrayList<Integer> sentenceIndices = new ArrayList<Integer>();
		float sentenceSize = sentences.size();
		int compressionRate = getCompressionRate();
		float summaryLength = Math.round(sentenceSize * (float) compressionRate
				/ (double) 100);
		if (summaryLength < 1)
			summaryLength = 1;
		for (int i = 0; i < summaryLength; i++) {
			sentenceIndices.add(sortedIndices.get(i));
		}
		Collections.sort(sentenceIndices); // sorting indices
		return sentenceIndices;
	}

	/**
	 * This method is used to producing final summary
	 * 
	 * @param sentences ArrayList<String>
	 * @param sortedIndices ArrayList<Integer>
	 * @return StringBuilder this will returns summary
	 */
	public StringBuilder makeSummary(ArrayList<String> sentences,
			ArrayList<Integer> sortedIndices) {
		StringBuilder str = new StringBuilder();
		ArrayList<Integer> sortedSentence = sortByDocumentIndices(sentences,
				sortedIndices); // sorting sentences as per original order in
								// document
		for (int sIndex : sortedSentence) {
			String sentence = sentences.get(sIndex);
			sentence = sentence.replaceAll("\\s+", " ");
			str.append("\n\n" + sentence);
		}
		return str;
	}

	/**
	 * This method is used find important sentence from content
	 * 
	 * @param content String
	 * @return List<String> this will returns list of important words
	 */
	public List<String> findImportantwords(String content) {
		ArrayList<String> keyWords = new ArrayList<String>();

		Words words = new Words();
		Set<String> uniqwords = words.getKeywords(content);
		Map<String, Double> frequencieskeyword = words.findFrequencyWords(
				content, uniqwords);
		Map<String, Double> eachwords = sortKeyword(frequencieskeyword);
		// Converting HashMap keys into ArrayList
		List<String> keyList = new ArrayList<String>(eachwords.keySet());
		for (String temp : keyList) {
			// wordskey.append("\n"+temp);
			keyWords.add(temp);
		}
		return keyWords;
	}

	/**
	 * This method is sort keywords from higher to lower values
	 * 
	 * @param frequencieskeyword Map<String, Double>
	 * @return Map<String, Double> this will returns sorted key words
	 */
	private Map<String, Double> sortKeyword(
			Map<String, Double> frequencieskeyword) {
		Set<Entry<String, Double>> set = frequencieskeyword.entrySet();
		List<Entry<String, Double>> list = new ArrayList<Entry<String, Double>>(
				set);
		Collections.sort(list, new Comparator<Map.Entry<String, Double>>() {
			public int compare(Map.Entry<String, Double> o1,
					Map.Entry<String, Double> o2) {
				return (o2.getValue()).compareTo(o1.getValue());
			}
		});
		// Convert sorted map back to a Map
		Map<String, Double> sortedMap = new LinkedHashMap<String, Double>();

		for (Map.Entry<String, Double> entry : list) {
			System.out.println(entry.getKey() + " ==== " + entry.getValue());
			sortedMap.put(entry.getKey(), entry.getValue());

		}
		return sortedMap;
	}
}
