package Neo4J;
import org.neo4j.dbms.api.DatabaseManagementService;
import org.neo4j.dbms.api.DatabaseManagementServiceBuilder;
import org.neo4j.graphdb.*;
import types.RelTypes;
import Class.Citoyen;

import java.nio.file.Path;
import java.time.LocalDate;

import static org.neo4j.configuration.GraphDatabaseSettings.DEFAULT_DATABASE_NAME;

public class Neo4J_ClassImport {

    private static GraphDatabaseService graphDb;
    private static DatabaseManagementService managementService;



    public static void main(String[] args) {

        Neo4J_ClassImport neo4J_ClassImport = new Neo4J_ClassImport();

        neo4J_ClassImport.connectionGraph();

        try (Transaction tx = graphDb.beginTx()) {

            Citoyen cit = new Citoyen("kijehfi",
                                    "kdfsjn",
                                        'M',
                                        65,
                                            LocalDate.of(1857, 7, 18),
                            "Nantes",
                                    1.75f,
                                    "1 bis chemin des vignes du bourg",
                                44100, "Nantes",
                                        "Loire-Atltantique",
                                    "Pays de la Loire" );

            Node citoyen = neo4J_ClassImport.createNodeCitoyen(tx, cit);

            System.out.println(neo4J_ClassImport.displayNodeCitoyen(citoyen));

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

        Node nodeCitoyen = this.createNodes(tx, "id", 1);

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

        return nodeCitoyen;
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

