/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fanlda;

import java.io.*;
import java.util.*;

/**
 *
 * @author Liu Fan
 */
public class Utils {

    public static double logSum(double log_a, double log_b) {
        return log_a + Math.log(1 + Math.exp(log_b - log_a));
    }
    
    public static void main(String[] args) throws FileNotFoundException, IOException{
        String input="sample_data";
        String output="dataset.txt";
        boolean dir=true;
        wordCount(input, output, dir);
    }

    public static HashMap<String, Integer> wordCount(String path, String output, boolean dir) throws FileNotFoundException, IOException {
        HashMap<String, Integer> wordList = new HashMap();
        ArrayList<String> dict = new ArrayList();
        FileWriter fw = new FileWriter(output);
        BufferedWriter bw = new BufferedWriter(fw);
        if (dir) {
            File folder = new File(path);
            File[] files = folder.listFiles();
            for (int i = 0; i < files.length; i++) {
                String str = files[i].getAbsolutePath();
                System.out.println(str);
                wordCountFile(str, wordList, dict);
                System.out.println(wordList.size());
            }
            for (int i = 0; i < files.length; i++) {
                Set<String> dic = wordList.keySet();
                Iterator<String> iter = dic.iterator();
                while (iter.hasNext()) {
                    String key = iter.next();
                    wordList.put(key, 0);
                }
                System.out.println("Writing file "+i);
                wordCountFile(files[i].getAbsolutePath(), wordList, dict);
                iter = dic.iterator();
                int total = 0;
                String str = "";
                int length=0;
                while (iter.hasNext()) {
                    String key = iter.next();
                    int cnt = wordList.get(key);
                    if (cnt > 0) {
                        total += cnt;
                        int index = dict.indexOf(key);
                        str += " " + index + ":" + cnt;
                        length++;
                    }
                }
                str = length + str;
                bw.write(str);
                bw.newLine();
            }
            bw.close();
            fw.close();
            fw=new FileWriter("dict.txt");
            bw=new BufferedWriter(fw);
            for(int i=0;i<dict.size();i++){
                bw.write(i+"\t"+dict.get(i));
                bw.newLine();
            }
            bw.close();
            fw.close();
        } else {
            wordCountFile(path, wordList, dict);
        }
        return wordList;
    }

    public static void wordCountFile(String path, HashMap<String, Integer> wordList, ArrayList<String> dict) throws FileNotFoundException, IOException {
        FileReader fr = new FileReader(path);
        BufferedReader br = new BufferedReader(fr);
        int n = br.read();
        String str = "";
        while (n != -1) {
            if ((n > 64 && n < 91) || (n > 96 && n < 123)) {
                char ch = (char) n;
                str += ch;
            } else {
                if (!"".equals(str)) {
                    updateWordList(str, wordList, dict);
                }
                str = "";
            }
            n=br.read();
        }
    }

    public static void updateWordList(String str, HashMap<String, Integer> wordList, ArrayList<String> dict) {
        if (wordList.containsKey(str)) {
            int count = wordList.get(str);
            count++;
            wordList.put(str, count);
        } else {
            wordList.put(str, 1);
            dict.add(str);
        }
    }
}
