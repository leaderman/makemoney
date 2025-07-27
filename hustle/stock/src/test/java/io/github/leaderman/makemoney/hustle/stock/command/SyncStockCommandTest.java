package io.github.leaderman.makemoney.hustle.stock.command;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import picocli.CommandLine;
import picocli.spring.PicocliSpringFactory;

@SpringBootTest
public class SyncStockCommandTest {
  @Autowired
  private SyncStockCommand syncStockCommand;

  @Autowired
  private PicocliSpringFactory picocliSpringFactory;

  @Test
  public void testSyncStock() {
    new CommandLine(syncStockCommand, picocliSpringFactory).execute();
  }
}
