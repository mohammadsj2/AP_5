public class InterfaceBuild {
    /*
    Pattern recognition has its origins in engineering, whereas machine learning grew
out of computer science. However, these activities can be viewed as two facets of
the same field, and together they have undergone substantial development over the
past ten years. In particular, Bayesian methods have grown from a specialist niche to
become mainstream, while graphical models have emerged as a general framework
for describing and applying probabilistic models. Also, the practical applicability of
Bayesian methods has been greatly enhanced through the development of a range of
approximate inference algorithms such as variational Bayes and expectation propagation. Similarly, new models based on kernels have had significant impact on both
algorithms and applications.
This new textbook reflects these recent developments while providing a comprehensive introduction to the fields of pattern recognition and machine learning. It is
aimed at advanced undergraduates or first year PhD students, as well as researchers
and practitioners, and assumes no previous knowledge of pattern recognition or machine learning concepts. Knowledge of multivariate calculus and basic linear algebra
is required, and some familiarity with probabilities would be helpful though not essential as the book includes a self-contained introduction to basic probability theory.
Because this book has broad scope, it is impossible to provide a complete list of
references, and in particular no attempt has been made to provide accurate historical
attribution of ideas. Instead, the aim has been to give references that offer greater
detail than is possible here and that hopefully provide entry points into what, in some
cases, is a very extensive literature. For this reason, the references are often to more
recent textbooks and review articles rather than to original sources.
The book is supported by a great deal of additional material, including lecture
slides as well as the complete set of figures used in the book, and the reader is
encouraged to visit the book web site for the latest information:
http://research.microsoft.com/∼cmbishop/PRML
vii
viii PREFACE
Exercises
The exercises that appear at the end of every chapter form an important component of the book. Each exercise has been carefully chosen to reinforce concepts
explained in the text or to develop and generalize them in significant ways, and each
is graded according to difficulty ranging from (), which denotes a simple exercise
taking a few minutes to complete, through to (), which denotes a significantly
more complex exercise.
It has been difficult to know to what extent these solutions should be made
widely available. Those engaged in self study will find worked solutions very beneficial, whereas many course tutors request that solutions be available only via the
publisher so that the exercises may be used in class. In order to try to meet these
conflicting requirements, those exercises that help amplify key points in the text, or
that fill in important details, have solutions that are available as a PDF file from the
book web site. Such exercises are denoted by www . Solutions for the remaining
exercises are available to course tutors by contacting the publisher (contact details
are given on the book web site). Readers are strongly encouraged to work through
the exercises unaided, and to turn to the solutions only as required.
Although this book focuses on concepts and principles, in a taught course the
students should ideally have the opportunity to experiment with some of the key
algorithms using appropriate data sets. A companion volume (Bishop and Nabney,
2008) will deal with practical aspects of pattern recognition and machine learning,
and will be accompanied by Matlab software implementing most of the algorithms
discussed in this book.
Acknowledgements
First of all I would like to express my sincere thanks to Markus Svensen who ´
has provided immense help with preparation of figures and with the typesetting of
the book in LATEX. His assistance has been invaluable.
I am very grateful to Microsoft Research for providing a highly stimulating research environment and for giving me the freedom to write this book (the views and
opinions expressed in this book, however, are my own and are therefore not necessarily the same as those of Microsoft or its affiliates).
Springer has provided excellent support throughout the final stages of preparation of this book, and I would like to thank my commissioning editor John Kimmel
for his support and professionalism, as well as Joseph Piliero for his help in designing the cover and the text format and MaryAnn Brickner for her numerous contributions during the production phase. The inspiration for t
PREFACE ix
Many people have helped by proofreading draft material and providing comments and suggestions, including Shivani Agarwal, Cedric Archambeau, Arik Azran, ´
Andrew Blake, Hakan Cevikalp, Michael Fourman, Brendan Frey, Zoubin Ghahramani, Thore Graepel, Katherine Heller, Ralf Herbrich, Geoffrey Hinton, Adam Johansen, Matthew Johnson, Michael Jordan, Eva Kalyvianaki, Anitha Kannan, Julia
Lasserre, David Liu, Tom Minka, Ian Nabney, Tonatiuh Pena, Yuan Qi, Sam Roweis,
Balaji Sanjiya, Toby Sharp, Ana Costa e Silva, David Spiegelhalter, Jay Stokes, Tara
Symeonides, Martin Szummer, Marshall Tappen, Ilkay Ulusoy, Chris Williams, John
Winn, and Andrew Zisserman.
Finally, I would like to thank my wife Jenna who has been hugely supportive
throughout the several years it has taken to write this book.
Chris Bishop
Cambridge
February 2006
Mathematical notation
I have tried to keep the mathematical content of the book to the minimum necessary to achieve a proper understanding of the field. However, this minimum level is
nonzero, and it should be emphasized that a good grasp of calculus, linear algebra,
and probability theory is essential for a clear understanding of modern pattern recognition and machine learning techniques. Nevertheless, the emphasis in this book is
on conveying the underlying concepts rather than on mathematical rigour.
I have tried to use a consistent notation throughout the book, although at times
this means departing from some of the conventions used in the corresponding research literature. Vectors are denoted by lower case bold Roman letters such as
x, and all vectors are assumed to be column vectors. A superscript T denotes the
transpose of a matrix or vector, so that xT will be a row vector. Uppercase bold
roman letters, such as M, denote matrices. The notation (w1,...,wM) denotes a
row vector with M elements, while the correspondin
xii MATHEMATICAL NOTATION
E[x]. If the distribution of x is conditioned on another variable z, then the corresponding conditional expectation will be written Ex[f(x)|z]. Similarly, the variance
is denoted var[f(x)], and for vector variables the covariance is written cov[x, y]. We
shall also use cov[x] as a shorthand notation for cov[x, x]. The concepts of expectations and covariances are introduced in Section 1.2.2.
If we have N values x1,..., xN of a D-dimensional vector x = (x1,...,xD)T,
we can combine the observations into a data matrix X in which the nth row of X
corresponds to the row vector xT
n. Thus the n, i element of X corresponds to the
i
th element of the nth observation xn. For the case of one-dimensional variables we
shall denote such a matrix by x, which is a column vector whose nth element is xn.
Note that x (which has dimensionality N) uses a different typeface to distinguish it
from x (which has dimensionality D).
Contents
Preface vii
Mathematical notation xi
Contents xiii
1 Introduction 1
1.1 Example: Polynomial Curve Fitting . . . . ............. 4
1.2 Probability Theory . . . . . . . . . . . . . . . . . . . . . . . . . . 12
1.2.1 Probability densities . . . . . . . . . . . . . . . . . . . . . 17
1.2.2 Expectations and covariances . . . . . . . . . . . . . . . . 19
1.2.3 Bayesian probabilities . . . . . . . . . . . . . . . . . . . . 21
1.2.4 The Gaussian distribution . . . . . . . . . . . . . . . . . . 24
1.2.5 Curve fitting re-visited . . . . . . . . . . . . . . . . . . . . 28
1.2.6 Bayesian curve fitting . . . . . . . . . . . . . . . . . . . . 30
1.3 Model Selection . . . . . . . . . . . . . . . . . . . . . . . . . . . 32
1.4 The Curse of Dimensionality . . . . . . . . . . . . . . . . . . . . . 33
1.5 Decision Theory . . . . . . . . . . . . . . . . . . . . . . . . . . . 38
1.5.1 Minimizing the misclassification rate . . . . . . . . . . . . 39
1.5.2 Minimizing the expected loss . . . . . . . . . . . . . . . . 41
1.5.3 The reject option . . . . . . . . . . . . . . . . . . . . . . . 42
1.5.4 Inference and decision . . . . . . . . . . . . . . . . . . . . 42
1.5.5 Loss functions for regression . . . . . . . . . . . . . . . . . 46
1.6 Information Theory . . . . . . . . . . . . . . . . . . . . . . . . . . 48
1.6.1 Relative entropy and mutual information . . . . . . . . . . 55
Exercises . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . 58
xiii
xiv CONTENTS
2 Probability Distributions 67
2.1 Binary Variables . . . . . . . . . . . . . . . . . . . . . . . . . . . 68
2.1.1 The beta distribution . . . . . . . . . . . . . . . . . . . . . 71
2.2 Multinomial Variables . . . . . . . . . . . . . . . . . . . . . . . . 74
2.2.1 The Dirichlet distribution . . . . . . . . . . . . . . . . . . . 76
2.3 The Gaussian Distribution . . . . . . . . . . . . . . . . . . . . . . 78
2.3.1 Conditional Gaussian distributions . . . . . . . . . . . . . . 85
2.3.2 Marginal Gaussian distributions . . . . . . . . . . . . . . . 88
2.3.3 Bayes’ theorem for Gaussian variables . . . . . . . . . . . . 90
2.3.4 Maximum likelihood for the Gaussian . . . . . . . . . . . . 93
2.3.5 Sequential estimation . . . . . . . . . . . . . . . . . . . . . 94
2.3.6 Bayesian inference for the Gaussian . . . . . . . . . . . . . 97
2.3.7 Student’s t-distribution . . . . . . . . . . . . . . . . . . . . 102
2.3.8 Periodic variables . . . . . . . . . . . . . . . . . . . . . . . 105
2.3.9 Mixtures of Gaussians . . . . . . . . . . . . . . . . . . . . 110
2.4 The Exponential Family . . . . . . . . . . . . . . . . . . . . . . . 113
2.4.1 Maximum likelihood and sufficient statistics . . . . . . . . 116
2.4.2 Conjugate priors . . . . . . . . . . . . . . . . . . . . . . . 117
2.4.3 Noninformative priors . . . . . . . . . . . . . . . . . . . . 117
2.5 Nonparametric Methods . . . . . . . . . . . . . . . . . . . . . . . 120
2.5.1 Kernel density estimators . . . . . . . . . . . . . . . . . . . 122
2.5.2 Nearest-neighbour methods . . . . . . . . . . . . . . . . . 124
Exercises . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . 127
3 Linear Models for Regression 137
3.1 Linear Basis Function Models . . . . . . . . . . . . . . . . . . . . 138
3.1.1 Maximum likelihood and least squares . . . . . . . . . . . . 140
3.1.2 Geometry of least squares . . . . . . . . . . . . . . . . . . 143
3.1.3 Sequential learning . . . . . . . . . . . . . . . . . . . . . . 143
3.1.4 Regularized least squares . . . . . . . . . . . . . . . . . . . 144
3.1.5 Multiple outputs . . . . . . . . . . . . . . . . . . . . . . . 146
3.2 The Bias-Variance Decomposition . . . . . . . . . . . . . . . . . . 147
3.3 Bayesian Linear Regression . . . . . . . . . . . . . . . . . . . . . 152
3.3.1 Parameter distribution . . . . . . . . . . . . . . . . . . . . 152
3.3.2 Predictive distribution . . . . . . . . . . . . . . . . . . . . 156
3.3.3 Equivalent kernel . . . . . . . . . . . . . . . . . . . . . . . 159
3.4 Bayesian Model Comparison . . . . . . . . . . . . . . . . . . . . . 161
3.5 The Evidence Approximation . . . . . . . . . . . . . . . . . . . . 165
3.5.1 Evaluation of the evidence function . . . . . . . . . . . . . 166
3.5.2 Maximizing the evidence function . . . . . . . . . . . . . . 168
3.5.3 Effective number of parameters . . . . . . . . . . . . . . . 170
3.6 Limitations of Fixed Basis Functions . . . . . . . . . . . . . . . . 172
Exercises . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . 173
CONTENTS xv
4 Linear Models for Classification 179
4.1 Discriminant Functions . . . . . . . . . . . . . . . . . . . . . . . . 181
4.1.1 Two classes . . . . . . . . . . . . . . . . . . . . . . . . . . 181
4.1.2 Multiple classes . . . . . . . . . . . . . . . . . . . . . . . . 182
4.1.3 Least squares for classification . . . . . . . . . . . . . . . . 184
4.1.4 Fisher’s linear discriminant . . . . . . . . . . . . . . . . . . 186
4.1.5 Relation to least squares . . . . . . . . . . . . . . . . . . . 189
4.1.6 Fisher’s discriminant for multiple classes . . . . . . . . . . 191
4.1.7 The perceptron algorithm . . . . . . . . . . . . . . . . . . . 192
4.2 Probabilistic Generative Models . . . . . . . . . . . . . . . . . . . 196
4.2.1 Continuous inputs . . . . . . . . . . . . . . . . . . . . . . 198
4.2.2 Maximum likelihood solution . . . . . . . . . . . . . . . . 200
4.2.3 Discrete features . . . . . . . . . . . . . . . . . . . . . . . 202
4.2.4 Exponential family . . . . . . . . . . . . . . . . . . . . . . 202
4.3 Probabilistic Discriminative Models . . . . . . . . . . . . . . . . . 203
4.3.1 Fixed basis functions . . . . . . . . . . . . . . . . . . . . . 204
4.3.2 Logistic regression . . . . . . . . . . . . . . . . . . . . . . 205
4.3.3 Iterative reweighted least squares . . . . . . . . . . . . . . 207
4.3.4 Multiclass logistic regression . . . . . . . . . . . . . . . . . 209
4.3.5 Probit regression . . . . . . . . . . . . . . . . . . . . . . . 210
4.3.6 Canonical link functions . . . . . . . . . . . . . . . . . . . 212
4.4 The Laplace Approximation . . . . . . . . . . . . . . . . . . . . . 213
4.4.1 Model comparison and BIC . . . . . . . . . . . . . . . . . 216
4.5 Bayesian Logistic Regression . . . . . . . . . . . . . . . . . . . . 217
4.5.1 Laplace approximation . . . . . . . . . . . . . . . . . . . . 217
4.5.2 Predictive distribution . . . . . . . . . . . . . . . . . . . . 218
Exercises . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . 220
5 Neural Networks 225
5.1 Feed-forward Network Functions . . . . . . . . . . . . . . . . . . 227
5.1.1 Weight-space symmetries . . . . . . . . . . . . . . . . . . 231
5.2 Network Training . . . . . . . . . . . . . . . . . . . . . . . . . . . 232
5.2.1 Parameter optimization . . . . . . . . . . . . . . . . . . . . 236
5.2.2 Local quadratic approximation . . . . . . . . . . . . . . . . 237
5.2.3 Use of gradient information . . . . . . . . . . . . . . . . . 239
5.2.4 Gradient descent optimization . . . . . . . . . . . . . . . . 240
5.3 Error Backpropagation . . . . . . . . . . . . . . . . . . . . . . . . 241
5.3.1 Evaluation of error-function derivatives . . . . . . . . . . . 242
5.3.2 A simple example . . . . . . . . . . . . . . . . . . . . . . 245
5.3.3 Efficiency of backpropagation . . . . . . . . . . . . . . . . 246
5.3.4 The Jacobian matrix . . . . . . . . . . . . . . . . . . . . . 247
5.4 The Hessian Matrix . . . . . . . . . . . . . . . . . . . . . . . . . . 249
5.4.1 Diagonal approximation . . . . . . . . . . . . . . . . . . . 250
5.4.2 Outer product approximation . . . . . . . . . . . . . . . . . 251
5.4.3 Inverse Hessian . . . . . . . . . . . . . . . . . . . . . . . . 252
xvi CONTENTS
5.4.4 Finite differences . . . . . . . . . . . . . . . . . . . . . . . 252
5.4.5 Exact evaluation of the Hessian . . . . . . . . . . . . . . . 253
5.4.6 Fast multiplication by the Hessian . . . . . . . . . . . . . . 254
5.5 Regularization in Neural Networks . . . . . . . . . . . . . . . . . 256
5.5.1 Consistent Gaussian priors . . . . . . . . . . . . . . . . . . 257
5.5.2 Early stopping . . . . . . . . . . . . . . . . . . . . . . . . 259
5.5.3 Invariances . . . . . . . . . . . . . . . . . . . . . . . . . . 261
5.5.4 Tangent propagation . . . . . . . . . . . . . . . . . . . . . 263
5.5.5 Training with transformed data . . . . . . . . . . . . . . . . 265
5.5.6 Convolutional networks . . . . . . . . . . . . . . . . . . . 267
5.5.7 Soft weight sharing . . . . . . . . . . . . . . . . . . . . . . 269
5.6 Mixture Density Networks . . . . . . . . . . . . . . . . . . . . . . 272
5.7 Bayesian Neural Networks . . . . . . . . . . . . . . . . . . . . . . 277
5.7.1 Posterior parameter distribution . . . . . . . . . . . . . . . 278
5.7.2 Hyperparameter optimization . . . . . . . . . . . . . . . . 280
5.7.3 Bayesian neural networks for classification . . . . . . . . . 281
Exercises . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . 284
6 Kernel Methods 291
6.1 Dual Representations . . . . . . . . . . . . . . . . . . . . . . . . . 293
6.2 Constructing Kernels . . . . . . . . . . . . . . . . . . . . . . . . . 294
6.3 Radial Basis Function Networks . . . . . . . . . . . . . . . . . . . 299
6.3.1 Nadaraya-Watson model . . . . . . . . . . . . . . . . . . . 301
6.4 Gaussian Processes . . . . . . . . . . . . . . . . . . . . . . . . . . 303
6.4.1 Linear regression revisited . . . . . . . . . . . . . . . . . . 304
6.4.2 Gaussian processes for regression . . . . . . . . . . . . . . 306
6.4.3 Learning the hyperparameters . . . . . . . . . . . . . . . . 311
6.4.4 Automatic relevance determination . . . . . . . . . . . . . 312
6.4.5 Gaussian processes for classification . . . . . . . . . . . . . 313
6.4.6 Laplace approximation . . . . . . . . . . . . . . . . . . . . 315
6.4.7 Connection to neural networks . . . . . . . . . . . . . . . . 319
Exercises . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . 320
7 Sparse Kernel Machines 325
7.1 Maximum Margin Classifiers . . . . . . . . . . . . . . . . . . . . 326
7.1.1 Overlapping class distributions . . . . . . . . . . . . . . . . 331
7.1.2 Relation to logistic regression . . . . . . . . . . . . . . . . 336
7.1.3 Multiclass SVMs . . . . . . . . . . . . . . . . . . . . . . . 338
7.1.4 SVMs for regression . . . . . . . . . . . . . . . . . . . . . 339
7.1.5 Computational learning theory . . . . . . . . . . . . . . . . 344
7.2 Relevance Vector Machines . . . . . . . . . . . . . . . . . . . . . 345
7.2.1 RVM for regression . . . . . . . . . . . . . . . . . . . . . . 345
7.2.2 Analysis of sparsity . . . . . . . . . . . . . . . . . . . . . . 349
7.2.3 RVM for classification . . . . . . . . . . . . . . . . . . . . 353
Exercises . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . 357
CONTENTS xvii
8 Graphical Models 359
8.1 Bayesian Networks . . . . . . . . . . . . . . . . . . . . . . . . . . 360
8.1.1 Example: Polynomial regression . . . . . . . . . . . . . . . 362
8.1.2 Generative models . . . . . . . . . . . . . . . . . . . . . . 365
8.1.3 Discrete variables . . . . . . . . . . . . . . . . . . . . . . . 366
8.1.4 Linear-Gaussian models . . . . . . . . . . . . . . . . . . . 370
8.2 Conditional Independence . . . . . . . . . . . . . . . . . . . . . . 372
8.2.1 Three example graphs . . . . . . . . . . . . . . . . . . . . 373
8.2.2 D-separation . . . . . . . . . . . . . . . . . . . . . . . . . 378
8.3 Markov Random Fields . . . . . . . . . . . . . . . . . . . . . . . 383
8.3.1 Conditional independence properties . . . . . . . . . . . . . 383
8.3.2 Factorization properties . . . . . . . . . . . . . . . . . . . 384
8.3.3 Illustration: Image de-noising . . . . . . . . . . . . . . . . 387
8.3.4 Relation to directed graphs . . . . . . . . . . . . . . . . . . 390
8.4 Inference in Graphical Models . . . . . . . . . . . . . . . . . . . . 393
8.4.1 Inference on a chain . . . . . . . . . . . . . . . . . . . . . 394
8.4.2 Trees . . . . . . . . . . . . . . . . . . . . . . . . . . . . . 398
8.4.3 Factor graphs . . . . . . . . . . . . . . . . . . . . . . . . . 399
8.4.4 The sum-product algorithm . . . . . . . . . . . . . . . . . . 402
8.4.5 The max-sum algorithm . . . . . . . . . . . . . . . . . . . 411
8.4.6 Exact inference in general graphs . . . . . . . . . . . . . . 416
8.4.7 Loopy belief propagation . . . . . . . . . . . . . . . . . . . 417
8.4.8 Learning the graph structure . . . . . . . . . . . . . . . . . 418
Exercises . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . 418
9 Mixture Models and EM 423
9.1 K-means Clustering . . . . . . . . . . . . . . . . . . . . . . . . . 424
9.1.1 Image segmentation and compression . . . . . . . . . . . . 428
9.2 Mixtures of Gaussians . . . . . . . . . . . . . . . . . . . . . . . . 430
9.2.1 Maximum likelihood . . . . . . . . . . . . . . . . . . . . . 432
9.2.2 EM for Gaussian mixtures . . . . . . . . . . . . . . . . . . 435
9.3 An Alternative View of EM . . . . . . . . . . . . . . . . . . . . . 439
9.3.1 Gaussian mixtures revisited . . . . . . . . . . . . . . . . . 441
9.3.2 Relation to K-means . . . . . . . . . . . . . . . . . . . . . 443
9.3.3 Mixtures of Bernoulli distributions . . . . . . . . . . . . . . 444
9.3.4 EM for Bayesian linear regression . . . . . . . . . . . . . . 448
9.4 The EM Algorithm in General . . . . . . . . . . . . . . . . . . . . 450
Exercises . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . 455
10 Approximate Inference 461
10.1 Variational Inference . . . . . . . . . . . . . . . . . . . . . . . . . 462
10.1.1 Factorized distributions . . . . . . . . . . . . . . . . . . . . 464
10.1.2 Properties of factorized approximations . . . . . . . . . . . 466
10.1.3 Example: The univariate Gaussian . . . . . . . . . . . . . . 470
10.1.4 Model comparison . . . . . . . . . . . . . . . . . . . . . . 473
10.2 Illustration: Variational Mixture of Gaussians . . . . . . . . . . . . 474
xviii CONTENTS
10.2.1 Variational distribution . . . . . . . . . . . . . . . . . . . . 475
10.2.2 Variational lower bound . . . . . . . . . . . . . . . . . . . 481
10.2.3 Predictive density . . . . . . . . . . . . . . . . . . . . . . . 482
10.2.4 Determining the number of components . . . . . . . . . . . 483
10.2.5 Induced factorizations . . . . . . . . . . . . . . . . . . . . 485
10.3 Variational Linear Regression . . . . . . . . . . . . . . . . . . . . 486
10.3.1 Variational distribution . . . . . . . . . . . . . . . . . . . . 486
10.3.2 Predictive distribution . . . . . . . . . . . . . . . . . . . . 488
10.3.3 Lower bound . . . . . . . . . . . . . . . . . . . . . . . . . 489
10.4 Exponential Family Distributions . . . . . . . . . . . . . . . . . . 490
10.4.1 Variational message passing . . . . . . . . . . . . . . . . . 491
10.5 Local Variational Methods . . . . . . . . . . . . . . . . . . . . . . 493
10.6 Variational Logistic Regression . . . . . . . . . . . . . . . . . . . 498
10.6.1 Variational posterior distribution . . . . . . . . . . . . . . . 498
10.6.2 Optimizing the variational parameters . . . . . . . . . . . . 500
10.6.3 Inference of hyperparameters . . . . . . . . . . . . . . . . 502
10.7 Expectation Propagation . . . . . . . . . . . . . . . . . . . . . . . 505
10.7.1 Example: The clutter problem . . . . . . . . . . . . . . . . 511
10.7.2 Expectation propagation on graphs . . . . . . . . . . . . . . 513
Exercises . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . 517
11 Sampling Methods 523
11.1 Basic Sampling Algorithms . . . . . . . . . . . . . . . . . . . . . 526
11.1.1 Standard distributions . . . . . . . . . . . . . . . . . . . . 526
11.1.2 Rejection sampling . . . . . . . . . . . . . . . . . . . . . . 528
11.1.3 Adaptive rejection sampling . . . . . . . . . . . . . . . . . 530
11.1.4 Importance sampling . . . . . . . . . . . . . . . . . . . . . 532
11.1.5 Sampling-importance-resampling . . . . . . . . . . . . . . 534
11.1.6 Sampling and the EM algorithm . . . . . . . . . . . . . . . 536
11.2 Markov Chain Monte Carlo . . . . . . . . . . . . . . . . . . . . . 537
11.2.1 Markov chains . . . . . . . . . . . . . . . . . . . . . . . . 539
11.2.2 The Metropolis-Hastings algorithm . . . . . . . . . . . . . 541
11.3 Gibbs Sampling . . . . . . . . . . . . . . . . . . . . . . . . . . . 542
11.4 Slice Sampling . . . . . . . . . . . . . . . . . . . . . . . . . . . . 546
11.5 The Hybrid Monte Carlo Algorithm . . . . . . . . . . . . . . . . . 548
11.5.1 Dynamical systems . . . . . . . . . . . . . . . . . . . . . . 548
11.5.2 Hybrid Monte Carlo . . . . . . . . . . . . . . . . . . . . . 552
11.6 Estimating the Partition Function . . . . . . . . . . . . . . . . . . 554
Exercises . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . 556
12 Continuous Latent Variables 559
12.1 Principal Component Analysis . . . . . . . . . . . . . . . . . . . . 561
12.1.1 Maximum variance formulation . . . . . . . . . . . . . . . 561
12.1.2 Minimum-error formulation . . . . . . . . . . . . . . . . . 563
12.1.3 Applications of PCA . . . . . . . . . . . . . . . . . . . . . 565
12.1.4 PCA for high-dimensional data . . . . . . . . . . . . . . . 569
CONTENTS xix
12.2 Probabilistic PCA . . . . . . . . . . . . . . . . . . . . . . . . . . 570
12.2.1 Maximum likelihood PCA . . . . . . . . . . . . . . . . . . 574
12.2.2 EM algorithm for PCA . . . . . . . . . . . . . . . . . . . . 577
12.2.3 Bayesian PCA . . . . . . . . . . . . . . . . . . . . . . . . 580
12.2.4 Factor analysis . . . . . . . . . . . . . . . . . . . . . . . . 583
12.3 Kernel PCA . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . 586
12.4 Nonlinear Latent Variable Models . . . . . . . . . . . . . . . . . . 591
12.4.1 Independent component analysis . . . . . . . . . . . . . . . 591
12.4.2 Autoassociative neural networks . . . . . . . . . . . . . . . 592
12.4.3 Modelling nonlinear manifolds . . . . . . . . . . . . . . . . 595
Exercises . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . 599
13 Sequential Data 605
13.1 Markov Models . . . . . . . . . . . . . . . . . . . . . . . . . . . . 607
13.2 Hidden Markov Models . . . . . . . . . . . . . . . . . . . . . . . 610
13.2.1 Maximum likelihood for the HMM . . . . . . . . . . . . . 615
13.2.2 The forward-backward algorithm . . . . . . . . . . . . . . 618
13.2.3 The sum-product algorithm for the HMM . . . . . . . . . . 625
13.2.4 Scaling factors . . . . . . . . . . . . . . . . . . . . . . . . 627
13.2.5 The Viterbi algorithm . . . . . . . . . . . . . . . . . . . . . 629
13.2.6 Extensions of the hidden Markov model . . . . . . . . . . . 631
13.3 Linear Dynamical Systems . . . . . . . . . . . . . . . . . . . . . . 635
13.3.1 Inference in LDS . . . . . . . . . . . . . . . . . . . . . . . 638
13.3.2 Learning in LDS . . . . . . . . . . . . . . . . . . . . . . . 642
13.3.3 Extensions of LDS . . . . . . . . . . . . . . . . . . . . . . 644
13.3.4 Particle filters . . . . . . . . . . . . . . . . . . . . . . . . . 645
Exercises . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . 646
14 Combining Models 653
14.1 Bayesian Model Averaging . . . . . . . . . . . . . . . . . . . . . . 654
14.2 Committees . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . 655
14.3 Boosting . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . 657
14.3.1 Minimizing exponential error . . . . . . . . . . . . . . . . 659
14.3.2 Error functions for boosting . . . . . . . . . . . . . . . . . 661
14.4 Tree-based Models . . . . . . . . . . . . . . . . . . . . . . . . . . 663
14.5 Conditional Mixture Models . . . . . . . . . . . . . . . . . . . . . 666
14.5.1 Mixtures of linear regression models . . . . . . . . . . . . . 667
14.5.2 Mixtures of logistic models . . . . . . . . . . . . . . . . . 670
14.5.3 Mixtures of experts . . . . . . . . . . . . . . . . . . . . . . 672
Exercises . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . 674
Appendix A Data Sets 677
Appendix B Probability Distributions 685
Appendix C Properties of Matrices 695
xx CONTENTS
Appendix D Calculus of Variations 703
Appendix E Lagrange Multipliers 707
References 711
Index 729
1
Introduction
The problem of searching for patterns in data is a fundamental one and has a long and
successful history. For instance, the extensive astronomical observations of Tycho
Brahe in the 16th century allowed Johannes Kepler to discover the empirical laws of
planetary motion, which in turn provided a springboard for the development of classical mechanics. Similarly, the discovery of regularities in atomic spectra played a
key role in the development and verification of quantum physics in the early twentieth century. The field of pattern recognition is concerned with the automatic discovery of regularities in data through the use of computer algorithms and with the use of
these regularities to take actions such as classifying the data into different categories.
Consider the example of recognizing handwritten digits, illustrated in Figure 1.1.
Each digit corresponds to a 28×28 pixel image and so can be represented by a vector
x comprising 784 real numbers. The goal is to build a machine that will take such a
vector x as input and that will produce the identity of the digit 0,..., 9 as the output.
This is a nontrivial problem due to the wide variability of handwriting. It could be
1
2 1. INTRODUCTION
Figure 1.1 Examples of hand-written digits taken from US zip codes.
tackled using handcrafted rules or heuristics for distinguishing the digits based on
the shapes of the strokes, but in practice such an approach leads to a proliferation of
rules and of exceptions to the rules and so on, and invariably gives poor results.
Far better results can be obtained by adopting a machine learning approach in
which a large set of N digits {x1,..., xN } called a training set is used to tune the
parameters of an adaptive model. The categories of the digits in the training set
are known in advance, typically by inspecting them individually and hand-labelling
them. We can express the category of a digit using target vector t, which represents
the identity of the corresponding digit. Suitable techniques for representing categories in terms of vectors will be discussed later. Note that there is one such target
vector t for each digit image x.
The result of running the machine learning algorithm can be expressed as a
function y(x) which takes a new digit image x as input and that generates an output
vector y, encoded in the same way as the target vectors. The precise form of the
function y(x) is determined during the training phase, also known as the learning
phase, on the basis of the training data. Once the model is trained it can then determine the identity of new digit images, which are said to comprise a test set. The
ability to categorize correctly new examples that differ from those used for training is known as generalization. In practical applications, the variability of the input
vectors will be such that the training data can comprise only a tiny fraction of all
possible input vectors, and so generalization is a central goal in pattern recognition.
For most practical applications, the original input variables are typically preprocessed to transform them into some new space of variables where, it is hoped, the
pattern recognition problem will be easier to solve. For instance, in the digit recognition problem, the images of the digits are typically translated and scaled so that each
digit is contained within a box of a fixed size. This greatly reduces the variability
within each digit class, because the location and scale of all the digits are now the
same, which makes it much easier for a subsequent pattern recognition algorithm
to distinguish between the different classes. This pre-processing stage is sometimes
also called feature extraction. Note that new test data must be pre-processed using
the same steps as the training data.
Pre-processing might also be performed in order to speed up computation. For
example, if the goal is real-time face detection in a high-resolution video stream,
the computer must handle huge numbers of pixels per second, and presenting these
directly to a complex pattern recognition algorithm may be computationally infeasible. Instead, the aim is to find useful features that are fast to compute, and yet that
1. INTRODUCTION 3
also preserve useful discriminatory information enabling faces to be distinguished
from non-faces. These features are then used as the inputs to the pattern recognition
algorithm. For instance, the average value of the image intensity over a rectangular
subregion can be evaluated extremely efficiently (Viola and Jones, 2004), and a set of
such features can prove very effective in fast face detection. Because the number of
such features is smaller than the number of pixels, this kind of pre-processing represents a form of dimensionality reduction. Care must be taken during pre-processing
because often information is discarded, and if this information is important to the
solution of the problem then the overall accuracy of the system can suffer.
Applications in which the training data comprises examples of the input vectors
along with their corresponding target vectors are known as supervised learning problems. Cases such as the digit recognition example, in which the aim is to assign each
input vector to one of a finite number of discrete categories, are called classification
problems. If the desired output consists of one or more continuous variables, then
the task is called regression. An example of a regression problem would be the prediction of the yield in a chemical manufacturing process in which the inputs consist
of the concentrations of reactants, the temperature, and the pressure.
In other pattern recognition problems, the training data consists of a set of input
vectors x without any corresponding target values. The goal in such unsupervised
learning problems may be to discover groups of similar examples within the data,
where it is called clustering, or to determine the distribution of data within the input
space, known as density estimation, or to project the data from a high-dimensional
space down to two or three dimensions for the purpose of visualization.
Finally, the technique of reinforcement learning (Sutton and Barto, 1998) is concerned with the problem of finding suitable actions to take in a given situation in
order to maximize a reward. Here the learning algorithm is not given examples of
optimal outputs, in contrast to supervised learning, but must instead discover them
by a process of trial and error. Typically there is a sequence of states and actions in
which the learning algorithm is interacting with its environment. In many cases, the
current action not only affects the immediate reward but also has an impact on the reward at all subsequent time steps. For example, by using appropriate reinforcement
learning techniques a neural network can learn to play the game of backgammon to a
high standard (Tesauro, 1994). Here the network must learn to take a board position
as input, along with the result of a dice throw, and produce a strong move as the
output. This is done by having the network play against a copy of itself for perhaps a
million games. A major challenge is that a game of backgammon can involve dozens
of moves, and yet it is only at the end of the game that the reward, in the form of
victory, is achieved. The reward must then be attributed appropriately to all of the
moves that led to it, even though some moves will have been good ones and others
less so. This is an example of a credit assignment problem. A general feature of reinforcement learning is the trade-off between exploration, in which the system tries
out new kinds of actions to see how effective they are, and exploitation, in which
the system makes use of actions that are known to yield a high reward. Too strong
a focus on either exploration or exploitation will yield poor results. Reinforcement
learning continues to be an active area of machine learning research. However, a
4 1. INTRODUCTION
Figure 1.2 Plot of a training data set of N =
10 points, shown as blue circles,
each comprising an observation
of the input variable x along with
the corresponding target variable
t. The green curve shows the
function sin(2πx) used to generate the data. Our goal is to predict the value of t for some new
value of x, without knowledge of
the green curve.
x
t
0 1
−1
0
1
detailed treatment lies beyond the scope of this book.
Although each of these tasks needs its own tools and techniques, many of the
key ideas that underpin them are common to all such problems. One of the main
goals of this chapter is to introduce, in a relatively informal way, several of the most
important of these concepts and to illustrate them using simple examples. Later in
the book we shall see these same ideas re-emerge in the context of more sophisticated models that are applicable to real-world pattern recognition applications. This
chapter also provides a self-contained introduction to three important tools that will
be used throughout the book, namely probability theory, decision theory, and information theory. Although these might sound like daunting topics, they are in fact
straightforward, and a clear understanding of them is essential if machine learning
techniques are to be used to best effect in practical applications.
1.1. Example: Polynomial Curve Fitting
We begin by introducing a simple regression problem, which we shall use as a running example throughout this chapter to motivate a number of key concepts. Suppose we observe a real-valued input variable x and we wish to use this observation to
predict the value of a real-valued target variable t. For the present purposes, it is instructive to consider an artificial example using synthetically generated data because
we then know the precise process that generated the data for comparison against any
learned model. The data for this example is generated from the function sin(2πx)
with random noise included in the target values, as described in detail in Appendix A.
Now suppose that we are given a training set comprising N observations of x,
written x ≡ (x1,...,xN )T, together with corresponding observations of the values
of t, denoted t ≡ (t1,...,tN )T. Figure 1.2 shows a plot of a training set comprising
N = 10 data points. The input data set x in Figure 1.2 was generated by choosing values of xn, for n = 1,...,N, spaced uniformly in range [0, 1], and the target
data set t was obtained by first computing the corresponding values of the function
1.1. Example: Polynomial Curve Fitting 5
sin(2πx) and then adding a small level of random noise having a Gaussian distribution (the Gaussian distribution is discussed in Section 1.2.4) to each such point in
order to obtain the corresponding value tn. By generating data in this way, we are
capturing a property of many real data sets, namely that they possess an underlying
regularity, which we wish to learn, but that individual observations are corrupted by
random noise. This noise might arise from intrinsically stochastic (i.e. random) processes such as radioactive decay but more typically is due to there being sources of
variability that are themselves unobserved.
Our goal is to exploit this training set in order to make predictions of the value t of the target variable for some new value x of the input variable. As we shall see
later, this involves implicitly trying to discover the underlying function sin(2πx).
This is intrinsically a difficult problem as we have to generalize from a finite data
set. Furthermore the observed data are corrupted with noise, and so for a given x
there is uncertainty as to the appropriate value for t. Probability theory, discussed
in Section 1.2, provides a framework for expressing such uncertainty in a precise
and quantitative manner, and decision theory, discussed in Section 1.5, allows us to
exploit this probabilistic representation in order to make predictions that are optimal
according to appropriate criteria.
For the moment, however, we shall proceed rather informally and consider a
simple approach based on curve fitting. In particular, we shall fit the data using a
polynomial function of the form
y(x, w) = w0 + w1x + w2x2 + ... + wMxM =
M
j=0
wjxj (1.1)
where M is the order of the polynomial, and xj denotes x raised to the power of j.
The polynomial coefficients w0,...,wM are collectively denoted by the vector w.
Note that, although the polynomial function y(x, w) is a nonlinear function of x, it
is a linear function of the coefficients w. Functions, such as the polynomial, which
are linear in the unknown parameters have important properties and are called linear
models and will be discussed extensively in Chapters 3 and 4.
The values of the coefficients will be determined by fitting the polynomial to the
training data. This can be done by minimizing an error function that measures the
misfit between the function y(x, w), for any given value of w, and the training set
data points. One simple choice of error function, which is widely used, is given by
the sum of the squares of the errors between the predictions y(xn, w) for each data
point xn and the corresponding target values tn, so that we minimize
E(w) = 1
2

N
n=1
{y(xn, w) − tn}
2 (1.2)
where the factor of 1/2 is included for later convenience. We shall discuss the motivation for this choice of error function later in this chapter. For the moment we
simply note that it is a nonnegative quantity that would be zero if, and only if, the
6 1. INTRODUCTION
Figure 1.3 The error function (1.2) corresponds to (one half of) the sum of
the squares of the displacements
(shown by the vertical green bars)
of each data point from the function
y(x, w).
t
x
y(xn, w)
tn
xn
function y(x, w) were to pass exactly through each training data point. The geometrical interpretation of the sum-of-squares error function is illustrated in Figure 1.3.
We can solve the curve fitting problem by choosing the value of w for which
E(w) is as small as possible. Because the error function is a quadratic function of
the coefficients w, its derivatives with respect to the coefficients will be linear in the
elements of w, and so the minimization of the error function has a unique solution,
denoted by w Exercise 1.1 , which can be found in closed form. The resulting polynomial is
given by the function y(x, w).
There remains the problem of choosing the order M of the polynomial, and as
we shall see this will turn out to be an example of an important concept called model
comparison or model selection. In Figure 1.4, we show four examples of the results
of fitting polynomials having orders M = 0, 1, 3, and 9 to the data set shown in
Figure 1.2.
We notice that the constant (M = 0) and first order (M = 1) polynomials
give rather poor fits to the data and consequently rather poor representations of the
function sin(2πx). The third order (M = 3) polynomial seems to give the best fit
to the function sin(2πx) of the examples shown in Figure 1.4. When we go to a
much higher order polynomial (M = 9), we obtain an excellent fit to the training
data. In fact, the polynomial passes exactly through each data point and E(w)=0.
However, the fitted curve oscillates wildly and gives a very poor representation of
the function sin(2πx). This latter behaviour is known as over-fitting.
As we have noted earlier, the goal is to achieve good generalization by making
accurate predictions for new data. We can obtain some quantitative insight into the
dependence of the generalization performance on M by considering a separate test
set comprising 100 data points generated using exactly the same procedure used
to generate the training set points but with new choices for the random noise values
included in the target values. For each choice of M, we can then evaluate the residual
value of E(w) given by (1.2) for the training data, and we can also evaluate E(w)
for the test data set. It is sometimes more convenient to use the root-mean-square
1.1. Example: Polynomial Curve Fitting 7
x
t
M = 0
0 1
−1
0
1
x
t
M = 1
0 1
−1
0
1
x
t
M = 3
0 1
−1
0
1
x
t
M = 9
0 1
−1
0
1
Figure 1.4 Plots of polynomials having various orders M, shown as red curves, fitted to the data set shown in
Figure 1.2.
(RMS) error defined by
ERMS = 2E(w)/N (1.3)
in which the division by N allows us to compare different sizes of data sets on
an equal footing, and the square root ensures that ERMS is measured on the same
scale (and in the same units) as the target variable t. Graphs of the training and
test set RMS errors are shown, for various values of M, in Figure 1.5. The test
set error is a measure of how well we are doing in predicting the values of t for
new data observations of x. We note from Figure 1.5 that small values of M give
relatively large values of the test set error, and this can be attributed to the fact that
the corresponding polynomials are rather inflexible and are incapable of capturing
the oscillations in the function sin(2πx). Values of M in the range 3  M  8
give small values for the test set error, and these also give reasonable representations
of the generating function sin(2πx), as can be seen, for the case of M = 3, from
Figure 1.4.
8 1. INTRODUCTION
Figure 1.5 Graphs of the root-mean-square
error, defined by (1.3), evaluated
on the training set and on an independent test set for various values
of M.
M
ERMS
0 3 6 9
0
0.5
1
Training
Test
For M = 9, the training set error goes to zero, as we might expect because
this polynomial contains 10 degrees of freedom corresponding to the 10 coefficients
w0,...,w9, and so can be tuned exactly to the 10 data points in the training set.
However, the test set error has become very large and, as we saw in Figure 1.4, the
corresponding function y(x, w) exhibits wild oscillations.
This may seem paradoxical because a polynomial of given order contains all
lower order polynomials as special cases. The M = 9 polynomial is therefore capable of generating results at least as good as the M = 3 polynomial. Furthermore, we
might suppose that the best predictor of new data would be the function sin(2πx)
from which the data was generated (and we shall see later that this is indeed the
case). We know that a power series expansion of the function sin(2πx) contains
terms of all orders, so we might expect that results should improve monotonically as
we increase M.
We can gain some insight into the problem by examining the values of the coefficients w obtained from polynomials of various order, as shown in Table 1.1.
We see that, as M increases, the magnitude of the coefficients typically gets larger.
In particular for the M = 9 polynomial, the coefficients have become finely tuned
to the data by developing large positive and negative values so that the correspondTable 1.1 Table of the coefficients w for
polynomials of various order.
Observe how the typical magnitude of the coefficients increases dramatically as the order of the polynomial increases.
M = 0 M = 1 M = 6 M = 9
w
0 0.19 0.82 0.31 0.35
w
1 -1.27 7.99 232.37
w
2 -25.43 -5321.83
w
3 17.37 48568.31
w
4 -231639.30
w
5 640042.26
w
6 -1061800.52
w
7 1042400.18
w
8 -557682.99
w
9 125201.43
1.1. Example: Polynomial Curve Fitting 9
x
t
N = 15
0 1
−1
0
1
x
t
N = 100
0 1
−1
0
1
Figure 1.6 Plots of the solutions obtained by minimizing the sum-of-squares error function using the M = 9
polynomial for N = 15 data points (left plot) and N = 100 data points (right plot). We see that increasing the
size of the data set reduces the over-fitting problem.
ing polynomial function matches each of the data points exactly, but between data
points (particularly near the ends of the range) the function exhibits the large oscillations observed in Figure 1.4. Intuitively, what is happening is that the more flexible
polynomials with larger values of M are becoming increasingly tuned to the random
noise on the target values.
It is also interesting to examine the behaviour of a given model as the size of the
data set is varied, as shown in Figure 1.6. We see that, for a given model complexity,
the over-fitting problem become less severe as the size of the data set increases.
Another way to say this is that the larger the data set, the more complex (in other
words more flexible) the model that we can afford to fit to the data. One rough
heuristic that is sometimes advocated is that the number of data points should be
no less than some multiple (say 5 or 10) of the number of adaptive parameters in
the model. However, as we shall see in Chapter 3, the number of parameters is not
necessarily the most appropriate measure of model complexity.
Also, there is something rather unsatisfying about having to limit the number of
parameters in a model according to the size of the available training set. It would
seem more reasonable to choose the complexity of the model according to the complexity of the problem being solved. We shall see that the least squares approach
to finding the model parameters represents a specific case of maximum likelihood
(discussed in Section 1.2.5), and that the over-fitting problem can be understood as
Section 3.4 a general property of maximum likelihood. By adopting a Bayesian approach, the
over-fitting problem can be avoided. We shall see that there is no difficulty from
a Bayesian perspective in employing models for which the number of parameters
greatly exceeds the number of data points. Indeed, in a Bayesian model the effective
number of parameters adapts automatically to the size of the data set.
For the moment, however, it is instructive to continue with the current approach
and to consider how in practice we can apply it to data sets of limited size where we
10 1. INTRODUCTION
x
t
ln λ = −18
0 1
−1
0
1
x
t
ln λ = 0
0 1
−1
0
1
Figure 1.7 Plots of M = 9 polynomials fitted to the data set shown in Figure 1.2 using the regularized error
function (1.4) for two values of the regularization parameter λ corresponding to ln λ = −18 and ln λ = 0. The
case of no regularizer, i.e., λ = 0, corresponding to ln λ = −∞, is shown at the bottom right of Figure 1.4.
may wish to use relatively complex and flexible models. One technique that is often
used to control the over-fitting phenomenon in such cases is that of regularization,
which involves adding a penalty term to the error function (1.2) in order to discourage
the coefficients from reaching large values. The simplest such penalty term takes the
form of a sum of squares of all of the coefficients, leading to a modified error function
of the form
E(w) = 1
2

N
n=1
{y(xn, w) − tn}
2 +
λ
2
w2 (1.4)
where w2 ≡ wTw = w2
0 + w2
1 + ... + w2
M, and the coefficient λ governs the relative importance of the regularization term compared with the sum-of-squares error
term. Note that often the coefficient w0 is omitted from the regularizer because its
inclusion causes the results to depend on the choice of origin for the target variable
(Hastie et al., 2001), or it may be included but with its own regularization coefficient
(we shall discuss this topic in more detail in Section 5.5.1). Again, the error function
Exercise 1.2 in (1.4) can be minimized exactly in closed form. Techniques such as this are known
in the statistics literature as shrinkage methods because they reduce the value of the
coefficients. The particular case of a quadratic regularizer is called ridge regression (Hoerl and Kennard, 1970). In the context of neural networks, this approach is
known as weight decay.
Figure 1.7 shows the results of fitting the polynomial of order M = 9 to the
same data set as before but now using the regularized error function given by (1.4).
We see that, for a value of ln λ = −18, the over-fitting has been suppressed and we
now obtain a much closer representation of the underlying function sin(2πx). If,
however, we use too large a value for λ then we again obtain a poor fit, as shown in
Figure 1.7 for ln λ = 0. The corresponding coefficients from the fitted polynomials
are given in Table 1.2, showing that regularization has the desired effect of reducing
1.1. Example: Polynomial Curve Fitting 11
Table 1.2 Table of the coefficients w for M =
9 polynomials with various values for
the regularization parameter λ. Note
that ln λ = −∞ corresponds to a
model with no regularization, i.e., to
the graph at the bottom right in Figure 1.4. We see that, as the value of
λ increases, the typical magnitude of
the coefficients gets smaller.
ln λ = −∞ ln λ = −18 ln λ = 0
w
0 0.35 0.35 0.13
w
1 232.37 4.74 -0.05
w
2 -5321.83 -0.77 -0.06
w
3 48568.31 -31.97 -0.05
w
4 -231639.30 -3.89 -0.03
w
5 640042.26 55.28 -0.02
w
6 -1061800.52 41.32 -0.01
w
7 1042400.18 -45.95 -0.00
w
8 -557682.99 -91.53 0.00
w
9 125201.43 72.68 0.01
the magnitude of the coefficients.
The impact of the regularization term on the generalization error can be seen by
plotting the value of the RMS error (1.3) for both training and test sets against ln λ,
as shown in Figure 1.8. We see that in effect λ now controls the effective complexity
of the model and hence determines the degree of over-fitting.
The issue of model complexity is an important one and will be discussed at
length in Section 1.3. Here we simply note that, if we were trying to solve a practical
application using this approach of minimizing an error function, we would have to
find a way to determine a suitable value for the model complexity. The results above
suggest a simple way of achieving this, namely by taking the available data and
partitioning it into a training set, used to determine the coefficients w, and a separate
validation set, also called a hold-out set, used to optimize the model complexity
(either M or λ). In many cases, however, this will prove to be too wasteful of
Section 1.3 valuable training data, and we have to seek more sophisticated approaches.
So far our discussion of polynomial curve fitting has appealed largely to intuition. We now seek a more principled approach to solving problems in pattern
recognition by turning to a discussion of probability theory. As well as providing the
foundation for nearly all of the subsequent developments in this book, it will also
Figure 1.8 Graph of the root-mean-square error (1.3) versus ln λ for the M = 9
polynomial.
ERMS
ln λ −35 −30 −25 −20
0
0.5
1
Training
Test
12 1. INTRODUCTION
give us some important insights into the concepts we have introduced in the context of polynomial curve fitting and will allow us to extend these to more complex
situations.
1.2. Probability Theory
A key concept in the field of pattern recognition is that of uncertainty. It arises both
through noise on measurements, as well as through the finite size of data sets. Probability theory provides a consistent framework for the quantification and manipulation of uncertainty and forms one of the central foundations for pattern recognition.
When combined with decision theory, discussed in Section 1.5, it allows us to make
optimal predictions given all the information available to us, even though that information may be incomplete or ambiguous.
We will introduce the basic concepts of probability theory by considering a simple example. Imagine we have two boxes, one red and one blue, and in the red box
we have 2 apples and 6 oranges, and in the blue box we have 3 apples and 1 orange.
This is illustrated in Figure 1.9. Now suppose we randomly pick one of the boxes
and from that box we randomly select an item of fruit, and having observed which
sort of fruit it is we replace it in the box from which it came. We could imagine
repeating this process many times. Let us suppose that in so doing we pick the red
box 40% of the time and we pick the blue box 60% of the time, and that when we
remove an item of fruit from a box we are equally likely to select any of the pieces
of fruit in the box.
In this example, the identity of the box that will be chosen is a random variable,
which we shall denote by B. This random variable can take one of two possible
values, namely r (corresponding to the red box) or b (corresponding to the blue
box). Similarly, the identity of the fruit is also a random variable and will be denoted
by F. It can take either of the values a (for apple) or o (for orange).
To begin with, we shall define the probability of an event to be the fraction
of times that event occurs out of the total number of trials, in the limit that the total
number of trials goes to infinity. Thus the probability of selecting the red box is 4/10
Figure 1.9 We use a simple example of two
coloured boxes each containing fruit
(apples shown in green and oranges shown in orange) to introduce the basic ideas of probability.
1.2. Probability Theory 13
Figure 1.10 We can derive the sum and product rules of probability by
considering two random variables, X, which takes the values {xi} where
i = 1,...,M, and Y , which takes the values {yj} where j = 1,...,L.
In this illustration we have M = 5 and L = 3. If we consider a total
number N of instances of these variables, then we denote the number
of instances where X = xi and Y = yj by nij , which is the number of
points in the corresponding cell of the array. The number of points in
column i, corresponding to X = xi, is denoted by ci, and the number of
points in row j, corresponding to Y = yj , is denoted by rj .
}
}
ci
yj rj
xi
nij
and the probability of selecting the blue box is 6/10. We write these probabilities
as p(B = r)=4/10 and p(B = b)=6/10. Note that, by definition, probabilities
must lie in the interval [0, 1]. Also, if the events are mutually exclusive and if they
include all possible outcomes (for instance, in this example the box must be either
red or blue), then we see that the probabilities for those events must sum to one.
We can now ask questions such as: “what is the overall probability that the selection procedure will pick an apple?”, or “given that we have chosen an orange,
what is the probability that the box we chose was the blue one?”. We can answer
questions such as these, and indeed much more complex questions associated with
problems in pattern recognition, once we have equipped ourselves with the two elementary rules of probability, known as the sum rule and the product rule. Having
obtained these rules, we shall then return to our boxes of fruit example.
In order to derive the rules of probability, consider the slightly more general example shown in Figure 1.10 involving two random variables X and Y (which could
for instance be the Box and Fruit variables considered above). We shall suppose that
X can take any of the values xi where i = 1,...,M, and Y can take the values yj
where j = 1,...,L. Consider a total of N trials in which we sample both of the
variables X and Y , and let the number of such trials in which X = xi and Y = yj
be nij . Also, let the number of trials in which X takes the value xi (irrespective
of the value that Y takes) be denoted by ci, and similarly let the number of trials in
which Y takes the value yj be denoted by rj .
The probability that X will take the value xi and Y will take the value yj is
written p(X = xi, Y = yj ) and is called the joint probability of X = xi and
Y = yj . It is given by the number of points falling in the cell i,j as a fraction of the
total number of points, and hence
p(X = xi, Y = yj ) = nij
N . (1.5)
Here we are implicitly considering the limit N → ∞. Similarly, the probability that
X takes the value xi irrespective of the value of Y is written as p(X = xi) and is
given by the fraction of the total number of points that fall in column i, so that
p(X = xi) = ci
N . (1.6)
Because the number of instances in column i in Figure 1.10 is just the sum of the
number of instances in each cell of that column, we have ci = 
j nij and therefore,
14 1. INTRODUCTION
from (1.5) and (1.6), we have
p(X = xi) =
L
j=1
p(X = xi, Y = yj ) (1.7)
which is the sum rule of probability. Note that p(X = xi) is sometimes called the
marginal probability, because it is obtained by marginalizing, or summing out, the
other variables (in this case Y ).
If we consider only those instances for which X = xi, then the fraction of
such instances for which Y = yj is written p(Y = yj |X = xi) and is called the
conditional probability of Y = yj given X = xi. It is obtained by finding the
fraction of those points in column i that fall in cell i,j and hence is given by
p(Y = yj |X = xi) = nij
ci
. (1.8)
From (1.5), (1.6), and (1.8), we can then derive the following relationship
p(X = xi, Y = yj ) = nij
N = nij
ci
· ci
N
= p(Y = yj |X = xi)p(X = xi) (1.9)
which is the product rule of probability.
So far we have been quite careful to make a distinction between a random variable, such as the box B in the fruit example, and the values that the random variable
can take, for example r if the box were the red one. Thus the probability that B takes
the value r is denoted p(B = r). Although this helps to avoid ambiguity, it leads
to a rather cumbersome notation, and in many cases there will be no need for such
pedantry. Instead, we may simply write p(B) to denote a distribution over the random variable B, or p(r) to denote the distribution evaluated for the particular value
r, provided that the interpretation is clear from the context.
With this more compact notation, we can write the two fundamental rules of
probability theory in the following form.
The Rules of Probability
sum rule p(X) =
Y
p(X, Y ) (1.10)
product rule p(X, Y ) = p(Y |X)p(X). (1.11)
Here p(X, Y ) is a joint probability and is verbalized as “the probability of X and
Y ”. Similarly, the quantity p(Y |X) is a conditional probability and is verbalized as
“the probability of Y given X”, whereas the quantity p(X) is a marginal probability
1.2. Probability Theory 15
and is simply “the probability of X”. These two simple rules form the basis for all
of the probabilistic machinery that we use throughout this book.
From the product rule, together with the symmetry property p(X, Y ) = p(Y,X),
we immediately obtain the following relationship between conditional probabilities
p(Y |X) = p(X|Y )p(Y )
p(X) (1.12)
which is called Bayes’ theorem and which plays a central role in pattern recognition
and machine learning. Using the sum rule, the denominator in Bayes’ theorem can
be expressed in terms of the quantities appearing in the numerator
p(X) =
Y
p(X|Y )p(Y ). (1.13)
We can view the denominator in Bayes’ theorem as being the normalization constant
required to ensure that the sum of the conditional probability on the left-hand side of
(1.12) over all values of Y equals one.
In Figure 1.11, we show a simple example involving a joint distribution over two
variables to illustrate the concept of marginal and conditional distributions. Here
a finite sample of N = 60 data points has been drawn from the joint distribution
and is shown in the top left. In the top right is a histogram of the fractions of data
points having each of the two values of Y . From the definition of probability, these
fractions would equal the corresponding probabilities p(Y ) in the limit N → ∞. We
can view the histogram as a simple way to model a probability distribution given only
a finite number of points drawn from that distribution. Modelling distributions from
data lies at the heart of statistical pattern recognition and will be explored in great
detail in this book. The remaining two plots in Figure 1.11 show the corresponding
histogram estimates of p(X) and p(X|Y = 1).
Let us now return to our example involving boxes of fruit. For the moment, we
shall once again be explicit about distinguishing between the random variables and
their instantiations. We have seen that the probabilities of selecting either the red or
the blue boxes are given by
p(B = r)=4/10 (1.14)
p(B = b)=6/10 (1.15)
respectively. Note that these satisfy p(B = r) + p(B = b)=1.
Now suppose that we pick a box at random, and it turns out to be the blue box.
Then the probability of selecting an apple is just the fraction of apples in the blue
box which is 3/4, and so p(F = a|B = b)=3/4. In fact, we can write out all four
conditional probabilities for the type of fruit, given the selected box
p(F = a|B = r)=1/4 (1.16)
p(F = o|B = r)=3/4 (1.17)
p(F = a|B = b)=3/4 (1.18)
p(F = o|B = b)=1/4. (1.19)
16 1. INTRODUCTION
p(X,Y )
X
Y = 2
Y = 1
p(Y )
p(X)
X X
p(X|Y = 1)
Figure 1.11 An illustration of a distribution over two variables, X, which takes 9 possible values, and Y , which
takes two possible values. The top left figure shows a sample of 60 points drawn from a joint probability distribution over these variables. The remaining figures show histogram estimates of the marginal distributions p(X)
and p(Y ), as well as the conditional distribution p(X|Y = 1) corresponding to the bottom row in the top left
figure.
Again, note that these probabilities are normalized so that
p(F = a|B = r) + p(F = o|B = r)=1 (1.20)
and similarly
p(F = a|B = b) + p(F = o|B = b)=1. (1.21)
We can now use the sum and product rules of probability to evaluate the overall
probability of choosing an apple
p(F = a) = p(F = a|B = r)p(B = r) + p(F = a|B = b)p(B = b)
= 1
4
×
4
10 +
3
4
×
6
10 = 11
20 (1.22)
from which it follows, using the sum rule, that p(F = o)=1 − 11/20 = 9/20.
1.2. Probability Theory 17
Suppose instead we are told that a piece of fruit has been selected and it is an
orange, and we would like to know which box it came from. This requires that
we evaluate the probability distribution over boxes conditioned on the identity of
the fruit, whereas the probabilities in (1.16)–(1.19) give the probability distribution
over the fruit conditioned on the identity of the box. We can solve the problem of
reversing the conditional probability by using Bayes’ theorem to give
p(B = r|F = o) = p(F = o|B = r)p(B = r)
p(F = o) = 3
4
×
4
10
×
20
9 = 2
3
. (1.23)
From the sum rule, it then follows that p(B = b|F = o)=1 − 2/3=1/3.
We can provide an important interpretation of Bayes’ theorem as follows. If
we had been asked which box had been chosen before being told the identity of
the selected item of fruit, then the most complete information we have available is
provided by the probability p(B). We call this the prior probability because it is the
probability available before we observe the identity of the fruit. Once we are told that
the fruit is an orange, we can then use Bayes’ theorem to compute the probability
p(B|F), which we shall call the posterior probability because it is the probability
obtained after we have observed F. Note that in this example, the prior probability
of selecting the red box was 4/10, so that we were more likely to select the blue box
than the red one. However, once we have observed that the piece of selected fruit is
an orange, we find that the posterior probability of the red box is now 2/3, so that
it is now more likely that the box we selected was in fact the red one. This result
accords with our intuition, as the proportion of oranges is much higher in the red box
than it is in the blue box, and so the observation that the fruit was an orange provides
significant evidence favouring the red box. In fact, the evidence is sufficiently strong
that it outweighs the prior and makes it more likely that the red box was chosen
rather than the blue one.
Finally, we note that if the joint distribution of two variables factorizes into the
product of the marginals, so that p(X, Y ) = p(X)p(Y ), then X and Y are said to
be independent. From the product rule, we see that p(Y |X) = p(Y ), and so the
conditional distribution of Y given X is indeed independent of the value of X. For
instance, in our boxes of fruit example, if each box contained the same fraction of
apples and oranges, then p(F|B) = P(F), so that the probability of selecting, say,
an apple is independent of which box is chosen.
1.2.1 Probability densities
As well as considering probabilities defined over discrete sets of events, we
also wish to consider probabilities with respect to continuous variables. We shall
limit ourselves to a relatively informal discussion. If the probability of a real-valued
variable x falling in the interval (x, x + δx) is given by p(x)δx for δx → 0, then
p(x) is called the probability density over x. This is illustrated in Figure 1.12. The
probability that x will lie in an interval (a, b) is then given by
p(x ∈ (a, b)) =  b
a
p(x) dx. (1.24)
18 1. INTRODUCTION
Figure 1.12 The concept of probability for
discrete variables can be extended to that of a probability
density p(x) over a continuous
variable x and is such that the
probability of x lying in the interval (x, x+δx) is given by p(x)δx
for δx → 0. The probability
density can be expressed as the
derivative of a cumulative distribution function P(x).
δx x
p(x) P(x)

     */
}
