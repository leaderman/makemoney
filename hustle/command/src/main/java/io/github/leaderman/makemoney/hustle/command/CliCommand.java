package io.github.leaderman.makemoney.hustle.command;

import org.springframework.stereotype.Component;

import io.github.leaderman.makemoney.hustle.stock.command.SyncStockCommand;
import io.github.leaderman.makemoney.hustle.stock.command.SyncStockMarketInfoCommand;
import io.github.leaderman.makemoney.hustle.stock.command.trend.analyze.AnalyzeStockTrendCommand;
import picocli.CommandLine;
import picocli.CommandLine.Command;

@Component
@Command(name = "Cli", description = "命令行程序", mixinStandardHelpOptions = true, subcommands = { HelloCommand.class,
    SyncStockCommand.class, SyncStockMarketInfoCommand.class, AnalyzeStockTrendCommand.class })
public class CliCommand implements Runnable {
  @Override
  public void run() {
    CommandLine.usage(this, System.out);
  }
}
