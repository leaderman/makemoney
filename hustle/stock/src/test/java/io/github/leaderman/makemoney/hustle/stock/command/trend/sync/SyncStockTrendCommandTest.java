package io.github.leaderman.makemoney.hustle.stock.command.trend.sync;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import picocli.CommandLine;
import picocli.spring.PicocliSpringFactory;

@SpringBootTest
public class SyncStockTrendCommandTest {
  @Autowired
  private SyncStockTrendCommand syncStockTrendCommand;

  @Autowired
  private PicocliSpringFactory picocliSpringFactory;

  @Test
  public void testRun() {
    new CommandLine(syncStockTrendCommand, picocliSpringFactory).execute();
  }
}
