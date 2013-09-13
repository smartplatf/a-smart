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
