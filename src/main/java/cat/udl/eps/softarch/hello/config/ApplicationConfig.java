package cat.udl.eps.softarch.hello.config;

import java.net.URI;
import java.net.URISyntaxException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.connect.web.ConnectController;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

import javax.inject.Inject;
import javax.sql.DataSource;

/**
 * Created by http://rhizomik.net/~roberto/
 */

@Configuration
@EnableWebMvc
@ComponentScan("cat.udl.eps.softarch.hello")
@EnableJpaRepositories("cat.udl.eps.softarch.hello.repository")
@EnableTransactionManagement
@PropertySource("classpath:application.properties")
public class ApplicationConfig extends WebMvcConfigurerAdapter{

    @Inject
    private Environment env;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**").addResourceLocations("/static/");
    }

    @Bean
    public ConnectController connectController(ConnectionFactoryLocator connectionFactoryLocator, ConnectionRepository connectionRepository) {
        return new ConnectController(connectionFactoryLocator, connectionRepository);
    }

    @Bean
    public ViewResolver getViewResolver() {
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setViewClass(JstlView.class);
        viewResolver.setPrefix("/WEB-INF/views/");
        viewResolver.setSuffix(".jsp");
        return viewResolver;
    }

    @Bean
    public DataSource dataSource() throws URISyntaxException {

        if (env.getProperty("database.type") == "postgresql") {
            DriverManagerDataSource dataSource = new DriverManagerDataSource();
            URI dbUri = new URI(System.getenv("DATABASE_URL"));
            String username = dbUri.getUserInfo().split(":")[0];
            String password = dbUri.getUserInfo().split(":")[1];
            String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + dbUri.getPath();
            dataSource.setDriverClassName("org.postgresql.Driver");
            dataSource.setUrl(dbUrl);
            dataSource.setUsername(username);
            dataSource.setPassword(password);
            return dataSource;
        } else {
            EmbeddedDatabaseBuilder builder = new EmbeddedDatabaseBuilder();
            return builder.setType(EmbeddedDatabaseType.HSQL).build();
        }
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() throws URISyntaxException {

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        if (env.getProperty("database.type") == "postgresql") {
            vendorAdapter.setDatabase(Database.POSTGRESQL);
            vendorAdapter.setDatabasePlatform("org.hibernate.dialect.PostgreSQLDialect");
            vendorAdapter.setGenerateDdl(true);
            vendorAdapter.setShowSql(true);
        }
        else {
            vendorAdapter.setDatabase(Database.HSQL);
            vendorAdapter.setGenerateDdl(true);
        }
        LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
        factory.setJpaVendorAdapter(vendorAdapter);
        factory.setPackagesToScan("cat.udl.eps.softarch.hello");
        factory.setDataSource(dataSource());
        return factory;
    }

    @Bean
    public PlatformTransactionManager transactionManager() throws URISyntaxException {
        JpaTransactionManager txManager = new JpaTransactionManager();
        txManager.setEntityManagerFactory(entityManagerFactory().getObject());
        return txManager;
    }
}
