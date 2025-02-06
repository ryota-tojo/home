package confirmation

import com.example.home.config.DbConfig
import com.example.home.util.PropertiesUtil.loadProperties
import org.jetbrains.exposed.sql.Database

class DbConnectConfirmation {

    fun dbConnect() {
        val envHost = System.getenv("DB_HOST")
        val envPort = System.getenv("DB_PORT")
        val envUser = System.getenv("DB_USER")
        val envPassword = System.getenv("DB_PASSWORD")
        val envName = System.getenv("DB_NAME")
        val envUrl = "jdbc:postgresql://$envHost:$envPort/$envName"

        val dbConfig: DbConfig

        if (envHost == null || envPort == null || envUser == null || envPassword == null || envName == null) {
            val properties = loadProperties("db.properties")
            val pptyHost = "localhost"
            val pptyPort = properties.getProperty("DB_PORT")
            val pptyUser = properties.getProperty("DB_USER")
            val pptyPassword = properties.getProperty("DB_PASSWORD")
            val pptyName = properties.getProperty("DB_NAME")
            val pptyUrl = "jdbc:postgresql://$pptyHost:$pptyPort/$pptyName"
            dbConfig = DbConfig(pptyUrl, pptyUser, pptyPassword)

        } else {
            dbConfig = DbConfig(envUrl, envUser, envPassword)
        }
        Database.connect(
            url = dbConfig.url,
            driver = "org.postgresql.Driver",
            user = dbConfig.user,
            password = dbConfig.password
        )

    }
}
