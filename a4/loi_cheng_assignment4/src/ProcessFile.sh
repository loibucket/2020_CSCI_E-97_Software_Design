#run as shell script in linux terminal
#javac cscie97/ledger/*.java cscie97/smartcity/model/*.java cscie97/smartcity/controller/*.java cscie97/smartcity/helper/*.java cscie97/smartcity/test/*.java
java -cp . cscie97.smartcity.test.TestDriver a3_sample_script.txt > smartcity_model.original.script.out.txt
java -cp . cscie97.smartcity.test.TestDriver Ledger.script.txt a3_sample_script.extended.txt > smartcity_model.extended.script.out.txt