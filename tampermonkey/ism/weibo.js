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

/**
 * 获取信息流元素。
 * @param {Node} feed 信息流元素。
 * @returns {Object} 信息流对象。
 */
function getFeed(feed) {
  // 获取链接。
  const href = window.mm.attributeOf(
    window.mm.one(feed, 'a[class^="head-info_time"]'),
    "href"
  );

  // 点击展开。
  const exps = window.mm.all(feed, "span.expand");
  for (const exp of exps) {
    exp.click();
  }

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

  // 链接集合。
  const hrefs = new Set();

  while (true) {
    // 获取信息流列表。
    const feeds = getFeeds();
    for (const feed of feeds) {
      // 获取信息流对象。
      const { href, html } = getFeed(feed);
      if (hrefs.has(href)) {
        // 链接已存在，跳过。
        continue;
      }
      // 添加链接。
      hrefs.add(href);

      console.log("微博链接：", href);
      console.log("微博源码：", html);
    }

    console.log("微博数量：", hrefs.size);
    if (hrefs.size >= 100) {
      // 链接数量已达到阈值，退出。
      break;
    }

    // 向下滚动。
    window.mm.scrollDown(300);
    // 休眠。
    await window.mm.sleep(1000);
  }

  // 重新页面。
  window.mm.reload();
}

(function () {
  "use strict";

  main();
})();
