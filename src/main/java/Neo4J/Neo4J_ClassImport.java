package Neo4J;
import org.neo4j.dbms.api.DatabaseManagementService;
import org.neo4j.dbms.api.DatabaseManagementServiceBuilder;
import org.neo4j.graphdb.*;
import org.neo4j.graphdb.schema.IndexDefinition;
import org.neo4j.graphdb.schema.Schema;
import org.openjdk.jmh.annotations.*;
import types.RelTypes;
import Class.Citoyen;

import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Map;

import static org.neo4j.configuration.GraphDatabaseSettings.DEFAULT_DATABASE_NAME;

@State(Scope.Benchmark)
public class Neo4J_ClassImport {

    private static GraphDatabaseService graphDb;
    private static DatabaseManagementService managementService;
    private static int id;

    public Neo4J_ClassImport () {
        this.id = 1;
    }

    public int getId() {
        return id;
    }

    public static void main(String[] args) {

        Neo4J_ClassImport neo4J_ClassImport = new Neo4J_ClassImport();

        neo4J_ClassImport.connectionGraph();

        /*try (Transaction tx = graphDb.beginTx()) {
            neo4J_ClassImport.createIndexGraph(tx);
            tx.commit();
        }*/

        try (Transaction tx = graphDb.beginTx()) {



            //neo4J_ClassImport.deleteNodeCypher(tx);

            Citoyen cit = new Citoyen("Doe",
                                    "John",
                                        'M',
                                        25,
                                            LocalDate.of(1996, 7, 18),
                            "Polyland",
                                    1.85f,
                                    "42 rue de la Vie",
                                42000, "Polyland",
                                        "Necrolimbe",
                                    "Elden Ring" );

            Citoyen cit2 = new Citoyen("Fouchard",
                    "Jean-Heude",
                    'F',
                    94,
                    LocalDate.of(1938, 11, 11),
                    "Montcuq",
                    1.1f,
                    "1 ter rue de la Jonquille",
                    54875, "Montcuq",
                    "Loire-Atlantique",
                    "Bretagne" );

            Citoyen cit3 = new Citoyen("Fouchard",
                    "Jean-Heude",
                    'F',
                    94,
                    LocalDate.of(1938, 11, 11),
                    "Montcuq",
                    1.1f,
                    "1 ter rue de la Jonquille",
                    54875, "Montcuq",
                    "Loire-Atlantique",
                    "Bretagne" );

            cit.ajouterAmis(cit2);
            cit2.ajouterAmis(cit3);

            //System.out.println(neo4J_ClassImport.getId());
            Node citoyen = neo4J_ClassImport.createNodeCitoyen(tx, cit);
            //System.out.println(neo4J_ClassImport.getId());
            Node citoyen2 = neo4J_ClassImport.createNodeCitoyen(tx, cit2);

            Node citoyen3 = neo4J_ClassImport.createNodeCitoyen(tx, cit3);

            //System.out.println("id1 : " + neo4J_ClassImport.id + cit2.getUid());

            neo4J_ClassImport.createRelationshipCitoyen(tx, cit);
            neo4J_ClassImport.createRelationshipCitoyen(tx, cit2);

            //tx.execute( "MATCH (n:Citoyen {uid: 13451})-[r:KNOWS]-(m:Citoyen {uid: 12862}) DELETE r" );

            System.out.println(neo4J_ClassImport.displayRelationshipCitoyenCypher(tx, RelTypes.KNOWS));
            System.out.println(neo4J_ClassImport.displayNodeCitoyenIndex(tx));

            neo4J_ClassImport.shutdownGraph();

        }
    }

    //***************************************************************************************************
    //Server graph
    
    public void connectionGraph() {
        Path databaseDirectory = Path.of("/Users/Artorias/Documents/JetBrains/LIB/neo4j-community-4.4.3-windows");
        managementService = new DatabaseManagementServiceBuilder( databaseDirectory ).build();
        graphDb = managementService.database( DEFAULT_DATABASE_NAME );
        registerShutdownHook( managementService );
    }

    private void shutdownGraph() {
        managementService.shutdown();
    }

    private void registerShutdownHook( final DatabaseManagementService managementService )
    {
        // Registers a shutdown hook for the Neo4j instance so that it
        // shuts down nicely when the VM exits (even if you "Ctrl-C" the
        // running application).
        Runtime.getRuntime().addShutdownHook( new Thread()
        {
            @Override
            public void run()
            {
                managementService.shutdown();
            }
        } );
    }

    //***************************************************************************************************
    //Node & Realtionship

    public Node createNodesLabel(Transaction tx, String message, Object property, Label label) {
            Node node = tx.createNode(label);
            node.setProperty(message, property);

            return node;
    }

    public Node createNodesNoLabel(Transaction tx, String message, Object property) {
        Node node = tx.createNode();
        node.setProperty(message, property);

        return node;
    }

    public void createRelationShip(Node firstNode, Node secondNode, String message, Object property, RelTypes type) {
        Relationship relationship = firstNode.createRelationshipTo(secondNode, type);
        relationship.setProperty(message, property);

    }

