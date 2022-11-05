# ColourSee

This is my ISP for my high school AP Computer Science (ICS4U0), in Java. (Me and most of my classmates took this course in grade 10 as part of an enriched STEM program.) The idea is that we wanted to create a game that simulated colourblind vision, and challenged players to identify colours present in an image. This project integrates concepts we learned earlier in the course, such as UX and graphics (using Java Swing) and image manipulation. 

We implemented four colour filters:
Deuteranopia (red-green)
Protanopia (red-green)
Tritanopia (blue-yellow)
Grayscale (achromatopsia)

For each level, we start with a line-drawn image, define a colour palette for the player to use to colour in the image, and record the expected colour of a pixel within each closed region of the image (as defined by the line drawing). The player would colour a closed region of the image by clicking on a pixel in that region with a selected colour from the palette, basically like a fill/paint bucket tool in MS Paint.

This project was honestly a pretty big failure for me, partly because I was in a bad spot at the time, and because I wasn't familiar with good project management techniques and coding best practices to incrementally develop and test a large programming project. To be honest, this experience put me off project programming for a while, and I'm only now getting back into it in my current CPEN 221 (software development) course. (I'm doing much better now since the course explicitly taught us good coding practice and testing methodology, and I appreciate their importance greatly because of my bad experience with this project.)

As far as I can remember, downloading the entire folder and running the Game.java file (which contains the main method) should run the game.
