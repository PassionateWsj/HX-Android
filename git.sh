gitPush() {

  br=`git symbolic-ref --short -q HEAD`

if [ "$br" = "developer" ] ; then

  timestamp=$(date "+%Y-%m-%d %H:%M:%S")
  commit_message=$("crontab task ${timestamp}")

  git add ./
  git commit -m "${commit_message}"
  git push origin developer

else
    echo "current branch is not developer"
fi
}
gitPush

