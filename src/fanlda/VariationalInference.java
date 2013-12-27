/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fanlda;

import org.apache.commons.math3.special.Gamma;

/**
 *
 * @author Liu Fan
 * Variation inference of a single document, used by parameterEstimation.java
 */
public class VariationalInference {

    Model mod;
    Document doc;
    double convergeThreshold;
    int max;
    double likelihood;
    double[][] phi;
    double[] gamma;
    double[] digamma;
    double dif_digamma_digammaSum;

    public VariationalInference(Model m, Document d, double threshold, int maxIter) {
        mod = m;
        doc = d;
        convergeThreshold = threshold;
        max = maxIter;
    }

    public void infer() {
        double[][] logBeta = mod.getLogBeta();
        double alpha = mod.getAlpha();
        int topics = mod.getTopics();
        int length = doc.getLength(); // # of unqiue words in the document
        phi = new double[length][topics];
        gamma = new double[topics];
        double initial = (double) doc.getTotal() / topics;
        double likelihoodOld = 0;
        digamma = new double[topics];
        for (int k = 0; k < topics; k++) {
            gamma[k] = alpha + initial;
            digamma[k] = Gamma.digamma(gamma[k]);
            for (int n = 0; n < length; n++) {
                phi[n][k] = (double) 1 / topics;
            }
        }
        boolean converged = false;
        int iter = 0;
        while (!converged) {
            iter++;
            for (int n = 0; n < length; n++) {
                double phiSum = 0;
                double[] phiOld = new double[topics];
                for (int k = 0; k < topics; k++) {
                    phiOld[k] = phi[n][k];
                    phi[n][k] = logBeta[k][doc.getWord(n)] + digamma[k];
                    if (k > 0) {
                        phiSum = Utils.logSum(phiSum, phi[n][k]);
                    } else {
                        phiSum = phi[n][k];
                    }                    
                }
                
                for (int k = 0; k < topics; k++) {
                    phi[n][k] = Math.exp(phi[n][k] - phiSum);
                    gamma[k] = gamma[k] + doc.getWordCount(n) * (phi[n][k] - phiOld[k]);
                    //digamma[k] = Gamma.digamma(gamma[k]);
                }//normalize phiSum[n][i] to sum to 1
            }
            for (int k = 0; k < topics; k++) {
                digamma[k] = Gamma.digamma(gamma[k]);
            } // this is where the difference between my implementation and David Blei's C implementation is
            likelihoodOld = likelihood;
            likelihood = likelihood();
            if (Math.abs((likelihood - likelihoodOld) / likelihoodOld) < convergeThreshold || iter == max) {
                converged = true;
            }            
        }
    }

    public double likelihood() {
        likelihood = 0;
        double alpha = mod.getAlpha();
        double[][] logBeta = mod.getLogBeta();
        int topics = mod.getTopics();
        int length = doc.getLength(); // # of unqiue words in the document
        double gammaSum = 0;
        for (int i = 0; i < topics; i++) {
            gammaSum += gamma[i];
        }
        double digammaGammaSum = Gamma.digamma(gammaSum);        
        likelihood = Gamma.logGamma(topics * alpha) - topics * Gamma.logGamma(alpha) - Gamma.logGamma(gammaSum);
        dif_digamma_digammaSum = 0;
        for (int i = 0; i < topics; i++) {
            double dif=digamma[i] - digammaGammaSum;// value of digamma[i] - digammaGammaSum 
            dif_digamma_digammaSum += dif;
            likelihood += ((alpha - 1) - (gamma[i] - 1)) * dif
                    + Gamma.logGamma(gamma[i]);
            for (int n = 0; n < length; n++) {
                likelihood += phi[n][i] * doc.getWordCount(n) * (dif
                        + logBeta[i][doc.getWord(n)]
                        - Math.log(phi[n][i]));
            }
        }        
        return likelihood;
    }
}
