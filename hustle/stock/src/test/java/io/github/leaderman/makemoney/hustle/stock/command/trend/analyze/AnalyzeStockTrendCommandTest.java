package io.github.leaderman.makemoney.hustle.stock.command.trend.analyze;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import picocli.CommandLine;
import picocli.spring.PicocliSpringFactory;

@SpringBootTest
public class AnalyzeStockTrendCommandTest {
  @Autowired
  private AnalyzeStockTrendCommand analyzeStockTrendCommand;

  @Autowired
  private PicocliSpringFactory picocliSpringFactory;

  @Test
  public void testRun() {
    new CommandLine(analyzeStockTrendCommand, picocliSpringFactory).execute();
  }
}
