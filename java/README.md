Requirements

Basic stuff
- [X] I have a basic domain model with an elevator, floors, and people
- [X] I have a simple display of the state of the world
- [X] A person can press a button to call the elevator, and it will move to the floor they're on and open the doors
- [X] A person can board the lift if it is on their floor and the doors are open
- [X] A person who is in the lift can press a button to select a destination floor for the lift
- [X] A person who is in the lift when it arrives at their destination floor will exit the lift if the doors are open
- [X] There are multiple people in the building trying to get to different floors, and the lift has a first-in-first-out system for going to floors

Improving the algorithm
- [X] The lift algorithm is pluggable
- [ ] The lift sweeps from min to max to drop people off
- [ ] People only board the lift if it is going in the direction they wish to go
- [ ] There are two lifts in the building
- [ ] A call for a lift to a floor is considered complete once one lift, not both, arrive at that floor

Tracking travel times
- [ ] People arrive randomly and travel to random destinations
- [ ] Operations (calling a lift, the lift moving, boarding, exiting, etc) all take time
- [ ] The simulation calculates mean wait and travel time for all people
