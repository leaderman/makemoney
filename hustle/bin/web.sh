#!/bin/bash

# 当前脚本目录
BASE_DIR="$(cd "$(dirname "$0")" && pwd)"

# 配置文件目录
CONFIG_DIR="$BASE_DIR/../../../makemoney-config"

# jar 路径
JAR_PATH="$BASE_DIR/../web/target/hustle-web.jar"

# 环境
PROFILE="${MAKEMONEY_PROFILE:-default}"

# 启动参数
SPRING_OPTS="-Dspring.config.location=file:${CONFIG_DIR}/ -Dspring.profiles.active=${PROFILE}"

# 执行
exec java ${SPRING_OPTS} -jar "${JAR_PATH}" "$@"