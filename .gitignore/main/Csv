package main;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;


class Csv {
    //reads the content of the database from a csv file and creates a new db-file
    static Database import_db(File file) {
        //create new database in the same directory as the csv-file
        Database database = new Database(file.getPath().replace("csv", "db"));

        //add the new database to the databases
        if (!Database_GUI.databases.contains(database)) {
            Database_GUI.databases.add(database);
        }

        //store all lines from the csv-file
        ArrayList<String> lines = new ArrayList<>();
        try {
            lines = (ArrayList<String>) Files.readAllLines(file.toPath(), Charset.forName("UTF-8"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        String table = "";    //string for storing the tables
        boolean attributes = true;  //true if the next line are attributes, otherwise false
        boolean relationship = false;   //true if the relationship section has been reached, otherwise false

        for (String line : lines) {
            if (line.equals("__relationship__")) {  //relationship section has been reached
                relationship = true;
            } else if (relationship) {  //line specifies a relationship
                EntityIdentifier e1 = new EntityIdentifier(line.split(",")[0]);
                EntityIdentifier e2 = new EntityIdentifier(line.split(",")[1]);
                database.create_relationship(e1, e2, line.split(",")[2]);
            } else if (line.startsWith("__") && line.endsWith("__")) {  //line specifies a table name
                table = line.substring(2, line.length() - 2);
                attributes = true;
            } else if (attributes) {    //line specifies the attributes of the table
                database.create_table(table, Arrays.asList(line.split(",")));
                attributes = false;
            } else {    //line specifies an entity
                attributes = false;
                database.create_entity(table, Arrays.asList(line.split(",")));
            }
        }
        return database;
    }

    //stores the content of a database in a new csv-file
    static LinkedList<String> export_db(Database database) {
        LinkedList<String> lines = new LinkedList<>();

        for (String table : database.get_table_names()) {   //go through all tables
            lines.add("__" + table + "__");
            StringBuilder stringBuilder = new StringBuilder();
            for (String attribute_name : database.get_attribute_name(table)) {  //go through all attributes of the table
                stringBuilder.append(attribute_name);
                stringBuilder.append(",");
            }
            stringBuilder.deleteCharAt(stringBuilder.length()-1);
            lines.add(stringBuilder.toString());

            for (int i : database.get_valid_ids(table)) {   //go through all entities via their id
                stringBuilder = new StringBuilder();
                for (String s : database.get_attributes(table, i)) {
                    stringBuilder.append(s);
                    stringBuilder.append(",");
                }
                stringBuilder.deleteCharAt(stringBuilder.length()-1);
                lines.add(stringBuilder.toString());
            }
        }

        //go to the relationship section
        lines.add("__relationship__");
        StringBuilder stringBuilder;
        for (int i : database.get_valid_ids("relationship")) {  //go through all relationships
            stringBuilder = new StringBuilder();
            for (String s : database.get_attributes("relationship", i)) {
                stringBuilder.append(s);
                stringBuilder.append(",");
            }
            stringBuilder.deleteCharAt(stringBuilder.length()-1);
            lines.add(stringBuilder.toString());
        }

        return lines;
    }
}
