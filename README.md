illegal_wiretaps
================


Illegal Wiretaps
After uncovering years of government malfeasance, you are tasked with leading a team of government programmers to decode copious amounts of possibly illegal wiretaps. Your congres\
sional hearing is coming up, and you need to have these wiretaps decoded as soon as possible so you can give as informative testimony as possible (and clear your posterior of any \
wrong doing!). Unfortunately, government programmers are not the most well adjusted, sharpest tools in the shed, and they require decisive and firm leadership (e.g. you) to guide \
them. Programmers are assigned integer numbers to protect their classified identities, while wiretap victims are referred to by their first names (a name is defined as a sequence \
of alphabetical characters).

Due to OSHA regulations, you will always have exactly the same number of programmers as wiretap victims, and each programmer decodes exactly one wiretap. Because this is the gover\
nment, to decode a wiretap it takes at least 1 server hour per letter in the victim's name. All programmers share the same secured government terminal and they can only work one a\
t a time. Once signed in, programmers must stay at the terminal until they are finished decoding a wiretap. To make things worse, each programmer has certain personality quirks an\
d foibles which can alter their efficiency.

Programmers with an even number suffer from vowelitosis, they require an additional 1.5 hours of work for every vowel (Note that the NEA only recognizes the letters A, E, I, O, an\
d U as vowels) in the victim's name.
Programmers with an odd number suffer from consonentia, they require an additional 1 hour of work for every consonant in the victim's name.
Programmers whose numbers share prime factors with the number of letters in a victim's name are struck with a severe phobia, that requires an additional 2 hours of therapy per com\
mon factor. Due to DHS regulations, the programmer must stay at the terminal while under therapy, preventing others from using it. For example, it took programmer 12 (factors of 2\
 and 3) an extra 4 hours of therapy to decode NORMAN's file (factors of 2 and 3).
You are given 26 programmers, numbered from 1 through 26. The 26 wiretap victims are named as follows:

ANDROMEDA
BARBARA
CAMERON
DAGMAR
EKATERINA
FLANNERY
GREGORY
HAMILTON
ISABELLA
JEBEDIAH
KIMBERLEY
LARISSA
MEREDITH
NORMAN
OSWALD
PENELOPE
QUENTIN
RANDALL
SAVANNAH
TABITHA
URSULA
VIVIENNE
WINONA
XAVIER
YVONNE
ZENOBIA
Write a program that tasks your team of programmers to the wiretap victims in a way that minimizes the total time necessary to crack all the wiretaps. Your program should run on t\
he command line, and take as input a file containing the first names of all the wiretap victims (newline separated). The output should be the total time cost (in hours) of decodin\
g all the wiretaps listed in the input file, in addition to your configuration of programmers and wiretaps. Your program should be able to handle both upper and lower case, be rob\
ust enough to handle or ignore malformed input, and it should work for as large a general case as possible. Remember, your congressional hearing is coming up and your lawyers may \
need more evidence to clear you of wrong doing!

You may use any of the following programming languages:
C++
Java
JavaScript
OCaml/SML
Perl
PHP
Python
