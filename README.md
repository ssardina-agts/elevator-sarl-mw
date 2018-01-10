# SARL Elevator Simulator Connectivity & Controller (for Java-based Elevator Simulator) 

This project provides the basic infrastructure, through a capacity/skill, to develop agent-based controllers for the [Java Elevator Simulator (RMIT Version)](https://bitbucket.org/sarlrmit/elevator-sim) in the [SARL Language](https://github.com/sarl/sarl) agent framework. The project also a very basic SARL elevator agent-based controllers using such infrastructure. 

The SARL infrastructure provided in this project can be used "off-the-shelf" to develop other more complex controllers. The infrastructure provides the core basic skills of a SARL agent to control an elevator simulator, by receiving sensor-events and issuing command-events to the simulator.  A very simple dummy agent is provided too.

This project was part of a team-based programming project course under the supervision of A/Prof. Sebastian Sardina; see below for contacts. 

## INSTALLATION ##

### Software Prerequisites ###
* Java Runtime Environment (JRE) and Java Compiler (javac) v1.8 (sun version recommended)
* Maven project management and comprehension tool (to meet dependencies, compile, package, run).
* [The Elevator Simulator Server (RMIT version)](https://bitbucket.org/sarlrmit/elevator-sim).
* SARL modules and execution engine 
	* Version tested: 0.6.1
	* Obtained with Maven automatically from http://mvnrepository.com/artifact/io.sarl.maven).


### Download the Project ###
To get the current version of the project, simply run ```git clone https://bitbucket.org/sarlrmit/sarl-elvatorsim-ctrl.git```.

### SARL ECLIPSE Controller Development Framework Setup ###

1. Obtain the corresponding [SARL Eclipse Distribution](http://www.sarl.io/download/) that you want to use.
2. Clone the SARL Elevator Sim Control system `git clone https://bitbucket.org/sarlrmit/sarl-elvatorsim-ctrl.git`.
3. In Eclipse, navigate to *File > Import > Existing Maven Projects*.
4. In the *Root Directory* field, browse to the root directory of the sarl-elvatorsim-ctrl repository.
5. Select the ```pom.xml``` (Project Object Model) provided in the *Projects* dialog and click *Finish* (see pom.xml details below)
6. The project should now be imported.
7. Right-click on the project directory in Eclipse and go to *Maven > Update Project*.
8. Click OK.
9. When the project has finished updating, go to *Project > Clean*. Click OK. System should compile.



## EXAMPLE AGENTS ##

### DummyMultiCarController ###

Simple agent controller that continuously makes cars go up and down, without doing anything else. SARL agent **DummyMultiCarController** in package **au.edu.rmit.elevatorsim.sarlctrl.controllers**

This agent uses the **Skill_SingleSimulationInteraction** skill to interact with the elevator simulator.


## RUNNING ##

To run the controllers provided in this package, follow the followin steps.

### 1 - Start Elevator Simulator ###
1. Run the **ElevatorSimulatorApplication** run configuration.
2. Navigate to *File > New*. Choose a simulator scenario from the available list.
3. Choose your simulation parameters and click *Apply*.


### 2 - Start SARL Controller ###

There are two methods to run the SARL Controller - through Eclipse or through the CLI.

#### Running from the SARL Eclipse Distribution ####

You can just run the main agent doing _RUN AS SARL Agent_ on the agent file (right click) or you can create a _SARL APPLICATION_ under _RUN Configurations_. Remember that you may then need to configure that runner to account for other aspects, like parameters or setting of environment variables. This woul de the easiest way. Once you run it for the first time, the run configuration will stay.

Alternatively, the "low-level" Java based configuration is as follows:

1. Go to *Run > Run Configurations* and double-click on **Java Application**.
2. In the *Project* field browse for the project you have just imported into the workspace (defaults to ```sarl-elevatorsim-ctrl```).
3. In the *Main class* field search for ```io.janusproject.Boot```.
4. Under the *Arguments* tab in the *Program arguments* field, type ```au.edu.rmit.elevatorsim.sarlctrl.controllers.DummyMultiCarController```.
5. Click *Apply*!

This needs to be set-up once. After that you can just run it by:

1. Run the **DummyMultiCarController** run configuration we previously setup.
2. Wait for the console to announce ```Launching the agent: au.edu.rmit.elevatorsim.sarlctrl.controllers.DummyMultiCarController```.


#### Compiling and Running from the command line (CLI) ###

The compilation is done via Maven (Maven Getting Started Guide http://tinyurl.com/y994z75j):

1. Make sure ```pom.xml``` is correctly configured with either:
	* In general for sarl.version: 0.x.y one should use janus.version: 2.0.x.y
	* Some versions of SARL may not compile on CLI but will do in ECLIPSE. See [this post](https://github.com/spring-projects/aws-maven/issues/25)
2. Compile with either:
	* ```mvn compile``` (default pom) or ```mvn compile -f <pom file>``` to compile the application. Compiled classes will be placed in ```target/classes```
	* To do a clean compile: ```mvn clean compile```
	* ```mvn package``` to generate the compile target and JAR file in current director under ```target/```
3. Run with (after starting Elevator simulator, of course, which will be waiting for client controller connection):
	* To run using Maven (which will take care of all dependencies needed for the SARL application):

		```
		mvn exec:java -Dexec.mainClass="io.janusproject.Boot" -Dexec.args=au.edu.rmit.elevatorsim.sarlctrl.controllers.DummyMultiCarController
		```
		
	* To run using plain Java we need to include the Janus Kernel JAR file containing the Janus Project execution engine ([available here](http://maven.sarl.io/io/janusproject/io.janusproject.kernel/)):
		
		```
		java -cp /path/to/sarl-elvatorsim-ctrl-<version>.jar:/path/to/io.janusproject.kernel-<version>-with-dependencies.jar io.janusproject.Boot au.edu.rmit.elevatorsim.sarlctrl.controllers.DummyMultiCarController
		```

### 3 - Start the Simulation ###
Click *Go, Dude!* in the Elevator Simulator and enjoy! 

You should start seeing the Elevator GUI interface moving the cars and issuing requests and the SARL-based agent acting and printing information.


## USING SARL CONNECTIVITY (CAPACITIES, SKILLS, AND EVENTS) ##

You can use this project off-the-shelf to make use of the SARL connectivity infrastructure, that is, the capacity and skill to connect SARL agents to the elevator simulator server. You can do that by suitably configuring your project's POM file `pom.xml` and then using the corresponding skills for the capacities defined:

### Configuring the POM file ##

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
			<groupId>org.bitbucket.sarlrmit</groupId>
			<artifactId>sarl-elvatorsim-ctrl</artifactId>
			<version>${sarl-elvatorsim-ctrl.version}</version>
			</dependency>
		</dependencies>

### Capacities and Skills provided ##

### Capacities ###

1. ***Cap_Reporting***: report various meaningful events happening in the system, such as arriving to a floor, a person leaving or entering a car, car passing by a floor while traveling, person requesting service at a floor, etc.
2. **Cap_SimulatorInteraction**: allows for actions to be submitted to the Elevator Simulator. Currently only supports two actions:
	* Sending a car to a floor via method `sendCar(action : SendCarAction)`.
	* Establishing the connection to the server: `act_connectToSimulator()`

### Skills ###

#### Skill_SingleSimulatorInteraction ####

This is the basic core skill implementing the **Cap_SimulatorInteractoin** capacity and allows SARL agents to receive information from the elevator simulator and send back actions to perform in the simulator:

* When events arise in the simulator as a JSON message, corresponding SARL events are emitted:
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

 All these event carry the id of the event and the timestamp, together with additional information relevant to the event.
SARL agents can just handle these events via SARL behaviors.

* Skill implements the capacity action `sendCar(action : SendCarAction)` and the connection action `act_connectToSimulator()` 

Internally, the skill extends the _abstract_ skill Java class **ClientElevatorSimulator** which acts a middle layer between the elevator simulator and the SARL framework. Such class does the low-level _translation of JSON_ to objects and _call-backs_ and has a **NetworkConnection**  handler to the network connection.

* It actually connects and maintains the network connection to the simulator via an object of class **NetworkConnection**. 
* It continuously receives JSON messages from simulator server, decodes them, and calls corresponding abstract methods. This methods are implemented in a fully instantiated skill, for example by emitting a corresponding SARL event (like **Skill_SingleSimulatorInteraction** concrete skill does).
* Provides a **sendCar** method to send a SARL `sendCar` action to the server via a JSON message.

In most cases, users developing SARL controllers will not need to bother with the **ClientElevatorSimulator** infrastructure. All they need to do is to handle the above SARL events and send cars to destinations by issuing a `sendCar(action: SendCarAction)` action, for example:

		reportTravellingTo(car, floor)
		sendCar(new SendCarAction(car, floor, direction)) // this is the actual MOVEMENT action!
		moving = true
		
To establish the connection, the agents adopts the skill and then issue the connection action:

		setSkill(new Skill_SingleSimulatorInteraction(simulator_host, simulator_port, defaultSpace), Cap_SimulatorInteraction)
		act_connectToSimulator()
		reportMessage("Successfully connected to elevator hardware at " + simulator_host + ":" + simulator_port);


#### Skill_MultipleSimulatorInteraction ####

Further extends skill **Skill_SingleSimulatorInteraction** to share a connection to the elevator simulator amongst multiple SARL agents (corresponding to multiple cars).

In a multi-agent SAR application, each agent register to control one car. There will be only _one_ connection to the simulator via a static singleton object `sharedConnection` of class `NetworkConnection`. The first time the skill is created, the connection will be established and `sharedConnection` will be instantiated. After that, it will be re-used.

Each agent should then connect as follows:

		setSkill(new Skill_MultipleSimulatorInteraction(simulator_host, simulator_port, #[carID], defaultSpace), Cap_SimulatorInteraction)
		act_connectToSimulator()

The third argument is a list of car ids that the agent is allowed to control. When a **sendCar** action is issued by an agent, it is only allowed (and corresponding message sent to simulator) if agent is controlling the corresponding car.

**NOTE:** While the network connection will be one, each agent will have its own **Skill_MultipleSimulatorInteraction** skill, which in term is a **Skill_SingleSimulatorInteraction**. So when a JSON message arrives, any of those skills using the same `NetworkConnection` handler could read it and act upon. This is a bit non-robust as one never knows which skill/agent will read a message from the simulator server.


#### Skill_ConsoleReporting ####

Implements **Cap_Reporting** capacity: do all the report to the screen console.


### Events ###

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



## TROUBLESHOOTING ##

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

* Matthew McNally (Project Lead and SARL Agent Developer) 
* Joshua Richards (Java Elevator Sim Server developer)
* Joshua Beale (SARL Agent Developer)
* Dylan Rock (Documentation)
* Sebastian Sardina (Project Supervisor)


## LICENSE ##
This project is using the GPLv3 for open source licensing for information and the license visit GNU website (https://www.gnu.org/licenses/gpl-3.0.en.html).