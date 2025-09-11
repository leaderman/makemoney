// ==UserScript==
// @name         Revoke
// @namespace    MakeMoney
// @version      2025-08-23
// @description  东方财富撤单
// @match        https://jywg.18.cn/Trade/Revoke
// @require      https://raw.githubusercontent.com/leaderman/makemoney/refs/heads/main/tampermonkey/common.js
// ==/UserScript==

(function () {
  "use strict";

  main();
})();

/**
 * 获取撤单列表。
 * @returns {Array<Object>} 撤单列表。
 */
function getRevokes() {
  const revokes = [];

  const trs = window.mm.all(document, "#tabBody > tr");

  for (const tr of trs) {
    const tds = window.mm.all(tr, "td");
    if (tds.length === 1) {
      // 暂无数据...
      continue;
    }

    // 委托时间。
    const orderTime = window.mm.textOf(tds[1]);
    // 证券代码。
    const securityCode = window.mm.textOf(window.mm.one(tds[2], "a"));
    // 证券名称。
    const securityName = window.mm.textOf(window.mm.one(tds[3], "a"));
    // 委托方向。
    const orderSide = window.mm.textOf(tds[4]);
    // 委托数量。
    const orderQuantity = window.mm.textOf(tds[5]);
    // 委托状态。
    const orderStatus = window.mm.textOf(tds[6]);
    // 委托价格。
    const orderPrice = window.mm.textOf(tds[7]);
    // 成交数量。
    const filledQuantity = window.mm.textOf(tds[8]);
    // 成交金额。
    const filledAmount = window.mm.textOf(tds[9]);
    // 成交价格。
    const avgFilledPrice = window.mm.textOf(tds[10]);
    // 委托编号。
    const orderId = window.mm.textOf(tds[11]);

    revokes.push({
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
    });
  }

  return revokes;
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
  window.mm.setTitle("撤单");

  // 等待数据。
  // console.log("等待撤单数据...");
  // await waitForData();
  // console.log("撤单数据已就绪");
  // 获取委托列表。
  // const revokes = getRevokes();
  // console.log("撤单列表：", revokes.length);
  // for (const revoke of revokes) {
  //   console.log(
  //     "委托时间：",
  //     revoke.orderTime,
  //     "，证券代码：",
  //     revoke.securityCode,
  //     "，证券名称：",
  //     revoke.securityName,
  //     "，委托方向：",
  //     revoke.orderSide,
  //     "，委托数量：",
  //     revoke.orderQuantity,
  //     "，委托状态：",
  //     revoke.orderStatus,
  //     "，委托价格：",
  //     revoke.orderPrice,
  //     "，成交数量：",
  //     revoke.filledQuantity,
  //     "，成交金额：",
  //     revoke.filledAmount,
  //     "，成交价格：",
  //     revoke.avgFilledPrice,
  //     "，委托编号：",
  //     revoke.orderId
  //   );
  // }
  // console.log("撤单数据解析完成");
  // 等待页面重新加载
  // console.log("等待页面重新加载...");
  // await window.mm.sleep(3000);
  // window.mm.reload();
}
