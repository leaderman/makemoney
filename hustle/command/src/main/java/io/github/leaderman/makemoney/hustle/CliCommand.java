package io.github.leaderman.makemoney.hustle;

import org.springframework.stereotype.Component;

import io.github.leaderman.makemoney.hustle.command.HelloCommand;
import picocli.CommandLine;
import picocli.CommandLine.Command;

@Component
@Command(name = "Cli", description = "命令行程序", subcommands = { HelloCommand.class })
public class CliCommand implements Runnable {
  @Override
  public void run() {
    CommandLine.usage(this, System.out);
  }
}
