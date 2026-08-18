#!/bin/bash

set -e

DISPLAY_NUM=:99

cleanup() {
    echo
    echo "Stopping GUI environment..."

    kill "$NOVNC_PID" 2>/dev/null || true
    kill "$X11VNC_PID" 2>/dev/null || true
    kill "$XVFB_PID" 2>/dev/null || true

    echo "GUI environment stopped."
}

trap cleanup EXIT INT TERM

echo "Starting Xvfb..."
Xvfb "$DISPLAY_NUM" -screen 0 1280x800x24 &
XVFB_PID=$!

sleep 1

echo "Starting x11vnc..."
x11vnc -display "$DISPLAY_NUM" -nopw -forever -shared -rfbport 5900 &
X11VNC_PID=$!

sleep 1

echo "Starting noVNC..."
/usr/share/novnc/utils/novnc_proxy \
    --vnc localhost:5900 \
    --listen 6080 &
NOVNC_PID=$!

sleep 2

echo
echo "GUI environment started."
echo "Open port 6080 in the Codespaces Ports panel."
echo

DISPLAY="$DISPLAY_NUM" mvn javafx:run