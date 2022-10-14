import java.sql.Connection
import java.sql.DriverManager

class Controller(
    host: String = "127.0.0.1", port: Int = 3306,
    mariaName: String = "", user: String = "root", passwd: String = ""
) {
    data class AuthData(
        val userID: Int, val username: String, val email: String, val phone: String, val hash: String
    )  // Model class for authentication data
    data class UserData(
        val userID: Int, val name: String, val google: String, val stepik: Int, val skills: String
    )  // Model class for user data
    private var connection: Connection

    init { // Open connection
        Class.forName("org.mariadb.jdbc.Driver")
        val fullLink = "jdbc:mariadb://$host:$port/$mariaName"
        connection = DriverManager.getConnection(fullLink, user, passwd)

        // Send to console connection data
        if (connection.isValid(0)) {
            println("DataBase controller class connect now to $mariaName")
            println("Host: $host:$port")
            println("Username: $user with password $passwd")
        } else println("Failed to connect!")
    }

    fun getAuthData(username: String): AuthData {
        if (!connection.isValid(0)) return AuthData(
            -1, "DBNotConnected", "", "", ""
        )  // Return if connection fail

        // Prepare variables for DB reading
        val query = connection.prepareStatement("SELECT * FROM UserList")
        val result = query.executeQuery()
        var data = AuthData(-1, "UserNotFound", "", "", "")

        while (result.next()) {
            // Get auth data from DB
            val id = result.getInt("userID")
            val name = result.getString("Username")
            val email = result.getString("Email")
            val phone = result.getString("Phone")
            val hash = result.getString("PasswdHash")

            if (name == username) data = AuthData(id, name, email, phone, hash) // Add get user to data
        }
        return data
    }

    fun postAuthData()
    /* TODO
        1) sendAuthData function
        2-3) (get and send)UserData function
     */
}