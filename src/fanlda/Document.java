/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fanlda;

/**
 *
 * @author Liu Fan
 */
public class Document {

    int length; // # of unique words in the document
    int[] words; // an array saves the indices of the unqiue words in the vocabulary from the document
    int[] wordCounts;   // an array saves the counts of each unique words from the document
    int total = 0;  //the total counts of words in the document

    public Document(int l) {
        length = l;
        words = new int[length];
        wordCounts = new int[length];
    }

    public void setWord(int i, int index) {
        words[i] = index;
    }

    public void setWordCount(int i, int count) {
        wordCounts[i] = count;
    }

    public void setTotal(int t) {
        total = t;
    }

    public int getLength() {
        return length;
    }

    public int getWord(int i) {
        return words[i];
    }

    public int getWordCount(int i) {
        return wordCounts[i];
    }

    public int getTotal() {
        return total;
    }
}
