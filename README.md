Tsugi for Java - Library Code
=============================

[![Apereo Incubating badge](https://img.shields.io/badge/apereo-incubating-blue.svg?logo=data%3Aimage%2Fpng%3Bbase64%2CiVBORw0KGgoAAAANSUhEUgAAAA4AAAAOCAYAAAAfSC3RAAAABmJLR0QA%2FwD%2FAP%2BgvaeTAAAACXBIWXMAAAsTAAALEwEAmpwYAAAAB3RJTUUH4QUTEi0ybN9p9wAAAiVJREFUKM9lkstLlGEUxn%2Fv%2B31joou0GTFKyswkKrrYdaEQ4cZAy4VQUS2iqH%2BrdUSNYmK0EM3IkjaChnmZKR0dHS0vpN%2FMe97TIqfMDpzN4XkeDg8%2Fw45R1XNAu%2Fe%2BGTgAqLX2KzAQRVGytLR0jN2jqo9FZFRVvfded66KehH5oKr3dpueiMiK915FRBeXcjo9k9K5zLz%2B3Nz8EyAqX51zdwGMqp738NSonlxf36Cn7zX9b4eYX8gSBAE1Bw9wpLaW%2BL5KWluukYjH31tr71vv%2FU0LJ5xzdL3q5dmLJK7gON5wjEQizsTkFMmeXkbHxtHfD14WkbYQaFZVMzk1zfDHERrPnqGz4wZ1tYfJ5%2FPMLOYYW16ltrqKRDyOMcYATXa7PRayixSc4%2FKFRhrqjxKGIWVlZVQkqpg1pYyvR%2BTFF2s5FFprVVXBAAqq%2F7a9uPKd1NomeTX4HXfrvZ8D2F9dTSwWMjwywueJLxQKBdLfZunue0Mqt8qPyMHf0HRorR0ArtbX1Zkrly7yPNnN1EyafZUVZLJZxjNLlHc%2BIlOxly0RyktC770fDIGX3vuOMAxOt19vJQxD%2BgeHmE6liMVKuNPawlZ9DWu2hG8bW1Tuib0LgqCrCMBDEckWAVjKLetMOq2ZhQV1zulGVFAnohv5wrSq3tpNzwMR%2BSQi%2FyEnIl5Ehpxzt4t6s9McRdGpIChpM8Y3ATXbkKdEZDAIgqQxZrKo%2FQUk5F9Xr20TrQAAAABJRU5ErkJggg%3D%3D)](https://www.apereo.org/content/projects-currently-incubation)

This is a Java version of the PHP Tsugi API 
(https://github.com/csev/tsugi-php).  This repository is the 
API and base JDBC implementation.  I have recorded a simple
<a href="https://www.youtube.com/watch?v=R2hsu0xusKo&list=PLlRFEj9H3Oj5WZUjVjTJVBN18ozYSWMhw&index=10"
target="_blank">Video Introduction to Java Tsugi</a>.

Pre-Requisites
--------------

You should install Tsugi PHP and set it up:

    https://github.com/csev/tsugi

This sets up all the database tables.   

API Documentation
-----------------

<a href="http://csev.github.io/tsugi-java/apidocs/index.html" target="_blank">http://csev.github.io/tsugi-java/apidocs/index.html</a>

Using from pom.xml
------------------

This artifact is in Sonatype, add these entries your `pom.xml` as follows:

    <dependency>
        <groupId>org.tsugi</groupId>
        <artifactId>tsugi-java</artifactId>
        <version>0.2-SNAPSHOT</version>
    </dependency>

    <dependency>
       <groupId>org.tsugi</groupId>
       <artifactId>tsugi-util</artifactId>
       <version>0.2-SNAPSHOT</version>
    </dependency>

In order to get the snapshot versions ass this to your `pom.xml`:

    <repositories>
       <repository>
         <id>ossrh</id>
         <name>Sonatype</name>
         <url>https://oss.sonatype.org/content/repositories/snapshots/</url>
         <layout>default</layout>
         <snapshots>
           <enabled>true</enabled>
         </snapshots>
       </repository>
     </repositories>

Or you can put this in your`~/.m2/settings.xml`:

    <settings>
      ..
      <profiles>
        <profile>
           <id>allow-snapshots</id>
              <activation><activeByDefault>true</activeByDefault></activation>
           <repositories>
             <repository>
               <id>snapshots-repo</id>
               <url>https://oss.sonatype.org/content/repositories/snapshots</url>
               <releases><enabled>false</enabled></releases>
               <snapshots><enabled>true</enabled></snapshots>
             </repository>
           </repositories>
         </profile>
      </profiles>
    </settings>    


Database Setup
--------------

This is expecting that PHP Tsugi already is installed running 
and its database is created and available on localhost:8889
using the default account, password, and database name 
and that the tables already exist.  If you want to change this, edit the file

    src/main/resources/tsugi.properties

The Sample Servlet
------------------

Once you have tsugi-java checked out and passing the unit tests, it is time to play with the sample
servlet at:

    https://github.com/csev/tsugi-java-servlet

And follow the instructions in its README.md - not that there is some overlap because it tells you to first install 
and configure this repository (java-tsugi).

    https://github.com/csev/tsugi-java-servlet/blob/master/README.md

Building from Source
--------------------

To produce a jar file and drops it into your maven repository. 

    mvn clean install

The unit tests actually want a live database.  To install without unit tests, use

    mvn -DskipTests clean install

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

Other Source Code
-----------------

A Sample servlet using this code

https://github.com/tsugiproject/tsugi-java-servlet

Tsugi low-level API library this depends on (formerly sakai-basicltiutil)

https://github.com/tsugiproject/tsugi-util

Releasing to SonaType
---------------------

To sign the artifacts, install the GPG tools:

    https://gpgtools.org/


Locations:

    https://oss.sonatype.org/#nexus-search;quick~tsugi-util
    https://oss.sonatype.org/#nexus-search;quick~tsugi-java

Make sure `~/.m2/settings.xml` looks like this:

    <settings>
      <servers>
        <server>
          <id>ossrh</id>
          <username>drchuck</username>
          <password>your-sonatype-password</password>
        </server>
      </servers>
    <profiles>
      <profile>
         <id>allow-snapshots</id>
            <activation><activeByDefault>true</activeByDefault></activation>
         <repositories>
           <repository>
             <id>snapshots-repo</id>
             <url>https://oss.sonatype.org/content/repositories/snapshots</url>
             <releases><enabled>false</enabled></releases>
             <snapshots><enabled>true</enabled></snapshots>
           </repository>
         </repositories>
       </profile>
    </profiles>
    </settings>    

Deploy:

    mvn install deploy -Dgpg.passphrase=Whatever clean

Documentation: 

    http://central.sonatype.org/pages/apache-maven.html

Releasing tsugi-util to Sonatype
--------------------------------

Note that the tsugi-util code is released from the Sakai source tree into Sonatype:

    https://github.com/sakaiproject/sakai/blob/master/basiclti/tsugi-util/README.md

Set up `settings.xml` as described above.

    cd trunk/basiclti/tsugi-util
    cp pom-tsugi.xml pom.xml 
    mvn install deploy
    git checkout pom.xml


Check results of the deploy at:

    https://oss.sonatype.org/#nexus-search;quick~tsugi-util

After a while the files migrate to:

    https://oss.sonatype.org/content/repositories/snapshots/org/tsugi/

Key Making Notes:

    m-c02m92uxfd57:tsugi-util csev$ gpg --gen-key
    gpg (GnuPG/MacGPG2) 2.0.30; Copyright (C) 2015 Free Software Foundation, Inc.
    This is free software: you are free to change and redistribute it.
    There is NO WARRANTY, to the extent permitted by law.

    Please select what kind of key you want:
    (1) RSA and RSA (default)
    (2) DSA and Elgamal
    (3) DSA (sign only)
    (4) RSA (sign only)
    Your selection? 1
    RSA keys may be between 1024 and 4096 bits long.
    What keysize do you want? (2048) 
    Requested keysize is 2048 bits   
    Please specify how long the key should be valid.
         0 = key does not expire
      <n>  = key expires in n days
      <n>w = key expires in n weeks
      <n>m = key expires in n months
      <n>y = key expires in n years
    Key is valid for? (0) 0
    Key does not expire at all
    Is this correct? (y/N) y

    GnuPG needs to construct a user ID to identify your key.

    Real name: Charles Severance
    Email address: drchuck@gmail.com
    Comment: For Sonatype           
    You selected this USER-ID:
    "Charles Severance (For Sonatype) <drchuck@gmail.com>"

    Change (N)ame, (C)omment, (E)mail or (O)kay/(Q)uit? O
    You need a Passphrase to protect your secret key.    

    We need to generate a lot of random bytes. It is a good idea to perform
    some other action (type on the keyboard, move the mouse, utilize the
    disks) during the prime generation; this gives the random number
    generator a better chance to gain enough entropy.
    We need to generate a lot of random bytes. It is a good idea to perform
    some other action (type on the keyboard, move the mouse, utilize the
    disks) during the prime generation; this gives the random number
    generator a better chance to gain enough entropy.
    gpg: key BCDACC58 marked as ultimately trusted
    public and secret key created and signed.

    gpg: checking the trustdb
    gpg: 3 marginal(s) needed, 1 complete(s) needed, PGP trust model
    gpg: depth: 0  valid:   3  signed:   0  trust: 0-, 0q, 0n, 0m, 0f, 3u
    gpg: next trustdb check due at 2018-08-19
    pub   2048R/BCDACC58 2016-07-26
      Key fingerprint = 0A6A FE01 ...
    uid       [ultimate] Charles Severance (For Sonatype) <drchuck@gmail.com>
    sub   2048R/9B8D98F2 2016-07-26

    m-c02m92uxfd57:tsugi-util csev$ gpg --keyserver hkp://pool.sks-keyservers.net --send-keys BCDACC58
    gpg: sending key BCDACC58 to hkp server pool.sks-keyservers.net
