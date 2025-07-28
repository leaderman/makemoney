package io.github.leaderman.makemoney.hustle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

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
    SpringApplication.run(Application.class, args);
  }
}
