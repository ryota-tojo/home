package com.example.home.config

import com.example.home.infrastructure.persistence.exposed_tables.master.TbMsChoices
import com.example.home.infrastructure.persistence.exposed_tables.master.TbMsSetting
import com.example.home.infrastructure.persistence.exposed_tables.transaction.*
import com.example.home.util.PropertiesUtil.loadProperties
import jakarta.annotation.PostConstruct
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.stereotype.Component

@Component
class DbInitializer {

    @PostConstruct
    fun initialize(){
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

        }else{
            dbConfig = DbConfig(envUrl, envUser, envPassword)
        }
        Database.connect(
            url = dbConfig.url,
            driver = "org.postgresql.Driver",
            user = dbConfig.user,
            password = dbConfig.password
        )
    }
//    fun createTable(){
//        SchemaUtils.create(
//            // マスター
//            TbMsSetting,
//            TbMsChoices,
//
//            // トランザクション
//            // -- お知らせ
//            TbTsNotice,
//
//            // -- グループ関連
//            TbTsGroupList,
//            TbTsGroupSetting,
//
//            // -- ユーザー関連
//            TbTsUserInfo,
//            TbTsUserSetting,
//            TbTsGroupInfo,
//
//            // -- 家計簿関連 - 予算
//            TbTsBudgets,
//            // -- 家計簿関連 - 購入データ
//            TbTsCategorys,
//            TbTsMembers,
//            TbTsShopping,
//            // -- 家計簿関連 - テンプレート
//            TbTsTmpShoppingInput,
//            TbTsTmpShoppingEntry,
//            TbTsTmpShoppingSearch,
//            // -- 家計簿関連 - コメント
//            TbTsComment,
//
//            // -- お付き合い帳
//            TbTsCommunication
//
//        )
//    }
//    fun defaultData(){
//
//    }
}