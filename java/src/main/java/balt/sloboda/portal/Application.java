package balt.sloboda.portal;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages={"balt.sloboda.portal"})
public class Application {

    public static void main(String[] args){
        SpringApplication.run(Application.class, args);
    }
}
