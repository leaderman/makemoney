package io.github.leaderman.makemoney.hustle.stock.command;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import picocli.CommandLine;
import picocli.spring.PicocliSpringFactory;

@SpringBootTest
public class SyncStockMarketInfoEntityTest {
  @Autowired
  private SyncStockMarketInfoEntityCommand syncStockMarketInfoEntityCommand;

  @Autowired
  private PicocliSpringFactory picocliSpringFactory;

  @Test
  public void testRun() {
    String[] args = { "-d", "2025-07-28" };

    new CommandLine(syncStockMarketInfoEntityCommand, picocliSpringFactory).execute(args);
  }
}