    public Node createNodeCitoyen(Transaction tx, Citoyen citoyen) {



        Node nodeCitoyen = this.createNodesLabel(tx, "uid", id, Label.label("Citoyen"));

        citoyen.setUidNode(nodeCitoyen.getId());
        citoyen.setUid(id);

        //System.out.println("id2 : " + this.id++ + nodeCitoyen.getId());

        Node nodeLastNameCitoyen = this.createNodesNoLabel(tx, "id", citoyen.getNom());
        this.createRelationShip(nodeCitoyen, nodeLastNameCitoyen, "lastname", "hisLastName", RelTypes.HAS_LASTNAME);

        Node nodeFirstNameCitoyen = this.createNodesNoLabel(tx, "id", citoyen.getPrenom());
        this.createRelationShip(nodeCitoyen, nodeFirstNameCitoyen, "firstname", "hisFirstName", RelTypes.HAS_FIRSTNAME);

        Node nodeSexCitoyen = this.createNodesNoLabel(tx, "id", citoyen.getSexe());
        this.createRelationShip(nodeCitoyen, nodeSexCitoyen, "sexe", "hisSexe", RelTypes.SEX);

        Node nodeAgeCitoyen = this.createNodesNoLabel(tx, "id", citoyen.getAge());
        this.createRelationShip(nodeCitoyen, nodeAgeCitoyen, "age", "hisAge", RelTypes.AGE);

        Node nodeBirthDayCitoyen = this.createNodesNoLabel(tx, "id", citoyen.getDateDeNaissance());
        this.createRelationShip(nodeCitoyen, nodeBirthDayCitoyen, "birthDay", "hisBirthDay", RelTypes.BIRTH_DAY);

        Node nodeBirthCityCitoyen = this.createNodesNoLabel(tx, "id", citoyen.getVilleDeNaissance());
        this.createRelationShip(nodeCitoyen, nodeBirthCityCitoyen, "birthCity", "hisBirthCity", RelTypes.BIRTH_CITY);

        Node nodeSizeCitoyen = this.createNodesNoLabel(tx, "id", citoyen.getTaille());
        this.createRelationShip(nodeCitoyen, nodeSizeCitoyen, "size", "hisSize", RelTypes.SIZE);

        Node nodeAddressCitoyen = this.createNodesNoLabel(tx, "id", citoyen.getAdresse());
        this.createRelationShip(nodeCitoyen, nodeAddressCitoyen, "address", "hisAdress", RelTypes.ADDRESS);

        Node nodePostalCodeCitoyen = this.createNodesNoLabel(tx, "id", citoyen.getCodePostale());
        this.createRelationShip(nodeCitoyen, nodePostalCodeCitoyen, "postalCode", "hisPostalCode", RelTypes.POSTAL_CODE);

        Node nodeCityCitoyen = this.createNodesNoLabel(tx, "id", citoyen.getVille());
        this.createRelationShip(nodeCitoyen, nodeCityCitoyen, "city", "hisCity", RelTypes.CITY);

        Node nodeDepartmentCitoyen = this.createNodesNoLabel(tx, "id", citoyen.getDepartement());
        this.createRelationShip(nodeCitoyen, nodeDepartmentCitoyen, "departement", "hisDepartement", RelTypes.DEPARTMENT);

        Node nodeRegionCitoyen = this.createNodesNoLabel(tx, "id", citoyen.getRegion());
        this.createRelationShip(nodeCitoyen, nodeRegionCitoyen, "region", "hisRegion", RelTypes.REGION);

        id++;

        return nodeCitoyen;
    }

    public void createRelationshipBetweenTwoNodeCitoyen(Node c1, Node c2) {
        this.createRelationShip(c1, c2, "knows", "knows", RelTypes.KNOWS);
    }

    public void createRelationshipCitoyen(Transaction tx, Citoyen citoyen) {
        Node citoyenNode = tx.getNodeById(citoyen.getUidNode());

        for (Citoyen c : citoyen.getAmis()) {
            //System.out.println("id3 : " + c.getUid());
            Node ci = tx.getNodeById(c.getUidNode());
            //System.out.println(this.displayRelationshipCitoyen(ci));

            this.createRelationshipBetweenTwoNodeCitoyen(citoyenNode, ci);
        }
    }

    //***************************************************************************************************
    //Index

    public void createIndexGraph(Transaction tx) {
        IndexDefinition usernamesIndex;
        Schema schema = tx.schema();

        usernamesIndex = schema.indexFor(Label.label("Citoyen"))
                .on("uid")
                .create();
    }



    //***************************************************************************************************
    //Finder

    public Result findNodeCitoyenCypherById(Transaction tx, int id) {
        return tx.execute( "MATCH (n {uid: "+ id + "}) RETURN n, n.uid" );
    }

    public Result findRelationshipCitoyenCypher(Transaction tx, RelTypes type) {
        return tx.execute( "MATCH (n:Citoyen)-[:" + type + "]-(m:Citoyen) RETURN n, n.uid, m, m.uid" );
    }

    public ResourceIterator<Node> findNodeCitoyenIndex(Transaction tx, Label label) {
        return tx.findNodes(label);
    }

