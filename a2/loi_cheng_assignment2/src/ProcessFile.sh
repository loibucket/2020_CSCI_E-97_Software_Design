#run as shell script
javac cscie97/smartcity/model/*.java cscie97/smartcity/test/*.java
java -cp . cscie97.smartcity.test.TestDriver smartcity_model.original.script > smartcity_model.original.script.out.txt
java -cp . cscie97.smartcity.test.TestDriver smartcity_model.extended.script > smartcity_model.extended.script.out.txt