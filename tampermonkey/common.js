window.mm = window.mm || {};

/**
 * 获取元素的文本。
 * @param {string} selector 选择器
 * @returns {string} 文本
 */
window.mm.getText = function (selector) {
  return document.querySelector(selector)?.textContent.trim() || "";
};
