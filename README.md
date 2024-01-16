## Code Execution
Our build system is Gradle.
### Running the Application

Create a runnable JAR as follows:

For Mac/Unix:
```
./gradlew dist
```

For Windows:
```
.\gradlew.bat dist
```

The runnable application JAR can be found under `build/libs`.

If double-clicking on the JAR does not bring up anything, your Java environment may be misconfigured. Try to run it from the command line in the `build/libs` directory:
```
java -jar GradeAnalytics-1.0.jar
```

Alternatively, grab the latest build [here](https://github.com/151044/GradeAnalytics/releases/tag/latest).
