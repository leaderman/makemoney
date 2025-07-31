package io.github.leaderman.makemoney.hustle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

import io.github.leaderman.makemoney.hustle.command.CliCommand;
import picocli.CommandLine;
import picocli.spring.PicocliSpringFactory;

@SpringBootApplication
public class Application implements CommandLineRunner {
  @Autowired
  private CliCommand cliCommand;

  @Autowired
  private PicocliSpringFactory picocliSpringFactory;

  @Override
  public void run(String... args) throws Exception {
    new CommandLine(cliCommand, picocliSpringFactory).execute(args);

    System.exit(0);
  }

  public static void main(String[] args) {
    new SpringApplicationBuilder(Application.class).web(WebApplicationType.NONE).run(args);
  }
}
