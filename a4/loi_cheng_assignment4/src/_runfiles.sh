#run as shell script in linux terminal
javac cscie97/ledger/*.java cscie97/smartcity/model/*.java cscie97/smartcity/controller/*.java cscie97/smartcity/shared/*.java cscie97/smartcity/test/*.java cscie97/smartcity/authenticator/*.java
java -cp . cscie97.smartcity.test.TestDriver all.script.txt > all.out.txt
