package io.github.eduardoluiz.libraryapi.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

@Configuration
public class DatabaseConfiguration {

    @Value("${spring.datasource.url}")
    String url;

    @Value("${spring.datasource.username}")
    String username;

    @Value("${spring.datasource.password}")
    String password;

    @Value("${spring.datasource.driver-class-name}")
    String driver;

//    @Bean
    public DataSource dataSource() {
        // Esse DriverManagerDataSource não é recomendado para prod
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(driver);
        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        return dataSource;
    }

    @Bean // Este já é o padrão do Spring está conexão é opcional
    public DataSource hikariDataSource() {
        HikariConfig config = new HikariConfig();
        config.setUsername(username);
        config.setPassword(password);
        config.setDriverClassName(driver);
        config.setJdbcUrl(url);

        config.setMaximumPoolSize(10); // Máximo de conexões liberadas
        config.setMinimumIdle(1); // Tamanho inicial do pull
        config.setPoolName("libraryapi-dp-pool");
        config.setMaxLifetime(600000); // ms (Dura no máximo 10 min)
        config.setConnectionTimeout(100000); // timeout para conseguir uma conexão
        config.setConnectionTestQuery("SELECT 1"); // Testar se o banco está funcionando

        return new HikariDataSource(config);
    }
}
