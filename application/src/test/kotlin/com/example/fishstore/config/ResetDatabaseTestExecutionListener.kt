package com.example.fishstore.config

import org.springframework.test.context.support.AbstractTestExecutionListener
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.TestContext
import java.lang.Exception
import kotlin.Throws
import java.sql.SQLException
import java.util.HashSet
import java.sql.ResultSet
import javax.sql.DataSource

class ResetDatabaseTestExecutionListener : AbstractTestExecutionListener() {

    @Autowired
    private lateinit var dataSource: DataSource

    override fun getOrder(): Int {
        return 2001
    }

    override fun afterTestMethod(testContext: TestContext) {
        cleanupDatabase()
    }

    private var alreadyCleared = false
    override fun beforeTestClass(testContext: TestContext) {
        testContext.applicationContext
            .autowireCapableBeanFactory
            .autowireBean(this)
    }

    override fun prepareTestInstance(testContext: TestContext) {
        if (!alreadyCleared) {
            cleanupDatabase()
            alreadyCleared = true
        }
    }

    @Throws(SQLException::class)
    private fun cleanupDatabase() {
        val c = dataSource.connection
        val s = c.createStatement()

        // Disable FK
        s.execute("SET REFERENTIAL_INTEGRITY FALSE")

        // Find all tables and truncate them
        val tables: MutableSet<String> = HashSet()
        var rs = s.executeQuery("SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES  where TABLE_SCHEMA='PUBLIC'")
        while (rs.next()) {
            tables.add(rs.getString(1))
        }
        rs.close()
        for (table in tables) {
            s.executeUpdate("TRUNCATE TABLE $table")
        }

        // Idem for sequences
        val sequences: MutableSet<String> = HashSet()
        rs = s.executeQuery("SELECT SEQUENCE_NAME FROM INFORMATION_SCHEMA.SEQUENCES WHERE SEQUENCE_SCHEMA='PUBLIC'")
        while (rs.next()) {
            sequences.add(rs.getString(1))
        }
        rs.close()
        for (seq in sequences) {
            s.executeUpdate("ALTER SEQUENCE $seq RESTART WITH 1")
        }

        // Enable FK
        s.execute("SET REFERENTIAL_INTEGRITY TRUE")
        s.close()
        c.close()
    }
}