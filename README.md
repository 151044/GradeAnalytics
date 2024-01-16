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

# Usage
## From Course History
Copy all the entries from [SIS's Course History](https://sisprod.psft.ust.hk/psc/SISPROD/EMPLOYEE/HRMS/c/SA_LEARNER_SERVICES.SSS_MY_CRSEHIST.GBL?Page=SSS_MY_CRSEHIST&Action=U&ForceSearch=Y&&) to a file.

## Manual Formatting
The program can also take information from a formatted file, in the format below per row:
Department;Course Code;Course Title;Grade;Credits;Is Major Course or Not (true or false); Semester

A sample line would be as follows:
```
COMP;1021;Introduction to Computer Science;A+;3;true;21-22 Fall
```
