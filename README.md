![image](banner.png)

## What it is...

Repository contains small webservice written in Kotlin and Javascript. Its task
is to recommend programming language to the end user.

At its heart is extendable and easily modifiable matrix of languages'
compatibilities with carefully chosen criteria. The criteria are 
either binary (yes/no) or numeric (scale 1 to 10) except of Popularity Index (1 to 50). 

UI presents three modes of operation:
* browsing of all languages
* filtering languages based on criteria
* interactive mode

The code is easy to customize and to bend towards company principles, vision,
strategy, etc.

There may be some typos and mistakes here and there, but their number is
relatively low. I'm constantly updating database to fix wrong predictions.
The app will truly shine when experts in respective languages start opening
issues on GitHub to align the database. 

Database itself contains 50 languages new and old, with all imaginable features
and paradigms. The languages are described with 48 features/criteria.

Project has been written and is best opened in IntelliJ IDEA Community Edition. To update the database
edit *Database.md* file in main and tests resources folders. 


## Modes

#### Browse

It presents all the languages in order from most popular according to 
StackOverflow's survey 2022 (fresh!). By default, description and criteria are hidden they are 
presented when card is clicked.

#### Filter

At the top there is list of criteria. Turning them on causes the list
to filter with only matching. Clicking the asterisk shows how I tried to measure binary and numeric features per language.
With features on scale you can change
input's value while it's on and list will reflect the critter.
There is no validation, if the number is not valid the behavior is undefined.

#### Interactive

The interaction is based on yes/no question from generated decision tree.
The "splits" were generated to maximize Information Gain on each split.
Due to performance tree has been cut off at depth 20. With answering 
questions as yes the list drills down further and further. There is no
reset button, for reset refresh the page.

## Decision tree

It is written in kotlin. First the Markdown document is parsed to list
of languages with attached features.

Then the tree is constructed top to bottom splitting the list based on
following algorithm:
* all features are candidates for split
* the penalty of the split is how uneven are left and right subset counts
* the split is taken with the minimum penalty and descending to children the candidate is excluded
* left and right children are added to the end of the queue with candidate list without that feature
* process repeats but cut-down depth is 20

The splits for numeric features are chosen with value that causes left and right to be maximally balanced.

## License

<a rel="license" href="http://creativecommons.org/licenses/by-nc-nd/4.0/"><img alt="Creative Commons License" style="border-width:0" src="https://i.creativecommons.org/l/by-nc-nd/4.0/88x31.png" /></a><br />This work is licensed under a <a rel="license" href="http://creativecommons.org/licenses/by-nc-nd/4.0/">Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License</a>.



