# SARL Elevator Simulator Middleware Connectivity Framework (for Java-based Elevator Simulator) 

This project provides the basic middleware infrastructure, through a capacity/skill, to develop agent-based controllers for the [Java Elevator Simulator (RMIT Version)](https://bitbucket.org/sarlrmit/elevator-sim) in the [SARL Language](https://github.com/sarl/sarl) agent framework. 
The project also a includes a basic demo SARL elevator agent-based controllers taht uses the middlware infrastructure. 

The SARL Middleware infrastructure provided in this project can be used "off-the-shelf" to develop complex SARL-based controllers for the elevator simulator. 
The middleware provides the core basic skills of a SARL agent to control an elevator simulator, by receiving sensor-events and issuing command-events to the simulator.  

This project was part of a team-based programming project course under the supervision of A/Prof. Sebastian Sardina (see below for contacts). 
The largest part of that project involved extending the Java-based elevator simulator and can be found [here](https://bitbucket.org/ssardina-research/elevator-sim).


Please refer to the [**Project Wiki**](https://bitbucket.org/sarlrmit/sarl-elevatorsim-ctrl/wiki/Home) for information on how to install and use this infrastructure. 

## PRE-REQUISITES 

* Java Runtime Environment (JRE) and Java Compiler (javac) v1.8 (sun version recommended)
* Maven project management and comprehension tool (to meet dependencies, compile, package, run).
* [The Elevator Simulator Server (RMIT version)](https://bitbucket.org/sarlrmit/elevator-sim).
* SARL modules and execution engine:
	* Environment variable `SARL_VERSION` stating the SARL version to be used (e.g., 0.7.2)
    * Version tested: 0.6.1, 0.7.2
    * Obtained with Maven automatically from [SARL Maven Resository](http://mvnrepository.com/artifact/io.sarl.maven).

## DEVELOP THE MIDDLEWARE FURTHER 

In most cases, one would just use the middleware to develop SARL controllers. However, if you want to change the middleware or extend it you need to setup and install it into a SARL development environment.
Please see Step 2 instructions [here](https://bitbucket.org/snippets/ssardina/6eybMg/sarl-application-general-information-setup) how to do so.

To debug your development you may want to use the simple demo controller **DummyMultiCarController** that continuously makes cars go up and down, without doing anything else.
This controller is provided mostly for testing the middleware. The demo controller is in package `au.edu.rmit.elevatorsim.sarlmw.controllers`

Once the middlware is ready, you can compile + install it in your local maven repository (e.g., `~/.m2`) via `mvn clean install`. 
To just produce the JAR file (to then be used in another SARL Controller project), run `mvn clean package` and the JAR file will be installed under `target/`.


## USING THE MIDDLEWARE 

You can use this project off-the-shelf to make use of the SARL connectivity infrastructure so as to build your own SARL controllers. 

When developing your own SARL controller, you just need to "import" this project in order to equip your agents with the interaction and reporting capacities/skills:

1. Define the version of the SARL controller that you want to use in the **properties** section:

		<properties>
			...
			<!-- SARL Elevator Controller framework version -->
			<sarl-elvatorsim-ctrl.version>ee4230e</sarl-elvatorsim-ctrl.version>
		</properties>
Other versions you can use: `-SNAPSHOT`, `sarl-0.6-SNAPSHOT`, or in general `<branch-name>-SNAPSHOT`


2. Include the JitPack under **repositories*** section be able to connect to bitbucket system:

		<repositories>
			...
			<!-- JitPack used for remote installation of dependencies from Github -->
			<repository>
			<id>jitpack.io</id>
			<name>JitPack Repository</name>
			<url>https://jitpack.io</url>
			</repository>
		</repositories>

3. Include the **sarl-elvatorsim-ctrl** framework (i.e., this project!) in the **dependencies** section:

		<!-- Project dependencies -->
		<dependencies>
			...
			<!--  SARL Elevator Controller framework -->
			<dependency>
			<groupId>org.bitbucket.ssardina-research</groupId>
			<artifactId>sarl-elvatorsim-ctrl</artifactId>
			<version>${sarl-elvatorsim-ctrl.version}</version>
			</dependency>
		</dependencies>


You can do that by suitably configuring your project's POM file `pom.xml` (to "import" this project) and then using the corresponding skills for the capacities provided.



Below we describe the middleware framework (that is, what is provided for you to use in your SARL elevator controller project).
In a nutshell, two capabilities with skills are provided to connect and interact with the elevator simulator and to report domain messages (like an elevator arriving to a floor or a person requesting service). 
In addition a set of events are defined signaling events in the elevator simulator. These are the events that your agent should handle.


### CAPACITIES

1. **Cap_Reporting**: tools to report various meaningful domain messages, such as a car arriving to a floor, a person leaving or entering a car, car passing by a floor while traveling, person requesting service at a floor, etc.
2. **Cap_SimulatorInteraction**: allows for actions to be submitted to the Elevator Simulator. Currently only supports two actions:
	* Sending a car to a floor via method `sendCar(action : SendCarAction)`.
	* Establishing the connection to the server: `act_connectToSimulator()`


### SKILLS

#### Skill_ConsoleReporting 

Implements **Cap_Reporting** capacity by printing in console. Formatting is allowed, such as the following action to report a message:
 
```
#!java

reportMessage("Successfully connected to elevator hardware at {1}:{2}", simulator_host, simulator_port);
```

or the following to report arriving to a floor:

```
#!java

	on CarArrivedPercept {
		reportArrivedAt(occurrence.car, occurrence.floor)
		currentFloor = occurrence.floor
		moving = false
	}

```




#### Skill_SingleSimulatorInteraction

This is the **main** core skill implementing the **Cap_SimulatorInteractoin** capacity, It  allows SARL agents to connect and receive information from the elevator simulator and send back actions to perform in the simulator.

The skill is initialized with the address of the simulator (hostname + port) and the SARL space `agentSpace` where events coming from the elevator need to be emitted.

Internally, the skill extends the _abstract_ skill Java class **ClientElevatorSimulator** which acts a middle layer between the elevator simulator and the SARL framework in pure Java. **ClientElevatorSimulator** connects and maintains the network connection to the simulator via an object of class **NetworkConnection**. It then continuously receives JSON messages from simulator server, decodes them into objects, and calls corresponding _abstract_ methods for each type of event. Such methods are to be implemented in a fully instantiated skill, which is what **Skill_SingleSimulatorInteraction** does by emitting the following corresponding events for agents registered in the `agentSpace`:

* onModelChanged
* onCarRequested
* onDoorOpened
* onDoorClosed
* onDoorSensorClear
* onCarArrived
* onPersonEnteredCar
* onPersonLeftCar
* onFloorRequested
* onFloorPassed
* onActionProcessed
* onSimulationEnded

All these event carry the id of the event and the timestamp, together with additional information relevant to the event. SARL agents who are registered to space `agentSpace` (could be the agent of the skill or holons of it, for example) can then just handle these events via SARL behaviors.

The skill also implements the two capacity actions:

* `act_connectToSimulator()`: connect to the game server (with the address defined when skill created)
* `sendCar(action : SendCarAction)`: do `action` in simulator, which contains car id, floor to go, and advertised next direction. 

The skill will send corresponding JSON messages to the simulator.

In most cases, users developing SARL controllers will not need to bother with the **ClientElevatorSimulator** infrastructure. All they need to do is to handle the above SARL events and send cars to destinations by issuing a `sendCar(action: SendCarAction)` action.

Here is the core of the Dummy agent every time a door has been closed in a given car:

```
#!java

	on DoorClosedPercept {
		var next_floor : int = -1
		var next_dir : Direction

		if (currentFloor == highestFloor) {
			next_floor = lowestFloor
			next_dir = Direction.UP
		} else if (currentFloor == lowestFloor) {
			next_floor = highestFloor
			next_dir = Direction.DOWN
		}
		
		if (next_floor > -1) {
                        // this is the actual MOVEMENT action!
			sendCar(new SendCarAction(occurrence.car, next_floor, next_dir)) 
			reportTravellingTo(occurrence.car, next_floor, next_dir)
			moving = true
		} 
		
	}

```
		
To establish the connection, the agents adopts the skill and then issue the connection action:


```
#!java

setSkill(new Skill_SingleSimulatorInteraction(simulator_host, simulator_port, defaultSpace), Cap_SimulatorInteraction)
act_connectToSimulator()
reportMessage("Successfully connected to elevator hardware at {1}:{2}", simulator_host, simulator_port);

```

#### Skill_MultipleSimulatorInteraction 

This is a further extension of skill **Skill_SingleSimulatorInteraction** to share a connection to the elevator simulator amongst multiple SARL agents/skills.

For example, in a multi-agent SAR application, each agent can register to control one car with this skill, but tere will be only _one_ actual connection to the simulator via a static singleton object `sharedConnection` of class `NetworkConnection`. The first time the skill is created, the connection will be established and `sharedConnection` will be instantiated. After that, it will be re-used.

Each agent should then connect as follows:

```
#!java

setSkill(new Skill_MultipleSimulatorInteraction(simulator_host, simulator_port, #[carID], defaultSpace), Cap_SimulatorInteraction)
act_connectToSimulator()

```

The third argument is a list of car ids that the agent is allowed to control (in this case, just one). When a **sendCar** action is issued by an agent, it is only allowed (and corresponding message sent to simulator) if agent is controlling the corresponding car.

**NOTE:** While the network connection will be one, each agent will have its own **Skill_MultipleSimulatorInteraction** skill, which in term is a **Skill_SingleSimulatorInteraction**. So when a JSON message arrives, any of those underlying skills using the same `NetworkConnection` handler could read it and act upon (emit corresponding event). This is a bit non-robust as one never knows which skill/agent will read a message from the simulator server and post the corresponding message (and in which space).


### Events 

The following self-explanatory events signal something that happened in the simulation:

	import au.edu.rmit.elevatorsim.sarlctrl.events.CarArrivedPercept
	import au.edu.rmit.elevatorsim.sarlctrl.events.CarPassedFloorPercept
	import au.edu.rmit.elevatorsim.sarlctrl.events.CarRequestPercept
	import au.edu.rmit.elevatorsim.sarlctrl.events.DoorClosedPercept
	import au.edu.rmit.elevatorsim.sarlctrl.events.DoorOpenedPercept
	import au.edu.rmit.elevatorsim.sarlctrl.events.DoorSensorClearedPercept
	import au.edu.rmit.elevatorsim.sarlctrl.events.FloorRequestPercept
	import au.edu.rmit.elevatorsim.sarlctrl.events.ModelChangePercept
	import au.edu.rmit.elevatorsim.sarlctrl.events.PersonEnteredPercept
	import au.edu.rmit.elevatorsim.sarlctrl.events.PersonExitedPercept


There is also an event corresponding to the send car action:

	import au.edu.rmit.elevatorsim.sarlctrl.events.SendCarAction





## TROUBLESHOOTING

If you have errors, these will usually tend to be as a result of a Maven issue or a SARL compilation issue.

1. Make sure your pom.xml contains the right version of SARL.
2. If using ECLIPSE, make sure you are using the right version distribution wrt `pom.xml`
3. Try to clean up Maven dependencies/cache:
	*  SARL Eclipse Distribution Update:
		1. Right-click on the project directory in Eclipse and go to *Maven > Update Project*.
		2. Check *Force Update of Snapshots/Releases* and click OK.
		3. When the project has finished updating, go to *Project > Clean*. Click OK.
	* From command cline (CLI):
		1. Run `mvn clean install` (this will regenerate Maven dependencies).
	* If you are still having errors, navigate to `~/.m2` on Linux or `C:/Users/<user>/.m2` on Windows and delete the `repository` directory. Trying the troubleshooting steps above again will forcibly regenerate
your Maven dependencies and generally resolve any corrupted dependencies or failures.



## LINKS ##

* Maven:
	* Doc: <https://maven.apache.org/general.html>
	* SARL distribution: <http://mvnrepository.com/artifact/io.sarl.maven>
	* JANUS / SRE (for janus.version in pom.xml):  <http://search.maven.org/#search%7Cga%7C1%7Cjanusproject>
* JitPack for remote installation of dependencies from Github & Bitbucket: 
	* Mochalog: <https://jitpack.io/#mochalog/mochalog>
	* sarl-elvatorsim-ctrl: <https://jitpack.io/#org.bitbucket.sarlrmit/sarl-elvatorsim-ctrl>
* SARL:
	* Main page: <http://www.sarl.io/>
	* github repo: <https://github.com/sarl/sarl>
	* User forum: <https://groups.google.com/forum/?hl=en#!forum/sarl>


## PROJECT LEADER & CONTACT ##

* Sebastian Sardina - ssardina@gmail.com


## PROJECT CONTRIBUTORS ##

* Matthew McNally (Project Coordinator and SARL Agent Developer) 
* Joshua Richards (Java Elevator Sim Server developer)
* Joshua Beale (SARL Agent Developer)
* Dylan Rock (Documentation)
* Sebastian Sardina (Project Supervisor)


## LICENSE ##
This project is using the GPLv3 for open source licensing for information and the license visit GNU website (https://www.gnu.org/licenses/gpl-3.0.en.html).