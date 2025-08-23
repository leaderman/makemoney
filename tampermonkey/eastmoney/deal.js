// ==UserScript==
// @name         Deal
// @namespace    MakeMoney
// @version      2025-08-23
// @description  东方财富当日成交
// @match        https://jywg.18.cn/Search/Deal
// @require      https://raw.githubusercontent.com/leaderman/makemoney/refs/heads/main/tampermonkey/common.js
// ==/UserScript==

(function () {
  "use strict";

  main();
})();

/**
 * 获取成交列表。
 * @returns {Array<Object>} 成交列表。
 */
function getDeals() {
  const deals = [];

  const trs = window.mm.all(document, "#tabBody > tr");

  for (const tr of trs) {
    // 成交时间。
    const executionTime = window.mm.textOf(
      window.mm.one(tr, "td:nth-child(1)")
    );
    // 证券代码。
    const securityCode = window.mm.textOf(
      window.mm.one(tr, "td:nth-child(2) > a")
    );
    // 证券名称。
    const securityName = window.mm.textOf(
      window.mm.one(tr, "td:nth-child(3) > a")
    );
    // 委托方向。
    const orderSide = window.mm.textOf(window.mm.one(tr, "td:nth-child(4)"));
    // 成交数量。
    const executedQuantity = window.mm.textOf(
      window.mm.one(tr, "td:nth-child(5)")
    );
    // 成交价格。
    const executedPrice = window.mm.textOf(
      window.mm.one(tr, "td:nth-child(6)")
    );
    // 成交金额。
    const executedAmount = window.mm.textOf(
      window.mm.one(tr, "td:nth-child(7)")
    );
    // 委托编号。
    const orderId = window.mm.textOf(window.mm.one(tr, "td:nth-child(8)"));
    // 成交编号。
    const executionId = window.mm.textOf(window.mm.one(tr, "td:nth-child(9)"));
    // 交易市场。
    const market = window.mm.textOf(window.mm.one(tr, "td:nth-child(10)"));
    // 币种。
    const currency = window.mm.textOf(window.mm.one(tr, "td:nth-child(11)"));

    deals.push({
      executionTime,
      securityCode,
      securityName,
      orderSide,
      executedQuantity,
      executedPrice,
      executedAmount,
      orderId,
      executionId,
      market,
      currency,
    });
  }

  return deals;
}

/**
 * 等待数据。
 */
async function waitForData() {
  while (true) {
    const trs = window.mm.all(document, "#tabBody > tr");
    if (trs.length !== 1) {
      return;
    }

    const tds = window.mm.all(trs[0], "td");
    if (tds.length !== 1) {
      return;
    }

    const text = window.mm.textOf(tds[0]);
    if (text !== "加载中...") {
      return;
    }

    await window.mm.sleep(100);
  }
}

async function main() {
  // 等待数据。
  console.log("等待当日成交数据...");
  await waitForData();
  console.log("当日成交数据已就绪");

  // 获取委托列表。
  const deals = getDeals();
  console.log("成交列表：", deals.length);

  for (const deal of deals) {
    console.log(
      "成交时间：",
      deal.executionTime,
      "，证券代码：",
      deal.securityCode,
      "，证券名称：",
      deal.securityName,
      "，委托方向：",
      deal.orderSide,
      "，成交数量：",
      deal.executedQuantity,
      "，成交价格：",
      deal.executedPrice,
      "，成交金额：",
      deal.executedAmount,
      "，委托编号：",
      deal.orderId,
      "，成交编号：",
      deal.executionId,
      "，交易市场：",
      deal.market,
      "，币种：",
      deal.currency
    );
  }

  console.log("当日成交数据解析完成");

  // 等待页面重新加载
  console.log("等待页面重新加载...");
  await window.mm.sleep(3000);
  // window.mm.reload();
}
