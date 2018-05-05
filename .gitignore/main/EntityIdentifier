package main;


//makes identification of entities easier
public class EntityIdentifier {

    //an entity can be identified by a table and a id
    public EntityIdentifier(String table, int id) {
        //maybe test if the id is valid
        this.table = table;
        this.id = id;
    }

    //return string representation; primarily used for storing entities in the relationship table
    @Override
    public String toString() {
        return table + ":" + id.toString();
    }

    //returns string representation which is readable by humans (values of attributes); used by delete relationship
    String display_entity() {
        StringBuilder stringBuilder = new StringBuilder();
        for (String s : Database_GUI.active_database.get_attributes(table, id)) {
            stringBuilder.append(s);
            stringBuilder.append(", ");
        }
        stringBuilder.delete(stringBuilder.length()-2, stringBuilder.length()-1);
        return stringBuilder.toString();
    }

    //creates new entity identifier; used when reading from the relationship table
    public EntityIdentifier(String id) {
        String[] splitted_id = id.split(":");
        this.table = splitted_id[0];
        this.id = Integer.parseInt(splitted_id[1]);
    }

    //getter for table value aka specifies entity-type
    public String getTable() {
        return table;
    }

    //getter for id value aka specifies unique number of this entity in the table
    public Integer getId() {
        return id;
    }

    //compares two entities with one another; equal if table and id are equal
    public boolean equals(Object other) {
        if (other instanceof EntityIdentifier) {
            EntityIdentifier other_ei = (EntityIdentifier) other;
            return this.table.equals(other_ei.table) && this.id.equals(other_ei.id);
        }
        return false;
    }

    private String table;   //specifies entity type
    private Integer id;     //specifies unique number in the table
}
