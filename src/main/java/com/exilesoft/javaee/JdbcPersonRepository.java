package com.exilesoft.javaee;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

public class JdbcPersonRepository implements PersonRepository {

    private DataSource dataSource;

    public JdbcPersonRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void savePerson(Person person) {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement stmt = connection.prepareStatement("insert into people (full_name) values (?)")) {
                stmt.setString(1, person.getFullName());
                stmt.execute();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Person> findPeople(String nameQuery) {
        try (Connection conn = dataSource.getConnection()) {
            return nameQuery == null || nameQuery.equals("") ?
                    findAllPeople(conn) : findPeopleByQuery(conn, nameQuery);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private List<Person> findPeopleByQuery(Connection conn, String nameQuery) throws SQLException {
        try (PreparedStatement stmt = conn.prepareStatement("select * from people where upper(full_name) like ?")) {
            stmt.setString(1, "%" + nameQuery.toUpperCase() + "%");
            try (ResultSet rs = stmt.executeQuery()) {
                List<Person> result = new ArrayList<>();
                while (rs.next()) {
                    result.add(mapPerson(rs));
                }
                return result;
            }
        }
    }

    private List<Person> findAllPeople(Connection conn) throws SQLException {
        try (ResultSet rs = conn.createStatement().executeQuery("select * from people")) {
            List<Person> result = new ArrayList<>();
            while (rs.next()) {
                result.add(mapPerson(rs));
            }
            return result;
        }
    }

    private Person mapPerson(ResultSet rs) throws SQLException {
        return Person.withName(rs.getString("full_name"));
    }

    public static void createDatabaseSchema(DataSource dataSource) {
        try (Connection conn = dataSource.getConnection()) {
            conn.createStatement()
                    .executeUpdate("create table people (full_name varchar(255))");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
