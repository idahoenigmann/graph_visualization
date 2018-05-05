//Ida Hönigmann 3AHIF
package main;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.LinkedList;
import java.util.List;

public class Database {

    //creates a new database (new file)
    public Database(String url) {
        this.url = url;

        File f = new File(url);

        if (f.exists()) {
            try {
                f.mkdirs();
                f.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        //create table relationship
        LinkedList<String> relationship_attributes = new LinkedList<>();
        relationship_attributes.add("id_entity1");
        relationship_attributes.add("id_entity2");
        relationship_attributes.add("description");
        this.create_table("relationship", relationship_attributes);
    }

    //opens connection to the database
    private Connection connect() {
        Connection c = null;
        try {
            Class.forName("org.sqlite.JDBC");       //make sure jdbc exists and can be found
            c = DriverManager.getConnection("jdbc:sqlite:" + url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return c;
    }

    //if the statement returns a resultSet the parameter returns_rs should be true otherwise false
    private ResultSet execute_statement(String sql, boolean returns_rs) {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            connection = this.connect();    //connect to database
            statement = connection.createStatement();
            if (returns_rs) {
                resultSet = statement.executeQuery(sql);    //calculate resultSet
            } else {
                statement.execute(sql);         //execute statement
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (!returns_rs) {
                try {
                    this.disconnect(null, statement, connection);      //disconnect from database
                } catch (SQLException ignored) {

                }
            }
        }
        return resultSet;
    }

    //closes connection to the database
    private void disconnect(ResultSet resultSet, Statement statement, Connection connection) throws SQLException{
        if (resultSet != null) {
            resultSet.close();      //disconnect from resultSet
        }

        if (statement != null) {
            statement.close();      //disconnect from statement
        }

        if (connection != null) {
            connection.close();     //disconnect from database
        }
    }

    //creates a table (without a content)
    public void create_table(String table_name, List<String> attributes) {
        //throw error if table already exists
        if (get_table_names().contains(table_name) && !table_name.equals("relationship")) {
            throw new RuntimeException("Table with this name already exists.");
        }
        //throw error if no name is given
        if (table_name.isEmpty()) {
            throw new RuntimeException("Table name can not be empty.");
        }
        //throw error if no attributes are given
        if (attributes.isEmpty()) {
            throw new RuntimeException("Table needs at least one attribute.");
        }

        StringBuilder stringBuilder = new StringBuilder("create table if not exists ");
        stringBuilder.append(table_name);
        //autoincrement handles the correct incrementation of the primary key
        stringBuilder.append(" (id integer primary key autoincrement, ");
        for (String attribute : attributes) {
            stringBuilder.append(attribute);
            stringBuilder.append(" varchar, ");     //values are varchar since the user inputs text
        }
        stringBuilder.delete(stringBuilder.length() - 2, stringBuilder.length());
        stringBuilder.append(")");

        String sql = stringBuilder.toString();

        execute_statement(sql, false);
    }

    //deletes the table (and its content) from the database
    void delete_table(String table_name) {
        //table does not exist
        if (!get_table_names().contains(table_name) && !table_name.equals("relationship")) {
            throw new RuntimeException("Table does not exists and can therefore not be deleted.");
        }

        String sql = "drop table if exists " + table_name;

        execute_statement(sql, false);
    }

    //creates a new data record of the specified entity
    public void create_entity(String table, List<String> attributes) {
        //table does not exist
        if (!get_table_names().contains(table) && !table.equals("relationship")) {
            throw new RuntimeException("Table does not exist.");
        }
        //number of attributes need to be correct
        if (attributes.size() != get_attribute_name(table).size()) {
            throw new RuntimeException("Number of attributes does not correspond with number of attributes in table.");
        }

        StringBuilder stringBuilder = new StringBuilder("insert into ");
        stringBuilder.append(table);

        stringBuilder.append(" (");
        for (String attribute : this.get_attribute_name(table)) {
            stringBuilder.append(attribute);    //all attribute names need to be specified since id is inserted automatically
            stringBuilder.append(", ");
        }
        stringBuilder.delete(stringBuilder.length()-2, stringBuilder.length());
        stringBuilder.append(")");

        stringBuilder.append(" values (");
        for (String attribute : attributes) {
            stringBuilder.append("\"");
            stringBuilder.append(attribute);
            stringBuilder.append("\", ");
        }
        stringBuilder.delete(stringBuilder.length()-2, stringBuilder.length());
        stringBuilder.append(");");

        String sql = stringBuilder.toString();

        execute_statement(sql, false);
    }

    //updates the values of a given entity (specified via the id) to the given ones
    void update_entity(String table, int id, List<String> new_attributes) {
        //table does not exist
        if (!get_table_names().contains(table) && !table.equals("relationship")) {
            throw new RuntimeException("Table does not exist.");
        }
        //invalid id given
        if (!get_valid_ids(table).contains(id)) {
            throw new RuntimeException("Id does not exist in this table.");
        }
        //number of attributes is incorrect
        if (new_attributes.size() != get_attribute_name(table).size()) {
            throw new RuntimeException("Number of attributes does not correspond with number of attributes in table.");
        }

        StringBuilder stringBuilder = new StringBuilder("update ");
        stringBuilder.append(table);

        int i = 0;
        stringBuilder.append(" set ");
        for (String attribute : this.get_attribute_name(table)) {
            stringBuilder.append(attribute);
            stringBuilder.append(" = \"");
            stringBuilder.append(new_attributes.get(i));
            stringBuilder.append("\", ");
            i++;
        }
        stringBuilder.delete(stringBuilder.length()-2, stringBuilder.length());
        stringBuilder.append(" where id = ");
        stringBuilder.append(id);       //update entity with corresponding id
        stringBuilder.append(";");

        String sql = stringBuilder.toString();

        execute_statement(sql, false);
    }

    //deletes the entity with the id from the table
    public void delete_entity(String table, int id) {
        //table does not exist
        if (!get_table_names().contains(table) && !table.equals("relationship")) {
            throw new RuntimeException("Table does not exist.");
        }
        //invalid id
        if (!get_valid_ids(table).contains(id)) {
            throw new RuntimeException("Id does not exist in this table.");
        }

        String sql = "delete from " + table + " where id = " + id + ";";    //delete corresponding entity

        execute_statement(sql, false);
    }

    //returns number of rows of the table
    int get_row_count(String table) {
        //table does not exist
        if (!get_table_names().contains(table) && !table.equals("relationship")) {
            throw new RuntimeException("Table does not exist.");
        }

        //maybe use select count(id) from table instead
        String sql = "select * from " + table;
        int count = 0;

        ResultSet resultSet = execute_statement(sql, true);
        try {
            while (resultSet.next()) {
                count++;        //count number of entities
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }


        try {
            this.disconnect(resultSet, null, null);     //disconnect from database
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }

    //returns a list of all attribute names of the table
    List<String> get_attribute_name(String table) {
        //table does not exist
        if (!get_table_names().contains(table) && !table.equals("relationship")) {
            throw new RuntimeException("Table does not exist.");
        }

        LinkedList<String> res = new LinkedList<>();

        String sql = "select * from " + table;

        ResultSet resultSet = execute_statement(sql, true);     //get all data records
        try {
            ResultSetMetaData rsmd = resultSet.getMetaData();
            int column_cnt = rsmd.getColumnCount();
            for (int i=2; i <= column_cnt; i++) {
                res.add(rsmd.getColumnName(i));     //add column name
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            this.disconnect(resultSet, null, null);     //disconnect from database
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return res;
    }

    //returns a list of all tables
    public List<String> get_table_names() {
        LinkedList<String> res = new LinkedList<>();

        //the table name information is stored in sqlite_master
        ResultSet resultSet = execute_statement("SELECT name FROM sqlite_master WHERE type='table'" +
                "AND name <> 'sqlite_sequence' AND name <> 'relationship'", true);

        try {
            while (resultSet.next()) {
                res.add(resultSet.getString(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return res;
    }

    //returns the values of the attributes of an entity
    public List<String> get_attributes(String table, int id) {
        //table does not exist
        if (!get_table_names().contains(table) && !table.equals("relationship")) {
            throw new RuntimeException("Table does not exist.");
        }
        //id is invalid
        if (!get_valid_ids(table).contains(id)) {
            throw new RuntimeException("Id does not exist in this table.");
        }

        LinkedList<String> res = new LinkedList<>();

        String sql = "select * from " + table + " where id = " + id;

        ResultSet resultSet = execute_statement(sql, true);
        try {
            while (resultSet.next()) {
                for (String name : get_attribute_name(table)) {
                    res.add(resultSet.getString(name));     //store attribute names
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            this.disconnect(resultSet, null, null);     //disconnect from database
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    //deletes the database file
    public void delete_database() {
        //deletes file and by doing so all the tables
        File file = new File(url);
        String path = ".." + File.separator + file.getPath();
        File file_path = new File(path);
        file_path.delete();
    }

    //returns a list of the ids that are currently being stored in the table
    public List<Integer> get_valid_ids(String table) {
        //table does not exist
        if (!get_table_names().contains(table) && !table.equals("relationship")) {
            throw new RuntimeException("Table does not exist.");
        }


        LinkedList<Integer> valid_ids = new LinkedList<>();
        String sql = "select id from " + table;
        ResultSet resultSet = execute_statement(sql, true);     //get all ids from the table

        try {
            while (resultSet.next()) {
                int i = resultSet.getInt("id");     //store all ids
                valid_ids.add(i);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return valid_ids;
    }

    //returns the file name of the database (including .db)
    String get_name() {
        File file = new File(url);
        return file.getName();
    }

    //returns the absolute path of the database
    private String get_url() {
        File file = new File(url);
        return file.getAbsolutePath();
    }

    //create data record of relationship
    void create_relationship(EntityIdentifier id1, EntityIdentifier id2, String description) {
        //a relationship is defined as two entities (table, id) and a description

        //description is empty
        if (description.isEmpty()) {
            throw new RuntimeException("Description can not be empty.");
        }

        LinkedList<String> attributes = new LinkedList<>();
        attributes.add(id1.toString());
        attributes.add(id2.toString());
        attributes.add(description);
        this.create_entity("relationship", attributes);

    }

    //compare two databases
    @Override
    public boolean equals(Object other) {
        //two databases are equal if their url matches
        return other instanceof Database && this.get_url().equals(((Database) other).get_url());
    }

    public static void main(String[] args) {
        //for test purposes
        Database database = new Database("test.db");

        LinkedList<String> attribute_names = new LinkedList<>();
        attribute_names.add("name");
        attribute_names.add("date of birth");
        database.create_table("person", attribute_names);

        attribute_names = new LinkedList<>();
        attribute_names.add("name");
        attribute_names.add("address");
        database.create_table("school", attribute_names);

        LinkedList<String> attributes = new LinkedList<>();
        attributes.add("Ida");
        attributes.add("2001-04-28");
        database.create_entity("person", attributes);

        attributes = new LinkedList<>();
        attributes.add("Manuel");
        attributes.add("2001-08-05");
        database.create_entity("person", attributes);

        database.delete_entity("person", 1);


        database.delete_table("person");
        database.delete_table("school");

    }

    private String url;     //stores the path to the db file

}

