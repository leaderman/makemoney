// ==UserScript==
// @name         Position
// @namespace    MakeMoney
// @version      2025-08-23
// @description  东方财富资金持仓
// @match        https://jywg.18.cn/Search/Position
// @require      https://raw.githubusercontent.com/leaderman/makemoney/refs/heads/main/tampermonkey/common.js
// ==/UserScript==

(function () {
  "use strict";

  main();
})();

/**
 * 获取总资产。
 * @returns {string} 总资产。
 */
function getTotalAssets() {
  return window.mm.text(
    "#assest_cont > table > tbody > tr.tb-tr-bot.lh300 > td.tb-tr-right.pad-box > span.padl10 > span"
  );
}

/**
 * 获取证券市值。
 * @returns {string} 证券市值。
 */
function getSecuritiesMarketValue() {
  return window.mm.text(
    "#assest_cont > table > tbody > tr.tb-tr-bot.lh300 > td:nth-child(2) > span.padl10 > span > span"
  );
}

/**
 * 获取可用资金。
 * @returns {string} 可用资金。
 */
function getAvailableFunds() {
  return window.mm.text(
    "#assest_cont > table > tbody > tr:nth-child(2) > td.tb-tr-right.lh200.pad-box > span.padl10 > span"
  );
}

/**
 * 获取持仓盈亏。
 * @returns {string} 持仓盈亏。
 */
function getPositionProfitLoss() {
  return window.mm.text(
    "#assest_cont > table > tbody > tr:nth-child(2) > td:nth-child(2) > span.padl10 > span"
  );
}

/**
 * 获取资金余额。
 * @returns {string} 资金余额。
 */
function getCashBalance() {
  return window.mm.text(
    "#assest_cont > table > tbody > tr:nth-child(2) > td:nth-child(3) > span.padl10 > span"
  );
}

/**
 * 获取可取资金。
 * @returns {string} 可取资金。
 */
function getWithdrawableFunds() {
  return window.mm.text(
    "#assest_cont > table > tbody > tr:nth-child(3) > td.tb-tr-right.lh200.pad-box > span.padl10 > span"
  );
}

/**
 * 获取当日盈亏。
 * @returns {string} 当日盈亏。
 */
function getDailyProfitLoss() {
  return window.mm.text(
    "#assest_cont > table > tbody > tr:nth-child(3) > td:nth-child(2) > span.padl10"
  );
}

/**
 * 获取冻结资金。
 * @returns {string} 冻结资金。
 */
function getFrozenFunds() {
  return window.mm.text(
    "#assest_cont > table > tbody > tr:nth-child(3) > td:nth-child(3) > span.padl10"
  );
}

/**
 * 获取证券列表。
 * @returns {Array<Object>} 证券列表。
 */
function getSecurities() {
  const securities = [];

  const trs = window.mm.all(document, "#tabBody > tr");

  for (const tr of trs) {
    // 证券代码。
    const securityCode = window.mm.textOf(
      window.mm.one(tr, "td:nth-child(1) > a")
    );

    // 证券名称。
    const securityName = window.mm.textOf(
      window.mm.one(tr, "td:nth-child(2) > a")
    );

    // 持仓数量。
    const holdingQuantity = window.mm.textOf(
      window.mm.one(tr, "td:nth-child(3)")
    );

    // 可用数量。
    const availableQuantity = window.mm.textOf(
      window.mm.one(tr, "td:nth-child(4)")
    );

    // 成本价。
    const costPrice = window.mm.textOf(window.mm.one(tr, "td:nth-child(5)"));

    // 当前价。
    const currentPrice = window.mm.textOf(window.mm.one(tr, "td:nth-child(6)"));

    // 最新市值。
    const marketValue = window.mm.textOf(window.mm.one(tr, "td:nth-child(7)"));

    // 持仓盈亏。
    const positionProfitLoss = window.mm.textOf(
      window.mm.one(tr, "td:nth-child(8)")
    );

    // 持仓盈亏比例。
    const positionProfitLossRatio = window.mm.textOf(
      window.mm.one(tr, "td:nth-child(9)")
    );

    // 当日盈亏。
    const dailyProfitLoss = window.mm.textOf(
      window.mm.one(tr, "td:nth-child(10)")
    );

    // 当日盈亏比例。
    const dailyProfitLossRatio = window.mm.textOf(
      window.mm.one(tr, "td:nth-child(11)")
    );

    securities.push({
      securityCode,
      securityName,
      holdingQuantity,
      availableQuantity,
      costPrice,
      currentPrice,
      marketValue,
      positionProfitLoss,
      positionProfitLossRatio,
      dailyProfitLoss,
      dailyProfitLossRatio,
    });
  }

  return securities;
}

/**
 * 等待数据。
 */
async function waitForData() {
  let totalAssets = "-";

  do {
    await window.mm.sleep(100);

    totalAssets = getTotalAssets();
  } while (totalAssets === "-");
}

async function main() {
  // 等待数据。
  console.log("等待资金持仓数据...");
  await waitForData();
  console.log("资金持仓数据已就绪");

  // 获取总资产。
  const totalAssets = getTotalAssets();
  console.log("总资产：", totalAssets);

  // 获取证券市值。
  const securitiesMarketValue = getSecuritiesMarketValue();
  console.log("证券市值：", securitiesMarketValue);

  // 获取可用资金。
  const availableFunds = getAvailableFunds();
  console.log("可用资金：", availableFunds);

  // 获取持仓盈亏。
  const positionProfitLoss = getPositionProfitLoss();
  console.log("持仓盈亏：", positionProfitLoss);

  // 获取资金余额。
  const cashBalance = getCashBalance();
  console.log("资金余额：", cashBalance);

  // 获取可取资金。
  const withdrawableFunds = getWithdrawableFunds();
  console.log("可取资金：", withdrawableFunds);

  // 获取当日盈亏。
  const dailyProfitLoss = getDailyProfitLoss();
  console.log("当日盈亏：", dailyProfitLoss);

  // 获取冻结资金。
  const frozenFunds = getFrozenFunds();
  console.log("冻结资金：", frozenFunds);

  // 获取证券列表。
  const securities = getSecurities();
  console.log("证券列表：", securities.length);

  for (const security of securities) {
    console.log(
      "证券代码：",
      security.securityCode,
      "，证券名称：",
      security.securityName,
      "，持仓数量：",
      security.holdingQuantity,
      "，可用数量：",
      security.availableQuantity,
      "，成本价：",
      security.costPrice,
      "，当前价：",
      security.currentPrice,
      "，最新市值：",
      security.marketValue,
      "，持仓盈亏：",
      security.positionProfitLoss,
      "，持仓盈亏比例：",
      security.positionProfitLossRatio,
      "，当日盈亏：",
      security.dailyProfitLoss,
      "，当日盈亏比例：",
      security.dailyProfitLossRatio
    );
  }

  // 休眠
  console.log("休眠...");
  await window.mm.sleep(3000);

  // 重新加载
  console.log("重新加载...");
  window.mm.reload();
}
