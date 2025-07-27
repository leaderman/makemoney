package io.github.leaderman.makemoney.hustle.command;

import org.springframework.stereotype.Component;

import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Component
@Command(name = "Hello", description = "演示命令")
public class HelloCommand implements Runnable {
  @Option(names = { "-n", "--name" }, description = "姓名")
  private String name = "world";

  @Override
  public void run() {
    System.out.println("Hello, " + name + "!");
  }
}
