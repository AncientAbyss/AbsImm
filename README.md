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

* Initialize

        // load the story from a string
        Story story = new Loader().fromString(...);
        // or from a file
        Story story = new Loader().fromFile(...);

* Setup retrieving messages from AbsImm

        story.addClient(...);

* Start the adventure

        story.tell();

* Handle user input

        story.interact(...);

* Get the current state, e.g. for saving the active session

        story.getState();

* Restore a state

        story.setState(...);


Story file format
-----------------

Stories are defined in XML and need to be conform to the story.xsd.
Examples can be found in the 'res' directory.


Usage (Client)
--------------

To start a server for a specific XMPP account use:

    java -jar AbsoluteImmersion.jar [storyfile] [user] [password] [host] [port]

Play by starting a chat with this account.


Notes
-----

Story files need to be encoded as UTF-8.
