# How to start the app
1. Change YOURPASSWORD in feed.sql to your encrypted password (should be easy to create with some debugging)
2. Run ```./start-app.sh```
3. Go to http://localhost:3000
4. Login with admin | YOURPASSWORD (non-encrypted ofc)

# How to run backend functional tests locally 
1. Change YOURPASSWORD in feed-functional.sql to your encrypted password (should be easy to create with some debugging)
2. Run ```./start-app-for-func-tests.sh```(this version doesn't create a volume, has a different name for the db and the --force-recreate -V ensures that the temp volume is dropped)
3. Run ```./run-func-tests.sh``` or run the test needed manually from the feature files