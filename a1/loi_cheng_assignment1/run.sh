#compile
javac com/cscie97/ledger/*.java com/cscie97/ledger/test/*.java
#run 
java -cp . com.cscie97.ledger.test.TestDriver ledger.script > ledger.script.out.txt
java -cp . com.cscie97.ledger.test.TestDriver ledgerB.script > ledgerB.script.out.txt
#interactive mode
#java -cp . com.cscie97.ledger.test.TestInteractive