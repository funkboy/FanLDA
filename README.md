FanLDA
======

This example is a simple JAVA implementation of variational expectation maximization inference for Latent Dirichlet Allocation(LDA) proposed in the original paper paper by David Blei, Andrew Ng, and Michael Jordan: http://www.cs.berkeley.edu/~blei/papers/blei03a.pdf.

data folder contains a formatted corpus and the original text file of 2245 documents from Associated Press -- acquired from David M. Blei: http://www.cs.princeton.edu/~blei/lda-c/index.html

sample_data contains a list of 3 plain text files, can be used as an example of processing plain text files into formatted data


Data format

Under LDA, the words of each document are assumed exchangeable.  Thus,
each document is succinctly represented as a sparse vector of word
counts. The data is a file where each line is of the form:

     [M] [term_1]:[count] [term_2]:[count] ...  [term_N]:[count]

where [M] is the number of unique terms in the document, and the
[count] associated with each term is how many times that term appeared
in the document.  Note that [term_1] is an integer which indexes the
term; it is not a string.


The main function of Utils.java helps convert a plain text file/a list od plain text file into the formatted data set.

You are free to redistribute and/or modify this code for research and acamedic purpose. Please contact lifuan.seu@gmail.com if you want use this code for publication or commercial use. 
