# Frank the Tank

## A wave based 2D zombie shooter with portals, written in Java

### [View full project page and docs](http://johanbrook.github.com/medioqre)

**Frank the Tank** is an eight week project where we're building a 2D game from the ground up. That is: no graphics libraries or frameworks – we're writing all the rendering code ourselves.

The goal is to create a simple game yet with stunning gameplay and beautiful retro pixel graphics. Know Mojang's Catacomb Snatch? If you are – well, there you go.

## Building and running

First clone the project:

	git clone git://github.com/johanbrook/medioqre.git && cd medioqre

The Eclipse IDE has been used for development, so we've bundled `.project` and `.classpath` files, which makes it easy to import the whole project into Eclipse as a new project. Use the `File -> Import -> Existing projects into workspace` guide.

The project should be compilable and runnable out of the box from a fresh clone.

**Things to note**

- `Main.java` is containing the `main()` method. Run this.
- Resources are located in the `res` directory.
- Libraries used are located in the `libs` directory.
- When making changes to non-source code files, i.e. external JSON data for instance: remember to clean the project before building and running (`Project -> Clean` in Eclipse).
- Native OpenGL bindings are located in the `libs/jogamp-all-platforms` directory (structured in directories for each platform).

## We are

- [John Barbero Unenge](http://github.com/JBarberU)
- [Chris Nordqvist](http://github.com/chrisnordqvist)
- [Jesper Persson](http://github.com/pungsnigel)
- [Johan Brook](http://github.com/johanbrook)

Made on a Mac.