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
  console.log("资金持仓脚本开始");

  // 等待数据。
  await waitForData();

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

  console.log("资金持仓脚本结束");
}
