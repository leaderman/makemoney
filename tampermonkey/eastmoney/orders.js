// ==UserScript==
// @name         Orders
// @namespace    MakeMoney
// @version      2025-08-23
// @description  东方财富当日委托
// @match        https://jywg.18.cn/Search/Orders
// @require      https://raw.githubusercontent.com/leaderman/makemoney/refs/heads/main/tampermonkey/common.js
// ==/UserScript==

(function () {
  "use strict";

  main();
})();

/**
 * 获取委托列表。
 * @returns {Array<Object>} 证券列表。
 */
function getOrders() {
  const orders = [];

  const trs = window.mm.all(document, "#tabBody > tr");

  for (const tr of trs) {
    // 委托时间。
    const orderTime = window.mm.textOf(window.mm.one(tr, "td:nth-child(1)"));
    // 证券代码。
    const orderType = window.mm.textOf(
      window.mm.one(tr, "td:nth-child(2) > a")
    );
    // 证券名称。
    const securityName = window.mm.textOf(
      window.mm.one(tr, "td:nth-child(3) > a")
    );
    // 委托方向。
    const orderSide = window.mm.textOf(window.mm.one(tr, "td:nth-child(4)"));
    // 委托数量。
    const orderQuantity = window.mm.textOf(
      window.mm.one(tr, "td:nth-child(5)")
    );
    // 委托状态。
    const orderStatus = window.mm.textOf(window.mm.one(tr, "td:nth-child(6)"));
    // 委托价格。
    const orderPrice = window.mm.textOf(window.mm.one(tr, "td:nth-child(7)"));
    // 成交数量。
    const filledQuantity = window.mm.textOf(
      window.mm.one(tr, "td:nth-child(8)")
    );
    // 成交金额。
    const filledAmount = window.mm.textOf(window.mm.one(tr, "td:nth-child(9)"));
    // 成交价格。
    const avgFilledPrice = window.mm.textOf(
      window.mm.one(tr, "td:nth-child(10)")
    );
    // 委托编号。
    const orderId = window.mm.textOf(window.mm.one(tr, "td:nth-child(11)"));
    // 币种。
    const currency = window.mm.textOf(window.mm.one(tr, "td:nth-child(12)"));

    orders.push({
      orderTime,
      orderType,
      securityName,
      orderSide,
      orderQuantity,
      orderStatus,
      orderPrice,
      filledQuantity,
      filledAmount,
      avgFilledPrice,
      orderId,
      currency,
    });
  }

  return orders;
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
  console.log("等待当日委托数据...");
  await waitForData();
  console.log("当日委托数据已就绪");

  // 获取委托列表。
  const orders = getOrders();
  console.log("委托列表：", orders.length);

  for (const order of orders) {
    console.log(
      "委托时间：",
      order.orderTime,
      "，证券代码：",
      order.orderType,
      "，证券名称：",
      order.securityName,
      "，委托方向：",
      order.orderSide,
      "，委托数量：",
      order.orderQuantity,
      "，委托状态：",
      order.orderStatus,
      "，委托价格：",
      order.orderPrice,
      "，成交数量：",
      order.filledQuantity,
      "，成交金额：",
      order.filledAmount,
      "，成交价格：",
      order.avgFilledPrice,
      "，委托编号：",
      order.orderId,
      "，币种：",
      order.currency
    );
  }

  console.log("当日委托数据解析完成");

  // 等待页面重新加载
  console.log("等待页面重新加载...");
  await window.mm.sleep(3000);
  window.mm.reload();
}
