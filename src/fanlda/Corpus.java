/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fanlda;

import java.util.ArrayList;

/**
 *
 * @author Liu Fan
 */
public class Corpus {

    ArrayList<Document> corpus = new ArrayList();
    int length=0; // # of documents in the corpus
    int terms=0; //# of terms in the vocabulary in the corpus

    public void addDoc(Document d) {
        corpus.add(d);
        length++;
    }

    public void setTerms(int n) {
        terms = n;
    }

    public Document getDoc(int i) {
        return corpus.get(i);
    }

    public int getSize() {
        return corpus.size();
    }

    public int getTerms() {
        return terms;
    }
}
