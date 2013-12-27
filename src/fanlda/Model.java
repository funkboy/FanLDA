/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fanlda;

/**
 *
 * @author Liu Fan
 */
public class Model {

    int topics;
    int terms;
    double alpha;
    double[][] beta; // the probability distribution of each topic over each terms(words)
    double[][] logBeta; // logarithm of the probability distribution of each topic over each terms(words)

    public Model(int t, int w) {
        topics = t;
        terms = w;
        beta = new double[t][w]; // the probality distribution matrix of topics over terms(words), 
        //row stands for topics, column stands for terms(words)
        logBeta = new double[t][w];
    }

    public void setAlpha(double a) {
        alpha = a;
    }

    public void setBeta(double[][] b) {
        beta = b;
        for (int i = 0; i < topics; i++) {
            for (int j = 0; j < terms; j++) {
                logBeta[i][j] = Math.log(beta[i][j]);
            }
        }
    }

    public int getTopics() {
        return topics;
    }

    public int getTerms() {
        return terms;
    }

    public double getAlpha() {
        return alpha;
    }

    public double[][] getBeta() {
        return beta;
    }

    public double[][] getLogBeta() {
        return logBeta;
    }

    public void randomInitialBeta() {
        double[][] cw = new double[topics][terms];
        double[] total = new double[topics];
        for (int i = 0; i < topics; i++) {
            total[i] = 0;
            for (int j = 0; j < terms; j++) {
                cw[i][j] = Math.random() + 1 / terms;
                total[i] += cw[i][j];
            }
            for (int j = 0; j < terms; j++) {
                if (cw[i][j] > 0) {
                    beta[i][j] = cw[i][j] / total[i];
                    logBeta[i][j] = Math.log(beta[i][j]);
                } else {
                    beta[i][j] = Math.exp(-100);
                    logBeta[i][j] = -100;
                }
            }
        }
    }
    
    public void InitialBeta(Corpus c) {
        double[][] cw = new double[topics][terms];
        for (int i = 0; i < topics; i++) {
            for (int j = 0; j < terms; j++) {
                cw[i][j] = 0;
            }
        }
        double[] total = new double[topics];
        for (int i = 0; i < topics; i++) {
            total[i] = 0;
            Document doc = c.getDoc(i);
            for (int j = 0; j < doc.length; j++) {
                cw[i][doc.getWord(j)] += doc.getWordCount(j);
            }

            for (int j = 0; j < terms; j++) {
                cw[i][j] += 0.1;
                total[i] += cw[i][j];
            }
            for (int j = 0; j < terms; j++) {
                if (cw[i][j] > 0) {
                    beta[i][j] = cw[i][j] / total[i];
                    logBeta[i][j] = Math.log(beta[i][j]);
                } else {
                    beta[i][j] = Math.exp(-100);
                    logBeta[i][j] = -100;
                }
            }
        }
    }
}
