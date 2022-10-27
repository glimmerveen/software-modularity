# Software Modularity 

This is a _dummy project_ used to experiment with defining, building and running OSGi modules.

## Prerequisites

* JDK 17
* Maven 3.8.6 (/mvnd 0.8.2)
* Git

By running `mvn validate` you can check if the pre-conditions are met.

**_NOTE:_** For any `mvn` command listed here, you could also use `mvnd`.

## Building it

Run `mvn package` to build (parts of) the project.

## Run it

The module `deployment` builds an executable JAR that combines the modules from this project with additional elements 
(OSGi framework, common services).

```shell
$ cd deployment
$ java -jar target/deployment.jar
```

## Change it

The executable JAR is composed using a `.bndrun` file located inside the `deployment` module. This file
contains _instructions_ for the `bnd-export-maven-plugin`. 

The instructions form a quite rich syntax, but for the purposes of this project, only look at `-runrequires.*`. This is 
used in the _resolve_ process to determine which _bundles_ need to be packaged. The maven plugin will query the
_dependencies_ of the `deployment` module to find _resources_ that match the _requirements_ that are either
explicitly declared through the _runrequires_ or indirectly for transitive dependencies.

Below an example of how the Gogo shell is added to the deployment. 

```properties
###########################################
# Gogo shell (commandline prompt)
###########################################
-runrequires.gogo: \
    bnd.identity; id=org.apache.felix.gogo.command,\
    bnd.identity; id=org.apache.felix.gogo.runtime,\
    bnd.identity; id=org.apache.felix.gogo.jline
```

Note that bundles are referred to by their `Bundle-SymbolicName` (also see `osgi.identity` capability). For the modules 
in this project, this is derived from Maven's `${project.groupId}.${project.artifactId}`.

## Inspect it

The Gogo shell provides various commands to inspect what is active in the deployment.

| Command               | Description                                                                         |
|-----------------------|-------------------------------------------------------------------------------------|
| `help`                | List all available commands                                                         |
| `help <command>`      | Obtain information on a specific command                                            |
| `lb`                  | Listing of the installed bundles                                                    |
| `lb <pattern>`        | Listing of the installed bundles, matching the provided pattern (ie name)           |
| `stop <bundle id>`    | Stops the associated bundle, which invalidates all services realized by that bundle |
| `start <bundle id>`   | Starts the associated bundle, which starts the bundle's activator                   |                                                                               
 | `list`                | List all Declarative Services components                                            |
| `list <bundle id>`    | List all Declarative Services components from a specific bundle                     |
 | `info <component id>` | List the details of a specific component                                            |

See the enRoute's FAQ page on the Gogo shell for more options.

## Links

* [bnd: documentation](https://bnd.bndtools.org/releases/6.3.0/)
* [enRoute: Gogo Shell examples](https://enroute.osgi.org/FAQ/500-gogo.html)