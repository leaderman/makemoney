window.mm = window.mm || {};

/**
 * 获取元素的文本。
 * @param {string} selector 选择器。
 * @returns {string} 文本。
 */
window.mm.getText = function (selector) {
  return document.querySelector(selector)?.textContent.trim() || "";
};

/**
 * 睡眠。
 * @param {number} ms 睡眠时间，单位：毫秒。
 * @returns {Promise<void>} 空。
 */
window.mm.sleep = function (ms) {
  return new Promise((resolve) => setTimeout(resolve, ms));
};
