function createdir
{
	param($dir)
	if (test-path $dir)
	{
	   Write-Output "Directory already exists"
	}
	else
	{
	    mkdir -p $dir
	}
}

function unzip() 
{
    param($file, $dest)
    if (test-path $dest)
    {
       $error.clear();
       $shell_app=new-object -com shell.application
       Write-Output $("Extracting..." + $file + " to " + $dest)
       $zip_file = $shell_app.namespace($file)
       $destination = $shell_app.namespace($dest)
       $destination.Copyhere($zip_file.items())
       if ($error[0])
       {
           $error[0].Exception.ToString()
           exit
       }
    }
}

function wget_url
{
	param($source, $dest)
	if (test-path $dest)
	{
	   Write-Output $("File Already downloaded " + $dest)
	}
	else
	{
	   Write-Output $("Downloading " + $source + " to " + $dest)
	   $error.clear()
	   $wc = New-Object System.Net.WebClient
	   $wc.DownloadFile($source, $dest)
	   if ($error[0])
	   {
	       $error[0].Exception.ToString()
	       exit
	   }
	}
}

$currpath = (Get-Location).Path
wget_url "https://github.com/smartplatf/a-smart/blob/master/sm.kernel/src/main/resources/setupsmartwin.zip?raw=true" $($currpath + "\setupsmartwin.zip")
createdir scripts
createdir smartinstall
unzip $($currpath + "\setupsmartwin.zip") $($currpath + "\scripts\")
$exec = $($currpath + "\scripts\setupSmart.ps1")
& $exec
