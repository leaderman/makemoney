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

  console.log("Hello Position!");

  const totalAssets = window.mm.getText(
    "#assest_cont > table > tbody > tr.tb-tr-bot.lh300 > td.tb-tr-right.pad-box > span.padl10 > span"
  );
  console.log(totalAssets);
})();
