window.mm = window.mm || {};

/**
 * 获取元素的文本。
 * @param {string} selector 选择器。
 * @returns {string} 文本。
 */
window.mm.text = function (selector) {
  return document.querySelector(selector)?.textContent.trim() || "";
};

/**
 * 获取元素的文本。
 * @param {Node} el 元素。
 * @returns {string} 文本。
 */
window.mm.textOf = function (el) {
  return el?.textContent.trim() || "";
};

/**
 * 睡眠。
 * @param {number} ms 睡眠时间，单位：毫秒。
 * @returns {Promise<void>} 空。
 */
window.mm.sleep = function (ms) {
  return new Promise((resolve) => setTimeout(resolve, ms));
};

/**
 * 获取所有元素。
 * @param {Node} root 根元素。
 * @param {string} selector 选择器。
 * @returns {NodeList} 元素列表。
 */
window.mm.all = function (root, selector) {
  return root.querySelectorAll(selector);
};

/**
 * 获取元素。
 * @param {Node} root 根元素。
 * @param {string} selector 选择器。
 * @returns {Node} 元素。
 */
window.mm.one = function (root, selector) {
  return root.querySelector(selector);
};
