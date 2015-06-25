# Graph OGM Examples

This project contains various OGM examples for different graph databases.

Blueprint based:

* [Ferma](https://github.com/Syncleus/Ferma) -> **/ferma**
* [Totorom](https://github.com/BrynCooke/totorom) -> **/totorom**
* [Frames](https://github.com/tinkerpop/frames) -> **/frames**

I added examples on how to use Ferma in conjunction with [TitanDB](https://github.com/Jotschi/graph-ogm-examples/tree/master/ferma/titandb), [OrientDB](https://github.com/Jotschi/graph-ogm-examples/tree/master/ferma/orientdb) and [Neo4j](https://github.com/Jotschi/graph-ogm-examples/tree/master/ferma/neo4j). All other examples use Neo4j.

Neo4j specific:

* [Spring Data Neo4j 3.4.x](http://docs.spring.io/spring-data/neo4j/docs/3.4.0.M1/) using Neo4j 2.2.x -> **/spring-data-neo4j-3.x**
* [Spring Data Neo4j 4.x](http://docs.spring.io/spring-data/neo4j/docs/4.0.0.M1/reference/html/) using Neo4j 2.2.x  ->  **/spring-data-neo4j-4.x**

*Note that SDN 4 is still in development.*

I wrote [a blogpost](http://jotschi.de/2015/06/10/graphdb-ogm-comparison.html) that highlights differences inbetween these OGMs.

#### TLDR;

My personal favourite is currently ferma since it intefaces nicely with the gremlin traversal api and uses the tinkerpop stack. SDN 3.x is also very nice when you know that you won't change the graph database for you project.


