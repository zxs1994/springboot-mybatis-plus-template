#!/bin/bash

echo "=============================="
echo "Deploy current directory JAR"
echo "=============================="

# 找到当前目录最新的 jar
APP_NAME=$(ls -t *.jar 2>/dev/null | head -n 1)

if [ -z "$APP_NAME" ]; then
  echo "❌ No jar file found in current directory"
  exit 1
fi

echo "✅ Found JAR: $APP_NAME"

# 查找正在运行的进程（只匹配这个 jar）
pids=$(ps -ef | grep "$APP_NAME" | grep -v grep | awk '{print $2}')

if [ -n "$pids" ]; then
  echo "🛑 Stopping old process..."
  for pid in $pids; do
    sudo kill -9 "$pid"
    echo "   Killed PID: $pid"
  done
else
  echo "ℹ️  No running process found."
fi

echo "🚀 Starting new process..."
# 设置 JVM 时区为东八区，确保应用内时间与数据库一致
sudo nohup java -Duser.timezone=Asia/Shanghai -jar "$APP_NAME" \
  --spring.profiles.active=prod \
  > app.log 2>&1 &

echo "✅ Started $APP_NAME"
echo "Done."