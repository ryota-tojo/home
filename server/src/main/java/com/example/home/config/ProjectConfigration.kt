package com.example.home.config

import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration

@Configuration
@ComponentScan(basePackages = ["com.example.home.service"])
@ComponentScan(basePackages = ["com.example.home.datasource"])

open class ProjectConfigration