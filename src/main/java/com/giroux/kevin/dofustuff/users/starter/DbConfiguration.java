package com.giroux.kevin.dofustuff.users.starter;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableJpaRepositories({"com.giroux.kevin.dofustuff.users"})
@EntityScan({"com.giroux.kevin.dofustuff.users"})
@EnableTransactionManagement
public class DbConfiguration {

}
