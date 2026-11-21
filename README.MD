# Kudh (Exercise Tracker)
**Author: Yudil Pariyar<br>**
---

### Instructions to run the application
The app has been built and tested to be run in IntelliJ. Open the project there, open the "ca.umantioba.ca.pariyary" folder,
then the "TrackerMain.java" file. Finally, click "Current File" in the top menu bar and run!


### Resources
Some inspiration taken from Strava [https://www.strava.com/]

Learned how to check if given points form a rectangle in the matrix from [https://stackoverflow.com/questions/41516164/how-to-know-if-a-list-of-coordinates-forms-a-rectangle]
 for obstacle invariant

Learned about Manhattan distance to ensure that Activity routes are continuous from https://www.datacamp.com/tutorial/manhattan-distance

### Changes
- Addition of logic, ui and exception packages.
- Moved all app logic function from ExcessiveTracker class to the logic layer
- REPL is commented out to allow the program to compile

### Flows of Interactions

```mermaid
    flowchart

    subgraph Exercise Tracker
        direction TB
        login[[Login]] == Logged in ==> feed[Feed]
        feed == Update profile ==> updateProfile[Update Profile]
        feed == Add activity ==> addActivity[Add activity]
        feed == Find path ==> findRoute[Find Path]
%%        feed == Local Feed ==> localFeed[Following feed]
        feed == Follow ==> follow[Follow]

%%        localFeed == Update profile ==> updateProfile[Update Profile]
%%        localFeed == Add activity ==> addActivity[Add activity]
%%        localFeed == Find path ==> findRoute[Find Path]

     updateProfile -. Exit / Cancel .-> feed
     addActivity -. Exit / Cancel .-> feed
     findRoute -. Exit / Cancel .-> feed
     follow -. Exit / Cancel .-> feed

        feed == log out ==> logout[[log out]]
%%        localFeed == log out ==> logout[[log out]]


%%     findRoute[Find Path] == log out ==> loogout[[log out]]
%%     feed== log out ==> loogout[[log out]]
%%     updateProfile== log out ==> loogout[[log out]]
%%     addActivity== log out ==> loogout[[log out]]

    end
```

```mermaid
flowchart
subgraph Update Profile
    
    getAction[[Get Action]]
    exit[[Go back to feed]]

    getAction == Change Username ==> getUsername[Get Username]
    getAction == Change Password ==> getPassword
    getAction == Remove activity ==> getActivity[Show prior Activities and
    user selects activity]
    getAction == Unfollow ==> getUser[Display followed Users and
    user selects user to unfollow]

    getUsername -. cancel .-> getAction
    getUsername -. invalid username .-> getUsername
    getUsername == username ==> checkUsername{check if 
    username taken}
    
    checkUsername -. username not available .-> getUsername
    checkUsername -. username available .-> changeUsername{change username}
    changeUsername -. username changed sucessfully .-> exit

    getPassword[Get new password] -. invalid password .-> getPassword
    getPassword -. cancel .-> getAction
    getPassword == password ==> changePassword{change password}
    changePassword -. password changed sucessfully .-> exit

    getActivity -. cancel .-> getAction
    getActivity -. invalid activity .-> getActivity
    getActivity == activity ==> removeActivity{remove activity 
    from user}
    removeActivity -. activity removed .-> exit
    
    getUser -. cancel .-> getAction
    getUser -. invalid user .-> getUser
    getUser == user ==> removeUser{remove user from 
    list of followed}
    removeUser -. user unfollowed  .-> exit

end
```

```mermaid
flowchart
subgraph Add obstacles
    direction TB
    getObs -. invalid input .-> getObs
    getObs[[Get obstacle type 
    and coordinates]]
    getObs == Exit ==> Exit[[Exit to Add activity menu]] 
    getObs 
    getObs == type and coordinates ==> validateObs{Check obstacles within 
    map bounds and
    valid type}
    validateObs -. obstacle out of bounds / invalid type .-> getObs
    validateObs -. valid obstacle .-> mapAdd{Add obstacle 
    to map}
    mapAdd -. Obstacle sucessfully added .-> getObs
          
end
```

```mermaid
flowchart
subgraph Add Activity and Obstacles
    direction TB
    getActDets[[Get activity type, start and end date and time]]
    
    validateDets{Check type validity 
    and verify start is before end}
    
    obsAdd[Came across obstacles?]
    obsAdd -. invalid input .-> obsAdd
    obsAdd -. cancel .-> getActDets
    
    isNew[Enter new route or use previous route]
    isNew -. invalid input .-> isNew
    addObs[Add obstacles]
    
    checkPrevAct{Check if previous
    activities exists}
    
    selectRoute[Select Route]
    
    isRouteValid{Check route within map
    and doesn't go
    through obstacles}
    
    addAct{Save activity to
    user history}

    Exit[[Exit to feed]]

    getActDets == Activity details ==> validateDets
    validateDets -. invalid type or start/end date time .-> getActDets
    obsAdd == skip adding obstacles ==> isNew
    obsAdd == Yes ==> addObs
    validateDets -. valid Activity details .-> obsAdd
    
    addObs == Obstacles Added ==> isNew
    isNew -. cancel .-> getActDets
    isNew == Use previous route ==> checkPrevAct
   checkPrevAct -. Display previous routes .-> selectRoute
    selectRoute  -. Invalid selection .-> selectRoute
    selectRoute == route from previous activity ==> addAct
    checkPrevAct -. no activites in record  .-> isNew
    isNew == New route ==> isRouteValid
    isRouteValid -. invalid route .-> isNew
    isRouteValid -. valid route .-> addAct
    addAct -. Display Activity in Map .-> Exit
end
```
```mermaid
flowchart 
    
subgraph Find Path 


    getStartEnd[[Get start and end coordinates]]
    validateStartEnd{Check if Coordinates 
    Exist within map}
    userCoordinatesCovered{Check if the
    coordinates have been covered
    by user's prev activities}
    coordinatesCovered{Check if the
    coordinates have been covered
    by user's + followed prev activities}
    pathFindAlg{ run path finding 
    algorithm}


    userOrFeed -. cancel .-> getStartEnd
    userCoordinatesCovered -. Coordinates not covered .-> getStartEnd
    coordinatesCovered -. Coordinates not covered .-> getStartEnd
    pathFindAlg -. no path exists .-> 

    getStartEnd == Coordinates ==> validateStartEnd
    validateStartEnd -. Coordinates out of Map's Bounds .-> getStartEnd
    validateStartEnd -. Coordinates within Map .-> userOrFeed[Self routes or self + users followed]
    userOrFeed == user only paths ==> userCoordinatesCovered
    userOrFeed == user + followed paths ==> coordinatesCovered
    userCoordinatesCovered -. Coordinates covered .-> pathFindAlg
    coordinatesCovered -. Coordinates covered .-> pathFindAlg

    exit[[Go back to feed]]

    pathFindAlg -. display path .-> exit


end
```
```mermaid
    flowchart
subgraph Follow

getUsername[Display list of 
users and get username]
getUsername -. Invalid Username .-> getUsername
%%getUsername == Exit ==> feed

checkUserExists{Check if 
        user exists and
user is not self}
checkUserExists -. User doesn't exist or
user is self .-> getUsername

addToUser{Add user to
followed list}

checkFollowed{is user already
followed}

follow[follow?]
follow -. cancel .-> getUsername


%% getUser == follow user ==> getUsername

getUsername == username ==> checkUserExists
checkUserExists -. display user details .-> follow

follow == follow ==> checkFollowed
checkFollowed -. user not followed .-> addToUser
checkFollowed -. user already followed .-> getUsername
addToUser -. User followed .-> feed[[Show feed]]

end
```

### Overview

<small>Kudh is an exercise tracker. Users are able to track their activities within a map (2D Matrix).
Users are able to choose between 3 activities: Run, Walk and Bike.
It stores their activities with information including start date and time, end date and time, and route taken.
Users are also able to access their past activities. Users have access to a feed where they can see activities of people
they follow. Users are able to follow/unfollow people within and make changes to their accounts (change password/username
, delete activities etc)

## Domain model

### Diagram

Below is the Domain model diagram.
```mermaid
classDiagram
    direction TB
    class ExerciseTracker {
        - Collection~User~ users
        - User currUser;
        - Map world
        void addUser(User user) 
    }

    class User {
        - String name
        - String password
        - HashMap~Integer, Activity~ historyActivity
        - Collection~Coordinates~ activitiesOccupiedCoords;
        - Collection~User~ followedUsers;

        void follow(User)
        void removeActivity(Activity)
        void addActivity(Activity)
        boolean checkPassword(String)
    }

    class Activity {
        - static int countActivity;
        - ActivityType type
        - LocalDateTime startDateTime, endDateTime
        - List~Coordinates~ route
        - int activityID
        void addCoordinates(Coordinates)
        void removeCoordinates(Coordinates)
    }

    class ActivityType {
        - WALK
        - RUN
        - BIKE
    }

    class Map {
        - Cell[][] plane
        - int width
        - int height
        - HashMap~Integer, Obstacle~ obstacles
        - Collection~Coordinates~ occupiedCoords;
        void addObstacle(Obstacle)
        void removeObstacle(Obstacle)
        boolean coordsWithinMap(Coordinates)
        
    }

    class Cell {
        - Obstacle obstacle

        boolean isEmpty()
    }

    class Obstacle {
        - ObstacleType type;
        - static int countObstacle;
        - int obstacleID;
        - ArrayList~Coordinates~ location
        void addCoordinates(Coordinates) ;
        void removeCoordinates(Coordinates) ;
    }

    class ObstacleType {
        - TREE
        - BUILDING
        - ROCK
    }

    class Coordinates {
        - int x
        - int y
    }


    class Stack~T~{
        push(T)
        T pop()
        T peek()
        int size()
        boolean isEmpty()
    }
    <<interface>> Stack

    class LinkedListStack~T~ {
        Node head
        int size

        push(T)
        T pop()
        T peek()
        int size()
        boolean isEmpty()
    }

    class Node~T~ {
        T data
        Node~T~ next
    }

    LinkedListStack ..|> Stack
    LinkedListStack --* Node

    <<enumuration>> ActivityType
    <<enumuration>> ObstacleType

    note for ExerciseTracker " Invariants:
    * users != null
    * loop through users to ensure no null user exists.
    * map != null
    * loop through activities in users' historyActivity to ensure
    &emsp;- activity's route does not go through World's obstacles
    &emsp;- activity's route is within World's bounds
    
    "
    note for User "Invariants:
        * name != null
        * password != null
        * followedUsers != null
        * name.length() > 0
        * password.length() > 0
        * loop through historyActivity to ensure no null Activities exists.
        * loop through activitiesOccupiedCoords to ensure no null Coordinates exists.
        * loop through followedUsers to ensure no null Users exists.
    "
    note for Activity "Invariants:
        * type != null
        * startDateTime, endDateTime != null
        * startDateTime &lt; endDateTime
        * route != null
        * route.size > 0
        * loop through route to ensure no null Coordinates
        "
    note for Map "Invariants:
        * plane != null
        * planeWidth, planeHeight &gt; 0
        * obstacles != null
        * loop through obstacles and ensure
        &emsp;- obstacle is not null
        &emsp;- obstacle is within map's bounds
        &emsp;- obstacle doesn't occupy coordinates occupied by other obstacles
        &emsp;- obstacle is rectangular
    "
    note for Obstacle "Invariants:
        * type != null
        * location != null
        * location.size > 0
        * loop through location to ensure no null Coordinates exists
    "
    note for Coordinates "Invariants:
        * x, y != null
    "

    note for LinkedStack "Invariants:
        * size >= 0
        * if size >= 1 then head != null
    "

    note for Node "Invariants:
        * value != null
    "

    ExerciseTracker --* User
    ExerciseTracker --* Map
    Map --* Coordinates
    Activity --o Coordinates
    Map --* Obstacle
    Map --* Cell
    Obstacle --o Coordinates
    User --* Activity
    Activity --* ActivityType
    Obstacle --* ObstacleType
```