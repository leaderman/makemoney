// ==UserScript==
// @name         IsmWeibo
// @namespace    MakeMoney
// @version      2025-09-13
// @description  微博投资舆情监测
// @match        https://weibo.com/mygroups?gid=5210361993364437
// @require      https://raw.githubusercontent.com/leaderman/makemoney/refs/heads/main/tampermonkey/common.js
// ==/UserScript==

/**
 * 获取信息流列表。
 * @returns {NodeList} 信息流列表。
 */
function getFeeds() {
  return window.mm.all(document, ".vue-recycle-scroller__item-view");
}

/**
 * 等待信息流列表。
 */
async function waitForFeeds() {
  while (true) {
    const feeds = getFeeds();
    if (feeds.length > 0) {
      return;
    }

    await window.mm.sleep(100);
  }
}

function getFeed(feed) {
  const href = window.mm.attributeOf(
    window.mm.one(feed, 'a[class^="head-info_time"]'),
    "href"
  );

  return {
    href,
    html: feed.outerHTML,
  };
}

/**
 * 主函数。
 */
async function main() {
  console.log("等待信息流列表...");
  await waitForFeeds();
  console.log("信息流列表已就绪");

  const feeds = getFeeds();
  for (const feed of feeds) {
    const { href, html } = getFeed(feed);
    console.log(href);
    console.log(html);
  }
}

(function () {
  "use strict";

  main();
})();
