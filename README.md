Tsugi for Java - Library Code
=============================

This is a Java version of the PHP Tsugi application 
(https://github.com/csev/tsugi).  This repository is the 
API and base JDBC implementation.  I have recorded a simple
<a href="https://www.youtube.com/watch?v=R2hsu0xusKo&list=PLlRFEj9H3Oj5WZUjVjTJVBN18ozYSWMhw&index=10"
target="_blank">Video Introduction to Java Tsugi</a>.

Pre-Requisites
--------------

You should install Tsugi PHP and set it up:

    https://github.com/csev/tsugi

This sets up all the database tables.   

In order to get the tsugi-util maven artifact into your repo,
you may need to install and compile Sakai - you do not need 
to run Sakai:

    https://github.com/csev/sakai-scripts

Alternatively, you could extract the file

    tmp/tsugi-util-repo.jar

And place it in:

    ~/.m2/repository/org/tsugi

Until I get the progres in place to release the tsugi-util jars
from the Sakai source tree.

API Documentation
-----------------

<a href="http://csev.github.io/tsugi-java/apidocs/index.html" target="_blank">http://csev.github.io/tsugi-java/apidocs/index.html</a>

Build
-----
This will produces a jar file and drops it into your maven repository. 

    mvn install

The unit tests actually want a live database.  To install without unit tests, use

    mvn -DskipTests install

Database
--------

This is expecting that PHP Tsugi already is installed running 
and its database is created and available on localhost:8889
using the default account, password, and database name 
and that the tables already exist.
If you want to change this, edit the file

    src/main/resources/tsugi.properties

The Sample Servlet
------------------

Once you have tsugi-java checked out and passing the unit tests, it is time to play with the sample
servlet at:

    https://github.com/csev/tsugi-java-servlet

And follow the instructions in its README.md - not that there is some overlap because it tells you to first install 
and configure this repository (java-tsugi).

    https://github.com/csev/tsugi-java-servlet/blob/master/README.md


Generating Tsugi API JavaDocs
-----------------------------

Make sure you are in master:

    git checkout master
    git status (there should be no pending changes)

    mvn javadoc:javadoc

Make sure you are happy by looking at:

    apidocs/index.html

When you are happy:

    tar cfv apidocs.tar apidocs
    git checkout gh-pages
    tar xfv apidocs.tar
    rm apidocs.tar

    git commit -a
    git push
    git checkout master

A sweet one-line version of the four steps is:

    tar cfv apidocs.tar apidocs; git checkout gh-pages; tar xfv apidocs.tar; rm apidocs.tar

For those of us who like to say things like "!tar" in the command line when 
doing the same things more than one time. :)

Git Details
-----------

If you forked my repo and want to grab the latest changes, do 
the following once:

    git remote add upstream https://github.com/csev/tsugi-java

Then from time to time when you want to pull mods and update your fork:

    git pull upstream master
    git push origin master
    

