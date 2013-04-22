SKGL
----

A really neat raytracing renderer that is more or less incomplete. When I get
done with adding more features, I plan to add a shader language (javascript). 
More or less, I want the thing to be as OpenGL-like as possible. Perhaps I'll 
expose a way to make the engine as parallel as users would like as well, but
I'll have to think about how.


Compiling
---------

I use ant as the build system.
Just do

    ant

to compile the source.

    ant jar

Will package it all up, and

    ant run

would compile, package, and run the thing.


Notes
-----
Right now it just runs the main function in Test.java, which, throughout the
development phase of this thing will probably just contain hardcoded scenes
that I use to test the renderer.

Feel free to contact me if you want any info/help building your own/some neat
sources/anything at all.

email: seanpkelly2992@gmail.com

Multithreading stuff
--------------------

Right now, it's made by splitting the image in n different squares or rectangles or whatever, and having those square's pixels rendered then written down on a global BufferedImage. This is all well and good for symmetric multi-threading on a single machine with a multicore cpu, but it just won't cut it if we want to network-parallel. Well, it might, but... It's ugly. 

Thus, I had two ideas. 

1) We have one actor per machine; each actor does old-school multi-threading on it's own machine. Eeach actor has to render it's own image, which actually is a subdivision of the "master" one; the way "actor" images are further divided is exactly the same way it's done on the current, single-machine code. Only on another machine. 

That's the equivalent of just having many instances of the current program running on different machines. 

Right away, I guess that would work pretty good if all our machines are the same strenght. There would be a big network burst when the tasks are sent, then another (probably bigger) when the results get back in, but probably no network noise at all during the actual render. When the render's done and everybody sent back it's slave image, the master sticks them together, which seems a bit hacky but totally doable. By BLITting them in a WritableRaster. Or something. As in the current program, every actor has the scene data, which is read by every render thread. As in the current implementation. It would probably be perfect for an array of similar machines, but it would be annoying on machines of different strenghts since the bigger one would be done first and everybody would wait on the puny last one. And don't even get me started on a better task distribution aglo. This shortcoming is simply a symptom of the fact that the multithreading algo is dumb. No hard feelings, cabbagebot. It sure worked great on a single machine. 


2) Everybody's actors; the system detects how many cores it has, and creates n-1 "render" threads and one "master" thread. I guess I'l have to get my "render"/"slave" nomenclature right one of these days. So, the master has a BufferedImage and dispatches the rays to the actors, which as I mentionned all have an entire core to them. The actor renders the ray/pixel, the master writes it down to the BufferedImage, then dispatches it the next available ray, and so on until the scene is done.

First, that would be funnier, but harder. I guess I'l have to modify the innards of some classes and get some inner classes out of their outer shell. I'l sure need to fuck around in Camera.renderScene, anyays. Second, that would be better on machines of different strength, given each ray is dynamically assigned. As a downside, I guess we'l have a lot more network traffic... 

Quick comment about state: the scene details would be copied in each actor in a "dumb" implementation; it doesn't really need to be, since the scene is immutable once the render's started. The ideal would be to have a copy of that state on each physical machine; I guess it will mean a bit of messing around to get different actors to share state, and perhaps make my algo a tad less nice-looking because the actors would be, uh, conscious of what machines they're on. Perhaps I could actually treat the data other "remote" actors, but simply make sure that it's always on the localhost of it's relevant "slave" actor. I'l probably see what fits better once I start coding. 

So, to get back to the network... Of course, we'd have right away way more traffic than on the first idea. Probably someting along the lines of "not even worth multi-threading" on a bad network; that would go down according to 1/x where x is the dof, asserting every reflection of a ray takes the same time to crunch. If it ever becomes apparent that sending single rays at times is inefficient, I guess we could send blocks of n rays at the same time. We'l see. 
