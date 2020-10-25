package es.bsc.dataclay.logic.accountmgr;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.commons.dbcp2.BasicDataSource;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

public class AccountManagerDBTest {

    @Mock
    private PreparedStatement preparedStatement;

    @Mock
    private BasicDataSource dataSource;

    @Mock
    private Connection connection;

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Before
    public void setUp() throws Exception {
        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void createTables() throws SQLException {
        AccountManagerDB db = new AccountManagerDB(dataSource);
        db.createTables();
    }

    @Test
    public void dropTables() {
        AccountManagerDB db = new AccountManagerDB(dataSource);
        db.dropTables();
    }

    @Test
    public void store() {
    }

    @Test
    public void testStore() {
    }

    @Test
    public void getByID() {
    }

    @Test
    public void getByName() {
    }

    @Test
    public void existsAccountByName() {
    }

    @Test
    public void existsAccountByID() {
    }

    @Test
    public void getAllNormalAccounts() {
    }

    @Test
    public void close() {
    }
}