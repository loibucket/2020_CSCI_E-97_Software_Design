## Run tests with the shell script ProcessFile.sh in a linux based terminal

## Alternatively, manual enter the commands in ProcessFile.sh line by line in terminal

\
**two output files should be created:**

**smartcity_model.original.script.out.txt**\
This is the output based on inputs from the orignal provided command script "Assignment_3_sample_script.txt".  This is expected to have generated error lines.  Some of the errors are due to incorrect commands fed to the processor.  Also, commands related to money, e.g. charging for parking, should also show errors, as no ledger or accounts were created.

**smartcity_model.extended.script.out.txt**\
This is based on a set of inputs extended from the original script "Assignment_3_sample_script.extended.txt".  All the commands are expected to process as intended.  The ledger and accounts are created through another script with ledger commands "Ledger.script.txt".  Therefore, the subsequent city commands related to money function as intended.