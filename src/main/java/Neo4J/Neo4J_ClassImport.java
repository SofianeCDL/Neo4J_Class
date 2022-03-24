package Neo4J;
import org.neo4j.dbms.api.DatabaseManagementService;
import org.neo4j.dbms.api.DatabaseManagementServiceBuilder;
import org.neo4j.graphdb.*;
import types.RelTypes;
import Class.Citoyen;

import java.nio.file.Path;
import java.time.LocalDate;
import java.util.Map;

import static org.neo4j.configuration.GraphDatabaseSettings.DEFAULT_DATABASE_NAME;

public class Neo4J_ClassImport {

    private static GraphDatabaseService graphDb;
    private static DatabaseManagementService managementService;
    private int id;

    public Neo4J_ClassImport () {
        this.id = 1;
    }

    public int getId() {
        return id;
    }

    public static void main(String[] args) {

        Neo4J_ClassImport neo4J_ClassImport = new Neo4J_ClassImport();

        neo4J_ClassImport.connectionGraph();

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

            cit.ajouterAmis(cit2);

            //System.out.println(neo4J_ClassImport.getId());
            Node citoyen = neo4J_ClassImport.createNodeCitoyen(tx, cit);
            //System.out.println(neo4J_ClassImport.getId());
            Node citoyen2 = neo4J_ClassImport.createNodeCitoyen(tx, cit2);

            //System.out.println("id1 : " + neo4J_ClassImport.id + cit2.getUid());

            neo4J_ClassImport.createRelationshipCitoyen(tx, cit);

            //System.out.println(neo4J_ClassImport.displayNodeCitoyen(citoyen));
            //System.out.println(neo4J_ClassImport.displayAllNode(tx));
            //System.out.println(neo4J_ClassImport.findUserNodeCypher(tx, 1));
            System.out.println(neo4J_ClassImport.displayRelationshipCitoyen(citoyen));


            neo4J_ClassImport.shutdownGraph();

        }
    }

    public void connectionGraph() {
        Path databaseDirectory = Path.of("/Users/Artorias/Documents/JetBrains/LIB/neo4j-community-4.4.3-windows");
        managementService = new DatabaseManagementServiceBuilder( databaseDirectory ).build();
        graphDb = managementService.database( DEFAULT_DATABASE_NAME );
        registerShutdownHook( managementService );
    }

    public Node createNodes(Transaction tx, String message, Object property) {
            Node node = tx.createNode();
            node.setProperty(message, property);

            return node;
    }

    public Relationship createRelationShip(Node firstNode, Node secondNode, String message, Object property, RelTypes type) {
        Relationship relationship = firstNode.createRelationshipTo(secondNode, type);
        relationship.setProperty(message, property);

        return relationship;
    }

    public Node createNodeCitoyen(Transaction tx, Citoyen citoyen) {



        Node nodeCitoyen = this.createNodes(tx, "uid",this.id);

        citoyen.setUid(nodeCitoyen.getId());
        //System.out.println("id2 : " + this.id++ + nodeCitoyen.getId());

        Node nodeLastNameCitoyen = this.createNodes(tx, "id", citoyen.getNom());
        this.createRelationShip(nodeCitoyen, nodeLastNameCitoyen, "lastname", "hisLastName", RelTypes.HAS_LASTNAME);

        Node nodeFirstNameCitoyen = this.createNodes(tx, "id", citoyen.getPrenom());
        this.createRelationShip(nodeCitoyen, nodeFirstNameCitoyen, "firstname", "hisFirstName", RelTypes.HAS_FIRSTNAME);

        Node nodeSexCitoyen = this.createNodes(tx, "id", citoyen.getSexe());
        this.createRelationShip(nodeCitoyen, nodeSexCitoyen, "sexe", "hisSexe", RelTypes.SEX);

        Node nodeAgeCitoyen = this.createNodes(tx, "id", citoyen.getAge());
        this.createRelationShip(nodeCitoyen, nodeAgeCitoyen, "age", "hisAge", RelTypes.AGE);

        Node nodeBirthDayCitoyen = this.createNodes(tx, "id", citoyen.getDateDeNaissance());
        this.createRelationShip(nodeCitoyen, nodeBirthDayCitoyen, "birthDay", "hisBirthDay", RelTypes.BIRTH_DAY);

        Node nodeBirthCityCitoyen = this.createNodes(tx, "id", citoyen.getVilleDeNaissance());
        this.createRelationShip(nodeCitoyen, nodeBirthCityCitoyen, "birthCity", "hisBirthCity", RelTypes.BIRTH_CITY);

        Node nodeSizeCitoyen = this.createNodes(tx, "id", citoyen.getTaille());
        this.createRelationShip(nodeCitoyen, nodeSizeCitoyen, "size", "hisSize", RelTypes.SIZE);

        Node nodeAddressCitoyen = this.createNodes(tx, "id", citoyen.getAdresse());
        this.createRelationShip(nodeCitoyen, nodeAddressCitoyen, "address", "hisAdress", RelTypes.ADDRESS);

        Node nodePostalCodeCitoyen = this.createNodes(tx, "id", citoyen.getCodePostale());
        this.createRelationShip(nodeCitoyen, nodePostalCodeCitoyen, "postalCode", "hisPostalCode", RelTypes.POSTAL_CODE);

        Node nodeCityCitoyen = this.createNodes(tx, "id", citoyen.getVille());
        this.createRelationShip(nodeCitoyen, nodeCityCitoyen, "city", "hisCity", RelTypes.CITY);

        Node nodeDepartmentCitoyen = this.createNodes(tx, "id", citoyen.getDepartement());
        this.createRelationShip(nodeCitoyen, nodeDepartmentCitoyen, "departement", "hisDepartement", RelTypes.DEPARTMENT);

        Node nodeRegionCitoyen = this.createNodes(tx, "id", citoyen.getRegion());
        this.createRelationShip(nodeCitoyen, nodeRegionCitoyen, "region", "hisRegion", RelTypes.REGION);

        this.id++;

        return nodeCitoyen;
    }

    public void createRelationshipBetweenTwoCitoyen(Node c1, Node c2) {
        this.createRelationShip(c1, c2, "knows", "knows", RelTypes.KNOWS);
    }

    public void createRelationshipCitoyen(Transaction tx, Citoyen citoyen) {
        Node citoyenNode = tx.getNodeById(citoyen.getUid());

        for (Citoyen c : citoyen.getAmis()) {
            //System.out.println("id3 : " + c.getUid());
            Node ci = tx.getNodeById(c.getUid());
            //System.out.println(this.displayRelationshipCitoyen(ci));

            this.createRelationshipBetweenTwoCitoyen(citoyenNode, ci);
        }
    }

    public String findUserNodeCypher(Transaction tx, int id) {
        StringBuilder rows = new StringBuilder();

        Result result = tx.execute( "MATCH (n {uid: "+ id + "}) RETURN n, n.uid" );
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

    public String displayAllNode(Transaction tx) {
        StringBuilder output = new StringBuilder();

        for (int i = 1; i <= this.id ; ++i) {
            output.append(this.findUserNodeCypher(tx, i)).append("\n");
        }

        return output.toString();

    }

    /*public Citoyen createCitoyen(Node citoyen) {
        String nom = (String) citoyen.getSingleRelationship(RelTypes.HAS_LASTNAME, Direction.OUTGOING).getEndNode().getProperty("id");
        String prenom = (String) citoyen.getSingleRelationship(RelTypes.HAS_FIRSTNAME, Direction.OUTGOING).getEndNode().getProperty("id");

        char sexe = (char) citoyen.getSingleRelationship(RelTypes.SEX, Direction.OUTGOING).getEndNode().getProperty("id");

        int age = (int) citoyen.getSingleRelationship(RelTypes.AGE, Direction.OUTGOING).getEndNode().getProperty("id");

    }*/

    public String displayNodeCitoyen(Node citoyen) {

        StringBuilder str = new StringBuilder();

        for (Relationship relation: citoyen.getRelationships()) {
            str.append(relation.getEndNode().getProperty("id")).append("\n");
        }

        return str.toString();
    }

    public String displayRelationshipCitoyen(Node citoyen) {
        StringBuilder str = new StringBuilder();

        str.append(this.displayNodeCitoyen(citoyen)).append("\n\n");

        for (Relationship relationship : citoyen.getRelationships(RelTypes.KNOWS)) {

            str.append(this.displayNodeCitoyen(relationship.getEndNode())).append("\n\n");
        }

        return str.toString();
    }

    public void removeNodes(Transaction tx, Node firstNode) {
        firstNode = tx.getNodeById(firstNode.getId());
        firstNode.delete();
    }

    public void removeRelationShip(Node firstNode, RelTypes relTypes) {
        firstNode.getSingleRelationship(relTypes, Direction.OUTGOING).delete();
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
}

