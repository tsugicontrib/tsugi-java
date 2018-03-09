Tsugi for Java - Library Code
=============================

[![Apereo Incubating badge](https://img.shields.io/badge/apereo-incubating-blue.svg?logo=data%3Aimage%2Fpng%3Bbase64%2CiVBORw0KGgoAAAANSUhEUgAAAA4AAAAOCAYAAAAfSC3RAAAABmJLR0QA%2FwD%2FAP%2BgvaeTAAAACXBIWXMAAAsTAAALEwEAmpwYAAAAB3RJTUUH4QUTEi0ybN9p9wAAAiVJREFUKM9lkstLlGEUxn%2Fv%2B31joou0GTFKyswkKrrYdaEQ4cZAy4VQUS2iqH%2BrdUSNYmK0EM3IkjaChnmZKR0dHS0vpN%2FMe97TIqfMDpzN4XkeDg8%2Fw45R1XNAu%2Fe%2BGTgAqLX2KzAQRVGytLR0jN2jqo9FZFRVvfded66KehH5oKr3dpueiMiK915FRBeXcjo9k9K5zLz%2B3Nz8EyAqX51zdwGMqp738NSonlxf36Cn7zX9b4eYX8gSBAE1Bw9wpLaW%2BL5KWluukYjH31tr71vv%2FU0LJ5xzdL3q5dmLJK7gON5wjEQizsTkFMmeXkbHxtHfD14WkbYQaFZVMzk1zfDHERrPnqGz4wZ1tYfJ5%2FPMLOYYW16ltrqKRDyOMcYATXa7PRayixSc4%2FKFRhrqjxKGIWVlZVQkqpg1pYyvR%2BTFF2s5FFprVVXBAAqq%2F7a9uPKd1NomeTX4HXfrvZ8D2F9dTSwWMjwywueJLxQKBdLfZunue0Mqt8qPyMHf0HRorR0ArtbX1Zkrly7yPNnN1EyafZUVZLJZxjNLlHc%2BIlOxly0RyktC770fDIGX3vuOMAxOt19vJQxD%2BgeHmE6liMVKuNPawlZ9DWu2hG8bW1Tuib0LgqCrCMBDEckWAVjKLetMOq2ZhQV1zulGVFAnohv5wrSq3tpNzwMR%2BSQi%2FyEnIl5Ehpxzt4t6s9McRdGpIChpM8Y3ATXbkKdEZDAIgqQxZrKo%2FQUk5F9Xr20TrQAAAABJRU5ErkJggg%3D%3D)](https://www.apereo.org/content/projects-currently-incubation)

This is a Java version of the PHP Tsugi API 
(https://github.com/tsugiproject/tsugi-php).  This repository is the 
API and base JDBC implementation.  I have recorded a simple
<a href="https://www.youtube.com/watch?v=R2hsu0xusKo&list=PLlRFEj9H3Oj5WZUjVjTJVBN18ozYSWMhw&index=10"
target="_blank">Video Introduction to Java Tsugi</a>.

Pre-Requisites
--------------

You should install Tsugi PHP and set it up:

    https://github.com/tsugiproject/tsugi

This sets up all the database tables.   

API Documentation
-----------------

<a href="http://tsugiproject.github.io/tsugi-java/apidocs/index.html" target="_blank">http://tsugiproject.github.io/tsugi-java/apidocs/index.html</a>

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

In order to get the snapshot versions add this to your `pom.xml`:

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

    https://github.com/tsugiproject/tsugi-java-servlet

And follow the instructions in its README.md - not that there is some overlap because it tells you to first install 
and configure this repository (java-tsugi).

    https://github.com/tsugiproject/tsugi-java-servlet/blob/master/README.md

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

    mvn -DperformRelease=true clean javadoc:jar install deploy -Dgpg.passphrase=Whatever
    mvn clean # afterwards

Documentation: 

    http://central.sonatype.org/pages/apache-maven.html

