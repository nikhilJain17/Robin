#!/bin/sh
echo "Robn for Mac!"

#open a new tab
osascript -e 'tell application "Terminal" to activate' -e 'tell application "System Events" to tell process "Terminal" to keystroke "t" using command down' -e 'tell application "Terminal" to do script "node server.js" in selected tab of front window'

osascript -e 'tell application "Terminal" to activate' -e 'tell application "System Events" to tell process "Terminal" to keystroke "t" using command down' -e 'tell application "Terminal" to do script "./ngrok http 4444" in selected tab of front window'

#start the server on the old tab
#start ngrok on the new tab
# osascript -e 'tell application "Terminal" to activate' -e 'tell application "System Events" to tell process "Terminal" to keystroke "t" using command down'
# ./ngrok http 4444