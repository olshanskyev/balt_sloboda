package balt.sloboda.portal.configuration;

import balt.sloboda.portal.service.EmailService;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

@Profile("test")
@Configuration
public class MockitoTestConfiguration {

    @Bean(name = "mockitoEmailService")
    @Primary
    public EmailService emailService() {
        return Mockito.mock(EmailService.class);
    }

}
