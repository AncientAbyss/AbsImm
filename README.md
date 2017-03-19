AbsImm
======

AbsImm is an open source text adventure engine. Adventures are written in an intuitive open XML-based format.
At the moment XMPP and console clients are available.


Requirements
------------

* Java JRE >= 1.7
* Maven >= 2


Usage
-----

* Setup

    * Gradle:
    
            maven {
                url "http://rod.bplaced.net/maven2"
            }
            
            dependencies {
                compile 'at.absoluteimmersion:absim-xmpp-android:1.0-SNAPSHOT'
            }
            
    * Maven:
     
            TBD


* Initialize

        // load the story from a string
        Story story = new Loader(new XmlParser()).fromString(...);
        // or from a file
        Story story = new Loader(new XmlParser()).fromFile(...);
        // set the client to which AbsImm passes messages
        story.addClient(...);

* Run

        // Start the adventure
        story.tell();
        // Handle user input
        story.interact(...);
        
* Load/Save

        // Get the current state, e.g. for saving the active session
        story.getState();
        // Restore a state
        story.setState(...);


Story file format
-----------------

Stories are defined in XML and need to be conform to the story.xsd.
Examples can be found in the 'res' directory.
Story files need to be encoded as UTF-8.


Usage (Client)
--------------

To start a server for a specific XMPP account use:

    java -jar AbsoluteImmersion.jar [storyfile] [user] [password] [host] [port]

Play by starting a chat with this account.