    //***************************************************************************************************
    //Display

    public String displayAllNodeCypher(Transaction tx) {
        StringBuilder output = new StringBuilder();

        for (int i = 1; i <= id ; ++i) {
            output.append(this.displayNodeCitoyenCypherById(tx, i)).append("\n");
        }

        return output.toString();
    }

    public String displayNodeCitoyen(Node citoyen) {

        StringBuilder str = new StringBuilder();

        for (Relationship relation: citoyen.getRelationships()) {
            str.append(relation.getEndNode().getProperty("id")).append("\n");
        }

        return str.toString();
    }

    public String displayRelationshipNodeCitoyen(Node citoyen) {
        StringBuilder str = new StringBuilder();

        str.append(citoyen.getProperty("uid")).append("\n");

        for (Relationship relationship : citoyen.getRelationships(RelTypes.KNOWS)) {

            str.append(relationship.getEndNode().getProperty("uid")).append("\n");
        }

        return str.toString();
    }

    public String displayNodeCitoyenCypherById(Transaction tx, int id) {
        StringBuilder rows = new StringBuilder();

        Result result = this.findNodeCitoyenCypherById(tx, id);

        return displayResult(rows, result);
    }

    public String displayRelationshipCitoyenCypher(Transaction tx, RelTypes type) {
        StringBuilder rows = new StringBuilder();

        Result result = this.findRelationshipCitoyenCypher(tx, type);

        return displayResult(rows, result);
    }

    public String displayResult(StringBuilder rows, Result result) {
        while ( result.hasNext() )
        {
            //System.out.println("test");
            Map<String,Object> row = result.next();

            for ( Map.Entry<String,Object> column : row.entrySet() )
            {
                rows.append(column.getKey()).append(": ").append(column.getValue()).append("; ");
            }
            rows.append("\n");
        }

        return rows.toString();
    }

    public String displayNodeCitoyenIndex(Transaction tx) {
        ResourceIterator<Node> citoyens = this.findNodeCitoyenIndex(tx, Label.label("Citoyen"));

        StringBuilder str = new StringBuilder();

        for (ResourceIterator<Node> it = citoyens; it.hasNext(); ) {
            Node n = it.next();

            str.append(this.displayRelationshipNodeCitoyen(n)).append("\n\n");
        }

        return str.toString();
    }

    //***************************************************************************************************
    //Remove

    public void removeNodes(Transaction tx, Node firstNode) {
        firstNode = tx.getNodeById(firstNode.getId());
        firstNode.delete();
    }

    public void removeRelationShip(Node firstNode, RelTypes relTypes) {
        firstNode.getSingleRelationship(relTypes, Direction.OUTGOING).delete();
    }

    //***************************************************************************************************
    //Benchmark

    @Setup(Level.Trial)
    public void initBenchmarkCypher() {

        this.connectionGraph();

        try (Transaction tx = graphDb.beginTx()) {

            Citoyen cit = new Citoyen("Doe",
                    "John",
                    'M',
                    25,
                    LocalDate.of(1996, 7, 18),
                    "Polyland",
                    1.85f,
                    "42 rue de la Vie",
                    42000, "Polyland",
                    "Necrolimbe",
                    "Elden Ring");

            Citoyen cit2 = new Citoyen("Fouchard",
                    "Jean-Heude",
                    'F',
                    94,
                    LocalDate.of(1938, 11, 11),
                    "Montcuq",
                    1.1f,
                    "1 ter rue de la Jonquille",
                    54875, "Montcuq",
                    "Loire-Atlantique",
                    "Bretagne");

            Citoyen cit3 = new Citoyen("Fouchard",
                    "Jean-Heude",
                    'F',
                    94,
                    LocalDate.of(1938, 11, 11),
                    "Montcuq",
                    1.1f,
                    "1 ter rue de la Jonquille",
                    54875, "Montcuq",
                    "Loire-Atlantique",
                    "Bretagne");

            cit.ajouterAmis(cit2);
            cit2.ajouterAmis(cit3);

            Node citoyen = this.createNodeCitoyen(tx, cit);
            Node citoyen2 = this.createNodeCitoyen(tx, cit2);
            Node citoyen3 = this.createNodeCitoyen(tx, cit3);

            this.createRelationshipCitoyen(tx, cit);
            this.createRelationshipCitoyen(tx, cit2);

            tx.commit();
        }
    }

    @Benchmark
    public void benchmarkFinderCypher() {
        try (Transaction tx = graphDb.beginTx()) {
            this.findRelationshipCitoyenCypher(tx, RelTypes.KNOWS);

            tx.commit();
        }
    }

    @Benchmark
    public void benchmarkFinderIndex() {

        /*try (Transaction tx = graphDb.beginTx()) {
            this.createIndexGraph(tx);
            tx.commit();
        }*/

        try (Transaction tx = graphDb.beginTx()) {
            this.findNodeCitoyenIndex(tx, Label.label("Citoyen"));
            tx.commit();
        }

    }

    @TearDown(Level.Trial)
    public void finalBenchmarkCypher() {
        this.shutdownGraph();
    }
}

