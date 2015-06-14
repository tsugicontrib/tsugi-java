Tsugi for Java - Library Code
=============================

This is a Java version of the PHP Tsugi application 
(https://github.com/csev/tsugi).  This repository is the 
API and base JDBC implementation.

API Documentation
-----------------

<a href="http://csev.github.io/tsugi-java/apidocs/index.html" target="_blank">http://csev.github.io/tsugi-java/apidocs/index.html</a>

Build
-----
This will produces a jar file and drops it into your maven repository. 

    mvn install

Database
--------

This is expecting that PHP Tsugi already is installed running 
and its database is created and available on localhost:8889
using the default account, password, and database name 
and that the tables already exist.
If you want to change this, edit the file

    src/main/resources/tsugi.properties

Generating JavaDocs
-------------------

Make sure you are in master:

    git checkout master
    git status (there should be no pending changes)

    mvn javadoc:javadoc

Make sure you are happy by looking at:

    apidocs/index.html

When you are happy:

    tar cfv apidocs.tar apidocs
    git checkout gh-branches
    tar xfv apidocs.tar
    rm apidocs.tar

    git commit -a
    git push
    git checkout master

A sweet one-line version of the four steps is:

    tar cfv apidocs.tar apidocs; git checkout gh-branches; tar xfv apidocs.tar; rm apidocs.tar

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
    

