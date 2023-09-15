package ru.agpu.artikproject.background_work.datebase

import java.sql.Connection
import java.sql.DriverManager
import java.sql.Statement

object DataBaseOnlineHelper {
    private var con: Connection? = null

    @Synchronized
    fun getStatement(): Statement? {
        return getConnection()?.createStatement()
    }

    private fun getConnection(): Connection? {
        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver")
            con = DriverManager.getConnection(connectionUrl, userName, password)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return con
    }

    private const val url = "jdbc:jtds:sqlserver://"                // Драйвер
    private const val serverName = "aom13.tplinkdns.com"            // Адрес сервера
    private const val portNumber = "1433"                           // Порт
    private const val databaseName = "RASPISANIE_AGPU_MOBILE_APP"   // Название базы данных
    private const val userName = "RASPISANIE_AGPU"                  // Логин
    private const val password = "RASPISANIE_AGPU"                  // Пароль
    private const val connectionUrl = "$url$serverName:$portNumber;database=$databaseName"
}