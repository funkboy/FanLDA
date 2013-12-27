/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fanlda;

import java.io.*;

/**
 *
 * @author Liu Fan
 */
public class ImportData {

    String path="";
    
    public ImportData(String path){
        this.path=path;
    }
    
    public Corpus Import() throws FileNotFoundException, IOException {
        FileReader fr = new FileReader(path);
        System.out.println("Reading data from " + path);
        BufferedReader br = new BufferedReader(fr);
        String line = br.readLine();
        int term = 0;
        Corpus corpus = new Corpus();
        while (line != null) {
            String[] items = line.split(" ");
            int length = Integer.parseInt(items[0]);
            int total = 0;
            Document doc = new Document(length);
            for (int i = 0; i < length; i++) {
                String[] para = items[i + 1].split(":");
                int word = Integer.parseInt(para[0]);
                int count = Integer.parseInt(para[1]);
                doc.setWord(i, word);
                doc.setWordCount(i, count);
                total += count;
                if (word >= term) {
                    term = word+1;
                }
            }
            doc.setTotal(total);
            corpus.addDoc(doc);
            line = br.readLine();
        }
        corpus.setTerms(term);
        System.out.println("# of documents: " + corpus.getSize());
        System.out.println("# of terms in vocabulary: " + term);
        return corpus;
    }
}
