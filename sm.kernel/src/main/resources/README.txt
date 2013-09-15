To install in windows
- Download setupwin.zip from 
- create a directory with name "scripts"
- unzip to "scripts"
- modify setupSmart.ps1 with the correct paths for scripts and install directory

- start powershell as administrator
- Run the following command to enable execution of powershell scripts
Set-ExecutionPolicy -ExecutionPolicy Bypass -Scope CurrentUser
- cd scripts (Go to scripts directory)
- ./setupSmart.ps1 (run the set up - will take time to download dependencies)
- ./startSmart.bat SecureServer true (run the server)

---------------------------------------------------
To test flows in SMART
---------------------------------------------------
There is a simple html test client included with the installation that can be used to deploy and test flows.
1) Open FlowTest.html in a browser
2) Enter the server details and click on connect to smart
3) Enter the jar to deploy and click on Deploy and Enable
4) Now you can use the Post to SMART section to post different events to SMART.

The status messages in JSON format returned by SMART is shown in the STATUS Message box. List results are shown in the results section.
This is a very primitive test tool and helps you get started with writing a client.
smartcom.js can be accessed from fixchg.com. The js files that you write can be derived from this to inherit the standard SMART client functions.
