This is a libgdx java project that is pretty much and try at 2d kerbal space program. Many elements are directly inspired by it when it comes to names and controls

at this point the project is on hold as life caught up. I really learned a lot and had fun but want to do other things. 


I think there are other tech stacks way better suited for this task but crunching the orbit numbers and making a little game roughly e2e was great. I actually played a little bit but there are no objectives, landing mechanics, building or personalization options. it is just an accurate orbital space flight simulator.


My next step in the process was to try to figure out how to make the project packaged a deliverable to get feed back from my friends but that was proving to be difficult. building was always hard and gave errors which  id overcome just to deal with later. I have a preliminary build I sent to friends in a zip and it's in the project but overall , I do not have reliable steps to run yourself. I think it should work if you just unzip on windows and run the exe but you may need a JRE installed.

I also know there are edge cases where the math starts to break. Too close to planets and orbits with incredibly oblong shapes can fall apart. There are no boundaries either so there is bound to be rollover and overflow at the edges of data types. This is a simulation of the 3 body issue but the planets themselves are one rails and the gravity effect on the shop is a 2 body problem recursively applied. There are much better simulators out there with less bugs in the edges

thanks for reading.