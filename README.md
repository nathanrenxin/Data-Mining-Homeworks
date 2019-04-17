# Data Mining(ID2222)-Homeworks

1. We implemented the stages of finding textually similar documents based on Jaccard similarity using the shingling, minhashing, and locality-sensitive hashing (LSH) techniques and corresponding algorithms.

2. We implement the Apriori algorithm for finding frequent itemsets with support at
least ‘s’ in a dataset of sales transactions. We also implement the bonus part which requires to
implement an algorithm for generating association rules between frequent itemsets discovered by using
the Apriori algorithm in a dataset of sales transactions which requires the support of at least ‘s’ and
confidence at least ‘c’, where ‘s’ and ‘c’ are given as input parameters.

3. We implement the Flajolet-Martin algorithm called as HyperLogLog in the paper "In-Core Computation of Geometric Centralities with
HyperBall: A Hundred Billion Nodes and Beyond" and the graph algorithm called as HyperBall presented in the paper that makes use of the
HyperLogLog algorithm to calculate centrality.

4. We studied, implemented and tested the spectral graph clustering algorithm as described in the paper “On Spectral Clustering: Analysis and an algorithm” by Andrew Y. Ng, Michael I. Jordan, Yair Weiss. Using our implementation of the K-eigenvector algorithm, we analysed two sample graphs.

1). A real graph “example1.dat” -- This dataset was prepared by Ron Burt. He dug out the 1966 data collected by Coleman, Katz and Menzel on medical innovation. They had collected data from physicians in four towns in Illinois, Peoria, Bloomington, Quincy and Galesburg.
2). A synthetic graph “example2.dat”.

The implementation was done with Matlab.

5. The objective was to understand distributed graph partitioning using gossip-based peer-to-peer techniques, such as JaBeJa. The assignment consisted of two sub-tasks followed by a bonus task. 

The first task was to complete JaBeJa algorithm over the boilerplate code provided. The task essentially was to implement the sampleAndSwap and findPartner functions. 

The second task required us to tweak different JaBeJa configurations in order to find the smallest edge cuts for the given graphs.

The subtasks were:
To implement a mechanism such as Simulated Annealing Algorithm and observe how this change affects the rate of convergence.
To investigate how the Ja-Be-Ja algorithm behaves when the simulated annealing is restarted after Ja-Be-Ja has converged.

We also defined our own acceptance probability function for the bonus task.




