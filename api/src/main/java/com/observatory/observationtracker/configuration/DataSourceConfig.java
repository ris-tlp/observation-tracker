package com.observatory.observationtracker.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import javax.sql.DataSource;


@Configuration
public class DataSourceConfig {
    private final AwsConfig awsConfig;
    private final String rdsUri;
    private final String rdsUsername;
    private final String rdsPassword;
    private final String driverClassName;

    public DataSourceConfig(AwsConfig awsConfig) {
        this.awsConfig = awsConfig;
        this.rdsUri = "jdbc:postgresql://" + this.awsConfig.getRdsUri() + "/" + this.awsConfig.getRdsName();
        this.rdsUsername = this.awsConfig.getRdsUsername();
        this.rdsPassword = this.awsConfig.getRdsPassword();
        this.driverClassName = org.postgresql.Driver.class.getName();
    }

    @Bean
    public DataSource getDataSource() {
        DataSourceBuilder<?> builder = DataSourceBuilder.create();
        builder.url(rdsUri);
        builder.username(rdsUsername);
        builder.password(rdsPassword);
        builder.driverClassName(driverClassName);

        return builder.build();
    }
}
