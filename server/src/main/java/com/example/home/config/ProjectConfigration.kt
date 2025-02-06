package com.example.home.config

import com.example.home.service.category.CategoryService
import com.example.home.service.group.GroupControlService
import com.example.home.service.user.LoginService
import com.example.home.service.user.UserControlService
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import

@Configuration
@Import(

    // グループ
    GroupControlService::class,

    // ユーザー
    LoginService::class,
    UserControlService::class,

    // カテゴリー
    CategoryService::class,


    )
open class ProjectConfigration