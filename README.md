AbsImm
======

[![Build Status](https://travis-ci.org/AncientAbyss/AbsImm.svg?branch=master)](https://travis-ci.org/AncientAbyss/AbsImm) [![codecov](https://codecov.io/gh/AncientAbyss/AbsImm/branch/master/graph/badge.svg)](https://codecov.io/gh/AncientAbyss/AbsImm)

AbsImm is an open source text adventure engine. Adventures are written in an intuitive open XML-based format.
Several clients for different platforms are already [available](https://github.com/AncientAbyss).


Features
--------

* Simplified Text story format support allowing basic interactivity.
* Comprehensive XML story format support, which allows modelling complex situations using states.
* Save and load game states
* Open story file formats
* Cross platform (Android >= API level 16)


Requirements
------------

* Java JDK >= 1.8
* Maven >= 2


Usage
-----

For an example project, see the [Cli client](https://github.com/AncientAbyss/AbsImm-Cli).

### Setup

* Gradle:

        repositories {
            ...
            maven {
                url "http://rod.bplaced.net/maven2"
            }
        }
            
        dependencies {
            ...
            compile 'net.ancientabyss.absimm:absimm-core:0.4-SNAPSHOT'
        }
            
* Maven:
 
        <repositories>
            ....
            <repository>
                <id>ancientabyss</id>
                <url>http://rod.bplaced.net/maven2</url>
            </repository>
        </repositories>
        
        <dependencies>
            ...
            <dependency>
                <groupId>net.ancientabyss.absimm</groupId>
                <artifactId>absimm-core</artifactId>
                <version>0.4-SNAPSHOT</version>
            </dependency>
        </dependencies>


### Initialize

        // load the story from a string
        Story story = new Loader(new XmlParser()).fromString(...); // or TxtParser() for the simplified format
        // or from a file
        Story story = new Loader(new XmlParser()).fromFile(...); // or TxtParser() for the simplified format
        // set the client to which AbsImm passes messages
        story.addClient(...);

### Run

        // Start the adventure
        story.tell();
        // Handle user input
        while (...) {
            story.interact(...);
        }
        
### Load/Save

        // Get the current state, e.g. for saving the active session
        story.getState();
        // Restore a state
        story.setState(...);


Story file format
-----------------

### Simplified (TXT)

Simplified stories are text files which are divided into sections 
that use decision nodes to allow jumping between sections, where:

* section starts are indicated by lines in the form '\<section\>:'
* lines in the form '* \<label\> (\<section\>)' define decision nodes,
    which allow jumping to sections

Other commands:
* To immediately return to the previous section use the peek section operator: '<<'.
* When using '-' instead '*' as prefix for decision nodes, the available decision options will be hidden.

#### Example:

        Hello
        * world (sec_world)
        * humans (sec_humans)
        
        sec_world:
        Whoop!
        
        sec_humans:
        Well.

The following example prints

        Hello
        - world
        - humans

expects the user to enter either 'world' or 'humans', 
and then responds with either 'Whoop!' or 'Well.'.


### Comprehensive (XML)

Stories are defined in XML and need to be conform to the [story.xsd](story.xsd).
Examples can be found in the [res](res) directory.
Story files need to be encoded as UTF-8.


Notes
-----

For Android (API level <24) compatibility uses the streamsupport library instead Java 8 Streams.

