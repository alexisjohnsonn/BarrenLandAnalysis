# Barren Land Analysis Problem

## Introduction 
From the problem specification: 

You have a farm of 400m by 600m where coordinates of the field are from (0, 0) to (399, 599). A portion of the farm is barren, and all the barren land is in the form of rectangles. Due to these rectangles of barren land, the remaining area of fertile land is in no particular shape. An area of fertile land is defined as the largest area of land that is not covered by any of the rectangles of barren land. 
Read input from STDIN. Print output to STDOUT 
Input 
You are given a set of rectangles that contain the barren land. These rectangles are defined in a string, which consists of four integers separated by single spaces, with no additional spaces in the string. The first two integers are the coordinates of the bottom left corner in the given rectangle, and the last two integers are the coordinates of the top right corner. 
Output 
Output all the fertile land area in square meters, sorted from smallest area to greatest, separated by a space. 

## Solution 
I modeled the farm field using a matrix of size 400 x 600 (though it is extensible to any reasonable size). The matrix is initialized with all 0's. As barren rectangles
are added by the user, their corresponding regions in the matrix are filled with -1's. 

To find all fertile regions, I use a flood fill algorithm. This algorithm is easily envisioned by thinking about the "fill" function on Microsoft paint. If I click 
on a white space in Microsoft paint, all connected white space will be filled with the designated color. Likewise, when a fertile region is filled, all connected 
fertile space will also be filled. 

To find each fertile area, I iterate through the matrix. I initialize the flood fill value to 1. This value is incremented each time I need to flood a new region.
When I find a coordinate (i,j) where land[i][j] == 0, this means it is fertile land that has not been searched yet.
Beginning at land[i][j], I search for all connected 0's using a Depth First Search. Each time I reach a node, I change its value from 0 to the flood fill value
so it will not be counted again. Additionally I increment an "area" counter at each node. When the DFS finishes, the counter represents the number of 
nodes visited, which is the area of the fertile region. I append this to an integer list of fertile areas. 

When the DFS finishes, I continue iterating through the rest of the matrix, repeating this process each time I find a node with a value of 0. Once I've 
iterated through the entire matrix, I sort the list of areas in ascending order and return it.

## Obtaining the Project

git clone https://github.com/alexisjohnsonn/BarrenLandAnalysis

## Set Up
The project uses Java with Maven for build automation. Unit tests are in Junit4.13.

There are a few ways to run the project. 
### Command line with Maven. 
I'm hoping this is the easiest method for running the project if you have Maven on 
your machine. If not, it can be downloaded from https://maven.apache.org. However, I used 
brew to install it: 
 ```brew install maven```
 
 Once you have Maven installed, navigate to the BarrenLandAnalysis directory.
 Compile the program using the command 
 ```mvn compile```
 
 Run the command line program using the command
 ```mvn exec:java```
 
 Run the unit tests using the command
 ```mvn test```
 
 ### IDE Integration
 Another way to run the program is by opening a new Maven project in an IDE like Intellij or Eclipse. 
 I used Intellij. The quickest way to open the project is to select 
 ```File->New->ProjectFromExistingSources```
 Then, choose the ```pom.xml``` file in the root directory.
 
 From here, open the BarrenLandAnalysis.java file in src/main/java/com/target/barrenland. You should be able to 
 right click in the file and select "Run". Similarly, you should be able to run the unit tests
 in src/test/java/com/target/barrenland/FarmTest.java.
 
 ### Command line with Javac
 If you've made it this far, I'm sincerely sorry the first two options didn't work out. However you should
 be able to compile the command line program using the following instructions: 
 
 
 1. Navigate to BarrenLandAnalysis/src/main/java
 2. Compile with: 
 ```javac com/target/barrenland/InvalidBoundaryException.java com/target/barrenland/Farm.java com/target/barrenland/BarrenLandAnalysis.java ```
 3. Run with: 
 ```java com/target/barrenland/BarrenLandAnalysis```
