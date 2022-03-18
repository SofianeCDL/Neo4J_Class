package types;

import org.neo4j.graphdb.RelationshipType;

public enum RelTypes implements RelationshipType {
    KNOWS,

    HAS_FIRSTNAME,
    HAS_LASTNAME,

    SEX,

    AGE,
    BIRTH_DAY,
    BIRTH_CITY,

    SIZE,

    ADDRESS,
    POSTAL_CODE,
    CITY,
    DEPARTMENT,
    REGION
}
