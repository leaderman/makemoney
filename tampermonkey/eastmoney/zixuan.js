// ==UserScript==
// @name         Zixuan
// @namespace    MakeMoney
// @version      2025-08-23
// @description  东方财富自选股（盯盘）
// @match        https://quote.eastmoney.com/zixuan/*
// @require      https://raw.githubusercontent.com/leaderman/makemoney/refs/heads/main/tampermonkey/common.js
// @connect      *
// @grant        GM_xmlhttpRequest
// ==/UserScript==

const URL = "xxx";
const TOKEN = "xxx";

/**
 * 等待表格。
 */
async function waitForTable() {
  while (true) {
    const table = window.mm.text("#zxggrouplist > li.on > div > a");
    const type = window.mm.text("#wltypelist > li.on");
    if (table === "盯盘" && type === "我的自选") {
      return;
    }

    await window.mm.sleep(100);
  }
}

/**
 * 获取基金列表。
 * @returns {Array<Object>} 基金列表。
 */
function getFunds() {
  const funds = [];

  const trs = window.mm.all(document, "#table_m > table > tbody > tr");

  for (const tr of trs) {
    const tds = window.mm.all(tr, "td");

    // 代码。
    const code = window.mm.textOf(window.mm.one(tds[1], "a"));
    // 名称。
    const name = window.mm.attributeOf(window.mm.one(tds[2], "a"), "title");
    // 开盘价。
    const openPrice = window.mm.textOf(window.mm.one(tds[4], "span"));
    // 最新价。
    const latestPrice = window.mm.textOf(window.mm.one(tds[5], "span"));
    // 最高价。
    const highPrice = window.mm.textOf(window.mm.one(tds[6], "span"));
    // 最低价。
    const lowPrice = window.mm.textOf(window.mm.one(tds[7], "span"));
    // 涨跌幅。
    const changePercent = window.mm.textOf(window.mm.one(tds[8], "span"));
    // 涨跌额。
    const changeAmount = window.mm.textOf(window.mm.one(tds[9], "span"));
    // 昨收。
    const prevClose = window.mm.textOf(window.mm.one(tds[10], "span"));

    const fund = {
      code,
      name,
      openPrice,
      latestPrice,
      highPrice,
      lowPrice,
      changePercent,
      changeAmount,
      prevClose,
    };

    funds.push(fund);
  }

  return funds;
}

/**
 * 打印基金列表。
 * @param {Array<Object>} funds 基金列表。
 */
function printFunds(funds) {
  for (const fund of funds) {
    console.log(
      "代码：",
      fund.code,
      "，名称：",
      fund.name,
      "， 开盘价：",
      fund.openPrice,
      "， 最新价：",
      fund.latestPrice,
      "， 最高价：",
      fund.highPrice,
      "， 最低价：",
      fund.lowPrice,
      "， 涨跌幅：",
      fund.changePercent,
      "， 涨跌额：",
      fund.changeAmount,
      "， 昨收：",
      fund.prevClose
    );
  }
}

/**
 * 同步基金列表。
 * @param {Array<Object>} funds 基金列表。
 */
async function syncFunds(funds) {
  try {
    await window.mm.post(
      URL,
      { funds },
      {
        Authorization: "Bearer " + TOKEN,
      }
    );
  } catch (error) {
    console.error("同步基金列表失败:", error.message);
    await window.mm.sleep(30000);
  }
}

/**
 * 主函数。
 */
async function main() {
  console.log("等待表格...");
  await waitForTable();
  console.log("表格已就绪");

  while (true) {
    const funds = getFunds();
    console.log("基金列表获取完成");

    if (funds.length === 0) {
      console.log("基金列表为空，等待下一轮...");
      await window.mm.sleep(1000);
      continue;
    }

    printFunds(funds);

    await syncFunds(funds);
    console.log("基金列表同步完成");
  }
}

(function () {
  "use strict";

  main();
})();
