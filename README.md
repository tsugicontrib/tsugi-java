Tsugi for Java - Library Code
=============================

This is a Java version of the PHP Tsugi application 
(https://github.com/csev/tsugi).  This repository is the 
API and base JDBC implementation.

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

Git Details
-----------

If you forked my repo and want to grab the latest changes, do 
the following once:

    git remote add upstream https://github.com/csev/tsugi-java

Then from time to time when you want to pull mods and update your fork:

    git pull upstream master
    git push origin master
    

