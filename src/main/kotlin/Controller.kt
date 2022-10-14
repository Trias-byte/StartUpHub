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
        val query = connection.prepareStatement(
            "SELECT * FROM UserList WHERE Username = '$username'"
        ).executeQuery()
        var data = AuthData(-1, "UserNotFound", "", "", "")

        if (query.next()) data = AuthData(
            query.getInt("userID"), query.getString("Username"),
            query.getString("Email"), query.getString("Phone"),
            query.getString("PasswdHash")
        ) // Get auth data from DB and add it to data variable

        return data
    }

    fun postAuthData(username: String, hash: String, email: String, phone: String) {
        if (!connection.isValid(0)) return  // Return if connection fail

        val lastID = connection.prepareStatement(
            "SELECT COUNT(DataID) FROM UserList"
        ).executeQuery(); lastID.next()
        val lid = lastID.getInt("COUNT(DataID)")
        val lastUser = connection.prepareStatement(
            "SELECT * FROM UserList WHERE DataID = '$lid'"
        ).executeQuery(); lastUser.next()

        val dataID = lastUser.getInt("DataID") + 1
        val userID = lastUser.getInt("UserID") + 1

        connection.createStatement().execute(
            "INSERT INTO UserList (`DataID`, `UserID`, `Username`, `Email`, `Phone`, `PasswdHash`) " +
                    "VALUES ('$dataID', '$userID', '$username', '$email', '$phone', '$hash')"
        ); connection.commit()
    }

    fun getUserData(userID: Int): UserData {
        if (!connection.isValid(0)) return UserData(
            -1, "DBNotConnected", "", 0, ""
        )  // Return if connection fail

        val query = connection.prepareStatement(
            "SELECT * FROM UserData WHERE UserID = '$userID'"
        ).executeQuery()
        var data = UserData(-1, "UserNotFound", "", 0, "")
        if (query.next()) data = UserData(
            query.getInt("userID"), query.getString("Fullname"),
            query.getString("Google"), query.getInt("Stepik"),
            query.getString("Skills")
        ) // Get auth data from DB and add it to data variable
        return data
    }

    fun postUserData(fullname: String, gmail: String, stepikID: Int, skills: String) {
        if (!connection.isValid(0)) return  // Return if connection fail

        val lastID = connection.prepareStatement(
            "SELECT COUNT(DataID) FROM UserData"
        ).executeQuery(); lastID.next()
        val lid = lastID.getInt("COUNT(DataID)")
        val lastUser = connection.prepareStatement(
            "SELECT * FROM UserData WHERE DataID = '$lid'"
        ).executeQuery(); lastUser.next()

        val dataID = lastUser.getInt("DataID") + 1
        val userID = lastUser.getInt("UserID") + 1

        connection.createStatement().execute(
            "INSERT INTO UserData (`DataID`, `UserID`, `Fullname`, `Google`, `Stepik`, `Skills`) " +
                    "VALUES ('$dataID', '$userID', '$fullname', '$gmail', '$stepikID', '$skills')"
        ); connection.commit()
    }
}