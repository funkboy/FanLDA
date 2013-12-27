/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fanlda;

import org.apache.commons.math3.special.Gamma;

/**
 *
 * @author Liu Fan
 * An implementation of the algorithm proposed
 * byDavid Blei, Andrew Ng, and Michael Jordan:
 * http://www.cs.berkeley.edu/~blei/papers/blei03a.pdf 
 */
public class ParameterEstimation {

    Model mod;
    Corpus cps;
    double varInferThresh;//convergence threshold for variational inference
    int varInferMax;//max number of iterations variational inference for a single docuemnt
    double emThresh;//convergence threshold for variational EM
    int emMax;//max number of iterations of variational EM

    public ParameterEstimation(Model m, Corpus c, double varInferThresh, int varInferMax, double emThresh, int emMax) {
        mod = m;
        cps = c;
        this.varInferThresh = varInferThresh;
        this.varInferMax = varInferMax;
        this.emThresh = emThresh;
        this.emMax = emMax;
    }

    public Model Estimate() {
        int docs = cps.getSize();
        double alpha = mod.getAlpha();
        double[][] beta = mod.getBeta();
        int topics = mod.getTopics();
        int terms = mod.getTerms();
        double[][] topicWordSufficientStats = new double[topics][terms];
        double[] wordSufficientStats = new double[topics];
        double[] dif_digamma_digammaSum = new double[docs];
        double sum_dif_digamma_digammaSum = 0;
        double likelihoodOld = 0;
        double likelihood = 0;
        double emConverge = 0;
        int count = 0;
        while (true) {
            sum_dif_digamma_digammaSum = 0;
            for (int i = 0; i < topics; i++) {
                wordSufficientStats[i] = 0;
                for (int j = 0; j < terms; j++) {
                    topicWordSufficientStats[i][j] = 0;
                }
            }
            likelihood = 0;
            for (int d = 0; d < docs; d++) {
                Document doc = cps.getDoc(d);
                VariationalInference varInfer = new VariationalInference(mod, doc, varInferThresh, varInferMax);
                varInfer.infer();
                likelihood += varInfer.likelihood;
                int length = doc.getLength();
                for (int i = 0; i < topics; i++) {
                    for (int n = 0; n < length; n++) {
                        double cal = varInfer.phi[n][i] * doc.getWordCount(n);
                        topicWordSufficientStats[i][doc.getWord(n)] += cal;
                        wordSufficientStats[i] += cal;
                    }
                }
                
                
                dif_digamma_digammaSum[d] = varInfer.dif_digamma_digammaSum;
                sum_dif_digamma_digammaSum += dif_digamma_digammaSum[d];              
            }
            //estimate alpha
            alpha=estimateAlpha(alpha,docs,topics,sum_dif_digamma_digammaSum);

            //estimate beta
            beta=estimateBeta(beta, topics,terms,topicWordSufficientStats,wordSufficientStats);

            count++;
            System.out.println("iteration " + count + " Alpha: " + alpha+" likelihood: "+likelihood);
            emConverge = Math.abs((likelihoodOld - likelihood) / likelihoodOld);
            likelihoodOld = likelihood;
            if (count == emMax || emConverge < emThresh) {
                break;
            }
            if(likelihoodOld - likelihood<0){
                varInferMax*=2;
            }
        }
        return mod;
    }
    
    public double estimateAlpha(double alpha, int docs, int topics, double sum_dif_digamma_digammaSum) {
        double df = 0;
        double d2f = 0;
        double logAlpha = Math.log(alpha);
        int iter = 0;
        boolean converged = false;
        while (!converged) {
            iter++;
            df = docs * (topics * Gamma.digamma(topics * alpha) - topics * Gamma.digamma(alpha)) + sum_dif_digamma_digammaSum;
            d2f = docs * (topics * topics * Gamma.trigamma(topics * alpha) - topics * Gamma.trigamma(alpha));
            logAlpha -= df / (d2f * alpha + df);
            alpha = Math.exp(logAlpha);
            if ((Math.abs(df) < 0.00001) || iter == 1000) {
                converged = true;
            }
        }
        mod.setAlpha(alpha);
        return alpha;
    }
    
    public double[][] estimateBeta(double[][] beta, int topics, int terms, 
            double[][] topicWordSufficientStats, double[] wordSufficientStats) {
        for (int i = 0; i < topics; i++) {
            for (int j = 0; j < terms; j++) {
                if (beta[i][j] > 0) {
                    beta[i][j] = topicWordSufficientStats[i][j] / wordSufficientStats[i];
                } else {
                    beta[i][j] = Math.exp(-100);
                }
            }
        }
        mod.setBeta(beta);
        return beta;
    }
    
}
