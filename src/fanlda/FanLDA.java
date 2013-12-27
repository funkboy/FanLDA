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
public class FanLDA {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws FileNotFoundException, IOException {
        //import the pre-processed document data, the format of data file can be
        //found in readme.txt
        ImportData ipt = new ImportData("data//ap.dat");
        Corpus c = ipt.Import();
        
        //set the number of topics, 10 in this case, it can be determined by users
        int numOfTopics=10;
        Model mod = new Model(numOfTopics, c.getTerms());
        
        //set the parameter Alpha in LDA, in this case it is set to 50/topic#,
        //it can be determined by users
        mod.setAlpha((double)50/numOfTopics);
        mod.randomInitialBeta();
        
        //set the convergence threshold for variational inference, 0.000001 in this case
        //max number of iterations variational inference for a single docuemnt, 20 in this case
        //convergence threshold for variational EM, 0.0001 in this case
        //max number of iterations of variational EM, 100 in this case
        //All these parameters can be determined by users
        ParameterEstimation pe = new ParameterEstimation(mod, c, 0.000001, 20, 0.0001, 100);
        
        //estimate the parameter Alpha and Beta in LDA
        pe.Estimate();
        
        System.out.println("Alpha= "+mod.getAlpha());
        
        //wrtie estimated Beta to file
        FileWriter fw=new FileWriter("beta.txt");
        BufferedWriter bw=new BufferedWriter(fw);
        String out="";
        double[][] beta=mod.getBeta();
        for(int i=0;i<numOfTopics;i++){            
            for(int j=0; j<c.getTerms();j++){
            out+=beta[i][j]+" ";
            }
            out+="\n";
        }
        bw.write(out);
        bw.close();
        fw.close();
    }
}
