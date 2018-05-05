package main;

public class RelationshipIdentifier {

    RelationshipIdentifier(EntityIdentifier entity1, EntityIdentifier entity2, String description, int id) {
        this.entity1 = entity1;
        this.entity2 = entity2;
        this.description = description;
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public int getId() {
        return id;
    }

    public String toString() {
        return entity1.display_entity() + "\n" + description + "\n" + entity2.display_entity() + "\n";
    }

    private EntityIdentifier entity1;
    private EntityIdentifier entity2;
    private String description;
    private int id = 0;
}
