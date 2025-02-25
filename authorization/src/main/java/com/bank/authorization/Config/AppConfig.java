package com.bank.authorization.Config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Configuration
@ComponentScan(basePackages = "com.bank.authorization")
@EnableAspectJAutoProxy
public class AppConfig {
}
