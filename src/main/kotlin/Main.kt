fun main(){
    val maria = Controller(
        "sql11.freesqldatabase.com", 3306, "sql11526114",
        "sql11526114", "Cz9uHHMJxR"
    )
    val user = maria.getAuthData("TestT")
    println(user)
    // maria.postAuthData("Test2", "edf56hy", "example@exmp.com", "88005553535")

    println("password".hashCode())
}
