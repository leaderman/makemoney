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

/**
 * 重新加载。
 */
window.mm.reload = function () {
  location.reload();
};

/**
 * 获取请求头。
 * @param {string} headers 请求头。
 * @param {string} name 请求头名称。
 * @returns {string} 请求头值。
 */
window.mm._getHeader = function (headers, name) {
  if (!headers) {
    return null;
  }

  return (
    headers
      ?.split(/\r?\n/)
      .find((header) =>
        header.toLowerCase().startsWith(`${name.toLowerCase()}:`)
      )
      ?.split(":")[1]
      ?.trim() || null
  );
};

/**
 * 发送 GET 请求。
 * @param {string} url 请求的 URL。
 * @param {Object} headers 请求头。
 * @returns {Promise<any>} 请求结果。
 */
window.mm.get = function (url, headers = {}) {
  return new Promise((resolve, reject) => {
    GM_xmlhttpRequest({
      method: "GET",
      url,
      headers,
      responseType: "json",
      onload: function (response) {
        try {
          const requestId = window.mm._getHeader(
            response.responseHeaders,
            "X-Request-Id"
          );

          if (response.status !== 200) {
            return reject(
              new Error(`请求 ID: ${requestId} 状态码异常: ${response.status}`)
            );
          }

          const result = response.response;

          if (result && typeof result.code === "number") {
            if (result.code === 200) {
              resolve(result.data);
            } else {
              reject(
                new Error(
                  `请求 ID: ${requestId} 响应错误, 错误码: ${result.code}, 错误信息: ${result.message}`
                )
              );
            }
          } else {
            reject(
              new Error(
                `请求 ID: ${requestId} 响应错误, 格式异常, 内容: ${response.response}`
              )
            );
          }
        } catch (e) {
          reject(e);
        }
      },
      onerror: function (error) {
        reject(error);
      },
    });
  });
};

/**
 * 发送 POST 请求。
 * @param {string} url 请求的 URL。
 * @param {Object} data 请求体。
 * @param {Object} headers 请求头。
 * @returns {Promise<any>} 请求结果。
 */
window.mm.post = function (url, data, headers = {}) {
  return new Promise((resolve, reject) => {
    headers = {
      "Content-Type": "application/json",
      ...headers,
    };

    GM_xmlhttpRequest({
      method: "POST",
      url,
      data: JSON.stringify(data),
      headers: headers,
      responseType: "json",
      onload: function (response) {
        try {
          const requestId = window.mm._getHeader(
            response.responseHeaders,
            "X-Request-Id"
          );

          if (response.status !== 200) {
            return reject(
              new Error(`请求 ID: ${requestId} 状态码异常: ${response.status}`)
            );
          }

          const result = response.response;

          if (result && typeof result.code === "number") {
            if (result.code === 200) {
              resolve(result.data);
            } else {
              reject(
                new Error(
                  `请求 ID: ${requestId} 响应错误, 错误码: ${result.code}, 错误信息: ${result.message}`
                )
              );
            }
          } else {
            reject(
              new Error(
                `请求 ID: ${requestId} 响应错误, 格式异常, 内容: ${response.response}`
              )
            );
          }
        } catch (e) {
          reject(e);
        }
      },
      onerror: function (error) {
        reject(error);
      },
    });
  });
};

/**
 * 查询元素。
 * @param {string} selector 选择器。
 * @returns {Node} 元素。
 */
window.mm.query = function (selector) {
  return document.querySelector(selector);
};

/**
 * 判断元素是否存在。
 * @param {string} selector 选择器。
 * @returns {boolean} 是否存在。
 */
window.mm.exists = function (selector) {
  return document.querySelector(selector) !== null;
};

/**
 * 点击元素。
 * @param {string} selector 选择器。
 */
window.mm.click = function (selector) {
  const el = document.querySelector(selector);
  if (el) {
    el.click();
  }
};

/**
 * 点击元素。
 * @param {Node} root 根元素。
 * @param {string} selector 选择器。
 */
window.mm.clickOf = function (root, selector) {
  const el = root.querySelector(selector);
  if (el) {
    el.click();
  }
};

/**
 * 获取元素的属性值。
 * @param {string} selector 选择器。
 * @param {string} name 属性名称。
 * @returns {string} 属性值。
 */
window.mm.attribute = function (selector, name) {
  return document.querySelector(selector)?.getAttribute(name) || "";
};

/**
 * 获取元素的属性值。
 * @param {Node} el 元素。
 * @param {string} name 属性名称。
 * @returns {string} 属性值。
 */
window.mm.attributeOf = function (el, name) {
  return el?.getAttribute(name) || "";
};

/**
 * 设置标题。
 * @param {string} title 标题。
 */
window.mm.setTitle = function (title) {
  document.title = title;
};
