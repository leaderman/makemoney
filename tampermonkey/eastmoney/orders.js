// ==UserScript==
// @name         Orders
// @namespace    MakeMoney
// @version      2025-08-23
// @description  东方财富当日委托
// @match        https://jywg.18.cn/Search/Orders
// @require      https://raw.githubusercontent.com/leaderman/makemoney/refs/heads/main/tampermonkey/common.js
// @connect      *
// @grant        GM_xmlhttpRequest
// ==/UserScript==

const URL = "xxx";
const TOKEN = "xxx";

const NAV =
  "#main > div > div.mt20 > div.v_nav > ul > li.top_item.open > ul > li.sub_item.current > a";
const NEXT = '#divContents > div > div.confooter > div > a[data-page="next"]';
const PAGE = "#nowPage";

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

/**
 * 获取委托列表。
 * @returns {Array<Object>} 委托列表。
 */
function getOrders() {
  const orders = [];

  const trs = window.mm.all(document, "#tabBody > tr");

  for (const tr of trs) {
    const tds = window.mm.all(tr, "td");
    if (tds.length === 1) {
      // 暂无数据...
      continue;
    }

    // 委托时间。
    const orderTime = window.mm.textOf(tds[0]);
    // 证券代码。
    const securityCode = window.mm.textOf(window.mm.one(tds[1], "a"));
    // 证券名称。
    const securityName = window.mm.textOf(window.mm.one(tds[2], "a"));
    // 委托方向。
    const orderSide = window.mm.textOf(tds[3]);
    // 委托数量。
    const orderQuantity = window.mm.textOf(tds[4]);
    // 委托状态。
    const orderStatus = window.mm.textOf(tds[5]);
    // 委托价格。
    const orderPrice = window.mm.textOf(tds[6]);
    // 成交数量。
    const filledQuantity = window.mm.textOf(tds[7]);
    // 成交金额。
    const filledAmount = window.mm.textOf(tds[8]);
    // 成交价格。
    const avgFilledPrice = window.mm.textOf(tds[9]);
    // 委托编号。
    const orderId = window.mm.textOf(tds[10]);
    // 币种。
    const currency = window.mm.textOf(tds[11]);

    orders.push({
      orderTime,
      securityCode,
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

function printOrders(orders) {
  console.log("委托列表：", orders.length);

  for (const order of orders) {
    console.log(
      "委托时间：",
      order.orderTime,
      "，证券代码：",
      order.securityCode,
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
}

/**
 * 同步当日委托数据。
 * @param {Array<Object>} orders 当日委托数据。
 */
async function syncOrders(orders) {
  try {
    await window.mm.post(URL, { orders }, { Authorization: "Bearer " + TOKEN });
  } catch (error) {
    console.error("同步当日委托数据失败:", error.message);

    await window.mm.sleep(30000);
  }
}

/**
 * 判断是否有下一页。
 * @returns {boolean} 是否有下一页。
 */
function hasNext() {
  return window.mm.exists(NEXT);
}

/**
 * 点击下一页。
 */
function next() {
  window.mm.click(NEXT);
}

/**
 * 获取当前页码。
 * @returns {number} 当前页码。
 */
function getPage() {
  return parseInt(window.mm.text(PAGE).replace(/\D/g, ""));
}

/**
 * 等待指定页码。
 * @param {number} page 指定页码。
 */
async function waitForPage(page) {
  while (getPage() !== page) {
    await window.mm.sleep(100);
  }
}

/**
 * 主函数。
 */
async function main() {
  // 等待数据。
  console.log("等待当日委托数据...");
  await waitForData();
  console.log("当日委托数据已就绪");

  // 获取首页委托列表。
  const orders = getOrders();
  console.log("首页委托数据解析完成");

  if (hasNext()) {
    // 当前页码。
    let page = 1;

    while (hasNext()) {
      // 点击下一页。
      next();

      // 等待下一页数据。
      await waitForPage(++page);

      // 获取委托列表。
      const pageOrders = getOrders();
      console.log(`第 ${page} 页委托数据解析完成`);

      // 合并委托列表。
      orders.push(...pageOrders);
    }
  }

  // 打印委托列表。
  printOrders(orders);

  // 同步委托数据。
  await syncOrders(orders);
  console.log("委托数据同步完成");

  // 重新加载页面。
  console.log("重新加载页面...");
  window.mm.reload();
}

(function () {
  "use strict";

  main();
})();
